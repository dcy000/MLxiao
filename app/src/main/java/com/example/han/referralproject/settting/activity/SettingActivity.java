package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.settting.EventType;
import com.example.han.referralproject.settting.dialog.ClearCacheOrResetDialog;
import com.example.han.referralproject.settting.dialog.TalkTypeDialog;
import com.example.han.referralproject.settting.dialog.UpDateDialog;
import com.example.han.referralproject.settting.dialog.VoicerSetDialog;
import com.example.han.referralproject.util.UpdateAppManager;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class SettingActivity extends BaseActivity implements ClearCacheOrResetDialog.OnDialogClickListener, UpDateDialog.OnDialogClickListener {
    @BindView(R.id.rl_voice_set)
    RelativeLayout rlVoiceSet;
    @BindView(R.id.rl_wifi_set)
    RelativeLayout rlWifiSet;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.rl_reset)
    RelativeLayout rlReset;

    public String upDateUrl;
    @BindView(R.id.rl_set_keyword)
    RelativeLayout rlSetKeyword;
    @BindView(R.id.rl_set_voice_name)
    RelativeLayout rlSetVoiceName;
    @BindView(R.id.rl_set_talk_type)
    RelativeLayout rlSetTalkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        MLVoiceSynthetize.startSynthesize("主人欢迎来到设置页面");
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("设置");
    }

    @OnClick({R.id.rl_voice_set, R.id.rl_wifi_set, R.id.rl_clear_cache, R.id.rl_update,
            R.id.rl_about, R.id.rl_reset, R.id.rl_set_keyword, R.id.rl_set_voice_name, R.id.rl_set_talk_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_voice_set:
                //声音设置
                startActivity(new Intent(this, VoiceSettingActivity.class));
                break;
            case R.id.rl_wifi_set:
                //设置页面
                startActivity(new Intent(this, WifiConnectActivity.class));
                break;
            case R.id.rl_clear_cache:
                //清理缓存
                showDialog(EventType.clearCache);
//                startActivity(new Intent(this, VoicerSettingDemoActivity.class));

                break;
            case R.id.rl_update:
                //检测更新
                checkAppInfo();
                break;
            case R.id.rl_about:
                //关于
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.rl_reset:
                //恢复出厂设置
                showDialog(EventType.reset);
                break;

            case R.id.rl_set_keyword:
                //设置关键词
//                startActivity(new Intent(this, SetKeyWordActivity.class));
//                startActivity(new Intent(this, VoicerSettingDemoActivity.class));
                startActivity(new Intent(this, CustomKeyWordsActivity.class));

                break;

            case R.id.rl_set_voice_name:
                //设置发音人
                setVoiceName();
                break;

            case R.id.rl_set_talk_type:
                //设置聊天模式
                setTalkType();
                break;


        }
    }

    private void setTalkType() {
        showTalkTypeDialog();
    }

    private void showTalkTypeDialog() {
        TalkTypeDialog dialog = new TalkTypeDialog();
        dialog.show(getFragmentManager(), "talkType");

    }

    private void setVoiceName() {
//        showSetVoiceNameDialog();
        showVoicerChoiceDialog();
    }

    private void showVoicerChoiceDialog() {
        VoicerSetDialog dialog = new VoicerSetDialog();
        dialog.show(getFragmentManager(), "voicedialog");
    }


    private void checkAppInfo() {
        showLoadingDialog("检查更新中");
        Box.getRetrofit(API.class)
                .getAppVersion(AppUtils.getMeta("com.gcml.version") + "")
                .compose(RxUtils.httpResponseTransformer())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoadingDialog();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<VersionInfoBean>() {
                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        SettingActivity.this.upDateUrl = versionInfoBean.url;
                        try {
                            if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                                UpDateDialog upDateDialog = new UpDateDialog();
                                upDateDialog.setListener(SettingActivity.this);
                                upDateDialog.show(getFragmentManager(), "updatedialog");
                            } else {
                                MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                        Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDialog(EventType type) {
        ClearCacheOrResetDialog dialog = new ClearCacheOrResetDialog(type);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickConfirm(EventType type) {
        if (type.getValue().equals(EventType.reset.getValue())) {
            //恢复出厂设置
//            LocalShared.getInstance(mContext).reset();
            KVUtils.clear();
            Box.getSessionManager().clear();
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (type.getValue().equals(EventType.clearCache.getValue())) {
            //清理缓存

        }
    }

    @Override
    public void onUpdateClick(UpDateDialog dialog) {
        dialog.dismiss();
        UpdateAppManager manager = new UpdateAppManager(this);
        manager.showDownloadDialog(this.upDateUrl);
    }
}
