package com.example.han.referralproject.recyclerview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.service.API;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckContractActivity extends BaseActivity {

    @BindView(R.id.iv_doctor_avatar)
    CircleImageView ivDoctorAvatar;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_professional_rank)
    TextView tvProfessionalRank;
    @BindView(R.id.tv_good)
    TextView tvGoodAt;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.tv_cancel_contract)
    TextView tvCancelContract;
    private Unbinder mUnbinder;
    private UserInfoBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_contract);
        mUnbinder = ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("签  约  医  生");

        user = Box.getSessionManager().getUser();
        Box.getRetrofit(API.class)
                .queryDoctorInfo(user.doid)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Doctor>() {
                    @Override
                    public void onNext(Doctor doctor) {
                        if (!TextUtils.isEmpty(doctor.getDocter_photo())) {

                            Glide.with(CheckContractActivity.this)
                                    .applyDefaultRequestOptions(new RequestOptions()
                                            .placeholder(R.drawable.avatar_placeholder)
                                            .error(R.drawable.avatar_placeholder))
                                    .load(doctor.getDocter_photo())
                                    .into(ivDoctorAvatar);
                        }
                        tvDoctorName.setText(String.format(getString(R.string.doctor_name), doctor.getDoctername()));
                        tvProfessionalRank.setText(String.format(getString(R.string.doctor_zhiji), doctor.getDuty()));
                        tvGoodAt.setText(String.format(getString(R.string.doctor_shanchang), doctor.getDepartment()));
                        tvService.setText(String.format(getString(R.string.doctor_shoufei), doctor.getService_amount()));
                    }
                });
    }

    @OnClick(R.id.tv_cancel_contract)
    public void onTvCancelContractClicked() {
        new AlertDialog(this)
                .builder()
                .setMsg("确定要取消签约吗？")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancelContract();
                    }
                }).show();
    }

    private void onCancelContract() {
        Box.getRetrofit(API.class)
                .cancelSignDoctor(user.bid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("取消成功");
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
