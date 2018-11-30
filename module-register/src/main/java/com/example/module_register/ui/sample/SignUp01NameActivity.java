package com.example.module_register.ui.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp01NameActivity extends VoiceToolBarActivity {

    @BindView(R2.id.et_sign_up_name)
    EditText mEtName;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView mTvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView mTvGoForward;
    @BindView(R2.id.cl_sign_up_root_name)
    ConstraintLayout mClSignUpRootName;

    private Unbinder mUnbinder;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_sign_up1_name;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);

    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up1_name_tip);
    }

    @OnClick(R2.id.cl_sign_up_root_name)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        ToastUtils.showShort("上一步");
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.empty_name);
            MLVoiceSynthetize.startSynthesize(R.string.empty_name);
            return;
        }

        UserInfoBean user = new UserInfoBean();
        user.bname = name;
        Box.getSessionManager().setUser(user);
        Intent intent = SignUp02MobileVerificationActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static final String REGEX_IN_NAME = "(.*)的(\\w)";
    public static final String REGEX_IN_DEL_ALL = ".*(quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_DEL = ".*(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);
        if (result.matches(REGEX_IN_GO_BACK) && mTvGoBack.isEnabled()) {
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
            if (!TextUtils.isEmpty(target)) {
                mEtName.setText(target.substring(0, target.length() - 1));
                mEtName.setSelection(target.length() - 1);
            }
        }
    }
}