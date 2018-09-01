package com.gcml.auth.face.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pools;
import android.view.SurfaceHolder;
import android.view.View;

import com.gcml.auth.face.utils.CameraUtils;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * 相机使用步骤:
 * 1. 打开相机
 * 2. 预览相机
 * 3. 设置预览回调
 * setPreviewCallback
 * setOneShotPreviewCallback
 * setPreviewCallbackWithBuffer / addCallbackBuffer
 * 4. 预览回调数据处理
 * <p>
 * 注意：
 * 1. 设置预览回调时，setPreviewCallback 和 setOneShotPreviewCallback 都存在内存抖动
 * 2. 在哪个 Looper 线程 open Camera，那么 onPreviewFrame 等 Camera 回调就执行在打开相机的 Looper 线程
 * 3. onPreviewFrame 方法中不要执行过于复杂的逻辑操作，这样会阻塞 Camera，无法获取新的 Frame，导致帧率下降
 */
public class PreviewHelper
        implements SurfaceHolder.Callback, LifecycleObserver {
    private static final int PREVIEW_WIDTH = 1280;
    private static final int PREVIEW_HEIGHT = 720;
    private static final int FPS = 30;

    private FragmentActivity mActivity;

    private SurfaceHolder mSurfaceHolder;

    private View mPreviewView;

    // 预览图像裁剪区域
    private Rect mCropRect;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private volatile Camera mCamera;
    private int previewWidth = PREVIEW_WIDTH;
    private int previewHeight = PREVIEW_HEIGHT;
    private int fps = FPS;

    public PreviewHelper(FragmentActivity activity) {
        mActivity = activity;
        mActivity.getLifecycle().addObserver(this);
        initFrameProcess();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        if (mCameraHandler != null) {
            mCameraHandler.removeCallbacksAndMessages(null);
        }
        CameraUtils.close(mCamera);
        mCamera = null;
        if (mCameraThread != null) {
            mCameraThread.quit();
            mCameraThread = null;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {

    }

    public void configCamera() {
        cameraHandler().post(new Runnable() {
            @Override
            public void run() {
                configCameraInternal();
            }
        });
    }

    private void configCameraInternal() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            fps = CameraUtils.calculateAndSetFps(parameters, CameraUtils.calculateAndSetFps(parameters, fps));
            Camera.Size size = CameraUtils.calculateSize(parameters.getSupportedPreviewSizes(), previewWidth, previewHeight);
            if (size != null) {
                previewWidth = size.width;
                previewHeight = size.height;
            }
            parameters.setPreviewSize(previewWidth, previewHeight);
            int degrees = CameraUtils.calculateRotation(mActivity, mCameraId);
            mCamera.setDisplayOrientation(degrees);
            mCamera.setParameters(parameters);
        }
    }

    public void setPreviewView(View previewView) {
        this.mPreviewView = previewView;
    }

    public void setCropRect(Rect cropRect) {
        mCropRect = cropRect;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            Timber.i("setSurfaceHolder (surfaceHolder == null)");
        }
        mSurfaceHolder = surfaceHolder;
        if (mSurfaceHolder != null) {
            mSurfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.i("surfaceCreated -> startPreview");
        openAndPreviewCamera(mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            Timber.i("surfaceChanged -> restartPreview failed (surface == null)");
            return;
        }
        Timber.i("surfaceChanged -> restartPreview of");
        CameraUtils.followScreenOrientation(mActivity, mCamera);
        CameraUtils.stopPreview(mCamera);
        CameraUtils.startPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Timber.i("surfaceDestroyed -> stopPreview");
        CameraUtils.stopPreview(mCamera);
    }

    private void openAndPreviewCamera(SurfaceHolder holder) {
        cameraHandler().post(new Runnable() {
            @Override
            public void run() {
                mCamera = CameraUtils.openByFacing(mCameraId);
                if (mCamera == null) {
                    rxStatus.onNext(Status.of(Status.ERROR_ON_OPEN_CAMERA));
                } else {
                    configCameraInternal();
                    CameraUtils.startPreview(mCamera, holder);
                    CameraUtils.setPreviewCallbackWithBuffer(mCamera, mPreviewCallback);
                }
            }
        });
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            rxData.onNext(data);
        }
    };

    /**
     * 用于预览帧数据
     */
    private final Pools.Pool<byte[]> pool = new Pools.SynchronizedPool<>(18);

    public byte[] obtainData(int size) {
        byte[] data;
        data = pool.acquire();
        if (data == null || data.length != size) {
            data = new byte[size];
        }
        return data;
    }

    public void recycleData(byte[] data) {
        try {
            pool.release(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void addBuffer(long delayMillis) {
        if (mCamera != null) {
            cameraHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addBufferInternal();
                }
            }, delayMillis);
        }
    }

    /**
     * start obtain Preview Frame
     */
    private void addBufferInternal() {
        int bufferSize = CameraUtils.calculateBufferSize(mCamera);
        byte[] buffer = obtainData(bufferSize);
        CameraUtils.addCallbackBuffer(mCamera, buffer);
    }

    private PublishSubject<byte[]> rxData = PublishSubject.create();

    private void initFrameProcess() {
        rxData.observeOn(Schedulers.io())
                .flatMap(new Function<byte[], ObservableSource<? extends byte[]>>() {
                    @Override
                    public ObservableSource<? extends byte[]> apply(byte[] bytes) throws Exception {
                        return processFrame(bytes);
                    }
                })
                .as(RxUtils.autoDisposeConverter(mActivity))
                .subscribe(new DefaultObserver<byte[]>() {
                    @Override
                    public void onNext(byte[] bytes) {
                        recycleData(bytes);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private Observable<byte[]> processFrame(byte[] bytes) {
        return Observable.just(bytes)
                .map(new Function<byte[], byte[]>() {
                    @Override
                    public byte[] apply(byte[] bytes) throws Exception {
                        processFrameInternal(bytes);
                        return bytes;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 预览帧 区域裁剪
     *
     * @param bytes 预览帧
     */
    private void processFrameInternal(byte[] bytes) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            int previewWidth = parameters.getPreviewSize().width;
            int previewHeight = parameters.getPreviewSize().height;
            // 由于预览图片和界面显示大小可能不一样，
            // 计算缩放后的区域
            Rect rect = getScaledRect(mPreviewView, mCropRect, previewWidth, previewHeight);

//            int rotationCount = getRotationCount();
//            if (rotationCount==1 || rotationCount == 3) {
//                int temp = previewWidth;
//                previewWidth = previewHeight;
//                previewHeight = temp;
//            }

            // 旋转裁剪区域
            // 可用于优化性能
//            bytes = rotateData(bytes, mCamera);

            // 预览图像数据方向可能有方向问题，
            // 计算旋转后的区域
            rect = getRotatedRect(previewWidth, previewHeight, rect);

            //裁剪区域
            YuvImage image = new YuvImage(bytes, ImageFormat.NV21, previewWidth, previewHeight, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compressToJpeg(rect, 100, baos);
            byte[] cropped = baos.toByteArray();
            Bitmap croppedBitmap = BitmapFactory.decodeByteArray(cropped, 0, cropped.length);

            //旋转图片
            int rotation = CameraUtils.calculateRotation(mActivity, mCameraId);
            if (rotation == 90 || rotation == 270) {
                croppedBitmap = rotate(croppedBitmap, rotation);
            }
            rxStatus.onNext(Status.of(Status.EVENT_CROPPED, croppedBitmap));
        }
    }

    private Rect scaledRect;

    public Rect getScaledRect(View previewView, Rect cropRect, int previewWidth, int previewHeight) {
        if (scaledRect == null) {
            int uiWidth = previewView.getWidth();
            int uiHeight = previewView.getHeight();

            int width, height;
            final int orientation = mActivity.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT//竖屏使用
                    && previewHeight < previewWidth) {
                width = previewHeight;
                height = previewWidth;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE//横屏使用
                    && previewHeight > previewWidth) {
                width = previewHeight;
                height = previewWidth;
            } else {
                width = previewWidth;
                height = previewHeight;
            }

            scaledRect = new Rect(cropRect);
            scaledRect.left = scaledRect.left * width / uiWidth;
            scaledRect.right = scaledRect.right * width / uiWidth;
            scaledRect.top = scaledRect.top * height / uiHeight;
            scaledRect.bottom = scaledRect.bottom * height / uiHeight;
        }
        return scaledRect;
    }

    private Rect rotatedRect;

    public Rect getRotatedRect(int previewWidth, int previewHeight, Rect rect) {
        if (rotatedRect == null) {
            int rotationCount = getRotationCount();
            rotatedRect = new Rect(rect);

            if (rotationCount == 1) {//若相机图像需要顺时针旋转90度，则将扫码框逆时针旋转90度
                rotatedRect.left = rect.top;
                rotatedRect.top = previewHeight - rect.right;
                rotatedRect.right = rect.bottom;
                rotatedRect.bottom = previewHeight - rect.left;
            } else if (rotationCount == 2) {//若相机图像需要顺时针旋转180度,则将扫码框逆时针旋转180度
                rotatedRect.left = previewWidth - rect.right;
                rotatedRect.top = previewHeight - rect.bottom;
                rotatedRect.right = previewWidth - rect.left;
                rotatedRect.bottom = previewHeight - rect.top;
            } else if (rotationCount == 3) {//若相机图像需要顺时针旋转270度，则将扫码框逆时针旋转270度
                rotatedRect.left = previewWidth - rect.bottom;
                rotatedRect.top = rect.left;
                rotatedRect.right = previewWidth - rect.top;
                rotatedRect.bottom = rect.right;
            }
        }

        return rotatedRect;
    }

    private int getRotationCount() {
        return CameraUtils.calculateRotation(mActivity, mCameraId) / 90;
    }

    public byte[] rotateData(byte[] src, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        int rotationCount = getRotationCount();
        for (int i = 0; i < rotationCount; i++) {
            byte[] rotatedData = new byte[src.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = src[x + y * width];
            }
            src = rotatedData;
            int tmp = width;
            width = height;
            height = tmp;
        }
        return src;
    }

    public static Bitmap rotate(Bitmap src, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotated = Bitmap.createBitmap(
                src,
                0,
                0,
                src.getWidth(),
                src.getHeight(),
                matrix,
                true
        );
        if (src != rotated && !src.isRecycled()) {
            src.recycle();
        }
        return rotated;
    }

    public static class Status {
        public static final int ERROR_ON_OPEN_CAMERA = -1;
        public static final int EVENT_CROPPED = 1;

        public int code;

        public Object payload;

        public static Status of(int code) {
            Status status = new Status();
            status.code = code;
            return status;
        }

        public static Status of(int code, Object payload) {
            Status status = new Status();
            status.code = code;
            status.payload = payload;
            return status;
        }
    }

    private Subject<Status> rxStatus = PublishSubject.<Status>create().toSerialized();

    public Observable<Status> rxStatus() {
        return rxStatus;
    }

    private Handler mCameraHandler;
    private HandlerThread mCameraThread;

    public Handler cameraHandler() {
        if (mCameraHandler == null) {
            synchronized (this) {
                if (mCameraHandler == null) {
                    mCameraThread = new HandlerThread("CameraThread");
                    mCameraThread.start();
                    mCameraHandler = new Handler(mCameraThread.getLooper());
                }
            }
        }
        return mCameraHandler;
    }
}