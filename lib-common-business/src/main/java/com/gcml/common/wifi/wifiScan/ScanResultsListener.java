package com.gcml.common.wifi.wifiScan;


import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

import java.util.List;

public interface ScanResultsListener
{
    void onScanResults(@NonNull List<ScanResult> scanResults);
}
