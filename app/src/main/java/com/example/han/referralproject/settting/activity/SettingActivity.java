package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.homepage.HospitalMainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.settting.dialog.TalkTypeDialog;
import com.example.han.referralproject.settting.dialog.VoicerSetDialog;
import com.example.han.referralproject.util.UpdateAppManager;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.VersionHelper;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import tech.linjiang.pandora.Pandora;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    LinearLayout llExitDoctorAccount;/*ll_exit_doctor_account*/
    TextView mVoice, mWifi, mKeyword, mInformant, mTalktype, mUpdate, mAbout, mReset, mClearcache, exit;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/autoupdate/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "xiaoe_autoupdate.apk";
    private TextView mLanguage;
    private TextView mDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_setting);
        mDebug = findViewById(R.id.tv_setting_debug);
        if (BuildConfig.DEBUG) {
            mDebug.setVisibility(View.VISIBLE);
        } else {
            mDebug.setVisibility(View.GONE);
        }
        mLanguage = findViewById(R.id.tv_setting_language);
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

        mDebug.setOnClickListener(this);
        mLanguage.setOnClickListener(this);
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
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), getString(R.string.set_voice_tips), false);
        mToolBar.setData(getString(R.string.set_title), R.drawable.common_icon_back, getString(R.string.set_back), 0, null, new ToolBarClickListener() {
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
            case R.id.tv_setting_debug:
                Pandora.get().open();
                break;
            case R.id.tv_setting_language:
                showLanguageDialog();
                break;
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
                showClearCacheDialog(getString(R.string.set_dialog_clear_tips));
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
                showResetDialog(getString(R.string.set_dialog_reset_tips));
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
                exitDoctorAccount(getString(R.string.set_exit_doctor_account_tips));
                break;
        }
    }

    private void showLanguageDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_layout_language)

                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.getView(R.id.language_simplified_chinese).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Resources resources = SettingActivity.this.getResources();
                                DisplayMetrics dm = resources.getDisplayMetrics();
                                Configuration config = resources.getConfiguration();
                                // 应用用户选择语言
                                config.locale = Locale.SIMPLIFIED_CHINESE;
                                resources.updateConfiguration(config, dm);

                                Intent intent = new Intent(SettingActivity.this, HospitalMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                        holder.getView(R.id.language_english).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Resources resources = SettingActivity.this.getResources();
                                DisplayMetrics dm = resources.getDisplayMetrics();
                                Configuration config = resources.getConfiguration();
                                // 应用用户选择语言
                                config.locale = Locale.ENGLISH;
                                resources.updateConfiguration(config, dm);

                                Intent intent = new Intent(SettingActivity.this, HospitalMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .setWidth(600)
                .show(getSupportFragmentManager());
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
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(), getString(R.string.set_dialog_version_tips), false);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                tipDialog.dismiss();
                MLVoiceSynthetize.startSynthesize(getApplicationContext(), getString(R.string.set_dialog_version_tips), false);
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
                .setPositiveButton(getString(R.string.dialog_button_yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserSpHelper.setDoctorId("");
                        CC.obtainBuilder(IConstant.KEY_HOSPITAL_DOCTOR_SIGN).build().callAsync();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_button_no), new View.OnClickListener() {
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
                .setPositiveButton(getString(R.string.dialog_button_yes), new View.OnClickListener() {
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
                .setNegativeButton(getString(R.string.dialog_button_no), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
