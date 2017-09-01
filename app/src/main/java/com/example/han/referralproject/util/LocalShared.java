package com.example.han.referralproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;


public class LocalShared {
    private final String SharedName = "ScopeMediaPrefsFile";
    private static LocalShared mInstance;
    private SharedPreferences mShared;

    private final String UserId = "user_id";

    private final String Guide_Add_Click = "guide_add_click";
    private final String Guide_Create_Text = "guide_create_text";
    private final String Guide_Sign_In = "guide_sign_in_two";

    private LocalShared(Context context){
        mShared = context.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
    }

    public static LocalShared getInstance(Context context){
        if (mInstance == null){
            mInstance = new LocalShared(context);
        }
        return mInstance;
    }


    public String getUserId(){
        return mShared.getString(UserId, "");
    }

    public void setUserInfo(UserInfoBean infoBean){
        if (infoBean == null) {
            return;
        }
        MyApplication.getInstance().userId = infoBean.bid;
        mShared.edit().putString(UserId, infoBean.bid)
                .putString(UserId, infoBean.bid).commit();
    }

    public void loginOut(){
        boolean isShowAddGuide = isShowAddGuide();
        mShared.edit().clear();
    }

    public boolean isShowAddGuide(){
        return mShared.getBoolean(Guide_Add_Click, true);
    }

    public void haveShowSignInGuide(){
        mShared.edit().putBoolean(Guide_Sign_In, false).commit();
    }

    public boolean isShowSignInGuide(){
        return mShared.getBoolean(Guide_Sign_In, true);
    }

    public void haveShowAddGuide(){
        mShared.edit().putBoolean(Guide_Add_Click, false).commit();
    }


    public boolean isShowCreateTextGuide(){
        return mShared.getBoolean(Guide_Create_Text, true);
    }

    public void haveShowCreateTextGuide(){
        mShared.edit().putBoolean(Guide_Create_Text, false).commit();
    }
}
