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
import com.example.han.referralproject.cc.CCFaceRecognitionActions;
import com.gcml.lib_utils.data.StringUtil;
import com.gcml.old.auth.entity.HealthInfo;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.medlink.danbogh.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
                Picasso.with(MyBaseDataActivity.this)
                        .load(response.userPhoto)
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(mHead);
                mName.setText(response.bname);

                mAge.setText(Utils.age(response.sfz) + "岁");
                mPhone.setText(TextUtils.isEmpty(response.sex) ? "尚未填写" : response.sex);
                mHeight.setText(TextUtils.isEmpty(response.height) ? "尚未填写" : response.height + "cm");
                mWeight.setText(TextUtils.isEmpty(response.weight) ? "尚未填写" : response.weight + "Kg");
                mBlood.setText(TextUtils.isEmpty(response.bloodType) ? "尚未填写" : response.bloodType + "型");
                mPhone.setText(TextUtils.isEmpty(response.tel) ? "尚未填写" : response.tel);
                mNumber.setText(response.eqid);
                String sports = HealthInfo.SPORTS_MAP.get(response.exerciseHabits);
                mMotion.setText(TextUtils.isEmpty(sports) ? "尚未填写" : sports);
                String smoke = HealthInfo.SPORTS_MAP.get(response.smoke);
                mSmoke.setText(TextUtils.isEmpty(smoke) ? "尚未填写" : smoke);
                String eat = HealthInfo.SPORTS_MAP.get(response.eatingHabits);
                mEating.setText(TextUtils.isEmpty(eat) ? "尚未填写" : eat);
                String drink = HealthInfo.SPORTS_MAP.get(response.drink);
                mDrinking.setText(TextUtils.isEmpty(drink) ? "尚未填写" : drink);
                mAddress.setText(TextUtils.isEmpty(response.dz) ? "尚未填写" : response.dz);
                String deseaseHistory = HealthInfo.getDeseaseHistory(response.mh);
                mHistory.setText(TextUtils.isEmpty(deseaseHistory) ? "尚未填写" : deseaseHistory);

                String shenfen = response.sfz.substring(0, 5) + "********" + response.sfz.substring(response.sfz.length() - 5, response.sfz.length());
                mIdcard.setText(shenfen);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
            }
        });
    }

    private void initView() {
        mHead = findViewById(R.id.head);
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

        mHead.setOnClickListener(this);
        mName.setOnClickListener(this);
        mAge.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mBlood.setOnClickListener(this);
        mHeight.setOnClickListener(this);
        mLlHeight = findViewById(R.id.ll_height);
        mWeight.setOnClickListener(this);
        mLlWeight = findViewById(R.id.ll_weight);
        mPhone.setOnClickListener(this);
        mIdcard.setOnClickListener(this);
        mNumber.setOnClickListener(this);
        mAddress = findViewById(R.id.address);
        mMotion.setOnClickListener(this);
        mSmoke.setOnClickListener(this);
        mEating.setOnClickListener(this);
        mDrinking = findViewById(R.id.drinking);
        mDrinking.setOnClickListener(this);
        mHistory = findViewById(R.id.history);
        mLlHistory = findViewById(R.id.ll_history);
        mLlHistory.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
                CC.obtainBuilder("face_recognition")
                        .setActionName("To_RegisterHead2XunfeiActivity")
                        .addParam("key_xfid", StringUtil.produceXfid())
                        .build().callAsyncCallbackOnMainThread((cc, result) -> {
                    if ("RegistHeadSuccess".equals(result.getDataItem("key_cc_callback"))) {


                    }

                });
                break;

        }
    }

}
