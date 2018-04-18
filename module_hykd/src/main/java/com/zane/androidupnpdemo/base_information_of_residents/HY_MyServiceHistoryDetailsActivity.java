package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.bean.ServiceDoctorInformation;
import com.zane.androidupnpdemo.bean.ServiceHistoryInformation;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

/**
 * Created by gzq on 2018/4/17.
 */

public class HY_MyServiceHistoryDetailsActivity extends CommonBaseActivity{
    private int serviceId;
    public static void startActivity(Context context,int serviceId,Class<?> clazz){
        context.startActivity(new Intent(context,clazz).putExtra("serviceId",serviceId));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_service_history_details);
        serviceId=getIntent().getIntExtra("serviceId",-1);
        getData();
    }

    private void getData() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + serviceId, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.RESIDENT_SERVER_DETAILS)
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
                                ServiceHistoryInformation historyInformation=new Gson().fromJson(data.optJSONObject("serviceInfo").toString(), ServiceHistoryInformation.class);
                                ServiceDoctorInformation doctorInfo = new Gson().fromJson(data.optJSONObject("doctorInfo").toString(), ServiceDoctorInformation.class);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
