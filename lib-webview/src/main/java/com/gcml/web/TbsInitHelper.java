package com.gcml.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.tencent.smtt.sdk.QbSdk;

public class TbsInitHelper {

    public interface Listener {
        void onInitComplete();

        void onInitError();
    }

    private static Listener listener;

    public static void setListener(Listener listener) {
        TbsInitHelper.listener = listener;

        if (inited) {
            if (TbsInitHelper.listener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TbsInitHelper.listener != null) {
                            TbsInitHelper.listener.onInitComplete();
                        }
                    }
                });
            }
        }
    }

    private static Context appContext;

    private static boolean inited;

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void initAsync(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                init(context);
            }
        }).start();
    }

    public static void init(Context context) {
        appContext = context.getApplicationContext();
//        if (!QbSdk.isTbsCoreInited()) {
//            QbSdk.preInit(appContext, null);
//        }
        QbSdk.initX5Environment(appContext, cb);
    }

    private static QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onViewInitFinished(boolean success) {
            if (!success) {
                Log.e("TbsInitHelper", "onViewInitFinished success = " + success);
                init(appContext);
                if (TbsInitHelper.listener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TbsInitHelper.listener != null) {
                                TbsInitHelper.listener.onInitError();
                            }
                        }
                    });
                }
            } else {
                inited = true;
                if (TbsInitHelper.listener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TbsInitHelper.listener != null) {
                                TbsInitHelper.listener.onInitComplete();
                            }
                        }
                    });
                }
            }
        }

        @Override
        public void onCoreInitFinished() {
            Log.e("TbsInitHelper", "onCoreInitFinished");
        }
    };
}
