package com.gcml.old.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.entity.HealthInfo;
import com.gcml.old.auth.entity.UserInfoBean;
import com.medlink.danbogh.utils.Utils;

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
    private UserInfoBean response;
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
        NetworkApi.getMyBaseData(new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                Log.e(TAG, response.toString());
                MyBaseDataActivity.this.response = response;
                ImageLoader.with(MyBaseDataActivity.this)
                        .load(response.userPhoto)
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .into(mHead);
                mName.setText(TextUtils.isEmpty(response.bname) ? "暂未填写" : response.bname);
                idCardCode = response.sfz;
                phone = response.tel;
                if (TextUtils.isEmpty(response.sfz)
                        || response.sfz.length() != 18) {
                    mAge.setText(TextUtils.isEmpty(response.age) ? "暂未填写" : response.age + "岁");
                } else {
                    mAge.setText(Utils.age(response.sfz) + "岁");
                }
                mSex.setText(TextUtils.isEmpty(response.sex) ? "暂未填写" : response.sex);
                mHeight.setText(TextUtils.isEmpty(response.height) ? "暂未填写" : response.height + "cm");
                mWeight.setText(TextUtils.isEmpty(response.weight) ? "暂未填写" : response.weight + "Kg");
                mBlood.setText(TextUtils.isEmpty(response.bloodType) ? "暂未填写" : response.bloodType + "型");
                mPhone.setText(TextUtils.isEmpty(response.tel) ? "暂未填写" : response.tel);
                mNumber.setText(response.eqid);
                String sports = HealthInfo.SPORTS_MAP.get(response.exerciseHabits);
                mMotion.setText(TextUtils.isEmpty(sports) ? "暂未填写" : sports);
                String smoke = HealthInfo.SMOKE_MAP.get(response.smoke);
                mSmoke.setText(TextUtils.isEmpty(smoke) ? "暂未填写" : smoke);
                String eat = HealthInfo.EAT_MAP.get(response.eatingHabits);
                mEating.setText(TextUtils.isEmpty(eat) ? "暂未填写" : eat);
                String drink = HealthInfo.DRINK_MAP.get(response.drink);
                mDrinking.setText(TextUtils.isEmpty(drink) ? "暂未填写" : drink);
                mAddress.setText(TextUtils.isEmpty(response.dz) ? "暂未填写" : response.dz);
                String deseaseHistory = HealthInfo.getDeseaseHistory(response.mh);
                mHistory.setText(TextUtils.isEmpty(deseaseHistory) ? "无" : deseaseHistory.replaceAll(",", "/"));

                if (!TextUtils.isEmpty(response.sfz) && response.sfz.length() == 18) {
                    String shenfen = response.sfz.substring(0, 6) + "********" + response.sfz.substring(response.sfz.length() - 4, response.sfz.length());
                    mIdcard.setText(shenfen);
                } else {
                    mIdcard.setText("暂未填写");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort("网络繁忙");
            }
        });
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
        if (response == null) {
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
            case R.id.ll_address_onfo:
                startActivity(new Intent(this, AlertAddressActivity.class).putExtra("data", response));
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
                    ToastUtils.showShort("年龄与身份证号关联,不可更改~");
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
