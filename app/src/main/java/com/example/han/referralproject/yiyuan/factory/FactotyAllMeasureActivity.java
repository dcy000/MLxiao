package com.example.han.referralproject.yiyuan.factory;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require2.dialog.AlertDialog;
import com.example.han.referralproject.single_measure.ChooseECGDeviceFragment;
import com.example.han.referralproject.single_measure.SelfECGDetectionFragment;
import com.example.han.referralproject.single_measure.SingleMeasureHandRingFragment;
import com.example.han.referralproject.single_measure.bean.BoShengResultBean;
import com.example.han.referralproject.single_measure.factory.FactoryDialogFragment;
import com.example.han.referralproject.single_measure.factory.FactoryHealthSelectSugarDetectionTimeFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureBloodoxygenFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureBloodpressureFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureBloodsugarFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureECGFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureTemperatureFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureThreeInOneFragment;
import com.example.han.referralproject.single_measure.factory.FactorySingleMeasureWeightFragment;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_PDF_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.DataUtils;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.gcml.module_video.measure.MeasureVideoPlayActivity;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.medlink.danbogh.utils.T;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class FactotyAllMeasureActivity extends BaseActivity implements FragmentChanged, ThreeInOne_Fragment.MeasureItemChanged {
    private BluetoothBaseFragment baseFragment;
    private int measure_type;
    private boolean isMeasureTask;
    private String pdfUrl = "";
    private boolean isMeasure = true;
    private Uri uri;
    private boolean isShowBloodsugarSelectTime = false;
    private ArrayList<Integer> threeInOnePosition = new ArrayList<>();

    public static void startActivity(Context context, int measure_type) {
        Intent intent = new Intent(context, FactotyAllMeasureActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(IPresenter.MEASURE_TYPE, measure_type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int measure_type, boolean is_measure_task) {
        Intent intent = new Intent(context, FactotyAllMeasureActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(IPresenter.MEASURE_TYPE, measure_type);
        intent.putExtra(IPresenter.IS_MEASURE_TASK, is_measure_task);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_all_measure);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.ic_blooth_beack);
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        measure_type = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, -1);
        isMeasureTask = getIntent().getBooleanExtra(IPresenter.IS_MEASURE_TASK, false);

        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE:
                //体温测量
                if (baseFragment == null) {
                    mTitleText.setText("体 温 测 量");
                    baseFragment = new FactorySingleMeasureTemperatureFragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                //血压
                if (baseFragment == null) {
                    mTitleText.setText("血 压 测 量");
                    Bundle bloodBundle = new Bundle();
                    bloodBundle.getBoolean(IPresenter.IS_MEASURE_TASK, isMeasureTask);
                    baseFragment = new FactorySingleMeasureBloodpressureFragment();
                    baseFragment.setArguments(bloodBundle);
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                //血糖
                if (baseFragment == null) {
                    mTitleText.setText("血 糖 测 量");
//                    mRightView.setImageResource(R.drawable.health_ic_home);
                    mRightView.setVisibility(View.GONE);
                    isShowBloodsugarSelectTime = true;
                    baseFragment = new FactoryHealthSelectSugarDetectionTimeFragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                //血氧
                if (baseFragment == null) {
                    mTitleText.setText("血 氧 测 量");
                    baseFragment = new FactorySingleMeasureBloodoxygenFragment();
                }
                break;
            case IPresenter.MEASURE_WEIGHT:
                //体重
                if (baseFragment == null) {
                    mTitleText.setText("体 重 测 量");
                    Bundle weightBundle = new Bundle();
                    weightBundle.getBoolean(IPresenter.IS_MEASURE_TASK, isMeasureTask);
                    baseFragment = new FactorySingleMeasureWeightFragment();
                    baseFragment.setArguments(weightBundle);
                }
                break;
            case IPresenter.MEASURE_ECG:
                //心电
                int device = (int) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 2);
                mTitleText.setText("心 电 测 量");
                if (device == 1) {
                    baseFragment = new SelfECGDetectionFragment();
                } else if (device == 2) {
                    baseFragment = new FactorySingleMeasureECGFragment();
                } else {
                    mTitleText.setText("心 电 设 备 选 择");
                    baseFragment = new ChooseECGDeviceFragment();
//                    mRightView.setImageResource(R.drawable.health_ic_home);
                    mRightView.setVisibility(View.GONE);
                }
                break;
            case IPresenter.MEASURE_OTHERS:
                //三合一
                if (baseFragment == null) {
                    mTitleText.setText("三 合 一 测 量");
//                    mRightView.setImageResource(R.drawable.health_ic_home);
                    mRightView.setVisibility(View.GONE);
                    isShowBloodsugarSelectTime = true;
                    baseFragment = new FactoryHealthSelectSugarDetectionTimeFragment();
                }
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
                    baseFragment = new Fingerpint_Fragment();
                }
                break;
            case IPresenter.MEASURE_HAND_RING:
                //手环
                if (baseFragment == null) {
                    mTitleText.setText("活 动 监 测");
                    baseFragment = new SingleMeasureHandRingFragment();
                }
                break;
            default:
                break;
        }

        fragmentTransaction
                .replace(R.id.frame, baseFragment)
                .addToBackStack(null)
                .commit();
        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(dealVoiceAndJump);
            baseFragment.setOnFragmentChangedListener(this);
        }

        setECGListener();
    }

    private DealVoiceAndJump dealVoiceAndJump = new DealVoiceAndJump() {
        @Override
        public void updateVoice(String voice) {
            String connected = getResources().getString(R.string.bluetooth_device_connected);
            String disconnected = getResources().getString(R.string.bluetooth_device_disconnected);
            if (connected.equals(voice)) {
                mRightView.setImageResource(R.drawable.ic_blooth_connect);
                mRightView.setVisibility(View.VISIBLE);
            } else if (disconnected.equals(voice)) {
                mRightView.setImageResource(R.drawable.ic_blooth_beack);
                mRightView.setVisibility(View.VISIBLE);
            }

            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), voice, false);
        }

        @Override
        public void jump2HealthHistory(int measureType) {
            switch (measureType) {
                case IPresenter.MEASURE_TEMPERATURE:
                    //体温测量
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 0);
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE:
                    //血压
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 1);
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR:
                    //血糖
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 2);
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN:
                    //血氧
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 3);
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    //体重
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 8);
                    break;
                case IPresenter.MEASURE_ECG:
                    HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 7);
                    break;
                case IPresenter.MEASURE_OTHERS:
                    //三合一 血糖的位置2，血尿酸位置：6；胆固醇位置：5
                    if (threeInOnePosition.size() == 0) {
                        HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, 6);
                    } else {
                        HealthRecordActivity.startActivity(FactotyAllMeasureActivity.this, threeInOnePosition.get(threeInOnePosition.size() - 1));
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void jump2DemoVideo(int measureType) {
            switch (measureType) {
                case IPresenter.MEASURE_TEMPERATURE:
                    //体温测量
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                    jump2MeasureVideoPlayActivity(uri, "耳温枪测量演示视频");
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE:
                    //血压
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                    jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR:
                    //血糖
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                    jump2MeasureVideoPlayActivity(uri, "血糖测量演示视频");
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN:
                    //血氧
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                    jump2MeasureVideoPlayActivity(uri, "血氧测量演示视频");
                    break;
                case IPresenter.MEASURE_ECG:
                    //心电
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                    jump2MeasureVideoPlayActivity(uri, "心电测量演示视频");
                    break;
                case IPresenter.MEASURE_OTHERS:
                    //三合一
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    jump2MeasureVideoPlayActivity(uri, "三合一测量演示视频");
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    //体重
                    ToastUtils.showShort("主人，该设备暂无演示视频");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        MeasureVideoPlayActivity.startForResultActivity(this, uri, null, title, 1001);
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
                        break;
                    case MeasureVideoPlayActivity.SendResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void backMainActivity() {
        if (baseFragment != null && baseFragment instanceof ChooseECGDeviceFragment) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        if (baseFragment != null && baseFragment instanceof FactoryHealthSelectSugarDetectionTimeFragment) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        if (isMeasure) {
            showRefreshBluetoothDialog();
        } else {
            if (DataUtils.isNullString(pdfUrl)) {
                return;
            }
            FactoryDialogFragment.newInstance(pdfUrl).show(getSupportFragmentManager(), "AllMeasure");
        }
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(FactotyAllMeasureActivity.this)
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
        mRightView.setVisibility(View.VISIBLE);
        //先清除已经绑定的设备
        unpairDevice();
        String nameAddress = null;
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE:
                //体温测量
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE);
                ((Temperature_Fragment) baseFragment).onStop();
                ((Temperature_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                //血压
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                ((Bloodpressure_Fragment) baseFragment).onStop();
                ((Bloodpressure_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                //血糖
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                ((Bloodsugar_Fragment) baseFragment).onStop();
                ((Bloodsugar_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                //血氧
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN);
                ((Bloodoxygen_Fragment) baseFragment).onStop();
                ((Bloodoxygen_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_WEIGHT:
                //体重
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                ((Weight_Fragment) baseFragment).onStop();
                ((Weight_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_ECG:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_ECG, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);
                if (baseFragment instanceof SelfECGDetectionFragment) {
                    ((SelfECGDetectionFragment) baseFragment).startDiscovery();
                } else if (baseFragment instanceof ECG_Fragment) {
                    ((ECG_Fragment) baseFragment).onStop();
                    ((ECG_Fragment) baseFragment).dealLogic();
                }
                break;
            case IPresenter.MEASURE_OTHERS:
                //三合一
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE);
                ((ThreeInOne_Fragment) baseFragment).onStop();
                ((ThreeInOne_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_FINGERPRINT, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_FINGERPRINT);
                ((Fingerpint_Fragment) baseFragment).onStop();
                ((Fingerpint_Fragment) baseFragment).dealLogic();
                break;
            case IPresenter.MEASURE_HAND_RING:
                nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_HAND_RING, "");
                SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_HAND_RING);
                ((SingleMeasureHandRingFragment) baseFragment).onStop();
                ((SingleMeasureHandRingFragment) baseFragment).dealLogic();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
        MLVoiceSynthetize.stop();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof FactoryHealthSelectSugarDetectionTimeFragment) {
            if (bundle != null) {
                if (measure_type == IPresenter.MEASURE_BLOOD_SUGAR) {
                    baseFragment = new FactorySingleMeasureBloodsugarFragment();
                    baseFragment.setArguments(bundle);
                } else if (measure_type == IPresenter.MEASURE_OTHERS) {
                    baseFragment = new FactorySingleMeasureThreeInOneFragment();
                    baseFragment.setArguments(bundle);
                    ((ThreeInOne_Fragment) baseFragment).setOnMeasureItemChanged(this);
                }

                isShowBloodsugarSelectTime = false;
                mRightView.setImageResource(R.drawable.ic_blooth_beack);
                mRightView.setVisibility(View.VISIBLE);
            }

        } else if (fragment instanceof ChooseECGDeviceFragment) {
            if (bundle != null) {
                int bundleInt = bundle.getInt(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 2);
                if (bundleInt == 1) {
                    baseFragment = new SelfECGDetectionFragment();
                } else if (bundleInt == 2) {
                    baseFragment = new FactorySingleMeasureECGFragment();
                }
                mTitleText.setText("心 电 测 量");
                mRightView.setImageResource(R.drawable.ic_blooth_beack);
                mRightView.setVisibility(View.VISIBLE);
            }
        } else if (fragment instanceof ECG_Fragment || fragment instanceof SelfECGDetectionFragment) {
            //先清除本地的缓存
            SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG);
            mTitleText.setText("心 电 设 备 选 择");
            baseFragment = new ChooseECGDeviceFragment();
//            mRightView.setImageResource(R.drawable.health_ic_home);
            mRightView.setVisibility(View.GONE);
        }
        baseFragment.setOnFragmentChangedListener(this);
        baseFragment.setOnDealVoiceAndJumpListener(dealVoiceAndJump);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, baseFragment).commit();
        setECGListener();

    }

    private void setECGListener() {
        if (baseFragment != null && measure_type == IPresenter.MEASURE_ECG && baseFragment instanceof ECG_Fragment) {
            ((ECG_Fragment) baseFragment).setOnAnalysisDataListener(new ECG_Fragment.AnalysisData() {
                @Override
                public void onSuccess(String fileNum, String fileJson, String filePDF) {
                    pdfUrl = filePDF;
                    BoShengResultBean resultBean = new Gson().fromJson(fileJson, BoShengResultBean.class);

//                    ArrayList<DetectionData> datas = new ArrayList<>();
//                    DetectionData ecgData = new DetectionData();
////detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
//                    ecgData.setDetectionType("2");
//                    ecgData.setEcg(resultBean.getStop_light() + "");
//                    ecgData.setResult(resultBean.getFindings());
//                    ecgData.setHeartRate(resultBean.getAvgbeats().get(0).getHR());
//                    datas.add(ecgData);
//                    String userId = ((UserInfoBean) Box.getSessionManager().getUser()).bid;
//                    Box.getRetrofit(API.class)
//                            .postMeasureData(userId, datas)
//                            .compose(RxUtils.httpResponseTransformer())
//                            .as(RxUtils.autoDisposeConverter(this))
//                            .subscribe(new CommonObserver<List<DetectionResult>>() {
//                                @Override
//                                public void onNext(List<DetectionResult> detectionResults) {
//                                    ToastUtils.showShort("上传数据成功");
//                                }
//
//                                @Override
//                                protected void onError(ApiException ex) {
//                                    super.onError(ex);
//                                    ToastUtils.showShort(ex.message);
//                                }
//                            });

                    DataInfoBean ecgInfo = new DataInfoBean();
                    ecgInfo.ecg = resultBean.getStop_light();
                    ecgInfo.heart_rate = resultBean.getAvgbeats().get(0).getHR();

                    NetworkApi.postData(ecgInfo, response -> {
                        T.show("数据上传成功");
                    }, message -> {
                        T.show("数据上传失败");
                    });

                    ECG_PDF_Fragment pdf_fragment = new ECG_PDF_Fragment();
                    Bundle pdfBundle = new Bundle();
                    pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, filePDF);
                    pdf_fragment.setArguments(pdfBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, pdf_fragment).commit();
                    isMeasure = false;
                    mRightView.setImageResource(R.drawable.health_measure_icon_qrcode);
                    mRightView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public void onChanged(int position) {
        threeInOnePosition.add(position);
    }

}

