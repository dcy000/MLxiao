package com.medlink.danbogh.call;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 *
 */

public class EMAccountHelper {

    public static void loginBeforeRegister(final String user, final String password) {
       onLogin(user, password);
    }

    public static void login(final String user, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(user, password);
                    onLogin(user, password);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    String msg = e.getDescription();
                    int errorCode=e.getErrorCode();
                    if(errorCode== EMError.NETWORK_ERROR){

                    }else if(errorCode == EMError.USER_ALREADY_EXIST) {
                        onLogin(user, password);
                    }else if(errorCode == EMError.INVALID_USER_NAME){

                    }else{
                        Log.e("EM_SIGN_IN", msg, e);
                    }
                }
            }
        }).start();
    }

    private static void onLogin(String user, String password) {
        EMClient.getInstance().login(user, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //EMClient.getInstance().chatManager().loadAllConversations();
                //EMUIHelper.callVideo(MyApplication.getInstance(), MyApplication.getInstance().emDoctorId);
            }

            @Override
            public void onError(int i, String s) {
                Log.e("EM", i + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
