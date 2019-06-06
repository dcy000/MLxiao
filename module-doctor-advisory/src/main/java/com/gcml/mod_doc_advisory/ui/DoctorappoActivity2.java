package com.gcml.mod_doc_advisory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.mod_doc_advisory.R;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.mod_doc_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/doctor/advisory/doctor/appo/activity2")
public class DoctorappoActivity2 extends ToolbarBaseActivity implements View.OnClickListener {
    private CircleImageView mCircleImageView1;
    /**
     * 赵曼林
     */
    private TextView mDocotorName;
    /**
     * 职&#160;&#160;&#160;级：主任医师
     */
    private TextView mDocotorPosition;
    /**
     * 擅&#160;&#160;&#160;长：高血压、糖尿病
     */
    private TextView mDocotorFeature;
    /**
     * 收费标准：10元/分钟
     */
    private TextView mServiceAmount;
    private LinearLayout mLinearlayou1;
    /**
     * 详细介绍
     */
    private TextView mTvTitle;
    /**  */
    private TextView mTvContent;
    private LinearLayout mLlPhoneFamily;
    private String doctorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappo2);
        initView();
        getDoctorInfo();
    }

    private void initView() {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.qianyue_doctor));
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(getString(R.string.doctor_qianyue));
        mCircleImageView1 = (CircleImageView) findViewById(R.id.circleImageView1);
        mCircleImageView1.setOnClickListener(this);
        mDocotorName = (TextView) findViewById(R.id.docotor_name);
        mDocotorPosition = (TextView) findViewById(R.id.docotor_position);
        mDocotorFeature = (TextView) findViewById(R.id.docotor_feature);
        mServiceAmount = (TextView) findViewById(R.id.service_amount);
        mLinearlayou1 = (LinearLayout) findViewById(R.id.linearlayou1);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mLlPhoneFamily = (LinearLayout) findViewById(R.id.ll_phone_family);
        mLlPhoneFamily.setOnClickListener(this);
    }

    public void getDoctorInfo() {
        new QianYueRepository()
                .DoctorInfo(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Doctor>() {
                    @Override
                    public void onNext(Doctor doctor) {
                        if (!TextUtils.isEmpty(doctor.getDocter_photo())) {
                            ImageLoader.with(DoctorappoActivity2.this)
                                    .load(doctor.getDocter_photo())
                                    .placeholder(R.drawable.avatar_placeholder)
                                    .error(R.drawable.avatar_placeholder)
                                    .into(mCircleImageView1);
                        }
                        mDocotorName.setText(String.format(getString(R.string.doctor_name), doctor.getDoctername()));
                        mDocotorPosition.setText(String.format(getString(R.string.doctor_zhiji), doctor.getDuty()));
                        mDocotorFeature.setText(String.format(getString(R.string.doctor_shanchang), doctor.getDepartment()));
                        mServiceAmount.setText(String.format(getString(R.string.doctor_shoufei), doctor.getService_amount()));
                        mTvContent.setText(doctor.getPro());
                        doctorId = doctor.docterid + "";
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.circleImageView1) {
        } else if (i == R.id.ll_phone_family) {
            if (TextUtils.isEmpty(doctorId)) {
                ToastUtils.showShort("呼叫健康顾问失败");
                return;
            }
            new QianYueRepository().getCallId(doctorId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            Routerfit.register(AppRouter.class).getCallProvider().call(DoctorappoActivity2.this, s);
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
        }
    }
}
