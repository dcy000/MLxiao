package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.medlink.danbogh.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gzq on 2017/11/24.
 */

public class MyBaseDataActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_age_info)
    LinearLayout llAgeInfo;
    @BindView(R.id.ll_sex_info)
    LinearLayout llSexInfo;
    @BindView(R.id.ll_blood_info)
    LinearLayout llBloodInfo;
    @BindView(R.id.ll_name_info)
    LinearLayout llNameInfo;
    @BindView(R.id.ll_phone_info)
    LinearLayout llPhoneInfo;
    @BindView(R.id.ll_idcard_info)
    LinearLayout llIdcardInfo;
    private CircleImageView mHead;
    /**
     * 曹建平
     */
    private TextView mName;
    /**
     * 67
     */
    private TextView mAge;
    /**
     * 男
     */
    private TextView mSex;
    /**
     * 175cm
     */
    private TextView mHeight;
    /**
     * 60Kg
     */
    private TextView mWeight;
    /**
     * O
     */
    private TextView mBlood;
    /**
     * 15181438908
     */
    private TextView mPhone;
    /**
     * 222222*********222222
     */
    private TextView mIdcard;
    /**
     * 100002
     */
    private TextView mNumber;
    /**
     * 每周一次
     */
    private TextView mMotion;
    /**
     * 每天吸烟
     */
    private TextView mSmoke;
    /**
     * 荤素搭配
     */
    private TextView mEating;
    private TextView mDrinking;
    private LinearLayout llHeight, llWeight, llExercise, llSmoke, llEating, llDrinking;
    private UserInfoBean response;
    private static String TAG = "MyBaseDataActivity";
    private LinearLayout mLlHeight;
    private LinearLayout mLlWeight;
    private TextView mAddress;
    private TextView mHistory;
    private LinearLayout mLlHistory;
    public static final String FROM_VALUE = "changeHead";
    public static final String FROM_KEY = "yst_from";
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybasedata);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健康档案");
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        NetworkApi.getMyBaseData(new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                Log.e(TAG, response.toString());
                MyBaseDataActivity.this.response = response;
                Picasso.with(MyBaseDataActivity.this)
                        .load(response.user_photo)
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(mHead);
                mName.setText(response.bname);

                mAge.setText(Utils.age(response.sfz) + "岁");
                mSex.setText(response.sex);
                mHeight.setText(response.height + "cm");
                mWeight.setText(response.weight + "Kg");
                mBlood.setText(response.blood_type + "型");
                mPhone.setText(response.tel);
                mNumber.setText(response.eqid);
                mMotion.setText(response.exercise_habits);
                mSmoke.setText(response.smoke);
                mEating.setText(response.eating_habits);
                mDrinking.setText(response.drink);
                mAddress.setText(response.dz);
                mHistory.setText(response.mh.trim());
                if (!TextUtils.isEmpty(response.sfz) && response.sfz.length() >= 15) {
                    String shenfen = response.sfz.substring(0, 5) + "********" + response.sfz.substring(response.sfz.length() - 5, response.sfz.length());
                    mIdcard.setText(shenfen);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
            }
        });
    }

    private void initView() {
        mHead = (CircleImageView) findViewById(R.id.head);
        mName = (TextView) findViewById(R.id.name);
        mAge = (TextView) findViewById(R.id.age);
        mSex = (TextView) findViewById(R.id.sex);
        mHeight = (TextView) findViewById(R.id.height);
        mWeight = (TextView) findViewById(R.id.weight);
        mBlood = (TextView) findViewById(R.id.blood);
        mPhone = (TextView) findViewById(R.id.phone);
        mIdcard = (TextView) findViewById(R.id.idcard);
        mNumber = (TextView) findViewById(R.id.number);
        mMotion = (TextView) findViewById(R.id.motion);
        mSmoke = (TextView) findViewById(R.id.smoke);
        mEating = (TextView) findViewById(R.id.eating);
        mDrinking = (TextView) findViewById(R.id.drinking);
        llHeight = (LinearLayout) findViewById(R.id.ll_height);
        llHeight.setOnClickListener(this);
        llWeight = (LinearLayout) findViewById(R.id.ll_weight);
        llWeight.setOnClickListener(this);
        llExercise = (LinearLayout) findViewById(R.id.ll_exercise);
        llExercise.setOnClickListener(this);
        llSmoke = (LinearLayout) findViewById(R.id.ll_smoke);
        llEating = (LinearLayout) findViewById(R.id.ll_eating);
        llDrinking = (LinearLayout) findViewById(R.id.ll_drinking);

        llSmoke.setOnClickListener(this);
        llEating.setOnClickListener(this);
        llDrinking.setOnClickListener(this);

        mHead.setOnClickListener(this);
        mName.setOnClickListener(this);
        mAge.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mBlood.setOnClickListener(this);
        mHeight.setOnClickListener(this);
        mLlHeight = (LinearLayout) findViewById(R.id.ll_height);
        mWeight.setOnClickListener(this);
        mLlWeight = (LinearLayout) findViewById(R.id.ll_weight);
        mPhone.setOnClickListener(this);
        mIdcard.setOnClickListener(this);
        mNumber.setOnClickListener(this);
        mAddress = (TextView) findViewById(R.id.address);
        mMotion.setOnClickListener(this);
        mSmoke.setOnClickListener(this);
        mEating.setOnClickListener(this);
        mDrinking = (TextView) findViewById(R.id.drinking);
        mDrinking.setOnClickListener(this);
        mHistory = (TextView) findViewById(R.id.history);
        mLlHistory = (LinearLayout) findViewById(R.id.ll_history);
        mLlHistory.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.icon_back:
                finish();
                break;
            case R.id.ll_height:
                startActivity(new Intent(this, AlertHeightActivity.class).putExtra("data", response));
                break;
            case R.id.ll_weight:
                startActivity(new Intent(this, AlertWeightActivity.class).putExtra("data", response));
                break;
            case R.id.ll_exercise:
                startActivity(new Intent(this, AlertSportActivity.class).putExtra("data", response));
                break;
            case R.id.ll_smoke:
                startActivity(new Intent(this, AlertSmokeActivity.class).putExtra("data", response));
                break;
            case R.id.ll_eating:
                startActivity(new Intent(this, AlertEatingActivity.class).putExtra("data", response));
                break;
            case R.id.ll_drinking:
                startActivity(new Intent(this, AlertDrinkingActivity.class).putExtra("data", response));
                break;
            case R.id.ll_history:
                startActivity(new Intent(this, AlertMHActivity.class).putExtra("data", response));
                break;
            case R.id.address:
                startActivity(new Intent(this, AlertAddressActivity.class).putExtra("data", response));
                break;
            case R.id.tv_reset:
                LocalShared.getInstance(mContext).reset();
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.head:
                startActivity(new Intent(this, RegisterVideoActivity.class).putExtra(FROM_KEY, FROM_VALUE));
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.ll_age_info, R.id.ll_sex_info, R.id.ll_blood_info, R.id.ll_name_info, R.id.ll_phone_info, R.id.ll_idcard_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_age_info:
                //修改年龄
                startActivity(new Intent(this, AlertAgeActivity.class));
                break;
            case R.id.ll_sex_info:
                startActivity(new Intent(this, AlertSexActivity.class));
                //修改性别
                break;
            case R.id.ll_blood_info:
                //修改血型
                startActivity(new Intent(this, AlertBloodTypeActivity.class));
                break;
            case R.id.ll_name_info:
                //修改姓名
                startActivity(new Intent(this, AlertNameActivity.class));
                break;
            case R.id.ll_idcard_info:
                //修改身份证号码
                break;
        }
    }
}
