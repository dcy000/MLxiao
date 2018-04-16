package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zane.androidupnpdemo.bean.MySignInformation;
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

public class MySignInfomationActivity extends CommonBaseActivity {
    private MySignInformation mySignInformation;
    private TextView mHySignTime;
    private TextView mHyTvTeamName;
    private TextView mHyTvTeamHospital;
    private RecyclerView mHyTeamDoctorsList;
    private List<MySignInformation.TeamDoc> mData;
    private BaseQuickAdapter<MySignInformation.TeamDoc, BaseViewHolder> adapter;

    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_signinfo);
        initView();
        setRecycleview();
        getData();
    }

    private void setRecycleview() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mHyTeamDoctorsList.setLayoutManager(manager);
        mHyTeamDoctorsList.setAdapter(adapter=new BaseQuickAdapter<MySignInformation.TeamDoc,BaseViewHolder>(R.layout.hy_item_doctors_team,mData) {
            @Override
            protected void convert(BaseViewHolder helper, MySignInformation.TeamDoc item) {
                helper.setText(R.id.hy_tv_doctor_name,item.doctorName);
                helper.setText(R.id.hy_tv_doctor_positon,item.docTitle);
                helper.getView(R.id.hy_tv_isMydoctor).setVisibility(item.isLeader? View.VISIBLE:View.GONE);
            }
        });
    }

    private void getData() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD, AesUtil.KeyCode);
//            param=AesUtil.urlEncode(Utils.parseCurrentTime2String()+"_"+"3102",AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.MY_SIGN_MESSAGE)
                .params("param", param)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == -1) {
                                ToastTool.showShort("网络请求出现错误");
                                return;
                            }
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                JSONObject signInfo = data.optJSONObject("signInfo");
                                mySignInformation = new Gson().fromJson(signInfo.toString(), MySignInformation.class);
                                mData.addAll(mySignInformation.doctorTeamDtos);
                                dealData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealData() {
        if (mySignInformation != null) {
            String[] time_s=mySignInformation.signTime.split("\\s+")[0].split("-");
            mHySignTime.setText("签约时间："+time_s[0]+"年"+time_s[1]+"月"+time_s[2]+"日");
            mHyTvTeamName.setText(mySignInformation.teamName);
            mHyTvTeamHospital.setText(mySignInformation.orgName);
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        mHySignTime = (TextView) findViewById(R.id.hy_sign_time);
        mHyTvTeamName = (TextView) findViewById(R.id.hy_tv_team_name);
        mHyTvTeamHospital = (TextView) findViewById(R.id.hy_tv_team_hospital);
        mHyTeamDoctorsList = (RecyclerView) findViewById(R.id.hy_team_doctors_list);
        mData=new ArrayList<>();
    }
}
