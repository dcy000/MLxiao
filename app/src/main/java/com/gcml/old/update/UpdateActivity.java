package com.gcml.old.update;

import android.content.Context;

import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.UpdateAppManager;
import com.gcml.common.communication.IVersionUpdate;
import com.gcml.common.utils.VersionHelper;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;

import static com.kk.taurus.playerbase.config.AppContextAttach.getApplicationContext;

/**
 * Created by lenovo on 2019/1/15.
 */

public class UpdateActivity implements IVersionUpdate {

    @Override
    public void update(Context context) {
        LoadingDialog tipDialog = new LoadingDialog.Builder(context)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("检查更新中")
                .create();
        tipDialog.show();
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                tipDialog.dismiss();
                if (response != null && response.vid > VersionHelper.getAppVersionCode(getApplicationContext())) {
                    new UpdateAppManager(context).showNoticeDialog(response.url);
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

}
