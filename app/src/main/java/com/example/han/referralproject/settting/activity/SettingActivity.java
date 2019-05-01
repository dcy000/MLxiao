package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.settting.dialog.TalkTypeDialog;
import com.example.han.referralproject.settting.dialog.VoicerSetDialog;
import com.example.han.referralproject.util.UpdateAppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    TextView mVoice, mWifi, mKeyword, mInformant, mTalktype, mUpdate, mAbout, mReset, mClearcache;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/autoupdate/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "xiaoe_autoupdate.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_setting);
        mVoice = findViewById(R.id.tv_setting_voice);
        mWifi = findViewById(R.id.tv_setting_wifi);
        mKeyword = findViewById(R.id.tv_setting_keyword);
        mInformant = findViewById(R.id.tv_setting_informant);
        mTalktype = findViewById(R.id.tv_setting_talktype);
        mUpdate = findViewById(R.id.tv_setting_update);
        mAbout = findViewById(R.id.tv_setting_about);
        mReset = findViewById(R.id.tv_setting_reset);
        mClearcache = findViewById(R.id.tv_setting_clearcache);

        mVoice.setOnClickListener(this);
        mWifi.setOnClickListener(this);
        mKeyword.setOnClickListener(this);
        mInformant.setOnClickListener(this);
        mTalktype.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mClearcache.setOnClickListener(this);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到设置页面。", false);
        mToolBar.setData("设 置", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting_voice:
                //声音设置
                startActivity(new Intent(this, VoiceSettingActivity.class));
                break;
            case R.id.tv_setting_wifi:
                //设置页面
                Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                break;
            case R.id.tv_setting_clearcache:
                //清理缓存
                showClearCacheDialog();
                break;
            case R.id.tv_setting_update:
                //检测更新
                checkAppInfo();
                break;
            case R.id.tv_setting_about:
                //关于
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.tv_setting_reset:
                //恢复出厂设置
                showResetDialog();
                break;
            case R.id.tv_setting_keyword:
                //设置关键词
                startActivity(new Intent(this, CustomKeyWordsActivity.class));
                break;
            case R.id.tv_setting_informant:
                //设置发音人
                showInformantDialog();
                break;
            case R.id.tv_setting_talktype:
                //设置聊天模式
                showTalkTypeDialog();
                break;
        }
    }

    private void showTalkTypeDialog() {
        TalkTypeDialog dialog = new TalkTypeDialog();
        dialog.show(getFragmentManager(), "talkType");
    }

    private void showInformantDialog() {
        VoicerSetDialog dialog = new VoicerSetDialog();
        dialog.show(getFragmentManager(), "voicedialog");
    }

    private void checkAppInfo() {
        LoadingDialog tipDialog = new LoadingDialog.Builder(SettingActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("检查更新中")
                .create();
        tipDialog.show();

        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                tipDialog.dismiss();
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(SettingActivity.this.getPackageName(), 0).versionCode) {
                        new UpdateAppManager(SettingActivity.this).showNoticeDialog(response.url);
                    } else {
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "当前已经是最新版本了", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                tipDialog.dismiss();
                MLVoiceSynthetize.startSynthesize(getApplicationContext(), "当前已经是最新版本了", false);
            }
        });
    }


    private void showClearCacheDialog() {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg("确认清除本地缓存吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void showResetDialog() {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg("确认恢复出厂设置吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复出厂设置
                        UserSpHelper.clear(getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
