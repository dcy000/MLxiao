package com.example.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.R2;
import com.example.module_doctor_advisory.bean.Doctor;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gcml.lib_widget.imageview.CircleImageView;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CheckContractActivity extends ToolbarBaseActivity {

    @BindView(R2.id.iv_doctor_avatar)
    CircleImageView ivDoctorAvatar;
    @BindView(R2.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R2.id.tv_professional_rank)
    TextView tvProfessionalRank;
    @BindView(R2.id.tv_good)
    TextView tvGoodAt;
    @BindView(R2.id.tv_service)
    TextView tvService;
    @BindView(R2.id.tv_cancel_contract)
    TextView tvCancelContract;
    private Unbinder mUnbinder;
    private UserInfoBean user;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_check_contract;
    }

    @Override
    public void initParams(Intent intentArgument) {
        user = Box.getSessionManager().getUser();
        Box.getRetrofit(DoctorAPI.class)
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

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTitleText.setText("签  约  医  生");

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    @OnClick(R2.id.tv_cancel_contract)
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
        Box.getRetrofit(DoctorAPI.class)
                .cancelSignDoctor(user.bid)
                .flatMap(new Function<Object, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(Object o) throws Exception {
                        return Box.getRetrofit(CommonAPI.class)
                                .queryUserInfo(Box.getUserId())
                                .compose(RxUtils.httpResponseTransformer(false))
                                .doOnNext(new Consumer<UserInfoBean>() {
                                    @Override
                                    public void accept(UserInfoBean userInfoBean) throws Exception {
                                        Box.getSessionManager().setUser(userInfoBean);
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean o) {
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
