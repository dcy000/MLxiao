package com.gcml.module_auth_hospital.ui.profile.update;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiException;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertIDCardActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTbIdcardTitle;
    /**
     * 您的身份证号码
     */
    private TextView mTvSignUpIdCard;
    /**
     * 请输入身份证号码
     */
    private EditText mEtIdCard;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;
    private ConstraintLayout mClSignUpRootIdCard;
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_n_activity_alert_idcard);
        user = getIntent().getParcelableExtra("data");
        initView();
    }

    private void initView() {
        mTbIdcardTitle = (TranslucentToolBar) findViewById(R.id.tb_idcard_title);
        mTvSignUpIdCard = (TextView) findViewById(R.id.tv_sign_up_id_card);
        mEtIdCard = (EditText) findViewById(R.id.et_id_card);
        mTvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);
        mClSignUpRootIdCard = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_id_card);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTbIdcardTitle.setData("修改身份证号", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipMainActivity();
                        finish();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_sign_up_go_back) {
            finish();
        } else if (i == R.id.tv_sign_up_go_forward) {
            nextStep();
        }
    }

    private void nextStep() {
        String idCard = mEtIdCard.getText().toString();
        if (TextUtils.isEmpty(idCard)) {
            speak("请输入身份证号码");
            return;
        }
        if (!Utils.checkIdCard1(idCard)) {
            speak("请输入正确的身份证号码");
            return;
        }
        checkIdCard(idCard);
    }

    private void checkIdCard(final String idCard) {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .isAccountExist(idCard, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("身份证已存在");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).code() == 500) {
                                putUserInfo(idCard);
                            }
                        }
                    }
                });
    }

    private void putUserInfo(String idCard) {
        if (user == null) {
            ToastUtils.showLong("请重新登录");
            return;
        }
        user.idCard = idCard;
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .updateUserEntity(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        speak("修改成功");
                        ToastUtils.showShort("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("修改失败");
                        speak("修改失败");
                    }
                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

}
