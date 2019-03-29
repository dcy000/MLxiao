package com.example.han.referralproject.service_package;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.ServicePackageBean;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.example.han.referralproject.network.AppRepository;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class ServicePackageActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private ConstraintLayout mCl1;
    private ConstraintLayout mCl2;
    private ConstraintLayout mCl3;
    private boolean isSkip;
    private boolean isClickDetail = false;
    private boolean isServicePackageEffective = false;
    private ServicePackageBean servicePackage;
    private TextView mTvService1;
    private TextView mTvService2;
    private TextView mTvService3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_package);
        initView();
        isSkip = getIntent().getBooleanExtra("isSkip", false);
        AppRepository.queryServicePackage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(ServicePackageActivity.this))
                .subscribe(new DefaultObserver<ServicePackageBean>() {
                    @Override
                    public void onNext(ServicePackageBean servicePackageBean) {
                        servicePackage = servicePackageBean;
                        isServicePackageEffective = true;
                        if (servicePackage != null && isServicePackageEffective) {
                            String type = servicePackage.getType();
                            if (type.equals("3")) {
                                mTvService3.setText("已购买");
                            }
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

    private void initView() {
        mTitleText.setText("健 康 检 测");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("查看详情");
        mRightText.setOnClickListener(this);
        mRightView.setVisibility(View.GONE);
        mCl1 = (ConstraintLayout) findViewById(R.id.cl1);
        mCl1.setOnClickListener(this);
        mCl2 = (ConstraintLayout) findViewById(R.id.cl2);
        mCl2.setOnClickListener(this);
        mCl3 = (ConstraintLayout) findViewById(R.id.cl3);
        mCl3.setOnClickListener(this);
        mTvService1 = findClickView(R.id.tv_service_1);
        mTvService2 = findClickView(R.id.tv_service_2);
        mTvService3 = findClickView(R.id.tv_service_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClickDetail = false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
            case R.id.cl1:
                startActivity(new Intent(this, QRCodeWXPayActivity.class)
                        .putExtra("isSkip", isSkip)
                        .putExtra("ServicePackage", "1")
                        .putExtra("number", "1")
                        .putExtra("description", "套餐一"));
                break;
            case R.id.cl2:
                startActivity(new Intent(this, QRCodeWXPayActivity.class)
                        .putExtra("isSkip", isSkip)
                        .putExtra("ServicePackage", "2")
                        .putExtra("number", "1")
                        .putExtra("description", "套餐二"));
                break;
            case R.id.cl3:
                if (isServicePackageEffective) {
                    //有套餐生效，跳转到测试界面
                    if (isSkip) {
                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true,
                                servicePackage.getType(), servicePackage.orderid + "");
                        return;
                    }
                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(false,
                            servicePackage.getType(), servicePackage.orderid + "");
                } else {
                    startActivity(new Intent(this, QRCodeWXPayActivity.class)
                            .putExtra("isSkip", isSkip)
                            .putExtra("ServicePackage", "3")
                            .putExtra("number", "1")
                            .putExtra("description", "会员套餐"));
                }
                break;
            case R.id.tv_top_right:
                isClickDetail = true;
                startActivity(new Intent(this, ServicePackageDetailActivity.class));
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (!isClickDetail) {
//            finish();
//        }
    }
}
