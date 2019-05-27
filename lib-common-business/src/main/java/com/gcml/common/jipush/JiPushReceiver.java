package com.gcml.common.jipush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.bus.RxBus;
import com.gcml.common.data.MessageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JiPushReceiver extends BroadcastReceiver {
    private static final String TAG = "jpush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Timber.tag(TAG).d("[JiPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Timber.tag(TAG).d("[JiPushReceiver] 接收Registration Id : %s", regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[JiPushReceiver] 接收到推送下来的自定义消息: %s", bundle.getString(JPushInterface.EXTRA_MESSAGE));

                //发送消息到BaseActivity
                RxBus.getDefault().post(new MessageBean(bundle.getString(JPushInterface.EXTRA_TITLE),
                        bundle.getString(JPushInterface.EXTRA_MESSAGE)));
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[JiPushReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Timber.tag(TAG).d("[JiPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                ToastUtils.showShort(bundle.getString(JPushInterface.EXTRA_ALERT));

                //发送消息到BaseActivity
                RxBus.getDefault().post(new MessageBean(bundle.getString(JPushInterface.EXTRA_TITLE),
                        bundle.getString(JPushInterface.EXTRA_ALERT)));

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[JiPushReceiver] 用户点击打开了通知");

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Timber.tag(TAG).d("[JiPushReceiver] 用户收到到RICH PUSH CALLBACK: %s", bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Timber.tag(TAG).d("[JiPushReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Timber.tag(TAG).d("[JiPushReceiver] Unhandled intent - %s", intent.getAction());
            }

        } catch (Exception e) {
            Timber.tag(TAG).e("JiPushReceiver:" + e);
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Timber.tag(TAG).d("Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}