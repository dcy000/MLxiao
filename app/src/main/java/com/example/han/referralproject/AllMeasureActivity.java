package com.example.han.referralproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.health.intelligentdetection.HealthSelectSugarDetectionTimeFragment;
import com.example.han.referralproject.measure.single.SingleMeasureBloodoxygenFragment;
import com.example.han.referralproject.measure.single.SingleMeasureBloodpressureFragment;
import com.example.han.referralproject.measure.single.SingleMeasureBloodsugarFragment;
import com.example.han.referralproject.measure.single.SingleMeasureTemperatureFragment;
import com.example.han.referralproject.measure.single.SingleMeasureThreeInOneFragment;
import com.example.han.referralproject.measure.single.SingleMeasureWeightFragment;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.qrcode.QRCodeUtils;
import com.gcml.lib_utils.ui.dialog.DialogImage;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
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
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.gcml.module_health_record.HealthRecordActivity;

public class AllMeasureActivity extends BaseActivity implements FragmentChanged {
    private BluetoothBaseFragment baseFragment;
    private int measure_type;
    private String pdfUrl = "";
    private boolean isMeasure = true;
    private Uri uri;

    public static void startActivity(Context context, int measure_type) {
        context.startActivity(new Intent(context, AllMeasureActivity.class)
                .putExtra(IPresenter.MEASURE_TYPE, measure_type));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.health_ic_blutooth);
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        measure_type = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, -1);
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE:
                //体温测量
                if (baseFragment == null) {
                    mTitleText.setText("体 温 测 量");
                    baseFragment = new SingleMeasureTemperatureFragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                //血压
                if (baseFragment == null) {
                    mTitleText.setText("血 压 测 量");
                    baseFragment = new SingleMeasureBloodpressureFragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                //血糖
                if (baseFragment == null) {
                    mTitleText.setText("血 糖 测 量");
                    baseFragment = new HealthSelectSugarDetectionTimeFragment();
                    baseFragment.setOnFragmentChangedListener(this);
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                //血氧
                if (baseFragment == null) {
                    mTitleText.setText("血 氧 测 量");
                    baseFragment = new SingleMeasureBloodoxygenFragment();
                }
                break;
            case IPresenter.MEASURE_WEIGHT:
                //体重
                if (baseFragment == null) {
                    mTitleText.setText("体 重 测 量");
                    baseFragment = new SingleMeasureWeightFragment();
                }
                break;
            case IPresenter.MEASURE_ECG:
                if (baseFragment == null) {
                    mTitleText.setText("心 电 测 量");
                    baseFragment = new ECG_Fragment();
                }
                break;
            case IPresenter.MEASURE_OTHERS:
                //三合一
                if (baseFragment == null) {
                    mTitleText.setText("三 合 一 测 量");
                    baseFragment = new SingleMeasureThreeInOneFragment();
                }
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
                    baseFragment = new Fingerpint_Fragment();
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
        }


        if (baseFragment != null && measure_type == IPresenter.MEASURE_ECG) {
            ((ECG_Fragment) baseFragment).setOnAnalysisDataListener(new ECG_Fragment.AnalysisData() {
                @Override
                public void onSuccess(String fileNum, String fileAddress, String filePDF) {
                    pdfUrl = filePDF;
                    ECG_PDF_Fragment pdf_fragment = new ECG_PDF_Fragment();
                    Bundle pdfBundle = new Bundle();
                    pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, filePDF);
                    pdf_fragment.setArguments(pdfBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, pdf_fragment).commit();
                    isMeasure = false;
                    mRightView.setImageResource(R.drawable.icon_qrcode);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private DealVoiceAndJump dealVoiceAndJump = new DealVoiceAndJump() {
        @Override
        public void updateVoice(String voice) {
            speak(voice);
        }

        @Override
        public void jump2HealthHistory(int measureType) {
            switch (measureType) {
                case IPresenter.MEASURE_TEMPERATURE://体温测量
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 0);
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 1);
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 2);
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 3);
                    break;
                case IPresenter.MEASURE_WEIGHT://体重
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 8);
                    break;
                case IPresenter.MEASURE_ECG:
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 7);
                    break;
                case IPresenter.MEASURE_OTHERS://三合一
                    HealthRecordActivity.startActivity(AllMeasureActivity.this, HealthRecordActivity.class, 5);
                    break;
            }

        }

        @Override
        public void jump2DemoVideo(int measureType) {
            switch (measureType) {
                case IPresenter.MEASURE_TEMPERATURE://体温测量
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "耳温枪测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "血压测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "血糖测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "血氧测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_ECG:
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "心电测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_OTHERS://三合一
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    MeasureVideoPlayActivity.startActivityForResult(AllMeasureActivity.this, uri, null, "三合一测量演示视频",
                            MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    ToastUtils.showShort("主人，该设备暂无演示视频");
                    break;
            }
        }
    };

    private boolean canClickRefresh = true;
    private final TimeCountDownUtils.TimeCountListener timeCountListener
            = new TimeCountDownUtils.TimeCountListener() {
        @Override

        public void onTick(long millisUntilFinished, String tag) {
            canClickRefresh = false;
        }

        @Override
        public void onFinish(String tag) {
            canClickRefresh = false;
            canClickRefresh = true;
        }
    };

    @Override
    protected void backMainActivity() {
        if (!canClickRefresh) {
            ToastUtils.showShort("您点击的太快了");
            return;
        }
        TimeCountDownUtils.getInstance().create(5000, 1000, timeCountListener);
        TimeCountDownUtils.getInstance().start();

        if (isMeasure) {
            switch (measure_type) {
                case IPresenter.MEASURE_TEMPERATURE:
                    //体温测量
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE);
                    ((Temperature_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE:
                    //血压
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                    ((Bloodpressure_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR:
                    //血糖
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                    ((Bloodsugar_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN:
                    //血氧
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN);
                    ((Bloodoxygen_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_WEIGHT:
                    //体重
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                    ((Weight_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_ECG:
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);
                    ((ECG_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.MEASURE_OTHERS:
                    //三合一
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE);
                    ((ThreeInOne_Fragment) baseFragment).dealLogic();
                    break;
                case IPresenter.CONTROL_FINGERPRINT:
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_FINGERPRINT);
                    ((Fingerpint_Fragment) baseFragment).dealLogic();
                    break;
                default:
                    break;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
        TimeCountDownUtils.getInstance().cancelAll();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof HealthSelectSugarDetectionTimeFragment) {
            if (bundle != null) {
                baseFragment = new SingleMeasureBloodsugarFragment();
                baseFragment.setOnDealVoiceAndJumpListener(dealVoiceAndJump);
                baseFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, baseFragment).commit();
            }

        }
    }
}

