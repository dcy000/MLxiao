package com.example.module_doctor_advisory.ui;

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
import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.bean.Doctor;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.imageview.CircleImageView;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppraiseActivity extends ToolbarBaseActivity implements View.OnClickListener {

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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_appraise;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize(R.string.doctor_appraises);

        daid = intentArgument.getIntExtra("daid", 0);
        doid = intentArgument.getStringExtra("doid");
    }

    @Override
    public void initView() {
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

        Box.getRetrofit(DoctorAPI.class)
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i1 = view.getId();
        if (i1 == R.id.star1) {
            mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

            i = 10;


        } else if (i1 == R.id.star2) {
            mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

            i = 20;


        } else if (i1 == R.id.star3) {
            mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

            i = 30;


        } else if (i1 == R.id.star4) {
            mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));


            i = 40;


        } else if (i1 == R.id.star5) {
            mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
            mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));

            i = 50;


        } else if (i1 == R.id.button1) {
            if (view.isSelected()) {
                mButton1.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton1.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.button2) {
            if (view.isSelected()) {
                mButton2.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton2.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.button3) {
            if (view.isSelected()) {
                mButton3.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton3.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.button4) {
            if (view.isSelected()) {
                mButton4.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton4.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.button5) {
            if (view.isSelected()) {
                mButton5.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton5.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.button6) {
            if (view.isSelected()) {
                mButton6.setTextColor(Color.parseColor("#000000"));
                view.setSelected(false);
            } else {
                mButton6.setTextColor(Color.parseColor("#FFFFFF"));

                view.setSelected(true);
            }


        } else if (i1 == R.id.niming_appraise) {
            MLVoiceSynthetize.startSynthesize(R.string.true_appraise);

            showNormal();


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
                        Box.getRetrofit(DoctorAPI.class)
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
