package com.example.han.referralproject.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DocInfoBean;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.yiyuan.bean.PersonInfoResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
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
        mTitleText.setText("签  约  医  生");

        initData();
    }

    private void initData() {
        showLoadingDialog("正在加载中...");
        NetworkApi.getPersonalInfo(this, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                hideLoadingDialog();
                if (response != null) {
                    Gson gson = new Gson();
                    PersonInfoResultBean bean = gson.fromJson(response.body(), PersonInfoResultBean.class);
                    if (bean != null) {
                        PersonInfoResultBean.DataBean data = bean.data;
                        if (data != null) {
                            getDoctorInfoByDocId(data.doid);
                        }
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                T.show("网络繁忙");
                hideLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });

    }


    private void getDoctorInfoByDocId(int categoryid) {
        showLoadingDialog("正在加载中...");
        NetworkApi.getDocInfoByDocId(categoryid + "", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                hideLoadingDialog();

                if (isDestroyed() || isFinishing()) {
                    return;
                }
                if (response == null) {
                    return;
                }
                DocInfoBean docInfoBean = new Gson().fromJson(response.body(), DocInfoBean.class);
                if (docInfoBean == null || !docInfoBean.tag) {
                    return;
                }
                DocInfoBean.DataBean data = docInfoBean.data;
                if (data == null) {
                    return;
                }
                String docter_photo = data.docter_photo;
                if (!TextUtils.isEmpty(docter_photo)) {
                    Picasso.with(CheckContractActivity.this)
                            .load(docter_photo)
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder)
                            .tag(this)
                            .fit()
                            .into(ivDoctorAvatar);
                }

                tvDoctorName.setText(String.format(getString(R.string.doctor_name), data.doctername));

                tvProfessionalRank.setText(String.format(getString(R.string.doctor_zhiji), data.duty));
                tvGoodAt.setText(String.format(getString(R.string.doctor_shanchang), data.department));
                tvService.setText(String.format(getString(R.string.doctor_shoufei), data.service_amount + ""));

//                tvDoctorLevel.setText(data.duty);
//                tvGoodAtNew.setText(data.department);
//                tvPrice.setText(data.service_amount + "");
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hideLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
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
        NetworkApi.cancelContract(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                T.show("取消成功");
                finish();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
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

    }

    private void initView() {
        tvDoctorLevel = (TextView) findViewById(R.id.tv_doctor_level);
        tvGoodAtNew = (TextView) findViewById(R.id.tv_good_at);
        tvPrice = (TextView) findViewById(R.id.tv_price);
    }


}
