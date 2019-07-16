package com.example.han.referralproject.service_package;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.ServicePackageBean;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.example.han.referralproject.network.AppRepository;
import com.example.han.referralproject.network.heguiserver.HeguiRepository;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServicePackageActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private ConstraintLayout mCl1;
    private ConstraintLayout mCl2;
    private ConstraintLayout mCl3;
    private boolean isSkip;
    private boolean isClickDetail = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSkip = getIntent().getBooleanExtra("isSkip", false);
        AppRepository.queryServicePackage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(ServicePackageActivity.this))
                .subscribe(new DefaultObserver<ServicePackageBean>() {
                    @Override
                    public void onNext(ServicePackageBean servicePackageBean) {
                        //有套餐生效，跳转到测试界面
                        if (isSkip) {
                            CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true,
                                    servicePackageBean.getType(), servicePackageBean.orderid + "");
                            return;
                        }
                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(false,
                                servicePackageBean.getType(), servicePackageBean.orderid + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //没有套餐生效
                        setContentView(R.layout.activity_service_package);
                        initView();
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
    }


    @Override
    protected void onResume() {
        super.onResume();
        isClickDetail = false;
        getProductsInfo();
    }

    HeguiRepository heguiRepository = new HeguiRepository();

    static final String value = "1";

    private void getProductsInfo() {
        Map<String, String> param = new HashMap<>();
        String eqId = UserSpHelper.getEqId();
        param.put("eqid", eqId);
        String time = System.currentTimeMillis() + "";
        param.put("timestamp", time + "");
        param.put("goodsType", value);
        String sign = HeGui.getSign(param);

        /*PostBean bean = new PostBean();
        bean.eqid = eqId;
        bean.timestamp = time;
        bean.sign = sign;
        bean.goodsType = value;*/

        heguiRepository.getProduct(eqId, time, value, sign)
                .compose(RxUtils.io2Main())
                .doOnSubscribe(disposable -> {

                })
                .doOnTerminate(() -> {

                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                        .putExtra("number", "2000")
                        .putExtra("description", "套餐二"));
                break;
            case R.id.cl3:
                startActivity(new Intent(this, QRCodeWXPayActivity.class)
                        .putExtra("isSkip", isSkip)
                        .putExtra("ServicePackage", "3")
                        .putExtra("number", "10000")
                        .putExtra("description", "套餐三"));
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
        if (!isClickDetail) {
            finish();
        }
    }
}
