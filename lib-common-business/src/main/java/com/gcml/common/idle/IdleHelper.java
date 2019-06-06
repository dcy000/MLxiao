package com.gcml.common.idle;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.gcml.common.utils.UM;

import timber.log.Timber;

public class IdleHelper {

    private static final int MSG_IDLE = 0;

    public static IdleHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final IdleHelper INSTANCE = new IdleHelper();
    }

    private boolean enable = false;

    private long delayMillis = 1145000L;

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private boolean hasIdle;

    private Handler handler;

    private IdleHelper() {
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case MSG_IDLE:
                        if (!enable) {
                            Timber.i("idle enable = %s", enable);
                            return true;
                        }
                        hasIdle = true;
                        idleRunnable.run();
                        return true;
                }
                return false;
            }
        });

        UM.getApp().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                wrapWindowCallback(activity.getWindow());
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                String simpleName = activity.getClass().getSimpleName();

                if ("Main3Activity".equals(simpleName)
                        || "MainActivity".equals(simpleName)) {
                    IdleHelper.getInstance().setEnable(true);
                }

                if ("AuthActivity".equals(simpleName)
                        || "UserLogins2Activity".equals(simpleName)
                        || "WelcomeActivity".equals(simpleName)) {
                    IdleHelper.getInstance().setEnable(false);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

//        initIdleHandler();
    }

    private static void wrapWindowCallback(Window window) {
        if (window == null) {
            return;
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof WCallback || callback == null) {
            return;
        }
        window.setCallback(IdleHelper.getInstance().new WCallback(callback));
    }

    private class WCallback implements Window.Callback {
        private Window.Callback callback;


        public WCallback(Window.Callback callback) {
            this.callback = callback;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (callback != null) {
                return callback.dispatchKeyEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            if (callback != null) {
                return callback.dispatchKeyShortcutEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (callback != null) {
                return callback.dispatchTouchEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            if (callback != null) {
                return callback.dispatchTrackballEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            Timber.w("IdleHelper dispatchGenericMotionEvent");
            onUserInteraction();
            if (callback != null) {
                return callback.dispatchGenericMotionEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            if (callback != null) {
                return callback.dispatchPopulateAccessibilityEvent(event);
            }
            return false;
        }

        @Nullable
        @Override
        public View onCreatePanelView(int featureId) {
            if (callback != null) {
                return callback.onCreatePanelView(featureId);
            }
            return null;
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            if (callback != null) {
                return callback.onCreatePanelMenu(featureId, menu);
            }
            return false;
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            if (callback != null) {
                return callback.onPreparePanel(featureId, view, menu);
            }
            return false;
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            if (callback != null) {
                return callback.onMenuOpened(featureId, menu);
            }
            return false;
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            if (callback != null) {
                return callback.onMenuItemSelected(featureId, item);
            }
            return false;
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
            if (callback != null) {
                callback.onWindowAttributesChanged(attrs);
            }
        }

        @Override
        public void onContentChanged() {
            if (callback != null) {
                callback.onContentChanged();
            }
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            if (callback != null) {
                callback.onWindowFocusChanged(hasFocus);
            }
        }

        @Override
        public void onAttachedToWindow() {
            if (callback != null) {
                callback.onAttachedToWindow();
            }
        }

        @Override
        public void onDetachedFromWindow() {
            if (callback != null) {
                callback.onDetachedFromWindow();
            }
        }

        @Override
        public void onPanelClosed(int featureId, Menu menu) {
            if (callback != null) {
                callback.onPanelClosed(featureId, menu);
            }
        }

        @Override
        public boolean onSearchRequested() {
            if (callback != null) {
                return callback.onSearchRequested();
            }
            return false;
        }

        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            if (callback != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return callback.onSearchRequested(searchEvent);
                }
            }
            return false;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (this.callback != null) {
                return this.callback.onWindowStartingActionMode(callback);
            }
            return null;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            if (this.callback != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return this.callback.onWindowStartingActionMode(callback, type);
                }
            }
            return null;
        }

        @Override
        public void onActionModeStarted(ActionMode mode) {
            if (callback != null) {
                callback.onActionModeStarted(mode);
            }

        }

        @Override
        public void onActionModeFinished(ActionMode mode) {
            if (callback != null) {
                callback.onActionModeFinished(mode);
            }
        }
    }

    private void onUserInteraction() {
        hasIdle = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onIdle(hasIdle);
                }
            }
        });

        if (handler.hasMessages(MSG_IDLE)) {
            Timber.i("idle remove ");
            handler.removeMessages(MSG_IDLE);
        }
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        Timber.i("idle ...");
        handler.sendEmptyMessageDelayed(MSG_IDLE, delayMillis);
    }

    private Runnable idleRunnable = new Runnable() {
        @Override
        public void run() {
            Timber.i("idle dispatch");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (actionOnIdle != null) {
                        actionOnIdle.run();
                    }
                    if (callback != null) {
                        callback.onIdle(true);
                    }
                }
            });
        }
    };

    private Runnable actionOnIdle;

    public void doOnIdle(Runnable actionOnIdle) {
        this.actionOnIdle = actionOnIdle;
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public interface Callback {
        void onIdle(boolean idle);
    }

}
