package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.service.API;
import com.example.module_register.ui.normal.SignUp1NameActivity;
import com.example.module_register.ui.sample.SignUp01NameActivity;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.signin.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ChooseLoginTypeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_phone_sign_in)
    TextView tvPhoneSignIn;
    @BindView(R.id.et_sign_in_password)
    TextView etSignInPassword;
    @BindView(R.id.tv_face_sign_in)
    TextView tvFaceSignIn;
    @BindView(R.id.account_tip)
    TextView accountTip;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.sign_up_fast)
    TextView signUpFast;
    @BindView(R.id.cv_sign_in)
    CardView cvSignIn;
    @BindView(R.id.cb_sign_in_agree)
    CheckBox cbSignInAgree;
    @BindView(R.id.tv_sign_in_agree)
    TextView tvSignInAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_type);
        ButterKnife.bind(this);
        MLVoiceSynthetize.startSynthesize("主人，想要登录，请说人脸登录或者手机登录。如果您还没有账号，请说我要注册。");
        tvPhoneSignIn.setOnClickListener(this);
        tvFaceSignIn.setOnClickListener(this);
        accountTip.setOnClickListener(this);
        regist.setOnClickListener(this);
        signUpFast.setOnClickListener(this);

        SpannableStringBuilder agreeBuilder = new SpannableStringBuilder("我同意用户协议");
        agreeBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF380000")),
                3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agreeBuilder.setSpan(agreeClickableSpan, 3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSignInAgree.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignInAgree.setText(agreeBuilder);
    }

    private ClickableSpan agreeClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            startActivity(new Intent(ChooseLoginTypeActivity.this, AgreementActivity.class));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_sign_in:
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.tv_face_sign_in:
                //获取所有账号
                List<UserInfoBean> users = Box.getUsersFromRoom();
                if (users == null) {
                    ToastUtils.showLong("未检测到您的登录历史，请输入账号和密码登录");
                    startActivity(new Intent(this, SignInActivity.class));
                } else {
                    signInWithFace();
                }
                break;
            case R.id.account_tip://注册
                startActivity(new Intent(ChooseLoginTypeActivity.this, SignUp1NameActivity.class));
                break;
            case R.id.regist://注册
                startActivity(new Intent(ChooseLoginTypeActivity.this, SignUp1NameActivity.class));
                break;
            case R.id.sign_up_fast://快速注册
                Intent intent = new Intent(ChooseLoginTypeActivity.this, SignUp01NameActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void signInWithFace() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<UserInfoBean> usersFromRoom = Box.getUsersFromRoom();
                if (usersFromRoom == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();
                for (UserInfoBean user : usersFromRoom) {
                    userIds.append(user.bid);
                }
                String substring = userIds.substring(0, userIds.length() - 1);
                emitter.onNext(substring);
            }
        }).flatMap(new Function<String, ObservableSource<HttpResult<ArrayList<UserInfoBean>>>>() {
            @Override
            public ObservableSource<HttpResult<ArrayList<UserInfoBean>>> apply(String s) throws Exception {
                return Box.getRetrofit(CommonAPI.class)
                        .queryAllLocalUsers(s);
            }
        }).compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<ArrayList<UserInfoBean>>() {
                    @Override
                    public void onNext(ArrayList<UserInfoBean> users) {
                        Intent intent = new Intent();
                        intent.setClass(ChooseLoginTypeActivity.this, FaceSignInActivity.class);
                        intent.putExtra("skip", false);
                        intent.putExtra("currentUser", false);
                        intent.putParcelableArrayListExtra("users", users);
                        startActivityForResult(intent, 1001);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showLong(ex.getMessage());
                        startActivity(new Intent(ChooseLoginTypeActivity.this, SignInActivity.class));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            int faceResult = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
            switch (faceResult) {
                case FaceConstants.AUTH_FACE_SUCCESS:
                    ActivityUtils.skipActivity(MainActivity.class);
                    break;
                case FaceConstants.AUTH_FACE_FAIL:

                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        String inSpell = PinYinUtils.converterToSpell(result);
        ToastUtils.showShort(result);
        if (inSpell.matches(".*((shou|sou)ji).*")) {
            tvPhoneSignIn.performClick();
            return;
        }
        if (inSpell.matches(".*((ren|reng)lian).*")) {
            tvFaceSignIn.performClick();
            return;
        }

        if (inSpell.matches(".*((zu|zhu)ce).*")) {
            regist.performClick();
            return;
        }
    }

}
