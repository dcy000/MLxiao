package com.gcml.health.measure.single_measure;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.data.TimeCountDownUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.qrcode.QRCodeUtils;
import com.gcml.common.widget.base.dialog.DialogImage;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.cc.CCHealthRecordActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.first_diagnosis.fragment.HealthSelectSugarDetectionTimeFragment;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.fragment.ChooseECGDeviceFragment;
import com.gcml.health.measure.single_measure.fragment.SelfECGDetectionFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodoxygenFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodpressureFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodsugarFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureTemperatureFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureThreeInOneFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureWeightFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureBloodoxygenFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureBloodpressureFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureBloodsugarFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureTemperatureFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureThreeInOneFragment;
import com.gcml.health.measure.single_measure.no_upload_data.NonUploadSingleMeasureWeightFragment;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGFragment;
import com.gcml.module_blutooth_devices.ecg.ECG_PDF_Fragment;
import com.gcml.module_blutooth_devices.three.ThreeInOneFragment;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class AllMeasureActivity extends ToolbarBaseActivity implements FragmentChanged, ThreeInOneFragment.MeasureItemChanged {
    private BluetoothBaseFragment baseFragment;
    private int measure_type;
    private boolean isMeasureTask;
    private String pdfUrl = "";
    private boolean isMeasure = true;
    private Uri uri;
    private boolean isFaceSkip;
    private boolean isShowBloodsugarSelectTime = false;
    private ArrayList<Integer> threeInOnePosition = new ArrayList<>();
    private String servicePackageUUID;
    private String servicePackage;

    public static void startActivity(Context context, int measure_type) {
        Intent intent = new Intent(context, AllMeasureActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(IPresenter.MEASURE_TYPE, measure_type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int measure_type, boolean is_measure_task) {
        Intent intent = new Intent(context, AllMeasureActivity.class);
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
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        measure_type = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, -1);
        isMeasureTask = getIntent().getBooleanExtra(IPresenter.IS_MEASURE_TASK, false);
        isFaceSkip = getIntent().getBooleanExtra(MeasureChooseDeviceActivity.IS_FACE_SKIP, false);
        servicePackageUUID = getIntent().getStringExtra("ServicePackageUUID");
        servicePackage = getIntent().getStringExtra("ServicePackage");
        //TODO:测试代码
        if (BuildConfig.RUN_AS_APP) {
            measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        }
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE:
                //体温测量
                if (baseFragment == null) {
                    mTitleText.setText("体 温 测 量");
                    if (isFaceSkip) {
                        baseFragment = new NonUploadSingleMeasureTemperatureFragment();
                    } else {
                        baseFragment = new SingleMeasureTemperatureFragment();
                    }
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                //血压
                if (baseFragment == null) {
                    mTitleText.setText("血 压 测 量");
                    if (isFaceSkip) {
                        //因为血压相比于其他检测项多出来一个惯用手判断 所以需要单独处理
                        baseFragment = new NonUploadSingleMeasureBloodpressureFragment();
                    } else {
                        Bundle bloodBundle = new Bundle();
                        bloodBundle.getBoolean(IPresenter.IS_MEASURE_TASK, isMeasureTask);
                        baseFragment = new SingleMeasureBloodpressureFragment();
                        baseFragment.setArguments(bloodBundle);
                    }
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                //血糖
                if (baseFragment == null) {
                    mTitleText.setText("血 糖 测 量");
                    mRightView.setImageResource(R.drawable.white_wifi_3);
                    isShowBloodsugarSelectTime = true;
                    baseFragment = new HealthSelectSugarDetectionTimeFragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                //血氧
                if (baseFragment == null) {
                    mTitleText.setText("血 氧 测 量");
                    if (isFaceSkip) {
                        baseFragment = new NonUploadSingleMeasureBloodoxygenFragment();
                    } else {
                        baseFragment = new SingleMeasureBloodoxygenFragment();
                    }
                }
                break;
            case IPresenter.MEASURE_WEIGHT:
                //体重
                if (baseFragment == null) {
                    mTitleText.setText("体 重 测 量");
                    if (isFaceSkip) {
                        baseFragment = new NonUploadSingleMeasureWeightFragment();
                    } else {
                        Bundle weightBundle = new Bundle();
                        weightBundle.getBoolean(IPresenter.IS_MEASURE_TASK, isMeasureTask);
                        baseFragment = new SingleMeasureWeightFragment();
                        baseFragment.setArguments(weightBundle);
                    }
                }
                break;
            case IPresenter.MEASURE_ECG:
                //心电
                int device = (int) SPUtil.get(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG, 0);
                mTitleText.setText("心 电 测 量");
                if (device == 1) {
                    baseFragment = new SelfECGDetectionFragment();
                } else if (device == 2) {
                    baseFragment = new ECGFragment();
                } else {
                    mTitleText.setText("心 电 设 备 选 择");
                    baseFragment = new ChooseECGDeviceFragment();
                    mRightView.setImageResource(R.drawable.white_wifi_3);
                }
                break;
            case IPresenter.MEASURE_THREE:
                //三合一
                if (baseFragment == null) {
                    mTitleText.setText("三 合 一 测 量");
                    mRightView.setImageResource(R.drawable.white_wifi_3);
                    isShowBloodsugarSelectTime = true;
                    baseFragment = new HealthSelectSugarDetectionTimeFragment();
                }
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
//                    baseFragment = new Fingerpint_Fragment();
                }
                break;
            case IPresenter.MEASURE_HAND_RING:
                //手环
                if (baseFragment == null) {
                    mTitleText.setText("活 动 监 测");
//                    baseFragment = new SingleMeasureHandRingFragment();
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
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_connected);
            } else if (disconnected.equals(voice)) {
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
            }

            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), voice, false);
        }

        @Override
        public void jump2HealthHistory(int measureType) {
            switch (measureType) {
                case IPresenter.MEASURE_TEMPERATURE:
                    //体温测量
                    CCHealthRecordActions.jump2HealthRecordActivity(0);
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE:
                    //血压
                    CCHealthRecordActions.jump2HealthRecordActivity(1);
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR:
                    //血糖
                    CCHealthRecordActions.jump2HealthRecordActivity(2);
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN:
                    //血氧
                    CCHealthRecordActions.jump2HealthRecordActivity(3);
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    //体重
                    CCHealthRecordActions.jump2HealthRecordActivity(8);
                    break;
                case IPresenter.MEASURE_ECG:
                    CCHealthRecordActions.jump2HealthRecordActivity(7);
                    break;
                case IPresenter.MEASURE_THREE:
                    //三合一 血糖的位置2，血尿酸位置：6；胆固醇位置：5
                    if (threeInOnePosition.size() == 0) {
                        CCHealthRecordActions.jump2HealthRecordActivity(6);
                    } else {
                        CCHealthRecordActions.jump2HealthRecordActivity(threeInOnePosition.get(threeInOnePosition.size() - 1));
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
                case IPresenter.MEASURE_THREE:
                    //三合一
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    jump2MeasureVideoPlayActivity(uri, "三合一测量演示视频");
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    //体重
                    ToastUtils.showShort("该设备暂无演示视频");
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
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        break;
                    default:
                }
            }
        });
    }

    @Override
    protected void backMainActivity() {
        if (baseFragment != null && (baseFragment instanceof ChooseECGDeviceFragment
                || baseFragment instanceof HealthSelectSugarDetectionTimeFragment)) {
//            CCAppActions.jump2MainActivity();
            super.backMainActivity();
            return;
        }
        if (isMeasure) {
            showRefreshBluetoothDialog();
        } else {
            if (DataUtils.isNullString(pdfUrl)) {
                return;
            }
            Bitmap bitmap = QRCodeUtils.creatQRCode(pdfUrl, 600, 600);
            DialogImage dialogImage = new DialogImage(AllMeasureActivity.this);
            dialogImage.setImage(bitmap);
            dialogImage.setDescription("扫一扫，下载该报告");
            dialogImage.setCanceledOnTouchOutside(true);
            dialogImage.show();
        }
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(AllMeasureActivity.this)
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
                        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                        baseFragment.autoConnect();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
        TimeCountDownUtils.getInstance().cancelAll();
        MLVoiceSynthetize.stop();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            if (bundle != null) {
                if (measure_type == IPresenter.MEASURE_BLOOD_SUGAR) {
                    if (isFaceSkip) {
                        baseFragment = new NonUploadSingleMeasureBloodsugarFragment();
                    } else {
                        baseFragment = new SingleMeasureBloodsugarFragment();
                        baseFragment.setArguments(bundle);
                    }
                } else if (measure_type == IPresenter.MEASURE_THREE) {
                    if (isFaceSkip) {
                        baseFragment = new NonUploadSingleMeasureThreeInOneFragment();
                    } else {
                        baseFragment = new SingleMeasureThreeInOneFragment();
                    }
                    baseFragment.setArguments(bundle);
                    ((ThreeInOneFragment) baseFragment).setOnMeasureItemChanged(this);
                }

                isShowBloodsugarSelectTime = false;
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
            }

        } else if (fragment instanceof ChooseECGDeviceFragment) {
            if (bundle != null) {
                int bundleInt = bundle.getInt(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG, 1);
                if (bundleInt == 1) {
                    baseFragment = new SelfECGDetectionFragment();
                } else if (bundleInt == 2) {
                    baseFragment = new ECGFragment();
                }
                mTitleText.setText("心 电 测 量");
                mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
            }
        } else if (fragment instanceof ECGFragment || fragment instanceof SelfECGDetectionFragment) {
            //先清除本地的缓存
            SPUtil.remove(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG);
            mTitleText.setText("心 电 设 备 选 择");
            baseFragment = new ChooseECGDeviceFragment();
            mRightView.setImageResource(R.drawable.white_wifi_3);
        }
        baseFragment.setOnFragmentChangedListener(this);
        baseFragment.setOnDealVoiceAndJumpListener(dealVoiceAndJump);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, baseFragment).commit();
        setECGListener();

    }

    private void setECGListener() {
        if (baseFragment != null && measure_type == IPresenter.MEASURE_ECG && baseFragment instanceof ECGFragment) {
            ((ECGFragment) baseFragment).setOnAnalysisDataListener(new ECGFragment.AnalysisData() {
                @SuppressLint("CheckResult")
                @Override
                public void onSuccess(String fileNum, String fileAddress, String flag, String result, String heartRate) {
                    if (!isFaceSkip) {
                        ArrayList<DetectionData> datas = new ArrayList<>();
                        DetectionData ecgData = new DetectionData();
                        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
                        ecgData.setDetectionType("2");
                        ecgData.setEcg(TextUtils.equals(flag, "2") ? "1" : flag);
                        ecgData.setResult(result);
                        ecgData.setHeartRate(Integer.parseInt(heartRate));
                        ecgData.setResultUrl(fileAddress);
                        datas.add(ecgData);
                        HealthMeasureRepository.postMeasureData(datas)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .as(RxUtils.autoDisposeConverter(AllMeasureActivity.this, LifecycleUtils.LIFE))
                                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                                    @Override
                                    public void onNext(List<DetectionResult> o) {
                                        ToastUtils.showShort("数据上传成功");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showLong("数据上传失败:" + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }

                    pdfUrl = fileAddress;
                    ECG_PDF_Fragment pdf_fragment = new ECG_PDF_Fragment();
                    Bundle pdfBundle = new Bundle();
                    pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, fileAddress);
                    pdf_fragment.setArguments(pdfBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, pdf_fragment).commitAllowingStateLoss();
                    isMeasure = false;
                    mRightView.setImageResource(R.drawable.health_measure_icon_qrcode);
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


    private void showQuitDialog(int measureType) {
        new AlertDialog(this)
                .builder()
                .setMsg("退出则消费本次购买的套餐，是否继续退出？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HealthMeasureRepository.cancelServicePackage(servicePackageUUID)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<Object>() {
                                    @Override
                                    public void onNext(Object o) {
                                        gotoHistory(measureType);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showShort("取消服务包失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).show();
    }

    private void gotoHistory(int measureType) {

    }
}

