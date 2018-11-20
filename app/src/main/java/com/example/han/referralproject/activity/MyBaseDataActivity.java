package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.util.HealthInfo;
import com.example.han.referralproject.util.LocalShared;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.medlink.danbogh.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by gzq on 2017/11/24.
 */

public class MyBaseDataActivity extends BaseActivity implements View.OnClickListener {
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
    private UserInfoBean mUser;
    private static String TAG = "MyBaseDataActivity";
    private LinearLayout mLlHeight;
    private LinearLayout mLlWeight;
    private TextView mAddress;
    private TextView mHistory;
    private LinearLayout mLlHistory;
    private LinearLayout mLlNameInfo;
    private LinearLayout mLlAgeInfo;
    private LinearLayout mLlSexInfo;
    private LinearLayout mLlBloodInfo;
    private LinearLayout mLlIdcardInfo;
    private LinearLayout mLlAddressOnfo;
    private CharSequence idCardCode;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybasedata);
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
        UserInfoBean user = Box.getSessionManager().getUser();
        Log.i(TAG, "getData: " + user);
        Box.getRetrofit(API.class)
                .queryUserInfo(user.bid)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        Box.getSessionManager().setUser(userInfoBean);
                        showUser(userInfoBean);
                    }
                });

    }

    private void showUser(UserInfoBean user) {
        this.mUser = user;
        Picasso.with(MyBaseDataActivity.this)
                .load(user.userPhoto)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(mHead);
        mName.setText(TextUtils.isEmpty(user.bname) ? "暂未填写" : user.bname);
        idCardCode = user.sfz;
        phone = user.tel;
        if (TextUtils.isEmpty(user.sfz)
                || user.sfz.length() != 18) {
            mAge.setText(TextUtils.isEmpty(user.age) ? "暂未填写" : user.age + "岁");
        } else {
            mAge.setText(String.format(Locale.getDefault(), "%d岁", Utils.age(user.sfz)));
        }
        mSex.setText(TextUtils.isEmpty(user.sex) ? "暂未填写" : user.sex);
        mHeight.setText(TextUtils.isEmpty(user.height) ? "暂未填写" : user.height + "cm");
        mWeight.setText(TextUtils.isEmpty(user.weight) ? "暂未填写" : user.weight + "Kg");
        mBlood.setText(TextUtils.isEmpty(user.bloodType) ? "暂未填写" : user.bloodType + "型");
        mPhone.setText(TextUtils.isEmpty(user.tel) ? "暂未填写" : user.tel);
        mNumber.setText(user.eqid);
        String sports = HealthInfo.SPORTS_MAP.get(user.exerciseHabits);
        mMotion.setText(TextUtils.isEmpty(sports) ? "暂未填写" : sports);
        String smoke = HealthInfo.SMOKE_MAP.get(user.smoke);
        mSmoke.setText(TextUtils.isEmpty(smoke) ? "暂未填写" : smoke);
        String eat = HealthInfo.EAT_MAP.get(user.eatingHabits);
        mEating.setText(TextUtils.isEmpty(eat) ? "暂未填写" : eat);
        String drink = HealthInfo.DRINK_MAP.get(user.drink);
        mDrinking.setText(TextUtils.isEmpty(drink) ? "暂未填写" : drink);
        mAddress.setText(TextUtils.isEmpty(user.dz) ? "暂未填写" : user.dz);
        String deseaseHistory = HealthInfo.getDeseaseHistory(user.mh);
        mHistory.setText(TextUtils.isEmpty(deseaseHistory)
                ? "无" : deseaseHistory.replaceAll(",", "/"));

        if (!TextUtils.isEmpty(user.sfz) && user.sfz.length() == 18) {
            String shenfen = user.sfz.substring(0, 6)
                    + "********"
                    + user.sfz.substring(user.sfz.length() - 4, user.sfz.length());
            mIdcard.setText(shenfen);
        } else {
            mIdcard.setText("暂未填写");
        }
    }

    private void initView() {
        mHead = findViewById(R.id.head);
        mHead.setOnClickListener(this);
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mSex = findViewById(R.id.sex);
        mHeight = findViewById(R.id.height);
        mWeight = findViewById(R.id.weight);
        mBlood = findViewById(R.id.blood);
        mPhone = findViewById(R.id.phone);
        mIdcard = findViewById(R.id.idcard);
        mNumber = findViewById(R.id.number);
        mMotion = findViewById(R.id.motion);
        mSmoke = findViewById(R.id.smoke);
        mEating = findViewById(R.id.eating);
        mDrinking = findViewById(R.id.drinking);
        llHeight = findViewById(R.id.ll_height);
        llHeight.setOnClickListener(this);
        llWeight = findViewById(R.id.ll_weight);
        llWeight.setOnClickListener(this);
        llExercise = findViewById(R.id.ll_exercise);
        llExercise.setOnClickListener(this);
        llSmoke = findViewById(R.id.ll_smoke);
        llEating = findViewById(R.id.ll_eating);
        llDrinking = findViewById(R.id.ll_drinking);

        llSmoke.setOnClickListener(this);
        llEating.setOnClickListener(this);
        llDrinking.setOnClickListener(this);

        mLlHeight = findViewById(R.id.ll_height);
        mLlWeight = findViewById(R.id.ll_weight);
        mAddress = findViewById(R.id.address);
        mDrinking = findViewById(R.id.drinking);
        mHistory = findViewById(R.id.history);
        mLlHistory = findViewById(R.id.ll_history);
        mLlHistory.setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);

//        findViewById(R.id.ll_address_onfo).setOnClickListener(this);
//        mLlNameInfo = (LinearLayout) findViewById(R.id.ll_name_info);
//        mLlNameInfo.setOnClickListener(this);
//        mLlAgeInfo = (LinearLayout) findViewById(R.id.ll_age_info);
//        mLlAgeInfo.setOnClickListener(this);
//        mLlSexInfo = (LinearLayout) findViewById(R.id.ll_sex_info);
//        mLlSexInfo.setOnClickListener(this);
//        mLlBloodInfo = (LinearLayout) findViewById(R.id.ll_blood_info);
//        mLlBloodInfo.setOnClickListener(this);
//        mLlIdcardInfo = (LinearLayout) findViewById(R.id.ll_idcard_info);
//        mLlIdcardInfo.setOnClickListener(this);
        mLlAddressOnfo = (LinearLayout) findViewById(R.id.ll_address_onfo);
        mLlAddressOnfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mUser == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }
        switch (v.getId()) {
            default:
                break;
            case R.id.icon_back:
                finish();
                break;
            case R.id.ll_height:
                startActivity(new Intent(this, AlertHeightActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_weight:
                startActivity(new Intent(this, AlertWeightActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_exercise:
                startActivity(new Intent(this, AlertSportActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_smoke:
                startActivity(new Intent(this, AlertSmokeActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_eating:
                startActivity(new Intent(this, AlertEatingActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_drinking:
                startActivity(new Intent(this, AlertDrinkingActivity.class).putExtra("data", mUser));
                break;
            case R.id.ll_history:
                startActivity(new Intent(this, AlertMHActivity.class).putExtra("data", mUser));
                break;
            case R.id.tv_reset:
                LocalShared.getInstance(mContext).reset();
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.ll_address_onfo:
                startActivity(new Intent(this, AlertAddressActivity.class).putExtra("data", mUser));
                break;
        }
    }

}
