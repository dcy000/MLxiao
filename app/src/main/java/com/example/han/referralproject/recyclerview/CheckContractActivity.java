package com.example.han.referralproject.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.qianyue.QianYueRepository;
import com.example.han.referralproject.qianyue.bean.DoctorInfoBean;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.route.Routerfit;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
    private QianYueRepository qianYueRepository;
    private TextView tvDoctorLevel;
    private TextView tvPrice;
    private TextView tvGoodAtNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_contract);
        initView();
        mUnbinder = ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("视  频  咨  询");
        qianYueRepository = new QianYueRepository();
    }


    @OnClick(R.id.tv_cancel_contract)
    public void onTvCancelContractClicked() {
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("确定要取消绑定吗？")
                .setMessageSize(35)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            onCancelContract();
                        }
                    }
                }).create(NDialog.CONFIRM).show();
    }

    private void onCancelContract() {
        NetworkApi.cancelContract(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                ToastUtils.showShort("取消成功");
                finish();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
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
                                       Picasso.with(CheckContractActivity.this)
                                               .load(response.docter_photo)
                                               .placeholder(R.drawable.avatar_placeholder)
                                               .error(R.drawable.avatar_placeholder)
                                               .tag(this)
                                               .fit()
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
    }
}
