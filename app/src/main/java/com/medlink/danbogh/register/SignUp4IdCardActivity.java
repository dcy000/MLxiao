package com.medlink.danbogh.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.medlink.danbogh.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/10/12.
 */

public class SignUp4IdCardActivity extends BaseActivity {


    TextView tvGoBack;

    TextView tvGoForward;

    EditText etIdCard;
    private ConstraintLayout clRoot;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp4IdCardActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up4_id_card);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        etIdCard = (EditText) findViewById(R.id.et_id_card);
        mToolbar.setVisibility(View.GONE);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_id_card);
        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClRootClicked();
            }
        });
        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_id_card_tip);
    }

    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String idCard = etIdCard.getText().toString().trim();
        if (!Utils.checkIdCard1(idCard)) {
            speak(R.string.sign_up_id_card_tip);
            return;
        }

        LocalShared.getInstance(this.getApplicationContext()).setSignUpIdCard(idCard);
        Intent intent = SignUp5MobileVerificationActivity.newIntent(this);
        startActivity(intent);
    }

    public static final String REGEX_IN_DEL = "(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole)";
    public static final String REGEX_IN_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_ID_CARD = "(\\d*[x|X]?)";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(REGEX_IN_DEL_ALL)) {
            etIdCard.setText("");
            return;
        }

        if (inSpell.matches(REGEX_IN_DEL)) {
            String target = etIdCard.getText().toString().trim();
            if (!TextUtils.isEmpty(target)) {
                etIdCard.setText(target.substring(0, target.length() - 1));
                etIdCard.setSelection(target.length() - 1);
            }
            return;
        }
        String in = Utils.isNumeric(result) || result.equalsIgnoreCase("x") ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        Pattern patternInIdCard = Pattern.compile(REGEX_IN_ID_CARD);
        Matcher matcherInIdCard = patternInIdCard.matcher(in);
        if (matcherInIdCard.find()) {
            String target = etIdCard.getText().toString().trim();
            String s = target + matcherInIdCard.group(matcherInIdCard.groupCount());
            if (s.length() > 18) {
                s = s.substring(0, 18);
            }
            etIdCard.setText(s);
            etIdCard.setSelection(s.length());
        }
    }
}