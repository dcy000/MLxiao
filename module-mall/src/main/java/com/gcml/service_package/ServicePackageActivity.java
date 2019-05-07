package com.gcml.service_package;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.get.ServicePackageBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.mall.R;
import com.gcml.old.GoodsRepository;
import com.gcml.pay.QRCodeWXPayActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/module/mall/service/package/activity")
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
    }

    private void initView() {
        mTitleText.setText("健 康 检 测");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("跳过");
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
        getPackageDetail();
    }

    private void getPackageDetail() {
        new GoodsRepository().queryServicePackage()
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.cl1) {
            startActivity(new Intent(this, QRCodeWXPayActivity.class)
                    .putExtra("isSkip", isSkip)
                    .putExtra("ServicePackage", "1")
                    .putExtra("number", "1")
                    .putExtra("description", "套餐一"));

        } else if (i == R.id.cl2) {
            startActivity(new Intent(this, QRCodeWXPayActivity.class)
                    .putExtra("isSkip", isSkip)
                    .putExtra("ServicePackage", "2")
                    .putExtra("number", "1")
                    .putExtra("description", "套餐二"));

        } else if (i == R.id.cl3) {
            if (isServicePackageEffective) {
                Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(isSkip, servicePackage.getType(), servicePackage.orderid + "");
            } else {
                startActivity(new Intent(this, QRCodeWXPayActivity.class)
                        .putExtra("isSkip", isSkip)
                        .putExtra("ServicePackage", "3")
                        .putExtra("number", "1")
                        .putExtra("description", "会员套餐"));
            }

        } else if (i == R.id.tv_top_right) {
            isClickDetail = true;
//                startActivity(new Intent(this, ServicePackageDetailActivity.class));
            Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(false);

        } else {
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
