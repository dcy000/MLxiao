package com.gcml.health.measure.first_diagnosis;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthDetectionIntelligentReportFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.health.measure.video.MeasureVideoPlayActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class HealthIntelligentDetectionActivity extends ToolbarBaseActivity implements FragmentChanged, DealVoiceAndJump {
    private BluetoothBaseFragment baseFragment;
    private Uri uri;
    //血压视频请求吗
    private static final int BLOODPRESSURE_VIDEO = 1001;
    //血糖视频请求吗
    private static final int BLOODSUGAR_VIDEO = 1002;
    private static final int JUMP_TO_ECG = 1003;
    //三合一演示视频
    private static final int THREE_IN_ONE_VIDEO = 1004;
    //心电演示视频
    private static final int ECG_VIDEO = 1005;
    private boolean isFirst = true;
    private int measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
    private static List<DetectionData> cacheDatas = new ArrayList<>();
    private static HealthBloodDetectionUiFragment.Data bloodpressureCacheData;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HealthIntelligentDetectionActivity.class);
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

    public void removeCacheData(DetectionData detectionData) {
        if (detectionData == null) {
            return;
        }
        cacheDatas.remove(detectionData);
    }

    public List<DetectionData> getCacheDatas() {
        return cacheDatas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_intelligent_detection);
        mToolbar.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        baseFragment = (BluetoothBaseFragment) fm.findFragmentByTag(HealthFirstTipsFragment.class.getName());
        if (baseFragment != null && baseFragment.isHidden()) {
            transaction.show(baseFragment);
        } else {
            baseFragment = new HealthFirstTipsFragment();
            baseFragment.setOnFragmentChangedListener(this);
            transaction.add(R.id.fl_container, baseFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void backMainActivity() {
        switch (measureType) {
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                baseFragment.onStop();
                ((HealthBloodDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                baseFragment.onStop();
                ((HealthSugarDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_WEIGHT:
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                baseFragment.onStop();
                ((HealthWeightDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_OTHERS:
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE);
                baseFragment.onStop();
                ((HealthThreeInOneDetectionUiFragment) baseFragment).dealLogic();
                break;
        }
    }

    @Override
    protected void backLastActivity() {
        // 获取当前回退栈中的Fragment个数
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        // 判断当前回退栈中的fragment个数,
        if (backStackEntryCount > 1) {
            // 立即回退一步
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            //回退栈中只剩一个时,退出应用
            finish();
        }
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof HealthFirstTipsFragment) {
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
            MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "血压测量演示视频", BLOODPRESSURE_VIDEO);
        } else if (fragment instanceof HealthBloodDetectionUiFragment) {
            move2Weight();
        } else if (fragment instanceof HealthWeightDetectionUiFragment) {
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
            MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "血糖测量演示视频", BLOODSUGAR_VIDEO);
        } else if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            move2Bloodsugar(bundle);
        } else if (fragment instanceof HealthSugarDetectionUiFragment) {
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
            MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "心电测量演示视频",
                    ECG_VIDEO);
        } else if (fragment instanceof HealthThreeInOneDetectionUiFragment) {
            move2FirstDiagnosisReport();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirst) {
            isFirst = false;
            return;
        }
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        mTitleText.setText("智能检测");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BLOODPRESSURE_VIDEO) {
                move2Bloodpressure();
            } else if (requestCode == BLOODSUGAR_VIDEO) {
                move2BloodsugarTimeSelection();
            } else if (requestCode == JUMP_TO_ECG) {
                if (data != null) {
                    DetectionData ecgData = new DetectionData();
                    ecgData.setDetectionType("2");
                    ecgData.setEcg(String.valueOf(data.getIntExtra("ecg", 0)));
                    ecgData.setHeartRate(data.getIntExtra("heartRate", 0));
                    putCacheData(ecgData);
                }
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null,
                        "三合一测量演示视频", THREE_IN_ONE_VIDEO);

            } else if (requestCode == THREE_IN_ONE_VIDEO) {
                move2ThreeInOne();
            } else if (requestCode == ECG_VIDEO) {
                move2ECG();
            }
        }
    }

    private void move2FirstDiagnosisReport() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return;
        }
        baseFragment = new HealthDetectionIntelligentReportFragment();
        baseFragment.setOnFragmentChangedListener(this);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, baseFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void move2ThreeInOne() {
        measureType = IPresenter.MEASURE_OTHERS;
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return;
        }
        baseFragment = new HealthThreeInOneDetectionUiFragment();
        baseFragment.setOnFragmentChangedListener(this);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, baseFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void move2ECG() {
        measureType = IPresenter.MEASURE_ECG;
        XinDianDetectActivity.startActivityForResult(this,
                HealthIntelligentDetectionActivity.class.getSimpleName(),
                JUMP_TO_ECG);
    }

    private void move2BloodsugarTimeSelection() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        baseFragment = (BluetoothBaseFragment) fm.findFragmentByTag(HealthSelectSugarDetectionTimeFragment.class.getName());
        if (baseFragment != null) {
            transaction.show(baseFragment);
        } else {
            baseFragment = new HealthSelectSugarDetectionTimeFragment();
            baseFragment.setOnFragmentChangedListener(this);
            transaction.add(R.id.fl_container, baseFragment);
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void move2Bloodsugar(Bundle bundle) {
        measureType = IPresenter.MEASURE_BLOOD_SUGAR;
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        baseFragment = (BluetoothBaseFragment) fm.findFragmentByTag(HealthSugarDetectionUiFragment.class.getName());
        if (baseFragment != null) {
            baseFragment.setArguments(bundle);
            transaction.show(baseFragment);
        } else {
            baseFragment = new HealthSugarDetectionUiFragment();
            baseFragment.setArguments(bundle);
            baseFragment.setOnFragmentChangedListener(this);
            transaction.add(R.id.fl_container, baseFragment);
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void move2Weight() {
        measureType = IPresenter.MEASURE_WEIGHT;
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        baseFragment = new HealthWeightDetectionUiFragment();
        baseFragment.setOnFragmentChangedListener(this);
        transaction.replace(R.id.fl_container, baseFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void move2Bloodpressure() {
        measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            baseFragment = new HealthBloodDetectionUiFragment();
            baseFragment.setOnFragmentChangedListener(this);
            baseFragment.setOnDealVoiceAndJumpListener(this);
            transaction.replace(R.id.fl_container, baseFragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bloodpressureCacheData = null;
        cacheDatas = null;
        MLVoiceSynthetize.stop();
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
}
