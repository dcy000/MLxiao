package com.example.han.referralproject.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.User;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.dialog.ChangeAccountDialog;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoListActivity;
import com.google.gson.Gson;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.squareup.picasso.Picasso;

public class PersonActivity extends BaseActivity implements View.OnClickListener {

    public String userId;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUser = (User) msg.obj;
                    mTextView.setText(mUser.getBname());
                    break;
                case 1:
                    LocalShared.getInstance(getApplicationContext()).setXunfeiID(msg.obj + "");

                    break;
            }
            super.handleMessage(msg);
        }


    };

    User mUser;

    public TextView mTextView;
    public ImageView headImg;
    public ImageView mIvAlarm;

    SharedPreferences sharedPreferences;
    public TextView tvSignDoctorName;
    public TextView tvBalance;

    public TextView tvIsSign;
    //public ImageView mImageView1;
    //public ImageView mImageView2;
    public ImageView mImageView3;

    public ImageView mImageView4;

    public ImageView mImageView5;


    private ChangeAccountDialog mChangeAccountDialog;

    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        speak(getString(R.string.person_info));
        mToolbar.setVisibility(View.VISIBLE);

        userId = MyApplication.getInstance().userId;
        headImg = (ImageView) findViewById(R.id.per_image);

        mImageView3 = (ImageView) findViewById(R.id.iv_pay);

        mImageView3.setOnClickListener(this);

        tvBalance = (TextView) findViewById(R.id.tv_balance);


        mImageView5 = (ImageView) findViewById(R.id.iv_order);

        setEnableListeningLoop(false);
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

        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        sharedPreferences1 = getSharedPreferences(ConstantData.PERSON_MSG, Context.MODE_PRIVATE);


        tvSignDoctorName = (TextView) findViewById(R.id.doctor_name);

        ((TextView)findViewById(R.id.tv_update)).setText("检查更新 v_" + Utils.getLocalVersionName(mContext));
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


        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = response.getXfid();
                mHandler.sendMessage(msg);


                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("userName", response.getBname());
                MyApplication.getInstance().userName = response.getBname();
                editor.commit();

                mTextView.setText(response.getBname());
                //    tvBalance.setText(String.format(getString(R.string.robot_amount), response.getAmount())+"元");
                Picasso.with(PersonActivity.this)
                        .load(response.getuser_photo())
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(headImg);


                if ("1".equals(response.getState())) {
                    tvIsSign.setText("已签约");
                } else if ("0".equals(response.getState()) && (TextUtils.isEmpty(response.getDoctername()))) {

                    tvIsSign.setText("未签约");

                } else {
                    tvIsSign.setText("待审核");

                }


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });

        NetworkApi.Person_Amount(Utils.getDeviceId(), new NetworkManager.SuccessCallback<RobotAmount>() {
            @Override
            public void onSuccess(final RobotAmount response) {


                if (response.getAmount() != null) {

                    tvBalance.setText(String.format(getString(R.string.robot_amount), response.getAmount()));

                }


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });


        NetworkApi.DoctorInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<Doctor>() {
            @Override
            public void onSuccess(Doctor response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("doctor_id", response.getDocterid() + "");
                editor.putString("name", response.getDoctername());
                editor.putString("position", response.getDuty());
                editor.putString("feature", response.getDepartment());
                editor.putString("hospital", response.getHosname());
                editor.putString("service_amount", response.getService_amount());
                editor.putString("docter_photo", response.getDocter_photo());
                editor.commit();


                if (!"".equals(response.getDoctername())) {
                    tvSignDoctorName.setText(response.getDoctername());

                }


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                tvSignDoctorName.setText("暂无");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_check://病症自查
                DiseaseUser diseaseUser=new DiseaseUser(
                        LocalShared.getInstance(this).getUserName(),
                        LocalShared.getInstance(this).getSex().equals("男")? 1:2,
                        Integer.parseInt(LocalShared.getInstance(this).getUserAge())*12,
                        LocalShared.getInstance(this).getUserPhoto()
                );
                String currentUser= new Gson().toJson(diseaseUser);
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
                startActivity(new Intent(this, HealthRecordActivity.class));
                break;
            case R.id.tv_update:
                showLoadingDialog("检查更新中");
                NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
                    @Override
                    public void onSuccess(VersionInfoBean response) {
                        hideLoadingDialog();
                        try {
                            if (response != null && response.vid > getPackageManager().getPackageInfo(PersonActivity.this.getPackageName(), 0).versionCode) {
                                new UpdateAppManager(PersonActivity.this).showNoticeDialog(response.url);
                            } else {
                                speak("当前已经是最新版本了");
                                Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        speak("当前已经是最新版本了");
                        Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
