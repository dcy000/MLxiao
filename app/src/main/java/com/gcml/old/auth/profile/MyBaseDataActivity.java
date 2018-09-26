package com.gcml.old.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.entity.HealthInfo;
import com.medlink.danbogh.utils.Utils;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    private UserEntity mUser;
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
        Observable<UserEntity> data = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        showUser(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void showUser(UserEntity user) {
        MyBaseDataActivity.this.mUser = user;
        ImageLoader.with(MyBaseDataActivity.this)
                .load(mUser.avatar)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(mHead);
        mName.setText(TextUtils.isEmpty(user.name) ? "暂未填写" : user.name);
        idCardCode = user.idCard;
        phone = user.phone;
        if (TextUtils.isEmpty(user.idCard)
                || user.idCard.length() != 18) {
            mAge.setText(TextUtils.isEmpty(user.age) ? "暂未填写" : user.age + "岁");
        } else {
            mAge.setText(String.format(Locale.getDefault(),"%d岁", Utils.age(user.idCard)));
        }
        mSex.setText(TextUtils.isEmpty(user.sex) ? "暂未填写" : user.sex);
        mHeight.setText(TextUtils.isEmpty(user.height) ? "暂未填写" : user.height + "cm");
        mWeight.setText(TextUtils.isEmpty(user.weight) ? "暂未填写" : user.weight + "Kg");
        mBlood.setText(TextUtils.isEmpty(user.bloodType) ? "暂未填写" : user.bloodType + "型");
        mPhone.setText(TextUtils.isEmpty(user.phone) ? "暂未填写" : user.phone);
        mNumber.setText(user.deviceId);
        String sports = HealthInfo.SPORTS_MAP.get(user.sportsHabits);
        mMotion.setText(TextUtils.isEmpty(sports) ? "暂未填写" : sports);
        String smoke = HealthInfo.SMOKE_MAP.get(user.smokeHabits);
        mSmoke.setText(TextUtils.isEmpty(smoke) ? "暂未填写" : smoke);
        String eat = HealthInfo.EAT_MAP.get(user.eatingHabits);
        mEating.setText(TextUtils.isEmpty(eat) ? "暂未填写" : eat);
        String drink = HealthInfo.DRINK_MAP.get(user.drinkHabits);
        mDrinking.setText(TextUtils.isEmpty(drink) ? "暂未填写" : drink);
        mAddress.setText(TextUtils.isEmpty(user.address) ? "暂未填写" : user.address);
        String deseaseHistory = HealthInfo.getDeseaseHistory(user.deseaseHistory);
        mHistory.setText(TextUtils.isEmpty(deseaseHistory)
                ? "无" : deseaseHistory.replaceAll(",", "/"));

        if (!TextUtils.isEmpty(user.idCard) && user.idCard.length() == 18) {
            String shenfen = user.idCard.substring(0, 6)
                    + "********"
                    + user.idCard.substring(user.idCard.length() - 4, user.idCard.length());
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

        findViewById(R.id.ll_address_onfo).setOnClickListener(this);
        mLlNameInfo = (LinearLayout) findViewById(R.id.ll_name_info);
        mLlNameInfo.setOnClickListener(this);
        mLlAgeInfo = (LinearLayout) findViewById(R.id.ll_age_info);
        mLlAgeInfo.setOnClickListener(this);
        mLlSexInfo = (LinearLayout) findViewById(R.id.ll_sex_info);
        mLlSexInfo.setOnClickListener(this);
        mLlBloodInfo = (LinearLayout) findViewById(R.id.ll_blood_info);
        mLlBloodInfo.setOnClickListener(this);
        mLlIdcardInfo = (LinearLayout) findViewById(R.id.ll_idcard_info);
        mLlIdcardInfo.setOnClickListener(this);
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
            case R.id.ll_address_onfo:
                startActivity(new Intent(this, AlertAddressActivity.class).putExtra("data", mUser));
                break;
            case R.id.tv_reset:
                LocalShared.getInstance(mContext).reset();
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.head:
                if (TextUtils.isEmpty(phone)
                        || !TextUtils.isDigitsOnly(phone)
                        || phone.length() != 11) {
                    ToastUtils.showShort("请重新登陆");
                    return;
                }
                SMSVerificationDialog dialog = new SMSVerificationDialog();
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                dialog.setArguments(bundle);
                dialog.setListener(() -> {
                    modifyHead();
                });
                dialog.show(getFragmentManager(), "phoneCode");
                break;

            case R.id.ll_age_info:
                //修改年龄
                if (mIdcard.getText().toString().equals("暂未填写")) {
                    startActivity(new Intent(this, AlertAgeActivity.class));
                } else {
                    ToastUtils.showShort("年龄与身份证号关联,不可更改");
                }
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
                startActivity(new Intent(this, AlertIDCardActivity.class));
                break;
        }
    }

    private void modifyHead() {
        CC.obtainBuilder("com.gcml.auth.face.signup")
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result.isSuccess()) {
                            ToastUtils.showShort("更换人脸成功");
                        }
                    }
                });
    }


}
