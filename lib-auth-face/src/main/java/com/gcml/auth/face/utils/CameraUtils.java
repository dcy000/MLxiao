package com.gcml.auth.face.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

/**
 * Created by afirez on 18-1-17.
 */

public class CameraUtils {

    private static final String TAG = "CameraUtils";

    public static int getCameraId(int cameraFacing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                return i;
            }
        }
        return 0;
    }

    public static Camera openByFacing(int cameraFacing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                return openById(i);
            }
        }
        return null;
    }

    public static Camera openById(int cameraId) {
        try {
            Timber.i("Camera open ...");
            return Camera.open(cameraId);
        } catch (Exception ignore) {
            try {
                Timber.i("Camera reopen after 200 millis ...");
                Thread.sleep(200);
                return Camera.open(cameraId);
            } catch (Throwable e) {
                Timber.i("Camera open error");
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Camera reconnect(Camera camera) {
        if (camera == null) {
            return null;
        }
        try {
            camera.reconnect();
            Timber.i("camera reconnect success");
            return camera;
        } catch (Throwable e) {
            Timber.e(e, "camera reconnect error");
            try {
                camera.release();
            } catch (Throwable e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    public static void close(Camera camera) {
        if (camera == null) {
            return;
        }
        isPreviewing = false;
        try {
            camera.release();
            Timber.i("Camera release");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void followScreenOrientation(Context context, Camera camera) {
        if (context == null || camera == null) {
            Timber.e("context == null or camera == null while followScreenOrientation for camera");
            return;
        }
        final int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(180);
            Timber.i("orientation %s", 180);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
            Timber.i("orientation %s", 90);
        }
    }

    private static volatile boolean isPreviewing;

    public static void startPreview(Camera camera, SurfaceHolder holder) {
        if (camera == null || holder == null) {
            Timber.e("camera == null or holder == null while START preview for camera");
            return;
        }
        if (isPreviewing) {
            Timber.i("isPreviewing: %s", isPreviewing);
            return;
        }
        isPreviewing = true;

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            isPreviewing = false;
            Timber.e(e, "Error while START preview for camera");
        }
    }

    public static void stopPreview(Camera camera) {
        if (camera == null) {
            Timber.e("camera == null while START preview for camera");
            return;
        }
        isPreviewing = false;
        try {
            camera.stopPreview();
            camera.setPreviewDisplay(null);
        } catch (Exception e) {
            Timber.e(e, "Error while STOP preview for camera");
        }
    }

    public static void setPreviewCallback(Camera camera, Camera.PreviewCallback previewCallback) {
        if (camera == null) {
            Timber.e("camera == null while setOneShotPreviewCallback for camera");
            return;
        }
        Timber.i("previewCallback: %s", previewCallback);
        try {
            camera.setPreviewCallback(previewCallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setOneShotPreviewCallback(Camera camera, Camera.PreviewCallback previewCallback) {
        if (camera == null) {
            Timber.e("camera == null while setOneShotPreviewCallback for camera");
            return;
        }
        Timber.i("previewCallback: %s", previewCallback);
        try {
            camera.setOneShotPreviewCallback(previewCallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setPreviewCallbackWithBuffer(Camera camera, Camera.PreviewCallback previewCallback) {
        if (camera == null) {
            Timber.e("camera == null while setOneShotPreviewCallback for camera");
            return;
        }
        Timber.i("previewCallback: %s", previewCallback);
        try {
            camera.setPreviewCallbackWithBuffer(previewCallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void addCallbackBuffer(Camera camera, byte[] buffer) {
        if (camera == null) {
            Timber.e("camera == null while addCallbackBuffer for camera");
            return;
        }
        Timber.i("addCallbackBuffer: %s", buffer);
        try {
            camera.addCallbackBuffer(buffer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Camera.Size calculateSize(List<Camera.Size> sizes, int targetWidth, int targetHeight) {
        if (sizes == null || sizes.isEmpty()) {
            return null;
        }
        sortSizes(sizes);
        Camera.Size targetSize = sizes.get(0);
        boolean singleCalculated = false;
        for (Camera.Size size : sizes) {
            Timber.i("SupportedPreviewSize: %s x %s", size.width, size.height);
            if (size.width == targetWidth && size.height == targetHeight) {
                // 宽高相等，直接返回
                targetSize = size;
                break;
            }
            if (size.width == targetWidth) {
                // 宽相等，选高最接近的
                singleCalculated = true;
                if (Math.abs(targetSize.height - targetHeight)
                        > Math.abs(size.height - targetHeight)) {
                    targetSize = size;
                }
            } else if (targetSize.height == targetHeight) {
                // 高相等，选宽最接近的
                singleCalculated = true;
                if (Math.abs(targetSize.width - targetWidth)
                        > Math.abs(size.width - targetWidth)) {
                    targetSize = size;
                }
            } else if (!singleCalculated) {
                // 没有宽或高相等的， 选宽和高均为最接近的
                if (Math.abs(targetSize.width - targetWidth)
                        > Math.abs(size.width - targetWidth)
                        && Math.abs(targetSize.height - targetHeight)
                        > Math.abs(size.height - targetHeight)) {
                    targetSize = size;
                }
            }
        }
        Timber.i("TargetPreviewSize: %s x %s", targetSize.width, targetSize.height);
        return targetSize;
    }

    private static void sortSizes(List<Camera.Size> sizes) {
        if (sizes == null || sizes.isEmpty()) {
            return;
        }
        Collections.sort(sizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                if (a.width > b.width) {
                    return 1;
                } else if (a.width < b.width) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public static int calculateAndSetFps(Camera.Parameters parameters, int targetThousandFps) {
        if (parameters == null) {
            return 0;
        }
        List<int[]> supportedFpsRanges = parameters.getSupportedPreviewFpsRange();
        for (int[] fpsRange : supportedFpsRanges) {
            if (fpsRange[0] == fpsRange[1] && fpsRange[0] == targetThousandFps) {
                parameters.setPreviewFpsRange(fpsRange[0], fpsRange[1]);
                return fpsRange[0];
            }
        }
        int targetFps;
        int[] tempFpsRange = new int[2];
        parameters.getPreviewFpsRange(tempFpsRange);
        if (tempFpsRange[0] == tempFpsRange[1]) {
            targetFps = tempFpsRange[0];
        } else {
            targetFps = tempFpsRange[1] / 2;
        }
        return targetFps;
    }

    public static int calculateRotation(Activity activity, int cameraId) {
        if (activity == null) {
            return 0;
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
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
        int targetRotation;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            targetRotation = (cameraInfo.orientation + degrees) % 360;
            targetRotation = (360 - targetRotation) % 360;
        } else {
            targetRotation = (360 + cameraInfo.orientation - degrees) % 360;
        }
        return targetRotation;
    }

    public static int calculateBufferSize(Camera camera) {
        if (camera == null) {
            return 0;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return 0;
        }
        Camera.Size previewSize = parameters.getPreviewSize();
        if (previewSize == null) {
            return 0;
        }
        int previewFormat = parameters.getPreviewFormat();
        int bitsPerPixel = ImageFormat.getBitsPerPixel(previewFormat);
        float bytesPerPixel = bitsPerPixel / 8f;
        return (int) (previewSize.height * previewSize.width * bytesPerPixel);
    }

    public static boolean hasCameraDevice(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean isAutoFocusSupported(Camera.Parameters params) {
        List<String> modes = params.getSupportedFocusModes();
        return modes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }
}
