package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class HYServiceAgreementActivity extends CommonBaseActivity implements View.OnClickListener {
    private TextView mHyTvName;
    private TextView mHyTvBuildTime;
    private TextView mHyTvSignTime;
    private TextView mHyTvServiceName;
    private TextView mHyTvJigouName;
    private TextView mHyTvSignAddress;
    private RecyclerView mHyMyServiceTeamList;
    private TextView mHyTvSignServiceSum;
    private ServiceAndTeamInformation serviceAndTeamInformation;
    private List<DoctorInformation> mData;
    private BaseQuickAdapter<DoctorInformation, BaseViewHolder> adapter;
    private Button mMySignAgreement;

    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_service_agreement);
        initView();
        setAdapter();
        getData();
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        mHyMyServiceTeamList.setLayoutManager(manager);
        mHyMyServiceTeamList.setHasFixedSize(true);
        mHyMyServiceTeamList.setNestedScrollingEnabled(false);
        mHyMyServiceTeamList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mHyMyServiceTeamList.setAdapter(adapter = new BaseQuickAdapter<DoctorInformation, BaseViewHolder>(R.layout.hy_item_doctor, mData) {
            @Override
            protected void convert(BaseViewHolder helper, DoctorInformation item) {
                helper.setText(R.id.name, item.doctorName);
                helper.setText(R.id.profress, item.doctorProfresson);
            }
        });
    }

    private void initView() {
        mHyTvName = (TextView) findViewById(R.id.hy_tv_name);
        mHyTvBuildTime = (TextView) findViewById(R.id.hy_tv_build_time);
        mHyTvSignTime = (TextView) findViewById(R.id.hy_tv_sign_time);
        mHyTvServiceName = (TextView) findViewById(R.id.hy_tv_service_name);
        mHyTvJigouName = (TextView) findViewById(R.id.hy_tv_jigou_name);
        mHyTvSignAddress = (TextView) findViewById(R.id.hy_tv_sign_address);
        mHyMyServiceTeamList = (RecyclerView) findViewById(R.id.hy_my_service_team_list);
        mHyTvName.setOnClickListener(this);
        mHyTvBuildTime.setOnClickListener(this);
        mHyTvSignTime.setOnClickListener(this);
        mHyTvServiceName.setOnClickListener(this);
        mHyTvJigouName.setOnClickListener(this);
        mHyTvSignAddress.setOnClickListener(this);
        mHyTvSignServiceSum = (TextView) findViewById(R.id.hy_tv_sign_service_sum);
        mHyMyServiceTeamList.setOnClickListener(this);
        mData = new ArrayList<>();
        mMySignAgreement = (Button) findViewById(R.id.my_sign_agreement);
        mMySignAgreement.setOnClickListener(this);
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
                                dealData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealData() {
        if (serviceAndTeamInformation != null) {
            mHyTvName.setText(serviceAndTeamInformation.getSignInfoFlow().getCreateName());
            mHyTvBuildTime.setText(serviceAndTeamInformation.getSignInfoFlow().getCreateTime().split("\\s+")[0]);
            mHyTvSignTime.setText(serviceAndTeamInformation.getSignInfoFlow().getSignTime().split("\\s+")[0]);
            mHyTvServiceName.setText(serviceAndTeamInformation.getHospitalTeam().getHospitalTeamName());
            mHyTvJigouName.setText(serviceAndTeamInformation.getHospitalTeam().getOrgName());
            mHyTvSignAddress.setText(serviceAndTeamInformation.getHospitalTeam().getOrgAddress());
            mHyTvSignServiceSum.setText(serviceAndTeamInformation.getServiceCount() + "次");

            String team = serviceAndTeamInformation.getHospitalTeam().getDoctorName();
            String[] teams = team.split(",");
            for (int i = 0; i < teams.length; i++) {
                String[] item = teams[i].split("_");
                mData.add(new DoctorInformation(item[0], item[1]));
            }
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.hy_tv_name) {
        } else if (i == R.id.hy_tv_build_time) {
        } else if (i == R.id.hy_tv_sign_time) {
        } else if (i == R.id.hy_tv_service_name) {
        } else if (i == R.id.hy_tv_jigou_name) {
        } else if (i == R.id.hy_tv_sign_address) {
        } else if (i == R.id.hy_my_service_team_list) {
        } else if (i == R.id.my_sign_agreement) {
            if (serviceAndTeamInformation != null) {
                MySignAgreementActivity.startActivity(this, serviceAndTeamInformation.getSignInfoFlow().getId(), MySignAgreementActivity.class);
            }

        } else {
        }
    }

    public static class DoctorInformation {
        public String doctorName;
        public String doctorProfresson;

        public DoctorInformation(String doctorName, String doctorProfresson) {
            this.doctorName = doctorName;
            this.doctorProfresson = doctorProfresson;
        }
    }
}
