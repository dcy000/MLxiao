package com.example.han.referralproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;

public class LocalShared {
    private final String SharedName = "ScopeMediaPrefsFile";
    private static LocalShared mInstance;
    private SharedPreferences mShared;

    private final String UserAccounts = "user_accounts";
    private final String UserId = "user_id";
    private final String UserImg = "user_img";
    private final String XunfeiId = "Xunfei_Id";
    private final String UserPhoneNum = "user_phone_num";

    private final String Guide_Add_Click = "guide_add_click";
    private final String Guide_Create_Text = "guide_create_text";
    private final String Guide_Sign_In = "guide_sign_in_two";

    private LocalShared(Context context) {
        mShared = context.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
    }

    public static LocalShared getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocalShared(context);
        }
        return mInstance;
    }

//    public String addAccount() {
//        return mShared.get(UserAccounts, "");
//    }

    public String getUserId() {
        return mShared.getString(UserId, "");
    }

    public String getPhoneNum() {
        return mShared.getString(UserPhoneNum, "");
    }

    public void setUserInfo(UserInfoBean infoBean) {
        if (infoBean == null) {
            return;
        }
        MyApplication.getInstance().userId = infoBean.bid;
        MyApplication.getInstance().telphoneNum = infoBean.tel;
        mShared.edit().putString(UserId, infoBean.bid).putString(UserPhoneNum, infoBean.tel).commit();
    }

    public void setUserImg(String imgUrl) {
        mShared.edit().putString(UserImg, imgUrl).commit();
    }

    public String getUserImg() {
        return mShared.getString(UserImg, "");
    }
    public void setXunfeiID(String imgUrl) {
        mShared.edit().putString(XunfeiId, imgUrl).commit();
    }

    public String getXunfeiId() {
        return mShared.getString(XunfeiId, "");
    }
    public void loginOut() {
        MyApplication.getInstance().userId = null;
        mShared.edit().clear().commit();
    }

    public boolean isShowAddGuide() {
        return mShared.getBoolean(Guide_Add_Click, true);
    }

    public void haveShowSignInGuide() {
        mShared.edit().putBoolean(Guide_Sign_In, false).commit();
    }

    public boolean isShowSignInGuide() {
        return mShared.getBoolean(Guide_Sign_In, true);
    }

    public void haveShowAddGuide() {
        mShared.edit().putBoolean(Guide_Add_Click, false).commit();
    }


    public boolean isShowCreateTextGuide() {
        return mShared.getBoolean(Guide_Create_Text, true);
    }

    public void haveShowCreateTextGuide() {
        mShared.edit().putBoolean(Guide_Create_Text, false).commit();
    }

    private static final String SIGN_UP_NAME = "sign_up_name";
    private static final String SIGN_UP_GENDER = "sign_up_gender";
    private static final String SIGN_UP_ADDRESS = "sign_up_address";
    private static final String SIGN_UP_ID_CARD = "sign_up_id_card";
    private static final String SIGN_UP_PHONE = "sign_up_phone";
    private static final String SIGN_UP_PASSWORD = "sign_up_password";
    private static final String SIGN_UP_HEIGHT = "sign_up_height";
    private static final String SIGN_UP_WEIGHT = "sign_up_weight";
    private static final String SIGN_UP_BLOOD_TYPE = "sign_up_blood_type";
    private static final String SIGN_UP_EAT = "sign_up_eat";
    private static final String SIGN_UP_SMOKE = "sign_up_smoke";
    private static final String SIGN_UP_DRINK = "sign_up_drink";
    private static final String SIGN_UP_SPORT = "sign_up_sport";

    public void setSignUpName(String name) {
        mShared.edit().putString(SIGN_UP_NAME, name).apply();
    }

    public String getSignUpName() {
        return mShared.getString(SIGN_UP_NAME, "");
    }

    public void setSignUpGender(String gender) {
        mShared.edit().putString(SIGN_UP_GENDER, gender).apply();
    }

    public String getSignUpGender() {
        return mShared.getString(SIGN_UP_GENDER, "");
    }

    public void setSignUpAddress(String address) {
        mShared.edit().putString(SIGN_UP_ADDRESS, address).apply();
    }

    public String getSignUpAddress() {
        return mShared.getString(SIGN_UP_ADDRESS, "");
    }

    public void setSignUpIdCard(String idCard) {
        mShared.edit().putString(SIGN_UP_ID_CARD, idCard).apply();
    }

    public String getSignUpIdCard() {
        return mShared.getString(SIGN_UP_ID_CARD, "");
    }

    public void setSignUpPhone(String phone) {
        mShared.edit().putString(SIGN_UP_PHONE, phone).apply();
    }

    public String getSignUpPhone() {
        return mShared.getString(SIGN_UP_PHONE, "");
    }

    public void setSignUpPassword(String password) {
        mShared.edit().putString(SIGN_UP_PASSWORD, password).apply();
    }

    public String getSignUpPassword() {
        return mShared.getString(SIGN_UP_PASSWORD, "");
    }

    public void setSignUpHeight(float height) {
        mShared.edit().putFloat(SIGN_UP_HEIGHT, height).apply();
    }

    public float getSignUpHeight() {
        return mShared.getFloat(SIGN_UP_HEIGHT, 180f);
    }

    public void setSignUpWeight(float weight) {
        mShared.edit().putFloat(SIGN_UP_WEIGHT, weight).apply();
    }

    public float getSignUpWeight() {
        return mShared.getFloat(SIGN_UP_WEIGHT, 65f);
    }

    public void setSignUpBloodType(String bloodType) {
        mShared.edit().putString(SIGN_UP_BLOOD_TYPE, bloodType).apply();
    }

    public String getSignUpBloodType() {
        return mShared.getString(SIGN_UP_BLOOD_TYPE, "A");
    }

    public void setSignUpEat(String eat) {
        mShared.edit().putString(SIGN_UP_EAT, eat).apply();
    }

    public String getSignUpEat() {
        return mShared.getString(SIGN_UP_EAT, "1");
    }

    public void setSignUpSmoke(String smoke) {
        mShared.edit().putString(SIGN_UP_SMOKE, smoke).apply();
    }

    public  String getSignUpSmoke() {
        return mShared.getString(SIGN_UP_SMOKE, "1");
    }

    public void setSignUpDrink(String drink) {
        mShared.edit().putString(SIGN_UP_DRINK, drink).apply();
    }

    public String getSignUpDrink() {
        return mShared.getString(SIGN_UP_DRINK, "1");
    }

    public void setSignUpSport(String sport) {
        mShared.edit().putString(SIGN_UP_SPORT, sport).apply();
    }

    public String getSignUpSport() {
        return mShared.getString(SIGN_UP_SPORT, "1");
    }

    private static final String NIM_ACCOUNT = "nim_account";
    private static final String NIM_TOKEN = "nim_token";

    public void setNimAccount(String account) {
        mShared.edit().putString(NIM_ACCOUNT, account).apply();
    }

    public String getNimAccount() {
        return mShared.getString(NIM_ACCOUNT, "");
    }

    public void setNimToken(String token) {
        mShared.edit().putString(NIM_TOKEN, token).apply();
    }

    public String getNimToken() {
        return mShared.getString(NIM_TOKEN, "");
    }
}
