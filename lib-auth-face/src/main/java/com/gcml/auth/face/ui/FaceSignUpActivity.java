package com.gcml.auth.face.ui;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.util.Pools;
import android.view.SurfaceHolder;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignUpBinding;
import com.gcml.auth.face.utils.CameraUtils;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;

import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;


/**
 * 相机使用步骤:
 * 1. 打开相机
 * 2. 预览相机
 * 3. 设置预览回调
 * - setPreviewCallback
 * - setOneShotPreviewCallback
 * - setPreviewCallbackWithBuffer / addCallbackBuffer
 * 4. 预览回调数据处理
 * <p>
 * 注意：
 * 1. 设置预览回调时，setPreviewCallback 和 setOneShotPreviewCallback 都存在内存抖动
 * 2. Camera API 在哪个 Looper 线程打开相机，那么 onPreviewFrame 等相机回调就执行在打开相机的 Looper 线程
 * 3. onPreviewFrame 方法中不要执行过于复杂的逻辑操作，这样会阻塞 Camera，无法获取新的 Frame，导致帧率下降
 */
public class FaceSignUpActivity extends BaseActivity<AuthActivityFaceSignUpBinding, FaceSignUpViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_face_sign_up;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        initCamera();
    }

    public void goBack() {

    }

    private void initCamera() {
        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Timber.i("surfaceCreated -> startPreview");
                openAndPreviewCamera(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (holder.getSurface() == null) {
                    Timber.i("surfaceChanged -> restartPreview failed (surface == null)");
                    return;
                }
                Timber.i("surfaceChanged -> restartPreview success");
                CameraUtils.followScreenOrientation(FaceSignUpActivity.this, mCamera);
                CameraUtils.stopPreview(mCamera);
                CameraUtils.startPreview(mCamera, holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Timber.i("surfaceDestroyed -> stopPreview");
                CameraUtils.stopPreview(mCamera);
            }
        });
        initDataProcess();
    }



    private void openAndPreviewCamera(SurfaceHolder holder) {
        CountDownLatch latch = new CountDownLatch(1);
        mCameraHandler.post(new Runnable() {
            @Override
            public void run() {
                mCamera = CameraUtils.openByFacing(mCameraId);
                if (mCamera == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCameraOpenError();
                        }
                    });
                }
                configCamera();
                latch.countDown();
            }
        });
        try {
            latch.await();
            CameraUtils.startPreview(mCamera, holder);
            CameraUtils.setPreviewCallbackWithBuffer(mCamera, mPreviewCallback);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * start obtain Preview Frame
     */
    private void addBuffer() {
        int bufferSize = CameraUtils.calculateBufferSize(mCamera);
        CameraUtils.addCallbackBuffer(mCamera, obtainData(bufferSize));
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            rxData.onNext(data);
        }
    };

    private PublishSubject<byte[]> rxData = PublishSubject.create();

    private void initDataProcess() {
        rxData.observeOn(Schedulers.io())
                .flatMap(new Function<byte[], ObservableSource<? extends byte[]>>() {
                    @Override
                    public ObservableSource<? extends byte[]> apply(byte[] bytes) throws Exception {
                        return processData(bytes);
                    }
                })
                .doOnNext(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {
                        recycleData(bytes);
                    }
                })
                .as(RxUtils.autoDisposeConverter(FaceSignUpActivity.this))
                .subscribe();
    }

    private Observable<byte[]> processData(byte[] bytes) {
        return Observable.just(bytes);
    }

    private void onCameraOpenError() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configCamera();
    }

    private void configCamera() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            fps = CameraUtils.calculateAndSetFps(parameters, CameraUtils.calculateAndSetFps(parameters, fps));
            Camera.Size size = CameraUtils.calculateSize(parameters.getSupportedPreviewSizes(), previewWidth, previewHeight);
            if (size != null) {
                previewWidth = size.width;
                previewHeight = size.height;
            }
            parameters.setPreviewSize(previewWidth, previewHeight);
            int degrees = CameraUtils.calculateRotation(FaceSignUpActivity.this, mCameraId);
            mCamera.setDisplayOrientation(degrees);
            mCamera.setParameters(parameters);
        }
    }

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private volatile Camera mCamera;
    private static final int PREVIEW_WIDTH = 1280;
    private static final int PREVIEW_HEIGHT = 720;
    private int previewWidth = PREVIEW_WIDTH;
    private int previewHeight = PREVIEW_HEIGHT;
    private int fps = 30;

    private Handler mCameraHandler;
    private HandlerThread mCameraThread;

    {
        mCameraThread = new HandlerThread("cameraThread");
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraUtils.close(mCamera);
        mCamera = null;
        mCameraHandler.removeCallbacksAndMessages(null);
        if (mCameraThread != null) {
            mCameraThread.quit();
            mCameraThread = null;
        }
    }
}
