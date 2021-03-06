package com.gzq.test_all_devices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.ui.dialog.DialogImage;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.qrcode.QRCodeUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_PDF_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;

public class AllMeasureActivity extends AppCompatActivity {
    private BluetoothBaseFragment baseFragment;
    private BluetoothBean bluetoothBean;
    private LinearLayout mLlBack;
    private ImageView mIvTopRight;
    private String pdfUrl = "";

    public static void startActivity(Context context, BluetoothBean bluetoothBean) {
        context.startActivity(new Intent(context, AllMeasureActivity.class)
                .putExtra("bluetoothbean", bluetoothBean));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        initView();
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (bluetoothBean.getMeasureType()) {
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
        bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
        bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
        bundle.putString(IPresenter.DEVICE_BLUETOOTH_ADDRESS, bluetoothBean.getAddress());
        baseFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, baseFragment).commit();

        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(new DealVoiceAndJump() {
                @Override
                public void updateVoice(String voice) {
                }

                @Override
                public void jump2HealthHistory(int type) {
                    ToastUtils.showShort("跳转到历史记录");
                }

                @Override
                public void jump2DemoVideo(int type) {
                    ToastUtils.showShort("跳转到演示视频");
                }
            });
        }

        if (baseFragment != null && bluetoothBean.getMeasureType() == IPresenter.MEASURE_ECG) {
            ((ECG_Fragment) baseFragment).setOnAnalysisDataListener(new ECG_Fragment.AnalysisData() {
                @Override
                public void onSuccess(String fileNum, String fileAddress, String filePDF) {
                    pdfUrl = filePDF;
                    ECG_PDF_Fragment pdf_fragment = new ECG_PDF_Fragment();
                    Bundle pdfBundle = new Bundle();
                    pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, filePDF);
                    pdf_fragment.setArguments(pdfBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, pdf_fragment).commit();

                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            bluetoothBean = intent.getParcelableExtra("bluetoothbean");
        }
        mLlBack = findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvTopRight = findViewById(R.id.iv_top_right);
        mIvTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
    }
}
