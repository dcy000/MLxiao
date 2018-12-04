package com.example.module_register.ui.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.RegularExpressionUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/10/12.
 */

public class SignUp4IdCardActivity extends VoiceToolBarActivity {

    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    @BindView(R2.id.et_id_card)
    EditText etIdCard;
    public Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp4IdCardActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up4_id_card;
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
        return new BasePresenter(this) {};
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_id_card_tip);
    }

    @OnClick(R2.id.cl_sign_up_root_id_card)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String idCard = etIdCard.getText().toString().trim();
        if (!RegularExpressionUtils.isIDCard18(idCard)) {
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_id_card_tip);
            return;
        }

        cacheUserInfo(idCard);
        Intent intent = SignUp5MobileVerificationActivity.newIntent(this);
        startActivity(intent);
    }
    private void cacheUserInfo(String idCard) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.sfz = idCard;
        Box.getSessionManager().setUser(user);
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
        String in = DataUtils.isInteger(result) || result.equalsIgnoreCase("x") ? result : DataUtils.removeNonnumeric(DataUtils.chineseMapToNumber(result));
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