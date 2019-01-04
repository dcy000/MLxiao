package com.example.han.referralproject.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.call2.NimCallActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        NetworkApi.DoctorInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<Doctor>() {
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
                    ToastUtils.showShort("呼叫医生失败");
                    return;
                }
//                NimCallActivity.launch(this, "docter_" + doctorId);
                getDoctorYxAcountId(doctorId);
                break;
        }
    }

    private void getDoctorYxAcountId(String doctorId) {
        NetworkApi.getDoctorYxAcountId(doctorId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject result = new JSONObject(body);
                    boolean tag = result.optBoolean("tag");
                    if (tag) {
                        String account = result.getString("data");
//                        NimAccountHelper.getInstance().login(account, "123456", null);
                        NimCallActivity.launch(DoctorappoActivity2.this, account);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }
}
