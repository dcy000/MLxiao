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
import com.gcml.common.data.UserSpHelper;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.FirstDiagnosisBean;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodOxygenDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthChooseDevicesFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.health.measure.health_report_form.HealthReportFormActivity;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodpressureFragment;
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
 * description:风险评估各Fragment调度Activity
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
    private boolean isShowHealthChooseDevicesFragment = false;
    private String finalFragment;
    private String userId;
    private String userHypertensionHand;

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
            case "HealthChooseDevicesFragment":
                fragment = new HealthChooseDevicesFragment();
                mToolbar.setVisibility(View.VISIBLE);
                mRightView.setImageResource(R.drawable.common_icon_home);
                mTitleText.setText("仪 器 选 择");
                break;
            case "HealthBloodDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 压 测 量");

                userId = UserSpHelper.getUserId();
                userHypertensionHand = UserSpHelper.getUserHypertensionHand();
                if (TextUtils.isEmpty(userId)) {
                    //首先判断userId,如果为空，则说明走的是注册流程到达这里的
                    fragment = new HealthBloodDetectionUiFragment();
                } else {
                    //如果本地缓存的有惯用手数据则只需测量一次，如果没有则需要惯用手判断
                    if (TextUtils.isEmpty(userHypertensionHand)) {
                        fragment = new HealthBloodDetectionUiFragment();
                    } else {
                        fragment = new HealthBloodDetectionOnlyOneFragment();
                    }
                }
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
        //引导页面
        FirstDiagnosisBean firstTip = new FirstDiagnosisBean(HealthFirstTipsFragment.class.getSimpleName(), null, null);
        firstDiagnosisBeans.add(firstTip);

        FirstDiagnosisBean chooseDevice = new FirstDiagnosisBean(HealthChooseDevicesFragment.class.getSimpleName(), null, null);
        firstDiagnosisBeans.add(chooseDevice);

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
        //最后一个Fragment点击了下一步应该跳转到HealthReportFormActivity
        if (fragment.getClass().getSimpleName().equals(finalFragment)) {
            HealthReportFormActivity.startActivity(this);
            finish();
            return;
        }
        //每次跳转到下一个Fragment的时候都应该把右上角的蓝牙按钮初始化
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        //因为在设备选择页面右上角的按钮是回到主界面，所以需要在此处做一个标记
        if (fragment instanceof HealthFirstTipsFragment) {
            isShowHealthChooseDevicesFragment = true;
        } else {
            isShowHealthChooseDevicesFragment = false;
        }
        //设备选择界面点击下一步的时候，需要把选中的设备对应的Fragment进行初始化（动态加载fragment）
        if (bundle != null && fragment instanceof HealthChooseDevicesFragment) {
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList(HealthChooseDevicesFragment.KEY_DEVICE_NUM);
            initMeasureDevicesFragment(integerArrayList);
        }
        showPosition++;
        //因为每一个Fragment中都有可能视频播放，所以应该先检查该Fragment中是否有视频播放
        checkVideo(showPosition);
    }

    private void initMeasureDevicesFragment(ArrayList<Integer> integerArrayList) {
        for (Integer bean : integerArrayList) {
            switch (bean) {
                case 1:
                    //血压
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                    FirstDiagnosisBean bloodpressure = new FirstDiagnosisBean(
                            HealthBloodDetectionUiFragment.class.getSimpleName(), uri, "测量血压演示视频");
                    firstDiagnosisBeans.add(bloodpressure);
                    break;
                case 5:
                    //血糖

                    FirstDiagnosisBean selectSugarTime = new FirstDiagnosisBean(
                            HealthSelectSugarDetectionTimeFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(selectSugarTime);

                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                    FirstDiagnosisBean bloodsugar = new FirstDiagnosisBean(
                            HealthSugarDetectionUiFragment.class.getSimpleName(), uri, "测量血糖演示视频");
                    firstDiagnosisBeans.add(bloodsugar);
                    break;
                case 3:
                    //耳温枪
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                    FirstDiagnosisBean temperature = new FirstDiagnosisBean(
                            HealthTemperatureDetectionFragment.class.getSimpleName(), uri, "测量耳温演示视频");
                    firstDiagnosisBeans.add(temperature);
                    break;
                case 2:
                    //血氧
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                    FirstDiagnosisBean bloodoxygen = new FirstDiagnosisBean(
                            HealthBloodOxygenDetectionFragment.class.getSimpleName(), uri, "测量血氧演示视频");
                    firstDiagnosisBeans.add(bloodoxygen);
                    break;
                case 4:
                    //心电

                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                    FirstDiagnosisBean ecg = new FirstDiagnosisBean(
                            HealthECGDetectionFragment.class.getSimpleName(), uri, "测量心电演示视频");
                    firstDiagnosisBeans.add(ecg);
                    break;
                case 6:
                    //三合一
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    FirstDiagnosisBean threeinone = new FirstDiagnosisBean(
                            HealthThreeInOneDetectionUiFragment.class.getSimpleName(), uri, "三合一测量演示视频");
                    firstDiagnosisBeans.add(threeinone);
                    break;
                case 7:
                    //体重
                    FirstDiagnosisBean weight = new FirstDiagnosisBean(
                            HealthWeightDetectionUiFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(weight);
                    break;
                default:
                    break;
            }
        }
        //TODO:报告做成了Activity,所以在初始化结束后应该记录一下最后一个Fragment是哪一个，
        // TODO:这样点击最后一个Fragment的下一步才能跳转到HealthReportFormActivity
        finalFragment = firstDiagnosisBeans.get(firstDiagnosisBeans.size() - 1).getFragmentTag();
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
    protected void backLastActivity() {
        showPosition--;
        if (showPosition > 0) {
            if (showPosition == 1) {
                isShowHealthChooseDevicesFragment = true;
            }
            showFragment(showPosition);
        } else {
            finish();
        }
    }

    @Override
    protected void backMainActivity() {
        if (isShowHealthChooseDevicesFragment) {
            CCAppActions.jump2MainActivity();
            return;
        }
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
                sureCancel.dismiss();
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
                if (TextUtils.isEmpty(userId)) {
                    ((HealthBloodDetectionUiFragment) fragment).onStop();
                    ((HealthBloodDetectionUiFragment) fragment).dealLogic();
                } else {
                    //如果本地缓存的有惯用手数据则只需测量一次，如果没有则需要惯用手判断
                    if (TextUtils.isEmpty(userHypertensionHand)) {
                        ((HealthBloodDetectionUiFragment) fragment).onStop();
                        ((HealthBloodDetectionUiFragment) fragment).dealLogic();
                    } else {
                        ((HealthBloodDetectionOnlyOneFragment) fragment).onStop();
                        ((HealthBloodDetectionOnlyOneFragment) fragment).dealLogic();
                    }
                }
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
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bloodpressureCacheData = null;

    }
}
