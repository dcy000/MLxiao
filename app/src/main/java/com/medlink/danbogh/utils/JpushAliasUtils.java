package com.medlink.danbogh.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.util.LocalShared;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by gzq on 2018/1/18.
 */

public class JpushAliasUtils {
    private Context context;

    public JpushAliasUtils(Context context) {
        this.context = context;
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public void setAlias(String userId) {
        if (TextUtils.isEmpty(userId)) {
            Log.e(TAG, "setAlias:设置别名不能为空");
            return;
        }
        if (JPushInterface.isPushStopped(context)) {
            JPushInterface.resumePush(context);
        }
        // 调用 Handler 来异步设置别名
        mHandler2.sendMessage(mHandler2.obtainMessage(MSG_SET_ALIAS, userId));
    }

    private String TAG = "极光推送";
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    Log.i(TAG, "设置别名成功");
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler2.sendMessageDelayed(mHandler2.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;

    private final Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
//                    JPushInterface.setAlias(context.getApplicationContext(),2000,(String) msg.obj);
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context.getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    public void deleteAlias(){
        // 调用 Handler 来异步设置别名
        JPushInterface.stopPush(context);
    }
}
