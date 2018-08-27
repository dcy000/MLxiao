package com.gcml.auth.face.utils;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by afirez on 2018/3/14.
 */

public class PreviewHelper implements SurfaceHolder.Callback {

    private static final String TAG = "PreviewHelper";
    private Context mContext;

    public PreviewHelper(Context context) {
        mContext = context;
    }

    private Camera mCamera;

    private SurfaceHolder mSurfaceHolder;

    public void setCamera(Camera camera) {
        if (camera == null) {
            Log.d(TAG, "setCamera (camera == null)");
        }
        mCamera = camera;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            Log.d(TAG, "setSurfaceHolder (surfaceHolder == null)");
        }
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated -> startPreview");
        CameraUtils.startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            Log.d(TAG, "surfaceChanged -> restartPreview failed (surface == null)");
            return;
        }
        Log.d(TAG, "surfaceChanged -> restartPreview success");
        CameraUtils.followScreenOrientation(mContext, mCamera);
        CameraUtils.stopPreview(mCamera);
        CameraUtils.startPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed -> stopPreview");
        CameraUtils.stopPreview(mCamera);
    }

    private Handler mCameraHandler;
    private HandlerThread cameraThread;

    public Handler cameraHandler() {
        if (mCameraHandler == null) {
            synchronized (this) {
                if (mCameraHandler == null) {
                    cameraThread = new HandlerThread("CameraThread");
                    cameraThread.start();
                    mCameraHandler = new Handler(cameraThread.getLooper());
                }
            }
        }
        return mCameraHandler;
    }
}
