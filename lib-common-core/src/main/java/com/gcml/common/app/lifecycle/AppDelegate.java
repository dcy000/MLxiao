package com.gcml.common.app.lifecycle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.gcml.common.utils.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by afirez on 2018/6/15.
 */

public enum AppDelegate implements AppLifecycleCallbacks {
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private static final String APP_LIFECYCLE_CALLBACKS = "AppLifecycleCallbacks";
    private static final String ACTIVITY_LIFECYCLE_CALLBACKS = "ActivityLifecycleCallbacks";
    private static final String FRAGMENT_LIFECYCLE_CALLBACKS = "FragmentLifecycleCallbacks";

    private Application app;

    public Application app() {
        return app;
    }

    private ManifestParser<AppLifecycleCallbacks> mAppLifecycleCallbacksManifestParser;
    private final ArrayList<AppLifecycleCallbacks> mAppLifecycleCallbacksList = new ArrayList<>();

    private Application.ActivityLifecycleCallbacks mAppActivityLifecycleCallbacks;
    private ManifestParser<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacksManifestParser;
    private final ArrayList<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacksList = new ArrayList<>();

    private FragmentManager.FragmentLifecycleCallbacks mAppFragmentLifecycleCallbacks;
    private ManifestParser<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycleCallbacksManifestParser;
    private final ArrayList<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycleCallbacksList = new ArrayList<>();

    @Override
    public void attachBaseContext(Application app, Context base) {
        this.app = app;
        if (mAppLifecycleCallbacksManifestParser == null) {
            mAppLifecycleCallbacksManifestParser = new ManifestParser<>(app, APP_LIFECYCLE_CALLBACKS);
            //parse AppLifecycleCallbacks
            List<AppLifecycleCallbacks> appLifecycleCallbacksList = mAppLifecycleCallbacksManifestParser.parse();
            mAppLifecycleCallbacksList.clear();
            mAppLifecycleCallbacksList.addAll(appLifecycleCallbacksList);
        }
        for (AppLifecycleCallbacks appLifecycleCallbacks : mAppLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.attachBaseContext(app, base);
                Timber.i("%s.attachBaseContext[after]: ", appLifecycleCallbacks.getClass().getName());
            }
        }
        Timber.i("%s.attachBaseContext[after]: ", "AppDelegate");
    }

    @Override
    public void onCreate(Application app) {
        for (AppLifecycleCallbacks appLifecycleCallbacks : mAppLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.onCreate(app);
                Timber.i("%s.onCreate: ", appLifecycleCallbacks.getClass().getName());
            }
        }
        app.registerActivityLifecycleCallbacks(appActivityLifecycleCallbacks());
        if (mActivityLifecycleCallbacksManifestParser == null) {
            mActivityLifecycleCallbacksManifestParser = new ManifestParser<>(app, ACTIVITY_LIFECYCLE_CALLBACKS);
            //parse ActivityLifecycleCallbacks
            List<Application.ActivityLifecycleCallbacks> activityLifecycleCallbacksList = mActivityLifecycleCallbacksManifestParser.parse();
            mActivityLifecycleCallbacksList.clear();
            mActivityLifecycleCallbacksList.addAll(activityLifecycleCallbacksList);
        }
        for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : mActivityLifecycleCallbacksList) {
            if (activityLifecycleCallbacks != null) {
                app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }
        if (mFragmentLifecycleCallbacksManifestParser == null) {
            mFragmentLifecycleCallbacksManifestParser = new ManifestParser<>(app, FRAGMENT_LIFECYCLE_CALLBACKS);
            //parse FragmentLifecycleCallbacks
            List<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycleCallbacksList = mFragmentLifecycleCallbacksManifestParser.parse();
            mFragmentLifecycleCallbacksList.clear();
            mFragmentLifecycleCallbacksList.addAll(fragmentLifecycleCallbacksList);
        }
        Timber.i("%s.onCreate[after]: ", "AppDelegate");
    }

    @Override
    public void onTerminate(Application app) {
        for (AppLifecycleCallbacks appLifecycleCallbacks : mAppLifecycleCallbacksList) {
            if (appLifecycleCallbacks != null) {
                appLifecycleCallbacks.onTerminate(app);
                Timber.i("%s.onCreate[after]: ", appLifecycleCallbacks.getClass().getName());
            }
        }
        mAppLifecycleCallbacksList.clear();
        mAppLifecycleCallbacksManifestParser = null;

        if (mAppActivityLifecycleCallbacks != null) {
            app.unregisterActivityLifecycleCallbacks(mAppActivityLifecycleCallbacks);
            mAppActivityLifecycleCallbacks = null;
        }
        for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : mActivityLifecycleCallbacksList) {
            if (activityLifecycleCallbacks != null) {
                app.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }
        mActivityLifecycleCallbacksList.clear();
        mActivityLifecycleCallbacksManifestParser = null;

        mAppFragmentLifecycleCallbacks = null;
        mFragmentLifecycleCallbacksList.clear();
        mFragmentLifecycleCallbacksManifestParser = null;
        Timber.i("%s.onTerminate[after]: ", "AppDelegate");
    }

    private Application.ActivityLifecycleCallbacks appActivityLifecycleCallbacks() {
        if (mAppActivityLifecycleCallbacks == null) {
            mAppActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    Timber.i("onActivityCreated: %s %s", activity, savedInstanceState);
                    if (activity instanceof FragmentActivity) {
                        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
                        fm.registerFragmentLifecycleCallbacks(appFragmentLifecycleCallbacks(), true);
                        for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks : mFragmentLifecycleCallbacksList) {
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
                    Timber.i("onActivityResumed: %s", activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {
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
                    Timber.i("onActivityDestroyed: %s", activity);
                }
            };
        }
        return mAppActivityLifecycleCallbacks;
    }

    private FragmentManager.FragmentLifecycleCallbacks appFragmentLifecycleCallbacks() {
        if (mAppFragmentLifecycleCallbacks == null) {
            mAppFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
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
        return mAppFragmentLifecycleCallbacks;
    }
}
