package com.example.han.referralproject.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.qianyue.QianYueRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_contract);
        mUnbinder = ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("签  约  医  生");
        qianYueRepository = new QianYueRepository();

        NetworkApi.DoctorInfo(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<Doctor>() {
            @Override
            public void onSuccess(Doctor response) {
                if (!TextUtils.isEmpty(response.getDocter_photo())) {
                    Picasso.with(CheckContractActivity.this)
                            .load(response.getDocter_photo())
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder)
                            .tag(this)
                            .fit()
                            .into(ivDoctorAvatar);
                }
                tvDoctorName.setText(String.format(getString(R.string.doctor_name), response.getDoctername()));
                tvProfessionalRank.setText(String.format(getString(R.string.doctor_zhiji), response.getDuty()));
                tvGoodAt.setText(String.format(getString(R.string.doctor_shanchang), response.getDepartment()));
                tvService.setText(String.format(getString(R.string.doctor_shoufei), response.getService_amount()));
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
            }
        });
    }


    @OnClick(R.id.tv_cancel_contract)
    public void onTvCancelContractClicked() {
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("确定要取消签约吗？")
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

//        CCResult result;
//        Observable<UserEntity> rxUser;
//        result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
//        rxUser = result.getDataItem("data");
//        rxUser.subscribeOn(Schedulers.io())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new DefaultObserver<UserEntity>() {
//                    @Override
//                    public void onNext(UserEntity user) {
//                        String doctorId = user.doctorId;
//
//
//                    }
//                });

    }
}
