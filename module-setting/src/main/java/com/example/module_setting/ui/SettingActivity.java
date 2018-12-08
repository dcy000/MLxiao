package com.example.module_setting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.module_setting.EventType;
import com.example.module_setting.R;
import com.example.module_setting.R2;
import com.example.module_setting.UpdateAppManager;
import com.example.module_setting.dialog.ClearCacheOrResetDialog;
import com.example.module_setting.dialog.TalkTypeDialog;
import com.example.module_setting.dialog.UpDateDialog;
import com.example.module_setting.dialog.VoicerSetDialog;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.VersionInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class SettingActivity extends ToolbarBaseActivity implements ClearCacheOrResetDialog.OnDialogClickListener, UpDateDialog.OnDialogClickListener {
    @BindView(R2.id.rl_voice_set)
    RelativeLayout rlVoiceSet;
    @BindView(R2.id.rl_wifi_set)
    RelativeLayout rlWifiSet;
    @BindView(R2.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R2.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R2.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R2.id.rl_reset)
    RelativeLayout rlReset;

    public String upDateUrl;
    @BindView(R2.id.rl_set_keyword)
    RelativeLayout rlSetKeyword;
    @BindView(R2.id.rl_set_voice_name)
    RelativeLayout rlSetVoiceName;
    @BindView(R2.id.rl_set_talk_type)
    RelativeLayout rlSetTalkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        MLVoiceSynthetize.startSynthesize("主人欢迎来到设置页面");
        initTitle();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("设置");
    }

    @OnClick({R2.id.rl_voice_set, R2.id.rl_wifi_set, R2.id.rl_clear_cache, R2.id.rl_update,
            R2.id.rl_about, R2.id.rl_reset, R2.id.rl_set_keyword, R2.id.rl_set_voice_name, R2.id.rl_set_talk_type})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.rl_voice_set) {//声音设置
            startActivity(new Intent(this, VoiceSettingActivity.class));

        } else if (i == R.id.rl_wifi_set) {//设置页面
            emitEvent("skip2WifiConnectActivity");

        } else if (i == R.id.rl_clear_cache) {//清理缓存
            showDialog(EventType.clearCache);
//                startActivity(new Intent(this, VoicerSettingDemoActivity.class));


        } else if (i == R.id.rl_update) {//检测更新
            checkAppInfo();

        } else if (i == R.id.rl_about) {//关于
            startActivity(new Intent(this, AboutActivity.class));

        } else if (i == R.id.rl_reset) {//恢复出厂设置
            showDialog(EventType.reset);

        } else if (i == R.id.rl_set_keyword) {//设置关键词
//                startActivity(new Intent(this, SetKeyWordActivity.class));
//                startActivity(new Intent(this, VoicerSettingDemoActivity.class));
            startActivity(new Intent(this, CustomKeyWordsActivity.class));


        } else if (i == R.id.rl_set_voice_name) {//设置发音人
            setVoiceName();

        } else if (i == R.id.rl_set_talk_type) {//设置聊天模式
            setTalkType();

        }
    }
    private void checkAppInfo() {
        showLoadingDialog("检查更新中");
        Box.getRetrofit(CommonAPI.class)
                .getAppVersion(AppUtils.getMeta("com.gcml.version") + "")
                .compose(RxUtils.<VersionInfoBean>httpResponseTransformer())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoadingDialog();
                    }
                })
                .as(RxUtils.<VersionInfoBean>autoDisposeConverter(this))
                .subscribe(new CommonObserver<VersionInfoBean>() {
                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        KVUtils.put("key_new_app_url",versionInfoBean.url);
                        try {
                            if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                                UpDateDialog upDateDialog = new UpDateDialog();
                                upDateDialog.setListener(SettingActivity.this);
                                upDateDialog.show(getFragmentManager(), "updatedialog");
                            } else {
                                MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                ToastUtils.showShort("当前已经是最新版本了");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                        ToastUtils.showShort("当前已经是最新版本了");
                    }
                });
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
            emitEvent("skip2WelcomeActivity-NewTask");
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
