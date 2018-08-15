package com.gcml.auth.register;

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
import com.gcml.lib_utils.display.ToastUtils;

public class SignUp2GenderActivity extends BaseActivity {


    ImageView mIvMan;

    ImageView mIvWoman;

    ImageView mIvRbMan;

    ImageView mIvRbWoman;

    TextView mTvBack;

    TextView mTvGoForward;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp2GenderActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.auth_activity_sign_up2_gender);
        mIvMan = (ImageView) findViewById(R.id.iv_sign_up_man);
        mIvWoman = (ImageView) findViewById(R.id.iv_sign_up_woman);
        mIvRbMan = (ImageView) findViewById(R.id.iv_rb_sign_up_man);
        mIvRbWoman = (ImageView) findViewById(R.id.iv_rb_sign_up_woman);
        mTvBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mIvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvRbManClicked();
            }
        });
        mIvRbWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvRbWomanClicked();
            }
        });
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvBackClicked();
            }
        });
        mTvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });

        mToolbar.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
        selectMan(true);
    }

    public void onIvRbManClicked() {
        selectMan(true);
    }

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
        speak(R.string.sign_up2_gender_tip);
    }


    public void onTvBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        if (!mIvRbMan.isSelected() && !mIvRbWoman.isSelected()) {
            ToastUtils.showShort(R.string.sign_up2_gender_tip);
            speak(R.string.sign_up2_gender_tip);
            return;
        }

        String gender = mIvRbMan.isSelected() ? "男" : "女";
        LocalShared.getInstance(this.getApplicationContext()).setSignUpGender(gender);
        Intent intent = SignUp3AddressActivity.newIntent(this);
        startActivity(intent);
    }

    public static final String REGEX_IN_MAN = ".*(nan|nang).*";
    public static final String REGEX_IN_WOMAN = ".*(nv|lv|nu|lu).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

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
