package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.bloodpressure_habit.GetHypertensionHandActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;

import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/7 10:36
 * created by:gzq
 * description:单独给流程化测试中使用的Fragment
 */
public class HealthBloodDetectionOnlyOneXienFragment extends Bloodpressure_Xien_Fragment {
    private boolean isJump2Next = false;
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        getHypertensionHand();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        setBtnClickableState(false);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "您好，您本次测量收缩压"
                    + results[0] + ",舒张压" + results[1] + ",脉搏" + results[2], false);

            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("0");
            int highPressure = Integer.parseInt(results[0]);
            pressureData.setHighPressure(highPressure);
            int lowPressure = Integer.parseInt(results[1]);
            pressureData.setLowPressure(lowPressure);
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[2]));
            datas.add(pressureData);
            datas.add(dataPulse);

            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
                            ToastUtils.showLong("上传数据成功");
                            setBtnClickableState(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort("上传数据失败:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mBtnHealthHistory.setClickable(true);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mBtnHealthHistory.setClickable(false);
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
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "您好，请使用" + hand + "测量");
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
