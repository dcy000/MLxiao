package com.example.han.referralproject.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.BodychartActivity;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.activity.SymptomAnalyseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.User;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.dialog.ChangeAccountDialog;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.shopping.ShopListActivity;
import com.example.han.referralproject.util.Utils;
import com.google.gson.Gson;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.example.han.referralproject.util.LocalShared;
import com.squareup.picasso.Picasso;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

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
    public ImageView mImageView;
    public ImageView mIvAlarm;

    SharedPreferences sharedPreferences;
    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;

    public ImageView mImageView1;
    public ImageView mImageView2;
    public ImageView mImageView3;

    public ImageView mImageView4;

    public ImageView mImageView5;


    private ChangeAccountDialog mChangeAccountDialog;

    SharedPreferences sharedPreferences1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        userId = MyApplication.getInstance().userId;
        mImageView = (ImageView) findViewById(R.id.per_image);

        mImageView3 = (ImageView) findViewById(R.id.iv_pay);

        mImageView3.setOnClickListener(this);

        mTextView3 = (TextView) findViewById(R.id.tv_balance);

        mImageView1 = (ImageView) findViewById(R.id.icon_back);
        mImageView2 = (ImageView) findViewById(R.id.icon_home);

        mImageView5 = (ImageView) findViewById(R.id.iv_order);

        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WifiConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mImageView4 = (ImageView) findViewById(R.id.iv_shopping);


        mImageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);

            }
        });

      /*  String imageData1 = LocalShared.getInstance(getApplicationContext()).getUserImg();

        if (imageData1 != null) {
            byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mImageView.setImageBitmap(bitmap);

        }*/

        mImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonActivity.this, ShopListActivity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.iv_message).setOnClickListener(this);
        findViewById(R.id.view_health).setOnClickListener(this);
        findViewById(R.id.iv_check).setOnClickListener(this);
        findViewById(R.id.view_wifi).setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.per_name);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.view_change).setOnClickListener(this);
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


        mTextView1 = (TextView) findViewById(R.id.doctor_id);
        mTextView2 = (TextView) findViewById(R.id.tv_hospital);

        findViewById(R.id.btn_logout).setOnClickListener(this);


        registerReceiver(mReceiver, new IntentFilter("change_account"));
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
                editor.commit();

                mTextView.setText(response.getBname());
                //    mTextView3.setText(String.format(getString(R.string.robot_amount), response.getAmount())+"元");
                Picasso.with(PersonActivity.this)
                        .load(response.getuser_photo())
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(mImageView);


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });


        NetworkApi.Person_Amount(Utils.getDeviceId(), new NetworkManager.SuccessCallback<RobotAmount>() {
            @Override
            public void onSuccess(RobotAmount response) {
                mTextView3.setText(String.format(getString(R.string.robot_amount), response.getAmount()));

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

                mTextView1.setText("签约医生：" + sharedPreferences.getString("name", ""));
                mTextView2.setText(sharedPreferences.getString("hospital", ""));


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                LocalShared.getInstance(this).loginOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.iv_check://病症自查
                startActivity(new Intent(this, BodychartActivity.class));
                break;
            case R.id.iv_message:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.iv_pay:
                startActivity(new Intent(this, PayActivity.class));
                break;
            case R.id.view_wifi:
                startActivity(new Intent(this, WifiConnectActivity.class));
                break;
            case R.id.view_change:
                mChangeAccountDialog = new ChangeAccountDialog(mContext);
                mChangeAccountDialog.show();
                break;
            case R.id.view_health://健康档案
                startActivity(new Intent(this, MyBaseDataActivity.class));
                break;
            case R.id.btn_record:
                startActivity(new Intent(this, HealthRecordActivity.class));
                break;

        }
    }
}
