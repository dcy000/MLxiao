package com.gcml.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.gcml.common.api.AppLifecycleCallbacks;

import java.util.ArrayList;
import java.util.ServiceLoader;

import timber.log.Timber;

/**
 * Created by afirez on 2018/6/15.
 */

public enum AppDelegate implements AppLifecycleCallbacks {
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private Application app;

    private Activity activity;

    private boolean resumed;

    public Application app() {
        return app;
    }

    public Activity activity() {
        return activity;
    }

    public boolean isResumed() {
        return resumed;
    }

    private ServiceLoader<AppLifecycleCallbacks> appLoader;
    private final ArrayList<AppLifecycleCallbacks> appLifecycleCallbacksList = new ArrayList<>();

    private Application.ActivityLifecycleCallbacks appActivityLifecycleCallbacks;
    private ServiceLoader<Application.ActivityLifecycleCallbacks> activityLoader;
    private final ArrayList<Application.ActivityLifecycleCallbacks> activityLifecycleCallbacksList = new ArrayList<>();

    private FragmentManager.FragmentLifecycleCallbacks appFragmentLifecycleCallbacks;
    private ServiceLoader<FragmentManager.FragmentLifecycleCallbacks> fragmentLoader;
    private final ArrayList<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycleCallbacksList = new ArrayList<>();

    @Override
    public void attachBaseContext(Application app, Context base) {
        this.app = app;
        if (appLoader == null) { //load AppLifecycleCallbacks
            appLoader = ServiceLoader.load(AppLifecycleCallbacks.class);
            appLifecycleCallbacksList.clear();
            for (AppLifecycleCallbacks appLifecycleCallbacks : appLoader) {
                appLifecycleCallbacksList.add(appLifecycleCallbacks);
            }
        }
        for (AppLifecycleCallbacks appLifecycleCallbacks : appLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.attachBaseContext(app, base);
                Timber.i("attachBaseContext[after]: %s", appLifecycleCallbacks);
            }
        }
        Timber.i("attachBaseContext[after]: %s", this);
    }

    @Override
    public void onCreate(Application app) {
        for (AppLifecycleCallbacks appLifecycleCallbacks : appLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.onCreate(app);
                Timber.i("onCreate: %s", appLifecycleCallbacks);
            }
        }
        app.registerActivityLifecycleCallbacks(appActivityLifecycleCallbacks());
        if (activityLoader == null) { //load ActivityLifecycleCallbacks
            activityLoader = ServiceLoader.load(Application.ActivityLifecycleCallbacks.class);
            activityLifecycleCallbacksList.clear();
            for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : activityLoader) {
                activityLifecycleCallbacksList.add(activityLifecycleCallbacks);
            }
        }
        for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : activityLifecycleCallbacksList) {
            if (activityLifecycleCallbacks != null) {
                app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }
        if (fragmentLoader == null) { //load FragmentLifecycleCallbacks
            fragmentLoader = ServiceLoader.load(FragmentManager.FragmentLifecycleCallbacks.class);
            fragmentLifecycleCallbacksList.clear();
            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks : fragmentLoader) {
                fragmentLifecycleCallbacksList.add(fragmentLifecycleCallbacks);
            }
        }
        Timber.i("onCreate[after]: %s", this);
    }

    @Override
    public void onTerminate(Application app) {
        for (AppLifecycleCallbacks appLifecycleCallbacks : appLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.onTerminate(app);
                Timber.i("onCreate[after]: %s", appLifecycleCallbacks);
            }
        }
        appLifecycleCallbacksList.clear();
        appLoader = null;

        if (this.appActivityLifecycleCallbacks != null) {
            app.unregisterActivityLifecycleCallbacks(appActivityLifecycleCallbacks);
            appActivityLifecycleCallbacks = null;
        }
        for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : activityLifecycleCallbacksList) {
            if (activityLifecycleCallbacks != null) {
                app.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }
        activityLifecycleCallbacksList.clear();
        appLoader = null;

        appFragmentLifecycleCallbacks = null;
        fragmentLifecycleCallbacksList.clear();
        fragmentLoader = null;
        Timber.i("onTerminate[after]: %s", this);
    }

    private Application.ActivityLifecycleCallbacks appActivityLifecycleCallbacks() {
        if (appActivityLifecycleCallbacks == null) {
            appActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    Timber.i("onActivityCreated: %s %s", activity, savedInstanceState);
                    if (activity instanceof FragmentActivity) {
                        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
                        fm.registerFragmentLifecycleCallbacks(appFragmentLifecycleCallbacks(), true);
                        for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks : fragmentLifecycleCallbacksList) {
                            fm.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true);
                        }
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    Timber.i("onActivityStarted: %s", activity);
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    AppDelegate.this.activity = activity;
                    AppDelegate.this.resumed = true;
                    Timber.i("onActivityResumed: %s", activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    AppDelegate.this.resumed = false;
                    Timber.i("onActivityPaused: %s", activity);
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    Timber.i("onActivityStopped: %s", activity);
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                    Timber.i("onActivitySaveInstanceState: %s %s", activity, outState);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    AppDelegate.this.activity = null;
                    Timber.i("onActivityDestroyed: %s", activity);
                }
            };
        }
        return appActivityLifecycleCallbacks;
    }

    private FragmentManager.FragmentLifecycleCallbacks appFragmentLifecycleCallbacks() {
        if (appFragmentLifecycleCallbacks == null) {
            appFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
                    Timber.i("onFragmentPreAttached: %s %s", f, fm);
                }

                @Override
                public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                    Timber.i("onFragmentAttached: %s %s", f, fm);
                }

                @Override
                public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    Timber.i("onFragmentPreCreated: %s %s", f, fm);
                }

                @Override
                public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    Timber.i("onFragmentCreated: %s %s", f, fm);
                }

                @Override
                public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    Timber.i("onFragmentActivityCreated: %s %s", f, fm);
                }

                @Override
                public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                    Timber.i("onFragmentViewCreated: %s %s", f, fm);
                }

                @Override
                public void onFragmentStarted(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentStarted: %s %s", f, fm);
                }

                @Override
                public void onFragmentResumed(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentResumed: %s %s", f, fm);
                }

                @Override
                public void onFragmentPaused(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentPaused: %s %s", f, fm);
                }

                @Override
                public void onFragmentStopped(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentStopped: %s %s", f, fm);
                }

                @Override
                public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
                    Timber.i("onFragmentSaveInstanceState: %s %s %s", f, fm, outState);
                }

                @Override
                public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentViewDestroyed: %s %s", f, fm);
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentDestroyed: %s %s", f, fm);
                }

                @Override
                public void onFragmentDetached(FragmentManager fm, Fragment f) {
                    Timber.i("onFragmentDetached: %s %s", f, fm);
                }
            };
        }
        return appFragmentLifecycleCallbacks;
    }
}
