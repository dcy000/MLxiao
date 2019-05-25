package com.gcml.health.measure.health_profile;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.FirstDiagnosisBean;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodOxygenDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthChooseDevicesFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGBoShengFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthHeightDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.health.measure.single_measure.fragment.ChooseECGDeviceFragment;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.base.ThisFragmentDatas;
import com.gcml.module_blutooth_devices.ecg.ECGFragment;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

public class InquiryHealthActivity extends ToolbarBaseActivity implements FragmentChanged, DealVoiceAndJump, ThisFragmentDatas {
    private Uri uri;
    private List<FirstDiagnosisBean> firstDiagnosisBeans;
    private int showPosition = 0;
    private BluetoothBaseFragment fragment;
    private int measureType = IBleConstants.MEASURE_BLOOD_PRESSURE;
    private boolean isShowSelectECGDevice = false;
    private int ecgDevice = 2;//默认博声
    private boolean isShowSelectBloodsugarMeasureTime = false;
    private Bundle bundle;
    private String finalFragment;
    private List<DetectionDataBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_checkup);
        initView();
        initMeasureDevicesFragment();
        checkVideo(showPosition);
    }

    private void initView() {
        firstDiagnosisBeans = new ArrayList<>();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
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
                fragment = new HealthBloodDetectionOnlyOneFragment();
                measureType = IBleConstants.MEASURE_BLOOD_PRESSURE;
                break;
            case "HealthBloodOxygenDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 氧 测 量");
                fragment = new HealthBloodOxygenDetectionFragment();
                measureType = IBleConstants.MEASURE_BLOOD_OXYGEN;
                break;
            case "HealthTemperatureDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 温 测 量");
                fragment = new HealthTemperatureDetectionFragment();
                measureType = IBleConstants.MEASURE_TEMPERATURE;
                break;
            case "ChooseECGDeviceFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 设 备 选 择");
                fragment = new ChooseECGDeviceFragment();
                mRightView.setImageResource(R.drawable.common_icon_home);
                isShowSelectECGDevice = true;
                break;
            case "ECGFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 测 量");
                if (ecgDevice == 1) {
                    fragment = new HealthECGDetectionFragment();
                } else {
                    fragment = new HealthECGBoShengFragment();
                }
                measureType = IBleConstants.MEASURE_ECG;
                break;
            case "HealthSelectSugarDetectionTimeFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("选 择 测 量 时 间");
                mRightView.setImageResource(R.drawable.common_icon_home);
                fragment = new HealthSelectSugarDetectionTimeFragment();
                isShowSelectBloodsugarMeasureTime = true;
                break;
            case "HealthSugarDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 糖 测 量");
                fragment = new HealthSugarDetectionUiFragment();
                measureType = IBleConstants.MEASURE_BLOOD_SUGAR;
                fragment.setArguments(bundle);
                break;
            case "HealthThreeInOneDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("三 合 一 测 量");
                fragment = new HealthThreeInOneDetectionUiFragment();
                measureType = IBleConstants.MEASURE_THREE;
                fragment.setArguments(bundle);
                break;
            case "HealthWeightDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 重 测 量");
                fragment = new HealthWeightDetectionUiFragment();
                measureType = IBleConstants.MEASURE_WEIGHT;
                break;
            case "HealthHeightDetectionUiFragment":
                mRightView.setImageResource(R.drawable.common_icon_home);
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("身 高 测 量");
                fragment = new HealthHeightDetectionUiFragment();
                measureType = IBleConstants.MEASURE_HEIGHT;
                break;
            default:
                break;
        }
        fragment.setOnFragmentChangedListener(this);
        fragment.setOnDealVoiceAndJumpListener(this);
        fragment.setOnThisFragmentDataChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        Routerfit.register(AppRouter.class).skipMeasureVideoPlayActivity(uri, null, title, new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (result == Activity.RESULT_OK) {
                    if (data == null) return;
                    if (data.toString().equals("pressed_button_skip")) {
                        //点击了跳过按钮
                        showFragment(showPosition);
                    } else if (data.toString().equals("video_play_end")) {
                        //视屏播放结束
                        showFragment(showPosition);
                    }
                } else if (result == Activity.RESULT_CANCELED) {
                }
            }
        });
    }

    private void initMeasureDevicesFragment() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        //体温、血压、血糖、体重、身高
        integerArrayList.add(1);
        integerArrayList.add(5);
        integerArrayList.add(7);
        integerArrayList.add(8);
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
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                    FirstDiagnosisBean selectSugarTime = new FirstDiagnosisBean(
                            HealthSelectSugarDetectionTimeFragment.class.getSimpleName(), uri, "测量血糖演示视频");
                    firstDiagnosisBeans.add(selectSugarTime);

                    FirstDiagnosisBean bloodsugar = new FirstDiagnosisBean(
                            HealthSugarDetectionUiFragment.class.getSimpleName(), null, null);
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

                    FirstDiagnosisBean ecgSelectDevice = new FirstDiagnosisBean(
                            ChooseECGDeviceFragment.class.getSimpleName(), uri, "测量心电演示视频");
                    firstDiagnosisBeans.add(ecgSelectDevice);
                    FirstDiagnosisBean ecg = new FirstDiagnosisBean(
                            ECGFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(ecg);
                    break;
                case 6:
                    //三合一
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    FirstDiagnosisBean selectSugarTime1 = new FirstDiagnosisBean(
                            HealthSelectSugarDetectionTimeFragment.class.getSimpleName(), uri, "三合一测量演示视频");
                    firstDiagnosisBeans.add(selectSugarTime1);


                    FirstDiagnosisBean threeinone = new FirstDiagnosisBean(
                            HealthThreeInOneDetectionUiFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(threeinone);
                    break;
                case 7:
                    //体重
                    FirstDiagnosisBean weight = new FirstDiagnosisBean(
                            HealthWeightDetectionUiFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(weight);
                    break;
                case 8:
                    //身高
                    FirstDiagnosisBean height = new FirstDiagnosisBean(
                            HealthHeightDetectionUiFragment.class.getSimpleName(), null, null);
                    firstDiagnosisBeans.add(height);
                    break;
                default:
                    break;
            }
        }
        //报告做成了Activity,所以在初始化结束后应该记录一下最后一个Fragment是哪一个，
        //这样点击最后一个Fragment的下一步才能跳转到HealthReportFormActivity
        finalFragment = firstDiagnosisBeans.get(firstDiagnosisBeans.size() - 1).getFragmentTag();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        this.bundle = bundle;
        if (fragment instanceof ChooseECGDeviceFragment) {
            isShowSelectECGDevice = false;
            if (bundle != null) {
                ecgDevice = bundle.getInt(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG, 2);
            }
        }
        if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            isShowSelectBloodsugarMeasureTime = false;
        }
        //最后一个Fragment点击了下一步应该跳转到HealthReportFormActivity
        if (fragment.getClass().getSimpleName().equals(finalFragment)) {
            //TODO：把所有数据上传，并跳转到打印页面
            Routerfit.register(AppRouter.class).skipOutputResultActivity(null, null, null);
            return;
        }
        //每次跳转到下一个Fragment的时候都应该把右上角的蓝牙按钮初始化
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        showPosition++;
        //因为每一个Fragment中都有可能视频播放，所以应该先检查该Fragment中是否有视频播放
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
        MLVoiceSynthetize.startSynthesize(UM.getApp(), voice, false);
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
            showFragment(showPosition);
        } else {
            finish();
        }
    }

    @Override
    protected void backMainActivity() {
        if (fragment instanceof HealthHeightDetectionUiFragment) {
            ToastUtils.showShort("请选择您的身高");
            return;
        }
        if (isShowSelectBloodsugarMeasureTime || isShowSelectECGDevice) {
            Routerfit.register(AppRouter.class).skipMainActivity();
            return;
        }
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.autoConnect();
                        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                    }

                }).show();
    }


    @Override
    public void data(DetectionDataBean detectionDataBean) {
        datas.add(detectionDataBean);
    }
}
