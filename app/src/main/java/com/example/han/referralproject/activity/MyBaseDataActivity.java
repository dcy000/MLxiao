package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.squareup.picasso.Picasso;

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
    private LinearLayout llHeight,llWeight,llExercise,llSmoke,llEating,llDrinking;
    private UserInfoBean response;
    private static String TAG="MyBaseDataActivity";
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
                Log.e(TAG,response.toString());
                MyBaseDataActivity.this.response=response;
                Picasso.with(MyBaseDataActivity.this)
                        .load(response.user_photo)
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(mHead);
                mName.setText(response.bname);
                mAge.setText(response.age+"岁");
                mSex.setText(response.sex);
                mHeight.setText(response.height+"cm");
                mWeight.setText(response.weight+"Kg");
                mBlood.setText(response.blood_type+"型");
                mPhone.setText(response.tel);
                mNumber.setText(response.eqid);
                mMotion.setText(response.exercise_habits);
                mSmoke.setText(response.smoke);
                mEating.setText(response.eating_habits);
                mDrinking.setText(response.drink);
                String shenfen=response.sfz.substring(0,5)+"********"+response.sfz.substring(response.sfz.length()-5,response.sfz.length());
                mIdcard.setText(shenfen);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
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
        mDrinking= (TextView) findViewById(R.id.drinking);
        llHeight= (LinearLayout) findViewById(R.id.ll_height);
        llHeight.setOnClickListener(this);
        llWeight= (LinearLayout) findViewById(R.id.ll_weight);
        llWeight.setOnClickListener(this);
        llExercise= (LinearLayout) findViewById(R.id.ll_exercise);
        llExercise.setOnClickListener(this);
        llSmoke= (LinearLayout) findViewById(R.id.ll_smoke);
        llEating= (LinearLayout) findViewById(R.id.ll_eating);
        llDrinking= (LinearLayout) findViewById(R.id.ll_drinking);

        llSmoke.setOnClickListener(this);
        llEating.setOnClickListener(this);
        llDrinking.setOnClickListener(this);

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
                startActivity(new Intent(this,AlertHeightActivity.class).putExtra("data",response));
                break;
            case R.id.ll_weight:
                startActivity(new Intent(this,AlertWeightActivity.class).putExtra("data",response));
                break;
            case R.id.ll_exercise:
                startActivity(new Intent(this,AlertSportActivity.class).putExtra("data",response));
                break;
            case R.id.ll_smoke:
                startActivity(new Intent(this,AlertSmokeActivity.class).putExtra("data",response));
                break;
            case R.id.ll_eating:
                startActivity(new Intent(this,AlertEatingActivity.class).putExtra("data",response));
                break;
            case R.id.ll_drinking:
                startActivity(new Intent(this,AlertDrinkingActivity.class).putExtra("data",response));
                break;
        }
    }
}
