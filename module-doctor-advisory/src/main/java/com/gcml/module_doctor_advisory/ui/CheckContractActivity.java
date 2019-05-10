package com.gcml.module_doctor_advisory.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.bean.DoctorInfoBean;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/doctor/advisory/check/contract/activity")
public class CheckContractActivity extends ToolbarBaseActivity {

    CircleImageView ivDoctorAvatar;
    TextView tvDoctorName;
    TextView tvProfessionalRank;
    TextView tvGoodAt;
    TextView tvService;
    TextView tvCancelContract;
    private QianYueRepository qianYueRepository;
    private TextView tvDoctorLevel;
    private TextView tvPrice;
    private TextView tvGoodAtNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_contract);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("视  频  咨  询");
        qianYueRepository = new QianYueRepository();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_cancel_contract) {
            new AlertDialog(this)
                    .builder()
                    .setMsg("确定要取消绑定吗？")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onCancelContract();
                        }
                    }).show();
        }
    }

    private void onCancelContract() {
        qianYueRepository.cancelContract(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("取消成功");
                        finish();
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
    protected void onResume() {
        super.onResume();

        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<UserEntity, ObservableSource<DoctorInfoBean>>() {
                    @Override
                    public ObservableSource<DoctorInfoBean> apply(UserEntity userEntity) throws Exception {
                        return qianYueRepository.getDoctorInfo(userEntity.doctorId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<DoctorInfoBean>() {
                               @Override
                               public void onNext(DoctorInfoBean response) {
                                   if (!TextUtils.isEmpty(response.docter_photo)) {
                                       ImageLoader.with(CheckContractActivity.this)
                                               .load(response.docter_photo)
                                               .placeholder(R.drawable.avatar_placeholder)
                                               .error(R.drawable.avatar_placeholder)
                                               .into(ivDoctorAvatar);
                                   }
                                   tvDoctorName.setText(String.format(getString(R.string.doctor_name), response.doctername));
                                   tvDoctorLevel.setText(response.duty);
                                   tvGoodAtNew.setText(response.gat);
                                   tvPrice.setText(String.format("%s元/分", String.valueOf(response.service_amount)));
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   super.onError(throwable);
                               }
                           }


                );

    }

    private void initView() {
        tvDoctorLevel = (TextView) findViewById(R.id.tv_doctor_level);
        tvGoodAtNew = (TextView) findViewById(R.id.tv_good_at);
        tvPrice = (TextView) findViewById(R.id.tv_price);


        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvProfessionalRank = findViewById(R.id.tv_professional_rank);
        tvGoodAt = findViewById(R.id.tv_good);
        tvService = findViewById(R.id.tv_service);
        tvCancelContract = findViewById(R.id.tv_cancel_contract);
        tvCancelContract.setOnClickListener(this);
    }
}
