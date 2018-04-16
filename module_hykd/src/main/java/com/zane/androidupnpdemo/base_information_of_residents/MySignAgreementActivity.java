package com.zane.androidupnpdemo.base_information_of_residents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gzq on 2018/4/13.
 */

public class MySignAgreementActivity extends CommonBaseActivity {
    private WebView mAgreement;
    private int id;

    public static void startActivity(Context context,int param,Class<?> clazz){
        context.startActivity(new Intent(context,clazz)
        .putExtra("id",param));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_agreement);
        initView();
        getData();
    }
    private void getData() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() +"_" + id, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() +"_" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.RESIDENT_SIGN_FILE)
                .params("param", param)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求结果", response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == -1) {
                                ToastTool.showShort("网络请求出现错误");
                                return;
                            }
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                String protocol=data.optString("protocol");
                                if (!TextUtils.isEmpty(protocol)){
                                    setWeb(HYKD_API.DOMAIN_NAME+protocol);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @SuppressLint("WrongConstant")
    private void setWeb(String protocol) {
        mAgreement.loadUrl(protocol);
    }

    private void initView() {
        mAgreement = (WebView) findViewById(R.id.agreement);
        id=getIntent().getIntExtra("id",-1);
    }
}
