package com.gcml.old.auth.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertNameActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTvNameTitle;
    private EditText mEtSignUpName;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_name);
        initView();
    }

    private void initView() {
        mTvNameTitle = findViewById(R.id.tb_name_title);
        mEtSignUpName = findViewById(R.id.et_sign_up_name);
        mTvSignUpGoBack = findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTvNameTitle.setData("修改姓名", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home")
                                .build().callAsync();
                        finish();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                nextStep();
                break;
        }
    }

    private void nextStep() {
        String name = mEtSignUpName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            speak("请输入您的姓名");
            return;
        }

        UserEntity user = new UserEntity();
        user.name = name;
        CCResult result = CC.obtainBuilder("com.gcml.auth.putUser")
                .addParam("user", user)
                .build()
                .call();
        Observable<UserEntity> data = result.getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        speak("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        speak("修改失败");
                    }
                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

}
