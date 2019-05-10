package com.example.module_control_volume.wifi;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.module_control_volume.R;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/wifi/detail/activity")
public class WifiDetailActivity extends ToolbarBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_detail);
        ScanResult wifi = getIntent().getParcelableExtra("wifi");
        mTitleText.setText("Wifi 详情");
        if (wifi!=null){
            String ssid = wifi.SSID;
            mTitleText.setText(ssid+" 详情");

        }
    }
}
