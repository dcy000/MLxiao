package com.example.han.referralproject.settting.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.Utils;
import com.medlink.danbogh.utils.Handlers;
import com.ml.zxing.QrCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;
    @BindView(R.id.iv_device_qrcode)
    ImageView ivQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    public int dp(float value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * value + 0.5f);
    }

    private void initView() {
        mTitleText.setText("关于");
        mToolbar.setVisibility(View.VISIBLE);
        tvVersion.setText("版本:" + Utils.getLocalVersionName(this));
//        tvDeviceId.setText(Utils.getDeviceId());

        String textFormat = "gcml_normal_%s_%s";
        String deviceId = Utils.getDeviceId();
        String checksum = com.medlink.danbogh.utils.Utils.md5("gcml_normal_" + deviceId);
        final String text = String.format(textFormat, deviceId, checksum);

        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = QrCodeUtils.encodeQrCode(text, dp(230), dp(230));
                if (bitmap != null) {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            ivQrcode.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }
}
