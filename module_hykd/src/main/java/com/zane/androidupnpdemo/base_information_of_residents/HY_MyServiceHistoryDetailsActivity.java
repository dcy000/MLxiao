package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzq on 2018/4/17.
 */

public class HY_MyServiceHistoryDetailsActivity extends CommonBaseActivity {
    private int serviceId;
    /**
     * 服务时间：2017年4月5日
     */
    private TextView mTvServiceTime;
    /**
     * 陈付完
     */
    private TextView mTvServiceDoctor;
    /**
     * 无
     */
    private TextView mTvDiseaseHistory;
    /**
     * 面对面访视
     */
    private TextView mTvServiceType;
    /**
     * 高血压健康管理
     */
    private TextView mTvServiceClassic;
    private RecyclerView mRvServiceContent;
    private List<ServiceHistoryInformation.ServiceContentBean> mData;
    private BaseQuickAdapter<ServiceHistoryInformation.ServiceContentBean, BaseViewHolder> adapter;

    public static void startActivity(Context context, int serviceId, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz).putExtra("serviceId", serviceId));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_service_history_details);
        initView();
        serviceId = getIntent().getIntExtra("serviceId", -1);
        setAdapter();
        getData();
    }

    private void setAdapter() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        mRvServiceContent.setLayoutManager(manager);
        mRvServiceContent.setHasFixedSize(true);
        mRvServiceContent.setNestedScrollingEnabled(false);
        mRvServiceContent.setAdapter(adapter=new BaseQuickAdapter<ServiceHistoryInformation.ServiceContentBean,BaseViewHolder>(R.layout.hy_item_history_service,mData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ServiceHistoryInformation.ServiceContentBean serviceContentBean) {
                baseViewHolder.setText(R.id.tv_title,serviceContentBean.getTitle());
                List<String> contents=serviceContentBean.getList();
                if (contents.size()>0) {
                    baseViewHolder.setText(R.id.tv_content,contents.toString().substring(1,contents.toString().length()-1));
                }
            }
        });
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
                                ServiceHistoryInformation historyInformation = new Gson().fromJson(data.optJSONObject("serviceInfo").toString(), ServiceHistoryInformation.class);
                                ServiceDoctorInformation doctorInfo = new Gson().fromJson(data.optJSONObject("doctorInfo").toString(), ServiceDoctorInformation.class);
                                String serviceTime = historyInformation.getServiceTime();
                                if (!TextUtils.isDigitsOnly(serviceTime)) {
                                    String[] serviceTimes = serviceTime.split("\\s+")[0].split("-");
                                    mTvServiceTime.setText("服务时间：" + serviceTimes[0] + "年" + serviceTimes[1] + "月" + serviceTimes[2] + "日");
                                }
                                mTvServiceDoctor.setText(doctorInfo.getName());
                                mTvDiseaseHistory.setText(historyInformation.getHealthy());
                                mTvServiceType.setText(historyInformation.getInterview() == 1 ? "面对面访视" : "视频访视");
                                StringBuffer buffer = new StringBuffer();
                                for (String str : historyInformation.getCrowdSpecial()) {
                                    buffer.append(str + ";");
                                }
                                if (buffer.length() > 1)
                                    mTvServiceClassic.setText(buffer.substring(0, buffer.length() - 1));
                                mData.addAll(historyInformation.getServiceContent());
                                List<String> xueya=new ArrayList<>();
                                xueya.add(historyInformation.getBloodPressure());
                                mData.add(new ServiceHistoryInformation.ServiceContentBean("血压",xueya));
                                List<String> xuetang=new ArrayList<>();
                                xuetang.add(historyInformation.getBloodSugar());
                                mData.add(new ServiceHistoryInformation.ServiceContentBean("空腹血糖",xuetang));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mTvServiceTime = (TextView) findViewById(R.id.tv_service_time);
        mTvServiceDoctor = (TextView) findViewById(R.id.tv_service_doctor);
        mTvDiseaseHistory = (TextView) findViewById(R.id.tv_disease_history);
        mTvServiceType = (TextView) findViewById(R.id.tv_service_type);
        mTvServiceClassic = (TextView) findViewById(R.id.tv_service_classic);
        mRvServiceContent = (RecyclerView) findViewById(R.id.rv_service_content);
        mData=new ArrayList<>();
    }
}
