package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.zane.androidupnpdemo.bean.ServiceAndTeamInformation;
import com.zane.androidupnpdemo.bean.ServiceHistoryList;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2018/4/13.
 */

public class MyServicedHistoryActivity extends CommonBaseActivity {


    private TextView mTvSeeAgreements;
    private LinearLayout mLlSignTime;
    private RadioButton mRbHealthService;
    private RadioButton mRbDoorToDoorDiagnosis;
    private RadioButton mRbPayService;
    private RadioGroup mRgMenu;
    private RecyclerView mRvServiceHistoryList;
    private List<ServiceHistoryList.ListBean> mData;
    private BaseQuickAdapter<ServiceHistoryList.ListBean, BaseViewHolder> adapter;
    private TextView mTvSignTime;
    private ServiceAndTeamInformation serviceAndTeamInformation;

    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_my_serviced_history);
        initView();
        setAdapter();
        getHealthServiceData();
        getSignTimeData();
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvServiceHistoryList.setLayoutManager(manager);
        mRvServiceHistoryList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvServiceHistoryList.setAdapter(adapter = new BaseQuickAdapter<ServiceHistoryList.ListBean, BaseViewHolder>(R.layout.hy_item_service_history, mData) {
            @Override
            protected void convert(BaseViewHolder helper, ServiceHistoryList.ListBean item) {
                String[] service_time = item.getServiceTime().split("\\s+")[0].split("-");
                helper.setText(R.id.tv_service_time, service_time[0] + "年" + service_time[1] + "月" + service_time[2] + "日");
                helper.setText(R.id.tv_doctor, item.getDoctorName());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HY_MyServiceHistoryDetailsActivity.startActivity(
                        MyServicedHistoryActivity.this,
                        mData.get(position).getServiceId(),HY_MyServiceHistoryDetailsActivity.class);
            }
        });
    }

    private void getSignTimeData() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.SIGN_TEAM_WITH_SERVER)
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
                                serviceAndTeamInformation = new Gson().fromJson(data.toString(), ServiceAndTeamInformation.class);
                                String sign_time=serviceAndTeamInformation.getSignInfoFlow().getSignTime();
                                if (!TextUtils.isEmpty(sign_time)) {
                                    String[] signs=sign_time.split("\\s+")[0].split("-");
                                    mTvSignTime.setText("签约时间："+signs[0]+"年"+signs[1]+"月"+signs[2]+"日");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getHealthServiceData() {
        mData.clear();
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
                                ServiceHistoryList historyList = new Gson().fromJson(data.optJSONObject("pageInfo").toString(), ServiceHistoryList.class);

                                mData.addAll(historyList.getList());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rb_health_service:
                getHealthServiceData();
                break;
            case R.id.rb_door_to_door_diagnosis:
                getDoor2doorData();
                break;
            case R.id.rb_pay_service:
                getPayServiceData();
                break;
            case R.id.tv_see_agreements:
                MySignAgreementActivity.startActivity(this,serviceAndTeamInformation.getSignInfoFlow().getId(),MySignAgreementActivity.class);
                break;
        }
    }

    private void getPayServiceData() {
        mData.clear();
        adapter.notifyDataSetChanged();
    }

    private void getDoor2doorData() {
        mData.clear();
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mTvSeeAgreements = (TextView) findViewById(R.id.tv_see_agreements);
        mTvSeeAgreements.setOnClickListener(this);
        mLlSignTime = (LinearLayout) findViewById(R.id.ll_sign_time);
        mRbHealthService = (RadioButton) findViewById(R.id.rb_health_service);
        mRbHealthService.setOnClickListener(this);
        mRbDoorToDoorDiagnosis = (RadioButton) findViewById(R.id.rb_door_to_door_diagnosis);
        mRbDoorToDoorDiagnosis.setOnClickListener(this);
        mRbPayService = (RadioButton) findViewById(R.id.rb_pay_service);
        mRgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        mRbPayService.setOnClickListener(this);
        mRvServiceHistoryList = (RecyclerView) findViewById(R.id.rv_service_history_list);
        mRgMenu.check(R.id.rb_health_service);
        mData = new ArrayList<>();
        mTvSignTime = (TextView) findViewById(R.id.tv_sign_time);
    }
}
