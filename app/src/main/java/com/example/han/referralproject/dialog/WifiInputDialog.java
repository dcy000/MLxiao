package com.example.han.referralproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.WiFiUtil;

public class WifiInputDialog extends Dialog implements View.OnClickListener{
    private ScanResult mDataResult;
    private EditText mPwdEt;
    private Context mContext;
    private WiFiUtil wiFiUtil;

    public WifiInputDialog(@NonNull Context context, ScanResult scanResult) {
        super(context, R.style.XDialog);
        mDataResult = scanResult;
        mContext = context;
        wiFiUtil = WiFiUtil.getInstance(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_info);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
        findViewById(R.id.tv_ok).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        if (mDataResult == null){
            return;
        }
        ((TextView)findViewById(R.id.tv_wifi_name)).setText(mDataResult.SSID);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()){
            case R.id.tv_ok:
                if (TextUtils.isEmpty(mPwdEt.getText())){
                    Toast.makeText(mContext, R.string.empty_pwd, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mDataResult.capabilities.contains("WPA2") || mDataResult.capabilities.contains("WPA-PSK")) {
                    wiFiUtil.addWiFiNetwork(mDataResult.SSID, mPwdEt.getText().toString(), WiFiUtil.Data.WIFI_CIPHER_WPA2);
                } else if (mDataResult.capabilities.contains("WPA")) {
                    wiFiUtil.addWiFiNetwork(mDataResult.SSID, mPwdEt.getText().toString(), WiFiUtil.Data.WIFI_CIPHER_WPA);
                } else if (mDataResult.capabilities.contains("WEP")) {
                    /* WIFICIPHER_WEP 加密 */
                    wiFiUtil.addWiFiNetwork(mDataResult.SSID, mPwdEt.getText().toString(), WiFiUtil.Data.WIFI_CIPHER_WEP);
                } else {
                    /* WIFICIPHER_OPEN NOPASSWORD 开放无加密 */
                    wiFiUtil.addWiFiNetwork(mDataResult.SSID, "", WiFiUtil.Data.WIFI_CIPHER_NOPASS);
                }
                break;
            case R.id.tv_cancel:
                break;
        }
    }
}
