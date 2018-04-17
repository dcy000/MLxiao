package com.zane.androidupnpdemo.base_information_of_residents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.zane.androidupnpdemo.bean.PageInfo;
import com.zane.androidupnpdemo.bean.ServiceAndTeamInformation;
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

public class HY_HealthServiceActivity extends CommonBaseActivity {
    private TextView mTvSeeAgreements;
    private TextView mTvServiceTeam;
    private TextView mTvServiceLocation;
    private RecyclerView mRvServiceDoctors;
    private ServiceAndTeamInformation serviceAndTeamInformation;
    private List<PageInfo> doctors;
    private BaseQuickAdapter<PageInfo, BaseViewHolder> adapter;
    private String signDoctorName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_health_service);
        mTitleText.setText("服务团队");
        initView();
        setAdapter();
        getData();
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        mRvServiceDoctors.setLayoutManager(manager);
        mRvServiceDoctors.setHasFixedSize(true);
        mRvServiceDoctors.setNestedScrollingEnabled(false);
        mRvServiceDoctors.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvServiceDoctors.setAdapter(adapter = new BaseQuickAdapter<PageInfo, BaseViewHolder>(R.layout.hy_item_mydoctor, doctors) {
            @Override
            protected void convert(BaseViewHolder helper, PageInfo item) {
                helper.setText(R.id.tv_name, item.title);
                helper.setText(R.id.tv_prof, item.content);
                if (TextUtils.isEmpty(signDoctorName)) {
                    helper.getView(R.id.tv_is_sign).setVisibility(View.GONE);
                    return;
                }
                if (item.title.equals(signDoctorName)) {
                    helper.getView(R.id.tv_is_sign).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.tv_is_sign).setVisibility(View.GONE);
                }
            }
        });
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
        signDoctorName = serviceAndTeamInformation.getSignInfoFlow().getCreateName();
        mTvServiceTeam.setText(serviceAndTeamInformation.getHospitalTeam().getHospitalTeamName());
        mTvServiceLocation.setText(serviceAndTeamInformation.getHospitalTeam().getOrgName());
        String doc = serviceAndTeamInformation.getHospitalTeam().getDoctorName();
        if (!TextUtils.isEmpty(doc)) {
            String[] docs = doc.split(",");
            for (int i = 0; i < docs.length; i++) {
                doctors.add(new PageInfo(docs[i].split("_")[0], docs[i].split("_")[1]));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_see_agreements) {
            MySignAgreementActivity.startActivity(this, serviceAndTeamInformation.getSignInfoFlow().getId(), MySignAgreementActivity.class);

        }
    }

    private void initView() {
        mTvSeeAgreements = (TextView) findViewById(R.id.tv_see_agreements);
        mTvSeeAgreements.setOnClickListener(this);
        mTvServiceTeam = (TextView) findViewById(R.id.tv_service_team);
        mTvServiceLocation = (TextView) findViewById(R.id.tv_service_location);
        mRvServiceDoctors = (RecyclerView) findViewById(R.id.rv_service_doctors);
        doctors = new ArrayList<>();
    }
}
