package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.bean.PageInformation;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gzq on 2018/4/13.
 */

public class MyServicedHistoryActivity extends CommonBaseActivity {
    private PageInformation pageInformation;
    private TextView mHyTvServiceTime;
    private TextView mHyTvServiceDoctor;

    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_my_serviced_history);
        initView();
        getData();
    }

    private void getData() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.RESIDENT_SERVER_HISTORY)
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
                                JSONObject pageInfo = data.optJSONObject("pageInfo");
                                pageInformation = new Gson().fromJson(pageInfo.toString(), PageInformation.class);
                                String doctorName=pageInformation.list.get(0).doctorName;
                                String serviceTime = pageInformation.list.get(0).serviceTime;
                                mHyTvServiceDoctor.setText(doctorName);
                                String[] time_s = serviceTime.split("\\s+")[0].split("-");
                                mHyTvServiceTime.setText(time_s[0]+"年"+time_s[1]+"月"+time_s[2]+"日");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mHyTvServiceTime = (TextView) findViewById(R.id.hy_tv_service_time);
        mHyTvServiceDoctor = (TextView) findViewById(R.id.hy_tv_service_doctor);
    }
}
