package com.gcml.module_doctor_advisory.ui;

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

import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.bean.Docter;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class DoctorMesActivity extends ToolbarBaseActivity implements View.OnClickListener {

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

        sharedPreferences = getSharedPreferences("online_time", Context.MODE_PRIVATE);

        sharedPreference = getSharedPreferences("online_id", Context.MODE_PRIVATE);

        mImageView1 = findViewById(R.id.circleImageView);
        mTextView = findViewById(R.id.names);
        mTextView1 = findViewById(R.id.duty);
        mTextView2 = findViewById(R.id.hospital);
        mTextView3 = findViewById(R.id.department);
        mTextView4 = findViewById(R.id.introduce);

        mButton = findViewById(R.id.qianyue);
        mStar1 = findViewById(R.id.star1);
        mStar2 = findViewById(R.id.star2);
        mStar3 = findViewById(R.id.star3);
        mStar4 = findViewById(R.id.star4);
        mStar5 = findViewById(R.id.star5);
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

            Picasso.with(this)
                    .load(doctor.getDocter_photo())
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(mImageView1);


            mTextView.setText(doctor.getDoctername());
            mTextView1.setText(doctor.getDuty());
            mTextView2.setText(doctor.getHosname());
            mTextView3.setText(doctor.getDepartment());
            mTextView4.setText(doctor.getPro());
            if (!TextUtils.isEmpty(doctor.department)) {
                if ("尚未开放,请勿绑定".equals(doctor.department)) {
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


    @Override
    protected void onResume() {
        super.onResume();
        if ("1".equals(sign)) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.online_info));

        } else {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.tips_info));
        }

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i1 = view.getId();
        if (i1 == R.id.qianyue) {
            if ("1".equals(sign)) {
//                    CallHelper.launch(mContext, "docter_" + doctor.docterid);
                new QianYueRepository().getCallId(doctor.docterid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new DefaultObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                Routerfit.register(AppRouter.class).getCallProvider().call(DoctorMesActivity.this, s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showShort("呼叫失败");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            } else {
                new QianYueRepository()
                        .Person_Amount(DeviceUtils.getIMEI())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<RobotAmount>() {
                            @Override
                            public void onNext(RobotAmount robotAmount) {
                                if (robotAmount == null) {
                                    return;
                                }
                                final String amount = robotAmount.getAmount();
                                String applyAmount = doctor.getApply_amount();
                                if (robotAmount.count != 0) {
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

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

        }
    }

    private void onLackOfAmount() {
        new AlertDialog(this)
                .builder()
                .setMsg("账户余额不足,我要充值?")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Routerfit.register(AppRouter.class).skipPayActivity();
                    }
                }).show();
    }
}
