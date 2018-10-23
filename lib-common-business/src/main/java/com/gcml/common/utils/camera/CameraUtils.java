package com.gcml.common.utils.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gcml.common.utils.display.ImageUtils;
import com.gcml.common.utils.qrcode.scaner.CameraManager;
import com.gcml.common.utils.ui.ScreenUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraUtils {
    /**
     * 是否有预览
     */
    private boolean hasSurface = false;
    /**
     * 聚焦
     */
    private static final int AUTO_FOCUS = 1001;
    /**
     * 重新预览
     */
    private static final int RESTART_PREVIEW = 1002;
    /**
     * 解析数据成功
     */
    private static final int DECODE_SUCCESSED = 1003;
    /**
     * 解析失败
     */
    private static final int DECODE_FAILED = 1004;
    /**
     * 解析
     */
    private static final int DECODE = 1005;
    /**
     * 退出
     */
    private static final int QUIT = 1006;

    private static volatile CameraUtils singleton = null;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int previewWidth;
    private int previewHeight;
    private int cropWidth;
    private int cropHeight;
    private Activity activity;
    //调整方向之后的角度
    private int orientationResult;
    private CaptureActivityHandler handler;
    private Rect definedRect;
    //等比缩放后裁剪框的大小
    private int mCropWidth;
    private int mCropHeight;

    private CameraUtils() {
    }

    public static CameraUtils getInstance() {

        if (singleton == null) {
            synchronized (CameraUtils.class) {
                if (singleton == null) {
                    singleton = new CameraUtils();
                }
            }
        }
        return singleton;
    }

    /**
     * 初始化 单位像素
     *
     * @param surfaceView
     * @param previewWidth  预览图像的宽
     * @param previewHeight 预览图像的高
     * @param rect          用户自定义的裁剪矩形框
     */
    public CameraUtils init(Activity activity, SurfaceView surfaceView, int previewWidth, int previewHeight, Rect rect) {
        this.surfaceView = surfaceView;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.activity = activity;
        this.definedRect = rect;
        this.surfaceHolder = surfaceView.getHolder();
        CameraManager.init(activity);
        return singleton;
    }

    /**
     * 初始化 单位像素
     *
     * @param surfaceView
     * @param previewWidth  预览图像的宽
     * @param previewHeight 预览图像的高
     * @param cropWidth     裁剪图像的宽
     * @param cropHeight    裁剪图像的高
     */
    public CameraUtils init(Activity activity, SurfaceView surfaceView, int previewWidth, int previewHeight, int cropWidth, int cropHeight) {
        this.surfaceView = surfaceView;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.activity = activity;
        this.surfaceHolder = surfaceView.getHolder();
        CameraManager.init(activity);
        return singleton;
    }

    /**
     * 打开摄像头并且预览
     *
     * @param cameraId
     */
    public void openCamera(final int cameraId) {
        if (hasSurface) {
            initCamera(surfaceHolder, cameraId);
        } else {
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (!hasSurface) {
                        hasSurface = true;
                        initCamera(holder, cameraId);
                    }
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    hasSurface = false;
                    if (callBack != null) {
                        callBack.cameraClosed();
                    }
                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    /**
     * 重新开始预览
     *
     * @param period
     */
    public void restartPreview(int period) {
        Message message = Message.obtain(handler, DECODE_FAILED);
        handler.sendMessageDelayed(message, period);
    }

    /**
     * 关闭摄像头和预览
     */
    public void closeCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        CameraManager.get().closeDriver();
        callBack = null;
        activity = null;
    }

    private void initCamera(SurfaceHolder holder, int cameraId) {
        try {
            CameraManager.get().openDriver(holder, cameraId);
            setCameraOrientation(Camera.CameraInfo.CAMERA_FACING_BACK);
            Point point = CameraManager.get().getCameraResolution();
            AtomicInteger width;
            AtomicInteger height;
            if (ScreenUtils.isScreenLandscape()) {
                width = new AtomicInteger(point.x);
                height = new AtomicInteger(point.y);
            } else {
                width = new AtomicInteger(point.y);
                height = new AtomicInteger(point.x);
            }
            mCropWidth = cropWidth * width.get() / previewWidth;
            mCropHeight = cropHeight * height.get() / previewHeight;
            CameraManager.FRAME_WIDTH = mCropWidth;
            CameraManager.FRAME_HEIGHT = mCropHeight;
        } catch (Throwable e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.openCameraFail(e);
            }
        }

        if (handler == null) {
            handler = new CaptureActivityHandler();
        }
    }

    private final class CaptureActivityHandler extends Handler {

        DecodeThread decodeThread = null;
        private State state;

        public CaptureActivityHandler() {
            decodeThread = new DecodeThread();
            decodeThread.start();
            state = State.SUCCESS;
            if (callBack != null) {
                callBack.onStartPreview();
            }
            CameraManager.get().startPreview();
            restartPreviewAndDecode();
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == AUTO_FOCUS) {
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, AUTO_FOCUS);
                }
            } else if (message.what == RESTART_PREVIEW) {
                restartPreviewAndDecode();
            } else if (message.what == DECODE_SUCCESSED) {
                state = State.SUCCESS;
//                handleDecode((Result) message.obj);// 解析成功，回调
            } else if (message.what == DECODE_FAILED) {
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), DECODE);
            }
        }

        public void quitSynchronously() {
            state = State.DONE;
            decodeThread.interrupt();
            CameraManager.get().stopPreview();
            removeMessages(DECODE_SUCCESSED);
            removeMessages(DECODE_FAILED);
            removeMessages(DECODE);
            removeMessages(AUTO_FOCUS);
        }

        private void restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), DECODE);
                CameraManager.get().requestAutoFocus(this, AUTO_FOCUS);
            }
        }
    }

    private final class DecodeThread extends Thread {

        private final CountDownLatch handlerInitLatch;
        private Handler handler;

        DecodeThread() {
            handlerInitLatch = new CountDownLatch(1);
        }

        Handler getHandler() {
            try {
                handlerInitLatch.await();
            } catch (InterruptedException ie) {
                // continue?
            }
            return handler;
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new DecodeHandler();
            handlerInitLatch.countDown();
            Looper.loop();
        }
    }

    private final class DecodeHandler extends Handler {
        DecodeHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == DECODE) {
                if (callBack != null && activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onStartResolveImage();
                        }
                    });
                }
                resolveData((byte[]) message.obj, message.arg1, message.arg2);
            } else if (message.what == QUIT) {
                Looper.myLooper().quit();
            }
        }
    }

    @WorkerThread
    private void resolveData(byte[] datas, int width, int height) {
        Bitmap preBitmap = ImageUtils.yuv2Bitmap(datas, width, height);
        //裁剪bitmap
        Bitmap rectBitmap;
        if (definedRect != null) {
            rectBitmap = Bitmap.createBitmap(preBitmap,
                    definedRect.left, definedRect.top, definedRect.width(), definedRect.height());
        } else {
            int leftOffset = (width - mCropWidth) / 2;
            int topOffset = (height - mCropHeight) / 2;
            Rect rect = new Rect(leftOffset, topOffset, width - leftOffset,
                    height - topOffset);
            rectBitmap = Bitmap.createBitmap(preBitmap,
                    rect.left, rect.top, rect.width(), rect.height());
        }
        Bitmap compressBitmap = ImageUtils.compressByQuality(rectBitmap, 80, true);
        if (callBack != null) {
            callBack.previewSuccess(datas, preBitmap, compressBitmap, width, height, cropWidth, cropHeight);
        }

    }


    private void setCameraOrientation(int cameraId) {
        Camera camera = CameraManager.get().getCamera();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            orientationResult = (info.orientation + degrees) % 360;
            orientationResult = (360 - orientationResult) % 360;  // compensate the mirror
        } else {  // back-facing
            orientationResult = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(orientationResult);
    }

    private CameraPreviewCallBack callBack;

    public void setOnCameraPreviewCallback(CameraPreviewCallBack callBack) {
        this.callBack = callBack;
    }

    public interface CameraPreviewCallBack {
        void onStartPreview();

        void onStartResolveImage();

        void previewSuccess(byte[] datas, Bitmap preBitmap, Bitmap cropBitmap, int prewidth, int preheight, int cropwidth, int cropheight);

        void openCameraFail(Throwable e);

        void cameraClosed();
    }

    private enum State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }
}