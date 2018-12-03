package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.service.API;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppraiseActivity extends BaseActivity implements View.OnClickListener {

    public ImageView mImageView1;
    public ImageView mImageView2;
    public ImageView mImageView3;
    public ImageView mImageView4;
    public ImageView mImageView5;


    public Button mButton1;
    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;


    public Button mButton;


    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;
    public TextView mTextView4;


    CircleImageView mCircleImageView;

    int i = 0;

    public int daid;//账单id
    private String doid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);

        MLVoiceSynthetize.startSynthesize(R.string.doctor_appraises);


        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.doctor_appraise));


        mCircleImageView = (CircleImageView) findViewById(R.id.circleImageView1);
        mImageView1 = (ImageView) findViewById(R.id.star1);
        mImageView2 = (ImageView) findViewById(R.id.star2);
        mImageView3 = (ImageView) findViewById(R.id.star3);
        mImageView4 = (ImageView) findViewById(R.id.star4);
        mImageView5 = (ImageView) findViewById(R.id.star5);

        mButton = (Button) findViewById(R.id.niming_appraise);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton6 = (Button) findViewById(R.id.button6);

        mTextView1 = (TextView) findViewById(R.id.doctor_name);
        mTextView2 = (TextView) findViewById(R.id.doctor_position);
        mTextView3 = (TextView) findViewById(R.id.doctor_feature);
        mTextView4 = (TextView) findViewById(R.id.pay_standard);


        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);

        mButton.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);

        mButton = (Button) findViewById(R.id.niming_appraise);
        daid = getIntent().getIntExtra("daid", 0);
        doid = getIntent().getStringExtra("doid");

        Box.getRetrofit(API.class)
                .queryDoctorInfo(doid)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Doctor>() {
                    @Override
                    public void onNext(Doctor doctor) {
                        if (!TextUtils.isEmpty(doctor.getDocter_photo())) {

                            Glide.with(AppraiseActivity.this)
                                    .applyDefaultRequestOptions(new RequestOptions()
                                            .placeholder(R.drawable.avatar_placeholder)
                                            .error(R.drawable.avatar_placeholder))
                                    .load(doctor.getDocter_photo())
                                    .into(mCircleImageView);
                        }
                        mTextView1.setText(String.format(getString(R.string.doctor_name), doctor.getDoctername()));
                        mTextView2.setText(String.format(getString(R.string.doctor_zhiji), doctor.getDuty()));
                        mTextView3.setText(String.format(getString(R.string.doctor_shanchang), doctor.getDepartment()));
                        mTextView4.setText(String.format(getString(R.string.doctor_shoufei), doctor.getService_amount()));
                    }
                });
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
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.star1:

                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

                i = 10;

                break;
            case R.id.star2:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

                i = 20;

                break;
            case R.id.star3:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

                i = 30;


                break;
            case R.id.star4:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));


                i = 40;

                break;
            case R.id.star5:

                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));

                i = 50;


                break;

            case R.id.button1:

                if (view.isSelected()) {
                    mButton1.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton1.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button2:
                if (view.isSelected()) {
                    mButton2.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton2.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button3:
                if (view.isSelected()) {
                    mButton3.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton3.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button4:

                if (view.isSelected()) {
                    mButton4.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton4.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button5:

                if (view.isSelected()) {
                    mButton5.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton5.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;

            case R.id.button6:

                if (view.isSelected()) {
                    mButton6.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton6.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;

            case R.id.niming_appraise:


                MLVoiceSynthetize.startSynthesize(R.string.true_appraise);

                showNormal();

                break;

        }

    }


    public void showNormal() {
        new AlertDialog(this)
                .builder()
                .setMsg("您确认要进行评价？")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuffer str = new StringBuffer();

                        if (mButton1.isSelected()) {

                            str.append(mButton1.getText().toString() + ",");


                        }
                        if (mButton2.isSelected()) {

                            str.append(mButton2.getText().toString() + ",");


                        }
                        if (mButton3.isSelected()) {

                            str.append(mButton3.getText().toString() + ",");


                        }
                        if (mButton4.isSelected()) {

                            str.append(mButton4.getText().toString() + ",");


                        }
                        if (mButton5.isSelected()) {

                            str.append(mButton5.getText().toString() + ",");


                        }
                        if (mButton6.isSelected()) {

                            str.append(mButton6.getText().toString());

                        }
                        Box.getRetrofit(API.class)
                                .appraiseDoctor(doid, Box.getUserId(), str.toString(), i, System.currentTimeMillis(), daid)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<Object>() {
                                    @Override
                                    public void onNext(Object o) {
                                        finish();
                                    }
                                });
                    }
                }).show();
    }


}
