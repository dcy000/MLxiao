package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 10:15
 * created by:gzq
 * description:不需要上传测量数据的单测
 */
public class NonUploadSingleMeasureBloodpressureFragment extends BloodpressureFragment {
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;
    private boolean isOnPause = false;

//    @Override
//    protected void initView(View view, Bundle bundle) {
//        super.initView(view, bundle);
//        getHypertensionHand();
//    }


    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(),
                "您本次测量高压" + detectionData.getHighPressure() + ",低压" + detectionData.getLowPressure() + ",脉搏" + detectionData.getPulse(), false);
    }
//    /**
//     * 获取惯用手
//     */
//    private void getHypertensionHand() {
//        String userHypertensionHand = UserSpHelper.getUserHypertensionHand();
//        if (TextUtils.isEmpty(userHypertensionHand)) {
//            //还没有录入惯用手，则跳转到惯用手录入activity
//            GetHypertensionHandActivity.startActivityForResult(this, CODE_REQUEST_GETHYPERTENSIONHAND);
//        } else {
//            if ("0".equals(userHypertensionHand)) {
//                showHypertensionHandDialog("左手");
//            } else if ("1".equals(userHypertensionHand)) {
//                showHypertensionHandDialog("右手");
//            }
//        }
//    }
//
//
//    private void showHypertensionHandDialog(String hand) {
//        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请使用" + hand + "测量");
//        new AlertDialog(mContext)
//                .builder()
//                .setMsg("请使用" + hand + "测量")
//                .setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                }).show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CODE_REQUEST_GETHYPERTENSIONHAND) {
//            if (resultCode == RESULT_OK) {
//                mActivity.finish();
//            } else {
//                getHypertensionHand();
//                autoConnect();
//            }
//        }
//    }
}
