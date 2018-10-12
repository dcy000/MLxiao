package com.gcml.health.measure.first_diagnosis;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthDetectionIntelligentReportFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSugarDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthThreeInOneDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.data.SPUtil;
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
    private int requestPlayVideoCode;

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
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
//        DialogSureCancel sureCancel = new DialogSureCancel(this);
//        sureCancel.setContent("您确定解绑之前的设备，重新连接新设备吗？");
//        sureCancel.setOnClickCancelListener(null);
//        sureCancel.setOnClickSureListener(new DialogClickSureListener() {
//            @Override
//            public void clickSure(BaseDialog dialog) {
//                untieDevice();
//            }
//        });
        new AlertDialog(this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        untieDevice();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void untieDevice() {
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        unpairDevice();
        String nameAddress = null;
        switch (measureType) {
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                ((HealthBloodDetectionUiFragment) baseFragment).onStop();
                ((HealthBloodDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                ((HealthSugarDetectionUiFragment) baseFragment).onStop();
                ((HealthSugarDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_WEIGHT:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                ((HealthWeightDetectionUiFragment) baseFragment).onStop();
                ((HealthWeightDetectionUiFragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_OTHERS:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE);
                ((HealthThreeInOneDetectionUiFragment) baseFragment).onStop();
                ((HealthThreeInOneDetectionUiFragment) baseFragment).dealLogic();
                break;
            default:
                break;
        }

        clearBluetoothCache(nameAddress);
    }

    private void clearBluetoothCache(String nameAddress) {
        if (!TextUtils.isEmpty(nameAddress)) {
            String[] split = nameAddress.split(",");
            if (split.length == 2 && !TextUtils.isEmpty(split[1])) {
                BluetoothClientManager.getClient().refreshCache(split[1]);
            }
        }
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
            requestPlayVideoCode = BLOODPRESSURE_VIDEO;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
            jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
        } else if (fragment instanceof HealthBloodDetectionUiFragment) {
            move2Weight();
        } else if (fragment instanceof HealthWeightDetectionUiFragment) {
            requestPlayVideoCode = BLOODSUGAR_VIDEO;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
            jump2MeasureVideoPlayActivity(uri, "血糖测量演示视频");
        } else if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            move2Bloodsugar(bundle);
        } else if (fragment instanceof HealthSugarDetectionUiFragment) {
            requestPlayVideoCode = ECG_VIDEO;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
            jump2MeasureVideoPlayActivity(uri, "心电测量演示视频");
        } else if (fragment instanceof HealthThreeInOneDetectionUiFragment) {
            move2FirstDiagnosisReport();
        }


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
                        afterVideo();
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        afterVideo();
                        break;
                    default:
                }
            }
        });
    }

    private void afterVideo() {
        if (requestPlayVideoCode == BLOODPRESSURE_VIDEO) {
            move2Bloodpressure();
        } else if (requestPlayVideoCode == BLOODSUGAR_VIDEO) {
            move2BloodsugarTimeSelection();
        } else if (requestPlayVideoCode == THREE_IN_ONE_VIDEO) {
            move2ThreeInOne();
        } else if (requestPlayVideoCode == ECG_VIDEO) {
            move2ECG();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == JUMP_TO_ECG) {
                if (data != null) {
                    DetectionData ecgData = new DetectionData();
                    ecgData.setDetectionType("2");
                    ecgData.setEcg(String.valueOf(data.getIntExtra("ecg", 0)));
                    ecgData.setHeartRate(data.getIntExtra("heartRate", 0));
                    putCacheData(ecgData);
                }
                requestPlayVideoCode = THREE_IN_ONE_VIDEO;
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                jump2MeasureVideoPlayActivity(uri, "三合一测量演示视频");
            }
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
