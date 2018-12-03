package com.example.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.bean.Docter;
import com.example.module_doctor_advisory.bean.RobotAmount;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class DoctorMesActivity extends ToolbarBaseActivity implements View.OnClickListener {


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


    int i = 300;


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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_doctor_mes;
    }

    @Override
    public void initParams(Intent intentArgument) {
        doctor = (Docter) intentArgument.getSerializableExtra("docMsg");

        sign = intentArgument.getStringExtra("sign");
    }

    @Override
    public void initView() {
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
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
    protected void onResume() {
        super.onResume();
        if ("1".equals(sign)) {
            MLVoiceSynthetize.startSynthesize(R.string.online_info);

        } else {
//            MLVoiceSynthetize.startSynthesize(R.string.tips_info);

        }

    }

    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

    public static final String REGEX_CHECK = ".*(qianyue).*";
//
//    @Override
//    protected void onSpeakListenerResult(String result) {
//        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
//        String inSpell = PinYinUtils.converterToSpell(result);
//
//        if (inSpell.matches(REGEX_CHECK)) {
//            mButton.performClick();
//            return;
//        }
//
//        if (inSpell.matches(REGEX_BACK)) {
//
//            Intent intent = new Intent(getApplicationContext(), RecoDocActivity.class);
//            startActivity(intent);
//            finish();
//
//            //  headImg.performClick();
//        }
//    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i1 = view.getId();
        if (i1 == R.id.qianyue) {
            if ("1".equals(sign)) {
                //TODO 给签约医生打电话
                emitEvent("NimCall", "docter_" + doctor.docterid);
            } else {
                Box.getRetrofit(DoctorAPI.class)
                        .queryMoneyById(DeviceUtils.getIMEI())
                        .compose(RxUtils.httpResponseTransformer())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new CommonObserver<RobotAmount>() {
                            @Override
                            public void onNext(RobotAmount robotAmount) {
                                if (robotAmount == null) {
                                    return;
                                }
                                final String amount = robotAmount.getAmount();
                                String applyAmount = doctor.getApply_amount();
                                if (robotAmount.count != 0) {
                                    if (Float.parseFloat(amount) > Float.parseFloat(applyAmount)) {
                                        emitEvent("skip2ConfirmContractActivity", doctor.getDocterid());
                                        finish();
                                    } else {
                                        onLackOfAmount();
                                    }
                                } else {
                                    emitEvent("skip2ConfirmContractActivity", doctor.getDocterid());
                                    finish();
                                }
                            }
                        });
            }

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
                        emitEvent("skip2PayActivity");
                    }
                }).show();
    }
}
