package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.yiyuan.bean.PersonInfoResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gzq on 2017/11/24.
 */

public class MyBaseDataActivity extends BaseActivity {
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.blood)
    TextView blood;
    @BindView(R.id.height)
    TextView height;
    @BindView(R.id.ll_height)
    LinearLayout llHeight;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.ll_weight)
    LinearLayout llWeight;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.idcard)
    TextView idcard;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.motion)
    TextView motion;
    @BindView(R.id.ll_exercise)
    LinearLayout llExercise;
    @BindView(R.id.smoke)
    TextView smoke;
    @BindView(R.id.ll_smoke)
    LinearLayout llSmoke;
    @BindView(R.id.eating)
    TextView eating;
    @BindView(R.id.ll_eating)
    LinearLayout llEating;
    @BindView(R.id.drinking)
    TextView drinking;
    @BindView(R.id.ll_drinking)
    LinearLayout llDrinking;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.jiazubingshi)
    TextView jiazubingshi;
    @BindView(R.id.yichuanbignshi)
    TextView yichuanbignshi;
    @BindView(R.id.chuafangpaiqi)
    TextView chuafangpaiqi;
    @BindView(R.id.chufangranliao)
    TextView chufangranliao;
    @BindView(R.id.yinshuizhuangkuang)
    TextView yinshuizhuangkuang;
    @BindView(R.id.cesuo)
    TextView cesuo;
    @BindView(R.id.shengchu)
    TextView shengchu;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.canji)
    TextView canji;
    private UserInfoBean response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybasedata);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("个人信息");
    }



    private void initView() {
        showLoadingDialog("正在加载中...");
        NetworkApi.getPersonalInfo(this, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response != null) {
                    Gson gson = new Gson();
                    PersonInfoResultBean bean = gson.fromJson(response.body(), PersonInfoResultBean.class);
                    if (bean != null) {
                        PersonInfoResultBean.DataBean data = bean.data;
                        if (data != null) {
                            name.setText(data.bname+"");
                            age.setText(data.age+"");
                            sex.setText(data.sex+"");
                            phone.setText(data.tel+"");
                            idcard.setText(data.sfz+"");
                            number.setText(data.eqid+"");

                            Picasso.with(MyBaseDataActivity.this)
                                    .load(data.user_photo)
                                    .placeholder(R.drawable.avatar_placeholder)
                                    .error(R.drawable.avatar_placeholder)
                                    .tag(this)
                                    .fit()
                                    .into(head);

                            PersonInfoResultBean.DataBean.RecordBean record = data.record;
                            if (record != null) {
                                address.setText(record.address);
                                blood.setText(record.bloodType + "");
                                height.setText(record.height + "");
                                weight.setText(record.weight + "");
                                motion.setText(record.educationalLevel + "");
                                smoke.setText(record.professionType + "");
                                eating.setText(record.medicalPayments + "");
                                drinking.setText(record.medicationAllergy + "");
                                String text2 = record.exposureHistory + "";
                                history.setText(text2.replaceAll("null",""));

                                String text = record.kinsfolkDiseasesType + "";
                                jiazubingshi.setText(text.replaceAll("null",""));
                                String text1 = record.geneticHistory + "";
                                yichuanbignshi.setText(text1.replaceAll("null",""));
                                canji.setText(record.disabilitySituation + "");
                                chuafangpaiqi.setText(record.kitchenExhaust + "");
                                chufangranliao.setText(record.kitchenFuel + "");
                                yinshuizhuangkuang.setText(record.waterEnvironment + "");
                                cesuo.setText(record.toiletPosition + "");
                                shengchu.setText(record.livestockBar + "");
                            }
                        }


                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                T.show("网络繁忙");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });


    }


}
