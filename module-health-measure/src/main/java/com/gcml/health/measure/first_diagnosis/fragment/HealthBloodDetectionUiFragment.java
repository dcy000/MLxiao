package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.TimeCountDownUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HealthBloodDetectionUiFragment extends BloodpressureFragment {
    //    private DialogSure dialogSure;
    private String tips_first = "为了保证测量数据准确性，请根据小E提示对左右手血压各进行<font color='#F56C6C'>2–3次</font>测量。请先测量左手！";
    private String tips_first_speak = "为了保证测量数据准确性，请根据小易提示对左右手血压各进行2–3次测量。请先测量左手！";
    private boolean isJump2Next = false;
    private boolean isMeasureOver = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        setBtnClickableState(false);
        notifyDetectionStepChanged(detectionStep);
    }

    private SparseIntArray highPressures = new SparseIntArray();
    private SparseIntArray lowPressures = new SparseIntArray();
    private SparseIntArray pulses = new SparseIntArray();

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3) {
            highPressures.put(detectionStep, Integer.parseInt(results[0]));
            lowPressures.put(detectionStep, Integer.parseInt(results[1]));
            pulses.put(detectionStep, Integer.parseInt(results[2]));
            TimeCountDownUtils.getInstance().create(3000, 1000, timeCountListener);
            TimeCountDownUtils.getInstance().start();
        }
    }

    private final TimeCountDownUtils.TimeCountListener timeCountListener = new TimeCountDownUtils.TimeCountListener() {
        @Override
        public void onTick(long millisUntilFinished, String tag) {
        }

        @Override
        public void onFinish(String tag) {
            dispatchNextDetectionStep();
        }
    };

    @IntDef({
            DetectionStep.LEFT_1,
            DetectionStep.LEFT_2,
            DetectionStep.LEFT_3,
            DetectionStep.RIGHT_1,
            DetectionStep.RIGHT_2,
            DetectionStep.RIGHT_3,
            DetectionStep.DONE
    })
    private @interface DetectionStep {
        int LEFT_1 = 1;
        int LEFT_2 = 2;
        int LEFT_3 = 3;
        int RIGHT_1 = 4;
        int RIGHT_2 = 5;
        int RIGHT_3 = 6;
        int DONE = 7;
    }

    @DetectionStep
    private int detectionStep = DetectionStep.LEFT_1;

    private boolean hasLeft3;

    private boolean hasRight3;


    private void dispatchNextDetectionStep() {
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                notifyDetectionStepChanged(DetectionStep.LEFT_2);
                break;
            case DetectionStep.LEFT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.LEFT_1)
                        - highPressures.get(DetectionStep.LEFT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.LEFT_1)
                        - lowPressures.get(DetectionStep.LEFT_2))) {
                    notifyDetectionStepChanged(DetectionStep.LEFT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                }
                break;
            case DetectionStep.LEFT_3:
                notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                break;
            case DetectionStep.RIGHT_1:
                notifyDetectionStepChanged(DetectionStep.RIGHT_2);
                break;
            case DetectionStep.RIGHT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.RIGHT_1)
                        - highPressures.get(DetectionStep.RIGHT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.RIGHT_1)
                        - lowPressures.get(DetectionStep.RIGHT_2))) {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.DONE);
                }
                break;
            case DetectionStep.RIGHT_3:
                notifyDetectionStepChanged(DetectionStep.DONE);
                break;
            case DetectionStep.DONE:
                notifyDetectionStepChanged(DetectionStep.DONE);
                break;
            default:
                break;
        }
    }

    private void notifyDetectionStepChanged(@DetectionStep int detectionStep) {
        this.detectionStep = detectionStep;
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                showFirstDialog(tips_first, tips_first_speak);
                break;
            case DetectionStep.LEFT_2:
                showDialog(UtilsManager.getApplication().getString(R.string.health_measure_tips_left_2));
                break;
            case DetectionStep.LEFT_3:
                hasLeft3 = true;
                showDialog(UtilsManager.getApplication().getString(R.string.health_measure_tips_left_3));
                break;
            case DetectionStep.RIGHT_1:
                showDialog(UtilsManager.getApplication().getString(R.string.health_measure_tips_right_1));
                break;
            case DetectionStep.RIGHT_2:
                showDialog(UtilsManager.getApplication().getString(R.string.health_measure_tips_right_2));
                break;
            case DetectionStep.RIGHT_3:
                hasRight3 = true;
                showDialog(UtilsManager.getApplication().getString(R.string.health_measure_tips_right_3));
                break;
            case DetectionStep.DONE:
                uploadHandData(prepareData());
                break;
            default:
                break;
        }
    }

    private void showDialog(String message) {
        //同时语音播报
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), message, false);
        new AlertDialog(mContext)
                .builder()
                .setMsg(message)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MLVoiceSynthetize.stop();
                    }
                }).show();
    }

    private void showFirstDialog(String message, String speak) {
        //同时语音播报
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), speak, false);
        new AlertDialog(mContext)
                .builder()
                .setMsg(Html.fromHtml(message))
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MLVoiceSynthetize.stop();
                    }
                }).show();
    }

    @SuppressLint("CheckResult")
    private void uploadHandData(final Data data) {
        Timber.i("开始上传惯用手到服务器");

        HealthMeasureRepository.postHypertensionHand(data.right)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Timber.i("上传惯用手成功");
                        UserSpHelper.setUserHypertensionHand(data.right + "");
                        uploadHandStateFinished();
                        uploadData(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("惯用手上传失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 表示上传惯用手状态结束  供子类使用
     */
    protected void uploadHandStateFinished() {

    }

    @SuppressLint("CheckResult")
    private void uploadData(Data data) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        final DetectionData pressureData = new DetectionData();
        final DetectionData pulseData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType(data.type);
        int highPressure = data.right == 1 ? data.rightHighPressure : data.leftHighPressure;
        pressureData.setHighPressure(highPressure);
        int lowPressure = data.right == 1 ? data.rightLowPressure : data.leftLowPressure;
        pressureData.setLowPressure(lowPressure);
        pulseData.setDetectionType("9");
        int pulse = data.right == 1 ? data.rightPulse : data.leftPulse;
        pulseData.setPulse(pulse);
        datas.add(pressureData);
        datas.add(pulseData);

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> o) {
                        isMeasureOver = true;
                        setBtnClickableState(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("上传数据失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * @return prepareData = int[1], lowPressure = int[2], pulse = int[3], left = int[4] == 1
     */
    private Data prepareData() {
        int leftHighPressure;
        int rightHighPressure;
        if (hasLeft3 && highPressures.get(DetectionStep.LEFT_3) > 0) {
            leftHighPressure = (highPressures.get(DetectionStep.LEFT_1)
                    + highPressures.get(DetectionStep.LEFT_2)
                    + highPressures.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftHighPressure = (highPressures.get(DetectionStep.LEFT_1)
                    + highPressures.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && highPressures.get(DetectionStep.RIGHT_3) > 0) {
            rightHighPressure = (highPressures.get(DetectionStep.RIGHT_1)
                    + highPressures.get(DetectionStep.RIGHT_2)
                    + highPressures.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightHighPressure = (highPressures.get(DetectionStep.RIGHT_1)
                    + highPressures.get(DetectionStep.RIGHT_2)) / 2;
        }

        int leftLowPressure;
        int rightLowPressure;
        if (hasLeft3 && lowPressures.get(DetectionStep.LEFT_3) > 0) {
            leftLowPressure = (lowPressures.get(DetectionStep.LEFT_1)
                    + lowPressures.get(DetectionStep.LEFT_2)
                    + lowPressures.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftLowPressure = (lowPressures.get(DetectionStep.LEFT_1)
                    + lowPressures.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && lowPressures.get(DetectionStep.RIGHT_3) > 0) {
            rightLowPressure = (lowPressures.get(DetectionStep.RIGHT_1)
                    + lowPressures.get(DetectionStep.RIGHT_2)
                    + lowPressures.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightLowPressure = (lowPressures.get(DetectionStep.RIGHT_1)
                    + lowPressures.get(DetectionStep.RIGHT_2)) / 2;
        }

        int leftPulse;
        int rightPulse;
        if (hasLeft3 && pulses.get(DetectionStep.LEFT_3) > 0) {
            leftPulse = (pulses.get(DetectionStep.LEFT_1)
                    + pulses.get(DetectionStep.LEFT_2)
                    + pulses.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftPulse = (pulses.get(DetectionStep.LEFT_1)
                    + pulses.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && pulses.get(DetectionStep.RIGHT_3) > 0) {
            rightPulse = (pulses.get(DetectionStep.RIGHT_1)
                    + pulses.get(DetectionStep.RIGHT_2)
                    + pulses.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightPulse = (pulses.get(DetectionStep.RIGHT_1)
                    + pulses.get(DetectionStep.RIGHT_2)) / 2;
        }

        Data data = new Data();
        data.right = rightHighPressure > leftHighPressure ? 1 : 0;
        data.leftHighPressure = leftHighPressure;
        data.rightHighPressure = rightHighPressure;
        data.leftLowPressure = leftLowPressure;
        data.rightLowPressure = rightLowPressure;
        data.leftPulse = leftPulse;
        data.rightPulse = rightPulse;
        //将该数据在Activity中缓存
//        ((FirstDiagnosisActivity) mActivity).putBloodpressureCacheData(data);
        return data;
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (isMeasureOver) {
            if (fragmentChanged != null && !isJump2Next) {
                fragmentChanged.onFragmentChanged(HealthBloodDetectionUiFragment.this, null);
            }
        } else {
            ToastUtils.showShort("测量次数不够");
        }
    }

    public static class Data {
        public String type = "0";
        public int right; // { 0:left, 1: right }
        public int leftHighPressure;
        public int rightHighPressure;
        public int leftLowPressure;
        public int rightLowPressure;
        public int leftPulse;
        public int rightPulse;
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mBtnHealthHistory.setVisibility(View.VISIBLE);
            mBtnHealthHistory.setClickable(true);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mBtnHealthHistory.setVisibility(View.GONE);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mBtnHealthHistory.setClickable(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        TimeCountDownUtils.getInstance().cancelAll();
    }
}
