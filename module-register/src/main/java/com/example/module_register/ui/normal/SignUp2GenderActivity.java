package com.example.module_register.ui.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp2GenderActivity extends VoiceToolBarActivity {

    @BindView(R2.id.iv_sign_up_man)
    ImageView mIvMan;
    @BindView(R2.id.iv_sign_up_woman)
    ImageView mIvWoman;
    @BindView(R2.id.iv_rb_sign_up_man)
    ImageView mIvRbMan;
    @BindView(R2.id.iv_rb_sign_up_woman)
    ImageView mIvRbWoman;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView mTvBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView mTvGoForward;
    public Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp2GenderActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up2_gender;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        selectMan(true);
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @OnClick(R2.id.iv_sign_up_man)
    public void onIvRbManClicked() {
        selectMan(true);
    }

    @OnClick(R2.id.iv_sign_up_woman)
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
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up2_gender_tip);
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        if (!mIvRbMan.isSelected() && !mIvRbWoman.isSelected()) {
            ToastUtils.showShort(R.string.sign_up2_gender_tip);
            MLVoiceSynthetize.startSynthesize(R.string.sign_up2_gender_tip);
            return;
        }

        String gender = mIvRbMan.isSelected() ? "男" : "女";
        cacheUserInfo(gender);
        Intent intent = SignUp3AddressActivity.newIntent(this);
        startActivity(intent);
    }
    private void cacheUserInfo(String sex) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.sex = sex;
        Box.getSessionManager().setUser(user);
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
