package com.medlink.danbogh.call;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 *
 */
public class EMUIHelper {
    public static void callVideo(Context context, String hisAccount) {
        Intent intent = new Intent(context, VideoCallActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        CallManager.getInstance().setChatId(hisAccount);
        CallManager.getInstance().setInComingCall(false);
        CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
        context.startActivity(intent);
    }
}
