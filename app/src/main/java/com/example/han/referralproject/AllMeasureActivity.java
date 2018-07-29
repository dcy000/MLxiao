package com.example.han.referralproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.ui.dialog.DialogImage;
import com.gcml.lib_utils.qrcode.QRCodeUtils;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_PDF_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.gcml.module_health_record.HealthRecordActivity;

public class AllMeasureActivity extends BaseActivity {
    private BaseFragment baseFragment;
    private int measure_type;
    private String pdfUrl = "";
    private boolean isMeasure = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        measure_type = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, -1);
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量
                if (baseFragment == null) {
                    baseFragment = new Temperature_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                if (baseFragment == null) {
                    baseFragment = new Bloodpressure_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                if (baseFragment == null) {
                    baseFragment = new Bloodsugar_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                if (baseFragment == null) {
                    baseFragment = new Bloodoxygen_Fragment();
                }
                break;
            case IPresenter.MEASURE_WEIGHT://体重
                if (baseFragment == null) {
                    baseFragment = new Weight_Fragment();
                }
                break;
            case IPresenter.MEASURE_ECG:
                if (baseFragment == null) {
                    baseFragment = new ECG_Fragment();
                }
                break;
            case IPresenter.MEASURE_OTHERS://三合一
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
                    baseFragment = new Fingerpint_Fragment();
                }
                break;
        }
        fragmentTransaction
                .replace(R.id.frame, baseFragment)
                .addToBackStack(null)
                .commit();
        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(new DealVoiceAndJump() {
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
                            break;
                    }

                }

                @Override
                public void jump2DemoVideo(int measureType) {

                }
            });

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

    @Override
    protected void backMainActivity() {
        if (isMeasure) {
            switch (measure_type) {
                case IPresenter.MEASURE_TEMPERATURE://体温测量
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE);
                    break;
                case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
                    break;
                case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
                    break;
                case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN);
                    break;
                case IPresenter.MEASURE_WEIGHT://体重
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_WEIGHT);
                    break;
                case IPresenter.MEASURE_ECG:
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);
                    break;
                case IPresenter.MEASURE_OTHERS://三合一
                    break;
                case IPresenter.CONTROL_FINGERPRINT:
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_FINGERPRINT);
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
    }
}
