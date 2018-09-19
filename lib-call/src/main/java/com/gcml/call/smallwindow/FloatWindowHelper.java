package com.gcml.call.smallwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import timber.log.Timber;

/**
 * Created by afirez on 2018/6/1.
 */

public class FloatWindowHelper {
    private static final String TAG = "FloatWindowHelper";

    private Context context;
    private WindowManager wm;
    private WindowManager.LayoutParams wParams;
    private MyFrameLayout contentParent;
    private View contentView;
    private int width;
    private int height;

    private int lastRotation;
    private OrientationEventListener orientationEventListener;

    public FloatWindowHelper(Context context) {
        context = context.getApplicationContext();
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wParams = new WindowManager.LayoutParams();
        contentParent = new MyFrameLayout(context);
        contentParent.setOnTouchListener(onTouchListener);
        lastRotation = getDisplayRotation();
        orientationEventListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation = getDisplayRotation();
                if (rotation != lastRotation) {
                    switchRotate();
                    lastRotation = rotation;
                }
            }
        };
    }

    public FrameLayout getContentParent() {
        return contentParent;
    }

    public void setContentView(@LayoutRes int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layoutId, contentParent, false);
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getContentView() {
        return contentView;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Timber.tag(TAG).d("onTouchListener -> onTouch: view=%s event=%s", v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = event.getRawX();
                    mTouchStartY = event.getRawY();
                    if (wParams != null) {
                        mOriginX = wParams.x;
                        mOriginY = wParams.y;
                    }
                    v.performClick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateViewPosition(event.getRawX(), event.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                    mTouchStartX = 30;
                    mTouchStartY = 30;
                    break;
            }
            return true;
        }
    };

    private float mTouchStartX;
    private float mTouchStartY;
    private int mOriginX;
    private int mOriginY;

    private void updateViewPosition(float x, float y) {
        if (wParams != null) {
            float offsetX = x - mTouchStartX;
            float offsetY = y - mTouchStartY;
            if ((wParams.gravity & Gravity.RIGHT) != 0) {
                offsetX = -offsetX;
            }
            if ((wParams.gravity & Gravity.BOTTOM) != 0) {
                offsetY = -offsetY;
            }
            wParams.x = (int) (mOriginX + offsetX);
            wParams.y = (int) (mOriginY + offsetY);
            wm.updateViewLayout(contentParent, wParams);
        }
    }

    private void updateViewPosition() {
        if (wParams != null
                && wm != null
                && contentParent != null) {
            wm.updateViewLayout(contentParent, wParams);
        }
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= 26) {
            wParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= 25) {
            wParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            wParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        wParams.format = PixelFormat.RGBA_8888;

        wParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wParams.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        wParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        wParams.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        wParams.gravity = Gravity.RIGHT | Gravity.TOP;
        wParams.x = 0;
        wParams.y = 0;

        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        boolean isLandscape = (getDisplayRotation() % 180) != 0;
        if ((isLandscape && screenWidth < screenHeight) ||
                (!isLandscape) && screenWidth > screenHeight) {
            screenWidth = wm.getDefaultDisplay().getHeight();
            screenHeight = wm.getDefaultDisplay().getWidth();
        }

        if (screenWidth < screenHeight) {
            width = align(screenWidth / 3, 8);
            height = align(screenHeight / 3, 8);
        } else {
            width = align(screenWidth / 3, 8);
            height = align(screenHeight / 3, 8);
        }

        wParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private int align(int value, int align) {
        return (value + align - 1) / align * align;
    }

    private int getDisplayRotation() {
        int rotation = wm.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    private boolean windowAdded;

    public void addWindow() {
        if (wm != null) {
            wm.addView(contentParent, wParams);
        }
        windowAdded = true;
    }

    private void removeWindow() {
        if (wm != null) {
            wm.removeViewImmediate(contentParent);
        }
        windowAdded = false;
    }

    public void show() {
        if (windowAdded) {
            removeWindow();
        }
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
        initWindow();
        addWindow();
        onShow();
    }

    private void onShow() {
        if (mOnShowListener != null) {
            mOnShowListener.onShow();
        }
    }

    public void dismiss() {
        if (windowAdded) {
            if (orientationEventListener != null) {
                orientationEventListener.disable();
            }
            removeWindow();
            onDismiss();
        }
    }

    private void onDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    private OnShowListener mOnShowListener;

    public OnShowListener getOnShowListener() {
        return mOnShowListener;
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        mOnShowListener = onShowListener;
    }

    private OnDismissListener mOnDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public interface OnShowListener {
        void onShow();
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void switchRotate() {
        if (windowAdded) {
            int rotation = getDisplayRotation();
            Timber.tag(TAG).d("switchRotate: %s -> %s", lastRotation, rotation);
            boolean isLastLandscape = (lastRotation % 180) != 0;
            boolean isLandscape = (rotation % 180) != 0;
            if (isLastLandscape != isLandscape) {
                if (contentView != null) {
                    int width = contentView.getHeight();
                    int height = contentView.getWidth();
                    ViewGroup.LayoutParams params = contentView.getLayoutParams();
                    params.width = width;
                    params.height = height;
                    contentParent.updateViewLayout(contentView, params);
                    updateViewPosition();
                }
            }
        }
    }
}
