package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.bloodpressure_habit.GetHypertensionHandActivity;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import static android.app.Activity.RESULT_OK;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 10:15
 * created by:gzq
 * description:不需要上传测量数据的单测
 */
public class NonUploadSingleMeasureBloodpressureFragment extends Bloodpressure_Fragment {
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;
    private boolean isOnPause=false;
    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        getHypertensionHand();
    }


    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3&&!isOnPause) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);
        }
    }

    /**
     * 获取惯用手
     */
    private void getHypertensionHand() {
        String userHypertensionHand = UserSpHelper.getUserHypertensionHand();
        if (TextUtils.isEmpty(userHypertensionHand)) {
            //还没有录入惯用手，则跳转到惯用手录入activity
            GetHypertensionHandActivity.startActivityForResult(this, CODE_REQUEST_GETHYPERTENSIONHAND);
        } else {
            if ("0".equals(userHypertensionHand)) {
                showHypertensionHandDialog("左手");
            } else if ("1".equals(userHypertensionHand)) {
                showHypertensionHandDialog("右手");
            }
        }
    }


    private void showHypertensionHandDialog(String hand) {
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请使用" + hand + "测量");
        new AlertDialog(mContext)
                .builder()
                .setMsg("请使用" + hand + "测量")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause=true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_GETHYPERTENSIONHAND) {
            if (resultCode == RESULT_OK) {
                mActivity.finish();
            } else {
                getHypertensionHand();
                dealLogic();
            }
        }
    }
}
