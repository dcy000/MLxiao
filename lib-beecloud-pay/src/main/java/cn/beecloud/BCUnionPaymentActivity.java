/**
 * BCUnionPaymentActivity.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.beecloud;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.unionpay.UPPayAssistEx;

import cn.beecloud.entity.BCPayResult;

/**
 * 用于银联支付
 */
public class BCUnionPaymentActivity extends Activity {

    private static final Integer TARGET_VERSION = 53;
    private static final String UN_APK_PACKAGE = "com.unionpay.uppay";

    @Override
    public void onStart(){
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tn= extras.getString("tn");
            int retPay;

            int curVer = getUNAPKVersion();
            if (curVer == -1)
                retPay = -1;
            else if (curVer < TARGET_VERSION)
                retPay = 2;
            else
                retPay = UPPayAssistEx.startPay(this, null, null, tn, "00");


            //插件问题 -1表示没有安装插件，2表示插件需要升级
            if (retPay==-1 || retPay==2) {

                if (BCPay.payCallback != null) {
                    BCPay.payCallback.done(new BCPayResult(BCPayResult.RESULT_FAIL,
                            BCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE,
                            (retPay == -1)? BCPayResult.FAIL_PLUGIN_NOT_INSTALLED:BCPayResult.FAIL_PLUGIN_NEED_UPGRADE,
                         "银联插件问题, 需重新安装或升级"));
                } else {
                    Log.e("BCUnionPaymentActivity", "BCPay payCallback NPE");
                }

                this.finish();
            }
        } else {
            finish();
        }
    }

    /**
     * 处理银联手机支付控件返回的支付结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = null;
        int errCode = -99;
        String errMsg = null;
        String detailInfo = "银联支付:";

        //银联插件内部升级会出现data为null的情况
        if (data == null) {
            result = BCPayResult.RESULT_FAIL;
            errCode = BCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE;
            errMsg = BCPayResult.FAIL_ERR_FROM_CHANNEL;
            detailInfo += "invalid pay result";
        } else {
            /*
             * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
             */
            String str = data.getExtras().getString("pay_result");

            if (str == null) {
                result = BCPayResult.RESULT_FAIL;
                errCode = BCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE;
                errMsg = BCPayResult.FAIL_ERR_FROM_CHANNEL;
                detailInfo += "invalid pay result";
            } else if (str.equalsIgnoreCase("success")) {
                result = BCPayResult.RESULT_SUCCESS;
                errCode = BCPayResult.APP_PAY_SUCC_CODE;
                errMsg = BCPayResult.RESULT_SUCCESS;
                detailInfo += "支付成功！";
            } else if (str.equalsIgnoreCase("fail")) {
                result = BCPayResult.RESULT_FAIL;
                errCode = BCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE;
                errMsg = BCPayResult.RESULT_FAIL;
                detailInfo += "支付失败！";
            } else if (str.equalsIgnoreCase("cancel")) {
                result = BCPayResult.RESULT_CANCEL;
                errCode = BCPayResult.APP_PAY_CANCEL_CODE;
                errMsg = BCPayResult.RESULT_CANCEL;
                detailInfo += "用户取消了支付";
            }
        }

        if (BCPay.payCallback != null) {
            BCPay.payCallback.done(new BCPayResult(result, errCode, errMsg,
                    detailInfo, BCCache.getInstance().billID));
        } else {
            Log.e("BCUnionPaymentActivity", "BCPay payCallback NPE");
        }

        this.finish();
    }

    private int getUNAPKVersion() {
        Integer version = -1;

        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo Info=packageManager.getPackageInfo(UN_APK_PACKAGE, 0);
            version = Info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("union payment", e.getMessage()==null ?
                    "PackageManager.NameNotFoundException":e.getMessage());
        }

        return version;
    }
}
