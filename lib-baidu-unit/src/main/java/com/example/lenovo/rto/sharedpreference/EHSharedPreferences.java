package com.example.lenovo.rto.sharedpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lenovo.rto.accesstoken.AccessToken;


/**
 * Created by huyin on 2017/9/10.
 */

public class EHSharedPreferences {
    public static SharedPreferences sharedPreferences;
    public static Context context;

    @SuppressLint("WrongConstant")
    public static void initSharedPreferences(String fileName) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
    }
    public static void initUNITContext(Context context){
        EHSharedPreferences.context=context.getApplicationContext();
    }

    public static void WriteInfo(String fileName, AccessToken accessToken) {
        initSharedPreferences(fileName);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken.getAccessToken());
        editor.putString("refreshToken", accessToken.getRefreshToken());
        editor.putString("sessionKey", accessToken.getSessionKey());
        editor.putString("sessionSecret", accessToken.getSessionSecret());
        editor.putString("scope", accessToken.getScope());
        editor.putInt("expiresIn", accessToken.getExpiresIn());
        editor.commit();
    }

    public static AccessToken ReadAccessToken(String fileNmae) {

        initSharedPreferences(fileNmae);
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(sharedPreferences.getString("accessToken", ""));
        accessToken.setRefreshToken(sharedPreferences.getString("refreshToken", ""));
        accessToken.setSessionKey(sharedPreferences.getString("sessionKey", ""));
        accessToken.setSessionSecret(sharedPreferences.getString("sessionSecret", ""));
        accessToken.setScope(sharedPreferences.getString("scope", ""));
        accessToken.setExpiresIn(sharedPreferences.getInt("expiresIn", 0));

        return accessToken;
    }
}
