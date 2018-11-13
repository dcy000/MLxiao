package com.example.han.referralproject.yiyuan.newdetect;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.health.DetectHealthSymptomsActivity;
import com.example.han.referralproject.require2.dialog.AlertDialog;
import com.example.han.referralproject.single_measure.ChooseECGDeviceFragment;
import com.example.han.referralproject.single_measure.HealthSelectSugarDetectionTimeFragment;
import com.example.han.referralproject.single_measure.SelfECGDetectionFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.ECGFollowUpFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.HypertensionFollowUpFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.SelfECGFollowUpFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.SugarFollowUpFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.TemperatureFollowUpFragment;
import com.example.han.referralproject.yiyuan.newdetect.followupfragment.WeightFollowUpFragment;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.gcml.module_video.measure.MeasureVideoPlayActivity;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HealthFollowUpActivity extends BaseActivity implements FragmentChanged {

    private int position = 0;
    private List<SurveyBean> followInfo = new ArrayList<>();
    private BluetoothBaseFragment posiontFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_follow_up);
        mToolbar.setVisibility(View.VISIBLE);
        initFollowDeviceInfo();
        showFragmentOrVideo(position);
    }

    private void initFollowDeviceInfo() {
        SurveyBean tiwen = new SurveyBean("TemperatureFollowUpFragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu),
                "耳温测量演示视频"
        );
        followInfo.add(tiwen);

        SurveyBean xueya = new SurveyBean("HypertensionFollowUpFragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya),
                "血压测量演示视频"
        );
        followInfo.add(xueya);

        SurveyBean xueyaYou = new SurveyBean("HypertensionFollowUpFragmentYou",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya),
                "血压测量演示视频"
        );
        followInfo.add(xueyaYou);

        SurveyBean choseXindian = new SurveyBean("ChooseECGDeviceFragment",
                null, null
        );
        followInfo.add(choseXindian);

        SurveyBean xindian = new SurveyBean("ECG_Fragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian),
                "心电测量演示视频");
        followInfo.add(xindian);

        SurveyBean xuanZheTime = new SurveyBean("HealthSelectSugarDetectionTimeFragment", null, null);
        followInfo.add(xuanZheTime);

        SurveyBean xuetang = new SurveyBean("SugarFollowUpFragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang),
                "血糖测量演示视频"
        );
        followInfo.add(xuetang);

        SurveyBean xuanZheTime2 = new SurveyBean("HealthSelectSugarDetectionTimeFragment2", null, null);
        followInfo.add(xuanZheTime2);

        SurveyBean sanheyi = new SurveyBean("SugarFollowUpFragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi),
                "三合一测量演示视频"
        );
        followInfo.add(sanheyi);


        SurveyBean weight = new SurveyBean("WeightFollowUpFragment", null, null);
        followInfo.add(weight);
    }


    @Override
    protected void backLastActivity() {
        position--;
        if (position > 0) {
            showFragment(position);
        } else {
            finish();
        }
    }

    @Override
    protected void backMainActivity() {
        super.backMainActivity();
        showRefreshBluetoothDialog();
    }

    private void showFragmentOrVideo(int position) {
        Uri videoUri = followInfo.get(position).getVideoUri();
        String videoTitle = followInfo.get(position).getVideoTitle();
        if (videoUri != null) {
            MeasureVideoPlayActivity.startForResultActivity(this, videoUri, null, videoTitle, 1001);
        } else {
            showFragment(position);
        }
    }

    private void showFragment(int position) {
        String fragmentTag = followInfo.get(position).getFragmentTag();
        switch (fragmentTag) {
            case "TemperatureFollowUpFragment":
                mTitleText.setText("体 温 测 量");
                posiontFragment = new TemperatureFollowUpFragment();
                break;

            case "HypertensionFollowUpFragment":
                mTitleText.setText("左 臂 血 压 测 量");
                posiontFragment = new HypertensionFollowUpFragment();
                break;

            case "HypertensionFollowUpFragmentYou":
                mTitleText.setText("右 臂 血 压 测 量");
                posiontFragment = new HypertensionFollowUpFragment();
                break;
            case "SugarFollowUpFragment":
                mTitleText.setText("血 糖 测 量");
                posiontFragment = new SugarFollowUpFragment();
                break;
            case "HealthSelectSugarDetectionTimeFragment":
                mTitleText.setText("血 糖 测 量");
                posiontFragment = new HealthSelectSugarDetectionTimeFragment();
                break;

            case "HealthSelectSugarDetectionTimeFragment2":
                mTitleText.setText("三 合 一 测 量");
                posiontFragment = new HealthSelectSugarDetectionTimeFragment();
                break;

            case "WeightFollowUpFragment":
                mTitleText.setText("体 重 测 量");
                posiontFragment = new WeightFollowUpFragment();
                break;

            case "ChooseECGDeviceFragment":
                mTitleText.setText("心 电 测 量");
                posiontFragment = new ChooseECGDeviceFragment();
                break;


            case "ECG_Fragment":
                mTitleText.setText("心 电 测 量");
                posiontFragment = new ECGFollowUpFragment();
                break;

            case "SelfECGDetectionFragment":
                mTitleText.setText("心 电 测 量");
                posiontFragment = new SelfECGFollowUpFragment();
                break;
            default:
                break;
        }

        if (posiontFragment != null) {
            posiontFragment.setOnFragmentChangedListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.health_follow_up_container, posiontFragment).commitAllowingStateLoss();
        } else {
            throw new IllegalArgumentException();
        }


    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof ChooseECGDeviceFragment) {
            if (bundle != null) {
                int anInt = bundle.getInt(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 1);
                if (anInt == 1) {
                    for (SurveyBean surveyBean : followInfo) {
                        String fragmentTag = surveyBean.getFragmentTag();
                        if (fragmentTag.equals("ECG_Fragment")) {
                            surveyBean.setFragmentTag("SelfECGDetectionFragment");
                        }
                    }
                }
            }
        }
        position++;

        if (position > followInfo.size() - 1) {
            Intent intent = new Intent(this, DetectHealthSymptomsActivity.class);
            intent.putExtra("detectCategory", "detectHealth");
            startActivity(intent);
            return;
        }
        //因为每一个Fragment中都有可能视频播放，所以应该先检查该Fragment中是否有视频播放
        showFragmentOrVideo(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                String result = data.getStringExtra("result");
                switch (result) {
                    case MeasureVideoPlayActivity.SendResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case MeasureVideoPlayActivity.SendResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        showFragment(position);
                        break;
                    case MeasureVideoPlayActivity.SendResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        showFragment(position);
                        break;
                    default:
                        break;
                }
            }
        }
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
                        untieDevice();
                    }
                }).show();
    }

    private void untieDevice() {
        mRightView.setImageResource(R.drawable.ic_blooth_beack);
        //先清除已经绑定的设备
        unpairDevice();
        String nameAddress = null;
        String fragmentTag = followInfo.get(position).getFragmentTag();
        switch (fragmentTag) {
            case "HypertensionFollowUpFragment":
                //血压
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                ((Bloodpressure_Fragment) posiontFragment).onStop();
                ((Bloodpressure_Fragment) posiontFragment).dealLogic();
                break;
            case "SugarFollowUpFragment":
                //血糖
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                ((SugarFollowUpFragment) posiontFragment).onStop();
                ((SugarFollowUpFragment) posiontFragment).dealLogic();
                break;
            case "WeightFollowUpFragment":
                //体重
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                ((Weight_Fragment) posiontFragment).onStop();
                ((Weight_Fragment) posiontFragment).dealLogic();
                break;

            case "TemperatureFollowUpFragment":
                //体温
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE);
                ((TemperatureFollowUpFragment) posiontFragment).onStop();
                ((TemperatureFollowUpFragment) posiontFragment).dealLogic();
                break;

            case "ECG_Fragment":
                //心电
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_ECG, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);
                ((ECG_Fragment) posiontFragment).onStop();
                ((ECG_Fragment) posiontFragment).dealLogic();
                break;

            case "SelfECGDetectionFragment":
                //心电
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_ECG, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);
                ((SelfECGDetectionFragment) posiontFragment).onStop();
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

            }
        }

    }

}
