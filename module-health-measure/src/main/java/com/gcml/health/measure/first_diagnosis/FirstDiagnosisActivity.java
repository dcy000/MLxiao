package com.gcml.health.measure.first_diagnosis;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
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
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.health.measure.health_report_form.HealthReportFormActivity;
import com.gcml.health.measure.single_measure.fragment.ChooseECGDeviceFragment;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGFragment;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:52
 * created by:gzq
 * description:风险评估各Fragment调度Activity
 */
@Route(path = "/health/measure/first/diagnosis")
public class FirstDiagnosisActivity extends ToolbarBaseActivity implements FragmentChanged, DealVoiceAndJump {
    private List<FirstDiagnosisBean> firstDiagnosisBeans;
    private FrameLayout mFrame;
    private int measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
    private int showPosition = 0;
    private Uri uri;
    private BluetoothBaseFragment fragment;
    private boolean isShowHealthChooseDevicesFragment = false;
    private String finalFragment;
    private String userId;
    private String userHypertensionHand;
    private Bundle bundle;
    private boolean isShowSelectBloodsugarMeasureTime = false;
    private boolean isShowSelectECGDevice=false;
    private int ecgDevice=1;//默认科瑞康

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FirstDiagnosisActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
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
                mRightView.setImageResource(R.drawable.white_wifi_3);
                mTitleText.setText("仪 器 选 择");
                break;
            case "HealthBloodDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 压 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                userId = UserSpHelper.getUserId();
                userHypertensionHand = UserSpHelper.getUserHypertensionHand();
                if (TextUtils.isEmpty(userId)) {
                    //首先判断userId,如果为空，则说明走的是注册流程到达这里的
                    fragment = new HealthBloodDetectionUiFragment();
                } else {
                    //如果本地缓存的有惯用手数据则只需测量一次，如果没有则需要惯用手判断
                    if (TextUtils.isEmpty(userHypertensionHand)) {
//                        fragment = new HealthBloodDetectionUiFragment();
                        fragment = new HealthBloodDetectionOnlyOneFragment();
                    } else {
                        fragment = new HealthBloodDetectionOnlyOneFragment();
                    }
                }
                measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
                break;
            case "HealthBloodOxygenDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 氧 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                fragment = new HealthBloodOxygenDetectionFragment();
                measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
                break;
            case "HealthTemperatureDetectionFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 温 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                fragment = new HealthTemperatureDetectionFragment();
                measureType = IPresenter.MEASURE_TEMPERATURE;
                break;
            case "ChooseECGDeviceFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 设 备 选 择");
                fragment = new ChooseECGDeviceFragment();
                mRightView.setImageResource(R.drawable.white_wifi_3);
                isShowSelectECGDevice=true;
                break;
            case "ECGFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                if (ecgDevice==1){
                    fragment = new HealthECGDetectionFragment();
                }else{
                    fragment=new HealthECGBoShengFragment();
                }
                measureType = IPresenter.MEASURE_ECG;
                break;
            case "HealthSelectSugarDetectionTimeFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("选 择 测 量 时 间");
                mRightView.setImageResource(R.drawable.white_wifi_3);
                fragment = new HealthSelectSugarDetectionTimeFragment();
                isShowSelectBloodsugarMeasureTime = true;
                break;
            case "HealthSugarDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("血 糖 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                fragment = new HealthSugarDetectionUiFragment();
                measureType = IPresenter.MEASURE_BLOOD_SUGAR;
                fragment.setArguments(bundle);
                break;
            case "HealthThreeInOneDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("三 合 一 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                fragment = new HealthThreeInOneDetectionUiFragment();
                measureType = IPresenter.MEASURE_THREE;
                fragment.setArguments(bundle);
                break;
            case "HealthWeightDetectionUiFragment":
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("体 重 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                fragment = new HealthWeightDetectionUiFragment();
                measureType = IPresenter.MEASURE_WEIGHT;
                break;
            default:
                break;
        }
        fragment.setOnFragmentChangedListener(this);
        fragment.setOnDealVoiceAndJumpListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commitAllowingStateLoss();
}

    /**
     * 将需要测量的设备放在一个有序集合中，按照先后顺序来配置即可
     */
    private void initFirstDiagnosis() {
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
        Routerfit.register(AppRouter.class).skipMeasureVideoPlayActivity(uri, null, title, new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (result == Activity.RESULT_OK) {
                    if (data == null) return;
                    if (data.toString().equals("pressed_button_skip")) {
                        showFragment(showPosition);
                    } else if (data.toString().equals("video_play_end")) {
                        showFragment(showPosition);
                    }
                } else if (result == Activity.RESULT_CANCELED) {
                }
            }
        });
    }


    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        firstDiagnosisBeans = new ArrayList<>();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        this.bundle = bundle;
        if (fragment instanceof ChooseECGDeviceFragment){
            isShowSelectECGDevice=false;
            if (bundle!=null){
                ecgDevice = bundle.getInt(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG, 1);
            }
        }
        if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            isShowSelectBloodsugarMeasureTime = false;
        }
        //最后一个Fragment点击了下一步应该跳转到HealthReportFormActivity
        if (fragment.getClass().getSimpleName().equals(finalFragment)) {
            HealthReportFormActivity.startActivity(this);
            finish();
            return;
        }
        //因为在设备选择页面右上角的按钮是回到主界面，所以需要在此处做一个标记
        if (fragment instanceof HealthFirstTipsFragment) {
            isShowHealthChooseDevicesFragment = true;
            mRightView.setImageResource(R.drawable.common_icon_home);
        } else {
            isShowHealthChooseDevicesFragment = false;
            //每次跳转到下一个Fragment的时候都应该把右上角的蓝牙按钮初始化
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
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
                default:
                    break;
            }
        }
        //报告做成了Activity,所以在初始化结束后应该记录一下最后一个Fragment是哪一个，
        //这样点击最后一个Fragment的下一步才能跳转到HealthReportFormActivity
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
            if (showPosition == 1) {
                isShowHealthChooseDevicesFragment = true;
                //如果是重新回到仪器选择界面，则把前数据初始化
                firstDiagnosisBeans.clear();
                initFirstDiagnosis();
            }
            showFragment(showPosition);
        } else {
            finish();
        }
    }

    @Override
    protected void backMainActivity() {
        if (isShowHealthChooseDevicesFragment) {
//            CCAppActions.jump2MainActivity();
            super.backMainActivity();
            return;
        }
        if (isShowSelectBloodsugarMeasureTime||isShowSelectECGDevice) {
//            CCAppActions.jump2MainActivity();
            super.backMainActivity();
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
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
