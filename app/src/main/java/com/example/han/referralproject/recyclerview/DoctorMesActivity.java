package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.module_register.ui.normal.ConfirmContractActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.call2.NimCallActivity;

public class DoctorMesActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView;

    ImageView mImageView1;
    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;

    public ImageView mStar1;
    public ImageView mStar2;
    public ImageView mStar3;
    public ImageView mStar4;
    public ImageView mStar5;
    private TextView goodat1, goodat2, goodat3;
    Button mButton;


    String sign;

    boolean flag = true;

    int i = 300;

    SharedPreferences sharedPreferences;

    SharedPreferences sharedPreference;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mButton.setText(i + "");
                    break;
                case 1:
                    mButton.setText(getString(R.string.zixun));
                    mButton.setEnabled(true);
                    break;
            }
        }
    };

    Docter doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mes);
        mToolbar.setVisibility(View.VISIBLE);
        //    initToolBar();

        Intent intent = getIntent();
        doctor = (Docter) intent.getSerializableExtra("docMsg");

        sign = intent.getStringExtra("sign");
        mToolbar.setVisibility(View.VISIBLE);

        sharedPreferences = getSharedPreferences(ConstantData.ONLINE_TIME, Context.MODE_PRIVATE);

        sharedPreference = getSharedPreferences(ConstantData.ONLINE_ID, Context.MODE_PRIVATE);

        mImageView1 = (ImageView) findViewById(R.id.circleImageView);
        mTextView = (TextView) findViewById(R.id.names);
        mTextView1 = (TextView) findViewById(R.id.duty);
        mTextView2 = (TextView) findViewById(R.id.hospital);
        mTextView3 = (TextView) findViewById(R.id.department);
        mTextView4 = (TextView) findViewById(R.id.introduce);

        mButton = (Button) findViewById(R.id.qianyue);
        mStar1 = (ImageView) findViewById(R.id.star1);
        mStar2 = (ImageView) findViewById(R.id.star2);
        mStar3 = (ImageView) findViewById(R.id.star3);
        mStar4 = (ImageView) findViewById(R.id.star4);
        mStar5 = (ImageView) findViewById(R.id.star5);
        goodat1 = findViewById(R.id.goodat1);
        goodat2 = findViewById(R.id.goodat2);
        goodat3 = findViewById(R.id.goodat3);
        mButton.setOnClickListener(this);

        if ("1".equals(sign)) {

            mTitleText.setText(getString(R.string.online_qianyue));
            mButton.setText(getString(R.string.zixun));


        } else {
            mTitleText.setText(getString(R.string.doctor_qianyue));

        }

        if (!"".equals(sharedPreferences.getString("online_time", ""))) {

            long countdown = System.currentTimeMillis() - Long.parseLong(sharedPreferences.getString("online_time", ""));
            if (countdown < 300000) {

                i = 300 - Integer.parseInt((countdown / 1000) + "");
                mButton.setEnabled(false);
                countdown();


            }


        }


        if (doctor != null) {
            if (Integer.parseInt(doctor.getEvaluation()) <= 60) {

                mStar1.setVisibility(View.VISIBLE);

            } else if (Integer.parseInt(doctor.getEvaluation()) <= 70 &&
                    Integer.parseInt(doctor.getEvaluation()) > 60) {
                mStar1.setVisibility(View.VISIBLE);
                mStar2.setVisibility(View.VISIBLE);


            } else if (Integer.parseInt(doctor.getEvaluation()) <= 80 &&
                    Integer.parseInt(doctor.getEvaluation()) > 70) {
                mStar1.setVisibility(View.VISIBLE);
                mStar2.setVisibility(View.VISIBLE);
                mStar3.setVisibility(View.VISIBLE);


            } else if (Integer.parseInt(doctor.getEvaluation()) <= 90 &&
                    Integer.parseInt(doctor.getEvaluation()) > 80) {
                mStar1.setVisibility(View.VISIBLE);
                mStar2.setVisibility(View.VISIBLE);
                mStar3.setVisibility(View.VISIBLE);
                mStar4.setVisibility(View.VISIBLE);


            } else if (Integer.parseInt(doctor.getEvaluation()) > 90
                    ) {
                mStar1.setVisibility(View.VISIBLE);
                mStar2.setVisibility(View.VISIBLE);
                mStar3.setVisibility(View.VISIBLE);
                mStar4.setVisibility(View.VISIBLE);
                mStar5.setVisibility(View.VISIBLE);


            }

            Glide.with(Box.getApp())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .load(doctor.getDocter_photo())
                    .into(mImageView1);

            mTextView.setText(doctor.getDoctername());
            mTextView1.setText(doctor.getDuty());
            mTextView2.setText(doctor.getHosname());
            mTextView3.setText(doctor.getDepartment());
            mTextView4.setText(doctor.getPro());
            if (!TextUtils.isEmpty(doctor.department)) {
                if ("尚未开放,请勿签约".equals(doctor.department)) {
                    goodat1.setText("暂未填写");
                    return;
                }
                goodat1.setText(doctor.department);
            } else {
                goodat1.setText(doctor.department);
            }
        }

    }

    public void countdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i >= 0; i--) {
                    mHandler.sendEmptyMessage(0);
                    if (i == 0) {
                        i = 300;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.sendEmptyMessage(1);
            }
        }).start();

    }


    @Override
    protected void onPause() {
        super.onPause();

        //   mButton.setEnabled(true);
        //  mButton.setText(getString(R.string.zixun));

    }


    /**
     * 返回上一页
     */
    protected void backLastActivity() {


        finish();

    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {

        Intent intent = new Intent(DoctorMesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        if ("1".equals(sign)) {
            MLVoiceSynthetize.startSynthesize(R.string.online_info);

        } else {
            MLVoiceSynthetize.startSynthesize(R.string.tips_info);

        }

    }

    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

    public static final String REGEX_CHECK = ".*(qianyue).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(REGEX_CHECK)) {
            mButton.performClick();
            return;
        }

        if (inSpell.matches(REGEX_BACK)) {

            Intent intent = new Intent(getApplicationContext(), RecoDocActivity.class);
            startActivity(intent);
            finish();

            //  headImg.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qianyue:

                if ("1".equals(sign)) {
                    NimCallActivity.launch(mContext, "docter_" + doctor.docterid);
//                    NetworkApi.postTelMessage(doctor.tel, MyApplication.getInstance().userName, new NetworkManager.SuccessCallback<Object>() {
//                        @Override
//                        public void onSuccess(Object response) {
//
//                        }
//                    }, new NetworkManager.FailedCallback() {
//                        @Override
//                        public void onFailed(String message) {
//
//                        }
//                    });
                    //countdown();
                    //mButton.setEnabled(false);
                    //OnlineTime();


                } else {
                    NetworkApi.Person_Amount(com.example.han.referralproject.util.Utils.getDeviceId(),
                            new NetworkManager.SuccessCallback<RobotAmount>() {
                                @Override
                                public void onSuccess(RobotAmount response) {
                                    if (response == null) {
                                        return;
                                    }
                                    final String amount = response.getAmount();
                                    String applyAmount = doctor.getApply_amount();
                                    if (response.count != 0) {
                                        if (Float.parseFloat(amount) > Float.parseFloat(applyAmount)) {
                                            ConfirmContractActivity.start(DoctorMesActivity.this, doctor.getDocterid());
                                            finish();
                                        } else {
                                            onLackOfAmount();
                                        }
                                    } else {
                                        ConfirmContractActivity.start(DoctorMesActivity.this, doctor.getDocterid());
                                        finish();
                                    }

                                }
                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {
                                    ToastUtils.showShort("服务器繁忙，请稍后再试");
                                }
                            });
                }
                break;

        }
    }

    private void onLackOfAmount() {
        new AlertDialog(this)
                .builder()
                .setMsg("账户余额不足,我要充值?")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DoctorMesActivity.this, PayActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    public void OnlineTime() {
        NetworkApi.onlinedoctor_zixun(doctor.getDocterid(), Box.getUserId(), 0, new NetworkManager.SuccessCallback<OnlineTime>() {
            @Override
            public void onSuccess(OnlineTime response) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("online_time", response.getTime());
                editor.commit();

                SharedPreferences.Editor editors = sharedPreference.edit();
                editors.putString("online_id", doctor.getDocterid());
                editors.commit();

                MLVoiceSynthetize.startSynthesize(R.string.doctor_online);

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });

    }

}
