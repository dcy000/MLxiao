package com.gcml.health.measure.first_diagnosis;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.FirstDiagnosisBean;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodOxygenDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:52
 * created by:gzq
 * description:TODO
 */
public class FirstDiagnosisActivity extends ToolbarBaseActivity implements FragmentChanged, DealVoiceAndJump {
    private List<FirstDiagnosisBean> firstDiagnosisBeans;
    private FrameLayout mFrame;
    private int measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
    private int showPosition = 0;
    private Uri uri;
    private static HealthBloodDetectionUiFragment.Data bloodpressureCacheData;
    private static List<DetectionData> cacheDatas = new ArrayList<>();
    private BluetoothBaseFragment fragment;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FirstDiagnosisActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void putBloodpressureCacheData(HealthBloodDetectionUiFragment.Data data) {
        bloodpressureCacheData = data;
    }

    public HealthBloodDetectionUiFragment.Data getBloodpressureCacheData() {
        return bloodpressureCacheData;
    }

    /**
     * 在activity中管理其容器中使用到的数据
     *
     * @param detectionData
     */
    public void putCacheData(DetectionData detectionData) {
        if (detectionData == null) {
            return;
        }
        cacheDatas.add(detectionData);
    }

    public List<DetectionData> getCacheDatas() {
        return cacheDatas;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);
        initView();
        initFirstDiagnosis();
        checkVideo(showPosition);
    }

    private void checkVideo(int showPosition) {
        Uri videoUri = firstDiagnosisBeans.get(showPosition).getVideoUri();
        String videoTitle = firstDiagnosisBeans.get(showPosition).getVideoTitle();
        if (videoUri != null) {
            jump2MeasureVideoPlayActivity(videoUri, videoTitle);
        } else {
            showFragment(showPosition);
        }
    }

    private void showFragment(int showPosition) {
        String fragmentTag = firstDiagnosisBeans.get(showPosition).getFragmentTag();
        switch (fragmentTag) {
            case "HealthFirstTipsFragment":
                mToolbar.setVisibility(View.GONE);
                fragment = new HealthFirstTipsFragment();
                break;
            case "HealthBloodDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 压 测 量");
                fragment = new HealthBloodDetectionUiFragment();
                measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
                break;
            case "HealthBloodOxygenDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 氧 测 量");
                fragment = new HealthBloodOxygenDetectionFragment();
                measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
                break;
            case "HealthTemperatureDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 温 测 量");
                fragment = new HealthTemperatureDetectionFragment();
                measureType = IPresenter.MEASURE_TEMPERATURE;
                break;
            case "HealthECGDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 测 量");
                fragment = new HealthECGDetectionFragment();
                measureType = IPresenter.MEASURE_ECG;
                break;
            case "HealthSelectSugarDetectionTimeFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("选择测量时间");
                fragment = new HealthSelectSugarDetectionTimeFragment();
                break;
            case "HealthSugarDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 糖 测 量");
                fragment = new HealthSugarDetectionUiFragment();
                measureType = IPresenter.MEASURE_BLOOD_SUGAR;
                break;
            case "HealthThreeInOneDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("三 合 一 测 量");
                fragment = new HealthThreeInOneDetectionUiFragment();
                measureType = IPresenter.MEASURE_OTHERS;
                break;
            case "HealthWeightDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 重 测 量");
                fragment = new HealthWeightDetectionUiFragment();
                measureType = IPresenter.MEASURE_WEIGHT;
                break;
            default:
                break;
        }
        fragment.setOnFragmentChangedListener(this);
        fragment.setOnDealVoiceAndJumpListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commitAllowingStateLoss();
    }

    /**
     * 将需要测量的设备放在一个有序集合中，按照先后顺序来配置即可
     */
    private void initFirstDiagnosis() {
        firstDiagnosisBeans = new ArrayList<>();
        FirstDiagnosisBean firstTip = new FirstDiagnosisBean(HealthFirstTipsFragment.class.getSimpleName(), null, null);
        firstDiagnosisBeans.add(firstTip);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
        FirstDiagnosisBean bloodpressure = new FirstDiagnosisBean(
                HealthBloodDetectionUiFragment.class.getSimpleName(), uri, "测量血压演示视频");
        firstDiagnosisBeans.add(bloodpressure);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
        FirstDiagnosisBean bloodoxygen = new FirstDiagnosisBean(
                HealthBloodOxygenDetectionFragment.class.getSimpleName(), uri, "测量血氧演示视频");
        firstDiagnosisBeans.add(bloodoxygen);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
        FirstDiagnosisBean temperature = new FirstDiagnosisBean(
                HealthTemperatureDetectionFragment.class.getSimpleName(), uri, "测量耳温演示视频");
        firstDiagnosisBeans.add(temperature);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
        FirstDiagnosisBean ecg = new FirstDiagnosisBean(
                HealthECGDetectionFragment.class.getSimpleName(), uri, "测量心电演示视频");
        firstDiagnosisBeans.add(ecg);

        FirstDiagnosisBean selectSugarTime = new FirstDiagnosisBean(
                HealthSelectSugarDetectionTimeFragment.class.getSimpleName(), null, null);
        firstDiagnosisBeans.add(selectSugarTime);
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
        FirstDiagnosisBean bloodsugar = new FirstDiagnosisBean(
                HealthSugarDetectionUiFragment.class.getSimpleName(), uri, "测量血糖演示视频");
        firstDiagnosisBeans.add(bloodsugar);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
        FirstDiagnosisBean threeinone = new FirstDiagnosisBean(
                HealthThreeInOneDetectionUiFragment.class.getSimpleName(), uri, "三合一测量演示视频");
        firstDiagnosisBeans.add(threeinone);

        FirstDiagnosisBean weight = new FirstDiagnosisBean(
                HealthWeightDetectionUiFragment.class.getSimpleName(), null, null);
        firstDiagnosisBeans.add(weight);

    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_MEASUREACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, uri)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        showFragment(showPosition);
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        showFragment(showPosition);
                        break;
                    default:
                }
            }
        });
    }


    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        Timber.e("变化的Fragment:" + fragment.getClass().getSimpleName());
        showPosition++;
        checkVideo(showPosition);
    }

    @Override
    public void updateVoice(String voice) {
        String connected = getResources().getString(R.string.bluetooth_device_connected);
        String disconnected = getResources().getString(R.string.bluetooth_device_disconnected);
        if (connected.equals(voice)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_connected);
        } else if (disconnected.equals(voice)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        }
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), voice, false);
    }

    @Override
    public void jump2HealthHistory(int measureType) {

    }

    @Override
    public void jump2DemoVideo(int measureType) {

    }

    @Override
    protected void backMainActivity() {
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        DialogSureCancel sureCancel = new DialogSureCancel(this);
        sureCancel.setContent("您确定解绑之前的设备，重新连接新设备吗？");
        sureCancel.show();
        sureCancel.setOnClickCancelListener(null);
        sureCancel.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                untieDevice();
            }
        });
    }

    private void untieDevice() {
        unpairDevice();
        String nameAddress = null;
        switch (measureType) {
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                ((HealthBloodDetectionUiFragment) fragment).onStop();
                ((HealthBloodDetectionUiFragment) fragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                ((HealthSugarDetectionUiFragment) fragment).onStop();
                ((HealthSugarDetectionUiFragment) fragment).dealLogic();
                break;
            case IPresenter.MEASURE_WEIGHT:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                ((HealthWeightDetectionUiFragment) fragment).onStop();
                ((HealthWeightDetectionUiFragment) fragment).dealLogic();
                break;
            case IPresenter.MEASURE_OTHERS:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE);
                ((HealthThreeInOneDetectionUiFragment) fragment).onStop();
                ((HealthThreeInOneDetectionUiFragment) fragment).dealLogic();
                break;
            default:
                break;
        }

        clearBluetoothCache(nameAddress);
    }

    /**
     * 解除已配对设备
     */
    private void unpairDevice() {
        List<BluetoothDevice> devices = BluetoothUtils.getBondedBluetoothClassicDevices();
        for (BluetoothDevice device : devices) {
            try {
                Method m = device.getClass()
                        .getMethod("removeBond", (Class[]) null);
                m.invoke(device, (Object[]) null);
            } catch (Exception e) {
                Timber.e(e.getMessage());
            }
        }

    }

    private void clearBluetoothCache(String nameAddress) {
        if (!TextUtils.isEmpty(nameAddress)) {
            String[] split = nameAddress.split(",");
            if (split.length == 2 && !TextUtils.isEmpty(split[1])) {
                BluetoothClientManager.getClient().refreshCache(split[1]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bloodpressureCacheData = null;
        MLVoiceSynthetize.stop();
    }
}
