package com.example.han.referralproject.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.han.referralproject.R;
import com.example.han.referralproject.StatusBarFragment;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.module_control_volume.VolumeControlFloatwindow;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

public class HospitalMainActivity extends AppCompatActivity implements HttpListener<AccessToken> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        //启动音量控制悬浮按钮
        VolumeControlFloatwindow.init(this.getApplicationContext());
        initAToken();
        initMainPage();
        ActivityHelper.finishAll();
    }

    private void initMainPage() {
        HosMainFragment hosMainFragment = new HosMainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_content, hosMainFragment).commit();
        if (showStateBar != null) {
            showStateBar.showStateBar(true);
        }
    }

    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        ToastUtils.showShort("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }

    private ShowStateBar showStateBar;

    /**
     * 控制状态bar中日期信息的显示或者隐藏
     *
     * @param showStateBar
     */
    public void setShowStateBarListener(ShowStateBar showStateBar) {
        this.showStateBar = showStateBar;
    }

    public interface ShowStateBar {
        void showStateBar(boolean isshow);
    }
}
