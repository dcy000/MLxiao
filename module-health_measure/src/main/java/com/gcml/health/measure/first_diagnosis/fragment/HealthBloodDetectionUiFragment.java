package com.gcml.health.measure.first_diagnosis.fragment;

import android.support.annotation.IntDef;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.bean.ApiResponse;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSure;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import timber.log.Timber;

public class HealthBloodDetectionUiFragment extends Bloodpressure_Fragment {
    private DialogSure dialogSure;
    private String tips_first = "为了保证测量数据准确性，请根据小E提示对左右手血压各进行<font color='#F56C6C'>2–3次</font>测量。请先测量左手！";
    private String tips_first_speak = "为了保证测量数据准确性，请根据小E提示对左右手血压各进行2–3次测量。请先测量左手！";
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
        Timber.e(results.toString());
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

    @Override
    public void onResume() {
        super.onResume();
        notifyDetectionStepChanged(detectionStep);
    }

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
                showDialog(getString(R.string.health_measure_tips_left_2));
                break;
            case DetectionStep.LEFT_3:
                hasLeft3 = true;
                showDialog(getString(R.string.health_measure_tips_left_3));
                break;
            case DetectionStep.RIGHT_1:
                showDialog(getString(R.string.health_measure_tips_right_1));
                break;
            case DetectionStep.RIGHT_2:
                showDialog(getString(R.string.health_measure_tips_right_2));
                break;
            case DetectionStep.RIGHT_3:
                hasRight3 = true;
                showDialog(getString(R.string.health_measure_tips_right_3));
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
        MLVoiceSynthetize.startSynthesize(getContext(), message, false);
        if (dialogSure == null) {
            dialogSure = new DialogSure(getContext());
            dialogSure.getTitleView().setVisibility(View.GONE);
        }
        dialogSure.setContent(message);
        dialogSure.show();
        dialogSure.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                dialog.dismiss();
                MLVoiceSynthetize.stop();
            }
        });
    }

    private void showFirstDialog(String message, String speak) {
        //同时语音播报
        MLVoiceSynthetize.startSynthesize(getContext(), speak, false);
        if (dialogSure == null) {
            dialogSure = new DialogSure(getContext());
            dialogSure.getTitleView().setVisibility(View.GONE);
        }
        dialogSure.getContentView().setText(Html.fromHtml(message));
        dialogSure.show();
        dialogSure.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                dialog.dismiss();
                MLVoiceSynthetize.stop();
            }
        });
    }

    private void uploadHandData(final Data data) {
        OkGo.<String>post(HealthMeasureApi.DETECTION_BLOOD_HAND + UserSpHelper.getUserId() + "/")
                .params("handState", data.right)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.isSuccessful()) {
                            ToastUtils.showLong("数据上传失败");
                            return;
                        }
                        String body = response.body();
                        //保存惯用手
                        UserSpHelper.setUserHypertensionHand(data.right + "");
                        try {
                            ApiResponse<Object> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<Object>>() {
                            }.getType());
                            if (apiResponse.isSuccessful()) {
                                uploadData(data);
                                return;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        ToastUtils.showLong("数据上传失败");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showLong("数据上传失败");
                    }
                });


    }

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

        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
//                if (fragmentChanged != null && !isJump2Next) {
//                    isJump2Next = true;
//                    fragmentChanged.onFragmentChanged(HealthBloodDetectionUiFragment.this, null);
//                }
                ((FirstDiagnosisActivity) mActivity).putCacheData(pressureData);
                ((FirstDiagnosisActivity) mActivity).putCacheData(pulseData);
                isMeasureOver = true;
                setBtnClickableState(true);
            }

            @Override
            public void onError() {
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
        ((FirstDiagnosisActivity) mActivity).putBloodpressureCacheData(data);
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
//        if (fragmentChanged != null && !isJump2Next) {
//            isJump2Next = true;
//            fragmentChanged.onFragmentChanged(HealthBloodDetectionUiFragment.this, null);
//        }
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
            mBtnHealthHistory.setClickable(true);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
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
