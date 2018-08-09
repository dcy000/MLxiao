package com.gzq.test_all_devices;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
public class TestCameraActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private RelativeLayout mScanBorder;
    private ImageView mPreImage;
    private RelativeLayout mRootBorder;
//    private SurfaceView mSurfaceView;
//    /**
//     * 扫描边界的宽度
//     */
//    private int mCropWidth = 0;
//
//    /**
//     * 扫描边界的高度
//     */
//    private int mCropHeight = 0;
//    /**
//     * 是否有预览
//     */
//    private boolean hasSurface = false;
//    private SurfaceHolder surfaceHolder;
//    private RelativeLayout mCropLayout;
//    private RelativeLayout mContainer;
//    private CaptureActivityHandler handler;
//    /**
//     * 聚焦
//     */
//    private static final int AUTO_FOCUS = 1001;
//    /**
//     * 重新预览
//     */
//    private static final int RESTART_PREVIEW = 1002;
//    /**
//     * 解析数据成功
//     */
//    private static final int DECODE_SUCCESSED = 1003;
//    /**
//     * 解析失败
//     */
//    private static final int DECODE_FAILED = 1004;
//    /**
//     * 解析
//     */
//    private static final int DECODE = 1005;
//    /**
//     * 退出
//     */
//    private static final int QUIT = 1006;
//    private ImageView mPreImage;
//    private int orientationResult;
//
//    //拿到数据进行解析
//    private void faceRecognition(byte[] datas, int width, int height) {
////        Message message;
////        message=Message.obtain(handler,DECODE_SUCCESSED);
////        message.sendToTarget();
////        message=Message.obtain(handler,DECODE_FAILED);
////        message.sendToTarget();
//        Bitmap bitmap = ImageUtils.yuv2Bitmap(datas, width, height);
//        //裁剪bitmap
//        int leftOffset = (width - mCropLayout.getWidth()) / 2;
//        int topOffset = (height - mCropLayout.getHeight()) / 2;
//        Timber.e("宽度：" + leftOffset + "高度：" + topOffset);
//        Rect rect = new Rect(leftOffset, topOffset, width - leftOffset,
//                height - topOffset);
//        Bitmap rectBitmap = Bitmap.createBitmap(bitmap,
//                rect.left, rect.top, rect.width(), rect.height());
//        Bitmap compressBitmap = ImageUtils.compressByQuality(rectBitmap, 20, true);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mPreImage.setImageBitmap(compressBitmap);
//            }
//        });
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Message message = Message.obtain(handler, DECODE_FAILED);
//        message.sendToTarget();
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        initView();
        CameraUtils2 instance = CameraUtils2.getInstance();
        instance.init(this,mSurfaceView,1920,1200,856,856);
        instance.setOnCameraPreviewCallback(new CameraUtils2.CameraPreviewCallBack() {
            @Override
            public void onStartPreview() {
                Log.e("线程名称", "onStartPreview: "+Thread.currentThread().getName() );
            }

            @Override
            public void onStartResolveImage() {
                Log.e("线程名称", "onStartResolveImage: "+Thread.currentThread().getName() );
            }

            @Override
            public void previewSuccess(byte[] datas, Bitmap preBitmap, Bitmap cropBitmap, int prewidth, int preheight, int cropwidth, int cropheight) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPreImage.setImageBitmap(cropBitmap);

                    }
                });
                CameraUtils2.getInstance().restartPreview(2000);
            }

            @Override
            public void openCameraFail(Exception e) {

            }
        });


//        initView();
//        CameraManager.init(this);
//        surfaceHolder = mSurfaceView.getHolder();
//        if (hasSurface) {
//
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//                }
//
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) {
//                    if (!hasSurface) {
//                        hasSurface = true;
//                        initCamera(holder);
//                    }
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) {
//                    hasSurface = false;
//
//                }
//            });
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CameraUtils2.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.surfaceView);
        mScanBorder = findViewById(R.id.scan_border);
        mPreImage = findViewById(R.id.preImage);
        mRootBorder = findViewById(R.id.root_border);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraUtils2.getInstance().closeCamera();
    }

    /* @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder holder) {
        try {
            CameraManager.get().openDriver(holder, Camera.CameraInfo.CAMERA_FACING_BACK);
            setCameraOrientation(Camera.CameraInfo.CAMERA_FACING_BACK);
            Point point = CameraManager.get().getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mCropLayout.getWidth() * width.get() / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height.get() / mContainer.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (handler == null) {
            handler = new CaptureActivityHandler();
        }
    }

    private void setCameraOrientation(int cameraId) {
        Camera camera = CameraManager.get().getCamera();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
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

    final class CaptureActivityHandler extends Handler {

        DecodeThread decodeThread = null;
        private State state;

        public CaptureActivityHandler() {
            decodeThread = new DecodeThread();
            decodeThread.start();
            state = State.SUCCESS;
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

    final class DecodeThread extends Thread {

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

    final class DecodeHandler extends Handler {
        DecodeHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == DECODE) {
                faceRecognition((byte[]) message.obj, message.arg1, message.arg2);
            } else if (message.what == QUIT) {
                Looper.myLooper().quit();
            }
        }
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;

    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mCropLayout = (RelativeLayout) findViewById(R.id.scan_border);
        mContainer = (RelativeLayout) findViewById(R.id.root_border);
        mPreImage = (ImageView) findViewById(R.id.preImage);
    }

    private enum State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }*/
}
