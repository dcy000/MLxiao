package com.example.han.referralproject.yiyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PregnancyWenActivity extends BaseActivity {

    @BindView(R.id.iv_sign_up_man)
    ImageView mIvMan;
    @BindView(R.id.iv_sign_up_woman)
    ImageView mIvWoman;
    @BindView(R.id.iv_rb_sign_up_man)
    ImageView mIvRbMan;
    @BindView(R.id.iv_rb_sign_up_woman)
    ImageView mIvRbWoman;
    @BindView(R.id.tv_sign_up_go_back)
    TextView mTvBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView mTvGoForward;
    public Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PregnancyWenActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up_gender);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initEvent();
        ActivityHelper.addActivity(this);
    }

    private void initEvent() {
        mIvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan(true);
            }
        });

        mIvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan(false);
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

    private void initView() {
        selectMan(true);
    }

    @OnClick(R.id.iv_rb_sign_up_man)
    public void onIvRbManClicked() {
        selectMan(true);
    }

    @OnClick(R.id.iv_rb_sign_up_woman)
    public void onIvRbWomanClicked() {
        selectMan(false);
    }

    private void selectMan(boolean select) {
        mIvRbMan.setSelected(select);
        mIvRbWoman.setSelected(!select);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
//        speak(R.string.sign_up2_gender_tip);
        speak("主人,您目前是否怀孕");
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        if (!mIvRbMan.isSelected() && !mIvRbWoman.isSelected()) {
//            T.show(R.string.sign_up2_gender_tip);
//            speak(R.string.sign_up2_gender_tip);
            return;
        }

        String gender = mIvRbMan.isSelected() ? "0" : "1";
        LocalShared.getInstance(this.getApplicationContext()).setHuaiYun(gender);
        if (mIvRbWoman.isSelected()) {
            //月经结束时间
            startActivity(new Intent(this, YueJingWenActivity.class));
        } else {
            startActivity(new Intent(this, DrinkInfoActivity.class
            ));
        }
    }

    public static final String REGEX_IN_MAN = ".*(nan|nang).*";
    public static final String REGEX_IN_WOMAN = ".*(nv|lv|nu|lu).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(REGEX_IN_MAN)) {
            selectMan(true);
            return;
        }

        if (inSpell.matches(REGEX_IN_WOMAN)) {
            selectMan(false);
        }
    }
}
