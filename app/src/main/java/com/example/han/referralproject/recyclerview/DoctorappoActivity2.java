package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.call2.NimCallActivity;
import com.squareup.picasso.Picasso;

public class DoctorappoActivity2 extends BaseActivity implements View.OnClickListener {
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
        speak(R.string.qianyue_doctor);
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
        NetworkApi.DoctorInfo(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<Doctor>() {
            @Override
            public void onSuccess(Doctor response) {
                if (!TextUtils.isEmpty(response.getDocter_photo())) {
                    Picasso.with(DoctorappoActivity2.this)
                            .load(response.getDocter_photo())
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder)
                            .tag(this)
                            .fit()
                            .into(mCircleImageView1);
                }
                mDocotorName.setText(String.format(getString(R.string.doctor_name), response.getDoctername()));
                mDocotorPosition.setText(String.format(getString(R.string.doctor_zhiji), response.getDuty()));
                mDocotorFeature.setText(String.format(getString(R.string.doctor_shanchang), response.getDepartment()));
                mServiceAmount.setText(String.format(getString(R.string.doctor_shoufei), response.getService_amount()));
                mTvContent.setText(response.getPro());
                doctorId = response.docterid + "";
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                ToastUtils.showShort(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.circleImageView1:
                break;
            case R.id.ll_phone_family:
                if (TextUtils.isEmpty(doctorId)) {
                    ToastUtils.showShort("呼叫健康顾问失败");
                    return;
                }
                NimCallActivity.launchNoCheck(this, "docter_" + doctorId);
                break;
        }
    }
}
