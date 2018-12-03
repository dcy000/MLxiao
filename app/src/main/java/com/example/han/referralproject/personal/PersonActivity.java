package com.example.han.referralproject.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.dialog.ChangeAccountDialog;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoListActivity;
import com.google.gson.Gson;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.alarm.AlarmList2Activity;

import io.reactivex.functions.Action;

public class PersonActivity extends BaseActivity implements View.OnClickListener {


    public TextView mTextView;
    public ImageView headImg;
    public ImageView mIvAlarm;

    public TextView tvSignDoctorName;
    public TextView tvBalance;

    public TextView tvIsSign;
    //public ImageView mImageView1;
    //public ImageView mImageView2;
    public ImageView mImageView3;

    public ImageView mImageView4;

    public ImageView mImageView5;


    private ChangeAccountDialog mChangeAccountDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        MLVoiceSynthetize.startSynthesize(R.string.person_info);
        mToolbar.setVisibility(View.VISIBLE);

        headImg = (ImageView) findViewById(R.id.per_image);

        mImageView3 = (ImageView) findViewById(R.id.iv_pay);

        mImageView3.setOnClickListener(this);

        tvBalance = (TextView) findViewById(R.id.tv_balance);


        mImageView5 = (ImageView) findViewById(R.id.iv_order);

        tvIsSign = (TextView) findViewById(R.id.doctor_status);
        findViewById(R.id.main_iv_health_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonActivity.this, VideoListActivity.class));
            }
        });
        tvIsSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("未签约".equals(tvIsSign.getText())) {
                    Intent intent = new Intent(PersonActivity.this, OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                    return;
                }
                if ("待审核".equals(tvIsSign.getText())) {
                    Intent intent = new Intent(PersonActivity.this, CheckContractActivity.class);
                    startActivity(intent);
                }
            }
        });

        mImageView4 = (ImageView) findViewById(R.id.main_iv_old);
        mTitleText.setText("个  人  中  心");
        mRightView.setImageResource(R.drawable.icon_wifi);
        mImageView5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);
            }
        });


        mImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonActivity.this, ChildEduHomeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.iv_message).setOnClickListener(this);
        findViewById(R.id.iv_check).setOnClickListener(this);
        findViewById(R.id.view_wifi).setOnClickListener(this);
        findViewById(R.id.iv_record).setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.per_name);
        findViewById(R.id.iv_change_account).setOnClickListener(this);
        headImg.setOnClickListener(this);
        findViewById(R.id.tv_update).setOnClickListener(this);
        mIvAlarm = (ImageView) findViewById(R.id.iv_alarm);
        mIvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AlarmList2Activity.newLaunchIntent(PersonActivity.this);
                startActivity(intent);
            }
        });

        tvSignDoctorName = (TextView) findViewById(R.id.doctor_name);

        ((TextView) findViewById(R.id.tv_update)).setText("检查更新 v" + Utils.getLocalVersionName(mContext));
        registerReceiver(mReceiver, new IntentFilter("change_account"));
    }

    @Override
    protected void backMainActivity() {
        startActivity(new Intent(this, WifiConnectActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "change_account":
                    if (mChangeAccountDialog != null) {
                        mChangeAccountDialog.dismiss();
                    }
                    getData();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    private void getData() {
        UserInfoBean user = Box.getSessionManager().getUser();

        mTextView.setText(user.bname);

        Glide.with(Box.getApp())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .load(user.userPhoto)
                .into(headImg);
        if ("1".equals(user.state)) {
            tvIsSign.setText("已签约");
        } else if ("0".equals(user.state)) {

            tvIsSign.setText("未签约");

        } else {
            tvIsSign.setText("待审核");

        }
        if (TextUtils.isEmpty(user.doid)) {
            tvSignDoctorName.setText("暂无");
        } else {
            Box.getRetrofit(API.class)
                    .queryDoctorInfo(user.doid)
                    .compose(RxUtils.httpResponseTransformer())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<Doctor>() {
                        @Override
                        public void onNext(Doctor doctor) {
                            if (!"".equals(doctor.getDoctername())) {
                                tvSignDoctorName.setText(doctor.getDoctername());

                            }
                        }
                    });
        }

        Box.getRetrofit(API.class)
                .queryMoneyById(DeviceUtils.getIMEI())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<RobotAmount>() {
                    @Override
                    public void onNext(RobotAmount robotAmount) {
                        if (robotAmount.getAmount() != null) {
                            tvBalance.setText(String.format(getString(R.string.robot_amount), robotAmount.getAmount()));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_check://病症自查
                UserInfoBean user = Box.getSessionManager().getUser();
                DiseaseUser diseaseUser = new DiseaseUser(
                        user.bname,
                        user.sex.equals("男") ? 1 : 2,
                        Integer.parseInt(user.age) * 12,
                        user.userPhoto
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                break;
            case R.id.iv_message:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.iv_pay:
                startActivity(new Intent(this, PayActivity.class));
                break;
            case R.id.iv_change_account:
                mChangeAccountDialog = new ChangeAccountDialog(mContext);
                mChangeAccountDialog.show();
                break;
            case R.id.per_image:
                startActivity(new Intent(this, MyBaseDataActivity.class));
                break;
            case R.id.iv_record:
//                startActivity(new Intent(this, HealthRecordActivity.class));
                com.gcml.module_health_record.HealthRecordActivity.startActivity(this, 0);
                break;
            case R.id.tv_update:
                showLoadingDialog("检查更新中");
                Box.getRetrofit(API.class)
                        .getAppVersion(AppUtils.getMeta("com.gcml.version") + "")
                        .compose(RxUtils.httpResponseTransformer())
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                hideLoadingDialog();
                            }
                        })
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new CommonObserver<VersionInfoBean>() {
                            @Override
                            public void onNext(VersionInfoBean versionInfoBean) {
                                try {
                                    if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                                        new UpdateAppManager(PersonActivity.this).showNoticeDialog(versionInfoBean.url);
                                    } else {
                                        MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                        Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onError(ApiException ex) {
                                MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
