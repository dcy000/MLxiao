package com.example.han.referralproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lenovo on 2018/12/17.
 */

public class ResetBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {
        if ("com.gcml.hos.reset".equals(data.getAction())) {
            MobclickAgent.onProfileSignOff();
            NimAccountHelper.getInstance().logout();
            LocalShared.getInstance(context).loginOut();
            Intent intent = new Intent(context, ChoiceLoginTypeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
