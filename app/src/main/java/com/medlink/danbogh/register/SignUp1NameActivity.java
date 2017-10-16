package com.medlink.danbogh.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.medlink.danbogh.utils.T;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp1NameActivity extends BaseActivity {

    @BindView(R.id.et_sign_up_name)
    EditText mEtName;
    @BindView(R.id.tv_sign_up_go_back)
    TextView mTvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView mTvGoForward;
    @BindView(R.id.cl_sign_up_root_name)
    ConstraintLayout mClSignUpRootName;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1_name);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.sign_up1_name_tip);
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        T.show("上一步");
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            T.show(R.string.empty_name);
            speak(R.string.empty_name);
            return;
        }
        Intent intent = SignUp2GenderActivity.newIntent(this);
        startActivity(intent);
    }

    public static final String REGEX_IN_NAME = "(.*)的(\\w)";
    public static final String REGEX_IN_DEL_ALL = ".*(quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_DEL = ".*(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        Pattern patternInName = Pattern.compile(REGEX_IN_NAME);
        Matcher matcherInName = patternInName.matcher(result);
        if (matcherInName.find()) {
            String post = matcherInName.group(matcherInName.groupCount());
            String postSpell = PinYinUtils.converterToSpell(post);
            String prev = matcherInName.group(1);
            int length = prev.length();
            String substring;
            String subSpell;
            for (int i = 0; i < length; i++) {
                substring = prev.substring(i, i + 1);
                subSpell = PinYinUtils.converterToSpell(substring);
                if (postSpell.contains(subSpell) || subSpell.contains(postSpell)) {
                    String name = mEtName.getText() + substring;
                    mEtName.setText(name);
                    mEtName.setSelection(name.length());
                    break;
                }
            }
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(REGEX_IN_DEL_ALL)) {
            mEtName.setText("");
            return;
        }

        if (inSpell.matches(REGEX_IN_DEL)) {
            String target = mEtName.getText().toString().trim();
            mEtName.setText(target.substring(0, target.length() - 1));
            mEtName.setSelection(target.length() - 1);
        }
    }
}
