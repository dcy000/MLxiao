package com.example.han.referralproject.settting.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.settting.EventType;
import com.example.han.referralproject.settting.dialog.ClearCacheOrResetDialog;
import com.example.han.referralproject.settting.dialog.TalkTypeDialog;
import com.example.han.referralproject.settting.dialog.UpDateDialog;
import com.example.han.referralproject.settting.dialog.VoicerSetDialog;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        speak("主人欢迎来到设置页面");
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

//    private void showSetVoiceNameDialog() {
//        final String[] voicers = voicers();
//        int index = mIatPreferences.getInt("language_index", 0);
//        new AlertDialog.Builder(this)
//                .setTitle("设置发音人")
//                .setSingleChoiceItems(
//                        voicers,
//                        index,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mIatPreferences.edit()
//                                        .putString("iat_language_preference", voicers[which])
//                                        .putInt("language_index", which)
//                                        .commit();
//                                dialog.dismiss();
//                            }
//                        }
//                )
//                .create()
//                .showShort();
//    }


    private void checkAppInfo() {
        showLoadingDialog("检查更新中");
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                SettingActivity.this.upDateUrl = response.url;
                hideLoadingDialog();
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(SettingActivity.this.getPackageName(), 0).versionCode) {
//                                new UpdateAppManager(SettingActivity.this).showNoticeDialog(response.url);
                        UpDateDialog upDateDialog = new UpDateDialog();
                        upDateDialog.setListener(SettingActivity.this);
                        upDateDialog.show(getFragmentManager(), "updatedialog");

                    } else {
                        speak("当前已经是最新版本了");
//                        ToastUtil.showShort(mContext, "当前已经是最新版本了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                speak("当前已经是最新版本了");
//                ToastUtil.showShort(mContext, "当前已经是最新版本了");
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
            LocalShared.getInstance(mContext).reset();
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
