package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.homepage.HospitalMainActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.settting.dialog.TalkTypeDialog;
import com.example.han.referralproject.settting.dialog.VoicerSetDialog;
import com.example.han.referralproject.util.UpdateAppManager;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.VersionHelper;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    LinearLayout llExitDoctorAccount;/*ll_exit_doctor_account*/
    TextView mVoice, mWifi, mKeyword, mInformant, mTalktype, mUpdate, mAbout, mReset, mClearcache, exit;
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
        exit = findViewById(R.id.tv_exit);
        llExitDoctorAccount = findViewById(R.id.ll_exit_doctor_account);

        mVoice.setOnClickListener(this);
        mWifi.setOnClickListener(this);
        mKeyword.setOnClickListener(this);
        mInformant.setOnClickListener(this);
        mTalktype.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mClearcache.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，欢迎来到设置页面。", false);
        mToolBar.setData("设 置", R.drawable.common_icon_back, "返回", 0, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                onRightClickWithPermission(new IAction() {
                    @Override
                    public void action() {
                        startActivity(new Intent(SettingActivity.this, HospitalMainActivity.class));
                        finish();
                    }
                });
            }
        });

        if (TextUtils.isEmpty(UserSpHelper.getDoctorId())) {
            llExitDoctorAccount.setVisibility(View.GONE);
        }
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
                startActivity(new Intent(this, WifiConnectActivity.class));
                break;
            case R.id.tv_setting_clearcache:
                //清理缓存
                showClearCacheDialog("确认清除本地缓存吗？");
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
                showResetDialog("确认恢复出厂设置吗？");
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
            case R.id.tv_exit:
                exitDoctorAccount("退出当前医生账号");
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
        LoadingDialog tipDialog = new LoadingDialog.Builder(SettingActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("检查更新中")
                .create();
        tipDialog.show();
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                tipDialog.dismiss();
                if (response != null && response.vid > VersionHelper.getAppVersionCode(getApplicationContext())) {
                    new UpdateAppManager(SettingActivity.this).showNoticeDialog(response.url);
//                    checkUpdate(FILE_NAME, response.v_log, response.vid, response.vnumber, response.url, response.v_md5);
                } else {
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(), "当前已经是最新版本了", false);
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

    private void checkUpdate(final String checkUrl, final String updateLog, final int versionCode, final String versionName, final String updateUrl, final String updateMD5) {
//        UpdateManager.create(this).setChecker(new IUpdateChecker() {
//            @Override
//            public void check(ICheckAgent agent, String url) {
//                Log.e("ezy.update", "checking");
//                agent.setInfo("");
//            }
//        }).setUrl(checkUrl).setManual(true).setNotifyId(998).setParser(new IUpdateParser() {
//            @Override
//            public UpdateInfo parse(String source) throws Exception {
//                UpdateInfo info = new UpdateInfo();
//                info.hasUpdate = true;
//                info.updateContent = updateLog;
//                info.versionCode = versionCode;
//                info.versionName = versionName;
//                info.url = updateUrl;
//                info.md5 = updateMD5;
//                info.size = 10149314;
//                info.isForce = false;
//                info.isIgnorable = true;
//                info.isSilent = false;
//                return info;
//            }
//        }).check();
    }

    private void exitDoctorAccount(String msg) {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg(msg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserSpHelper.setDoctorId("");
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

    private void showClearCacheDialog(String msg) {
        showResetDialog(msg);
    }

    private void showResetDialog(String msg) {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg(msg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复出厂设置
                        UserSpHelper.clear(getApplicationContext());
                        CC.obtainBuilder("com.gcml.auth.face.deleteGroup")
                                .build()
                                .callAsync();
                        CC.obtainBuilder("com.gcml.auth.deleteUsers")
                                .build()
                                .callAsync();
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
