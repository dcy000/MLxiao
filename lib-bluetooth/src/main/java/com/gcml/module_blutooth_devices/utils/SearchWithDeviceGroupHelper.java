package com.gcml.module_blutooth_devices.utils;

import android.text.TextUtils;

import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Kangtai_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_KN550_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Self_PresenterImp;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_GlucWell_PresenterImp;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Sannuo_PresenterImp;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_BoSheng_PresenterImp;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerprint_WeiEr_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Ailikang_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fudakang_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Meidilian_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Zhiziyun_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Bodivis_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Xiangshan_EF895i_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Yike_PresenterImp;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchWithDeviceGroupHelper implements Comparator<SearchResult> {
    private static final String[] BLOODOXYGEN_BRANDS = {"iChoice", "SpO2080971"};
    private static final String[] BLOODPRESSURE_BRANDS = {"eBlood-Pressure", "iChoice", "KN-550BT 110"};
    private static final String[] BLOODSUGAR_BRANDS = {"BLE-Glucowell", "BDE_WEIXIN_TTM"};
    private static final String[] TEMPERATURE_BRANDS = {"AET-WD", "ClinkBlood", "MEDXING-IRT", "FSRKB-EWQ01"};
    private static final String[] WEIGHT_BRANDS = {"VScale", "SHHC-60F1", "iChoice", "SENSSUN_CLOUD"};
    private static final String[] ECG_BRANDS = {"WeCardio STD", "A12-B"};
    private static final String[] FINGERPRINT_BRANDS = {"zjwellcom"};
    private List<SearchResult> devices;
    private int measureType;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private IView view;
    private boolean isSearching = false;

    public SearchWithDeviceGroupHelper(IView view, int measureType) {
        this.view = view;
        this.measureType = measureType;
        devices = new ArrayList<>();
    }

    public void start() {
        switch (measureType) {
            case IPresenter.MEASURE_TEMPERATURE:
                search(TEMPERATURE_BRANDS);
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                search(BLOODPRESSURE_BRANDS);
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                search(BLOODSUGAR_BRANDS);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                search(BLOODOXYGEN_BRANDS);
                break;
            case IPresenter.MEASURE_WEIGHT:
                search(WEIGHT_BRANDS);
                break;
            case IPresenter.MEASURE_ECG:
                search(ECG_BRANDS);
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                search(FINGERPRINT_BRANDS);
                break;
        }
    }

    private void search(final String[] brands) {
        final SearchRequest searchRequest = new SearchRequest.Builder()
                .searchBluetoothClassicDevice(3000, 1)
                .searchBluetoothLeDevice(10000, 1)
                .build();

        BluetoothClientManager.getClient().search(searchRequest, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                isSearching = true;
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                String name = searchResult.getName();
                String address = searchResult.getAddress();
                Logg.e(SearchWithDeviceGroupHelper.class, name + "-----" + address);
                if (!TextUtils.isEmpty(name)) {
                    for (String s : brands) {
                        if (name.equals(s) && !devices.contains(searchResult)) {
                            devices.add(searchResult);
                        }
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                isSearching = false;
                if (devices.size() > 0) {
                    Collections.sort(devices, SearchWithDeviceGroupHelper.this);
                    initPresenter(devices.get(0).getName(), devices.get(0).getAddress());
                }
            }

            @Override
            public void onSearchCanceled() {
                isSearching = false;
            }
        });
    }

    private void initPresenter(String brand, String address) {
        switch (measureType) {
            case IPresenter.MEASURE_TEMPERATURE:
                switch (brand) {
                    case "AET-WD":
                        baseBluetoothPresenter = new Temperature_Ailikang_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "AET-WD"));
                        break;
                    case "ClinkBlood":
                        baseBluetoothPresenter = new Temperature_Fudakang_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "ClinkBlood"));
                        break;
                    case "MEDXING-IRT":
                        baseBluetoothPresenter = new Temperature_Meidilian_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "MEDXING-IRT"));
                        break;
                    case "FSRKB-EWQ01":
                        baseBluetoothPresenter = new Temperature_Zhiziyun_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "FSRKB-EWQ01"));
                        break;
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                switch (brand) {
                    case "eBlood-Pressure":
                        baseBluetoothPresenter = new Bloodpressure_Self_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "eBlood-Pressure"));
                        break;
                    case "iChoice":
                        baseBluetoothPresenter = new Bloodpressure_Chaosi_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                        break;
                    case "KN-550BT 110":
                        baseBluetoothPresenter = new Bloodpressure_KN550_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "KN-550BT 110"));
                        break;
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                switch (brand) {
                    case "BLE-Glucowell":
                        baseBluetoothPresenter = new Bloodsugar_GlucWell_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BLE-Glucowell"));
                        break;
                    case "BDE_WEIXIN_TTM":
                        baseBluetoothPresenter = new Bloodsugar_Sannuo_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BDE_WEIXIN_TTM"));
                        break;
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                switch (brand) {
                    case "iChoice":
                        baseBluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                        break;
                    case "SpO2080971":
                        baseBluetoothPresenter = new Bloodoxygen_Kangtai_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SpO2080971"));
                        break;
                }
                break;
            case IPresenter.MEASURE_WEIGHT:
                switch (brand) {
                    case "VScale":
                        baseBluetoothPresenter = new Weight_Bodivis_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "VScale"));
                        break;
                    case "SHHC-60F1":
                        baseBluetoothPresenter = new Weight_Yike_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SHHC-60F1"));
                        break;
                    case "iChoice":
                        baseBluetoothPresenter = new Weight_Chaosi_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                        break;
                    case "SENSSUN CLOUD":
                        baseBluetoothPresenter = new Weight_Xiangshan_EF895i_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SENSSUN CLOUD"));
                        break;
                }
                break;
            case IPresenter.MEASURE_ECG:
                switch (brand) {
                    case "WeCardio STD":
                        baseBluetoothPresenter = new ECG_BoSheng_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "WeCardio STD"));
                        break;
                    case "A12-B":
                        baseBluetoothPresenter = new ECG_Chaosi_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "A12-B"));
                        break;
                }
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                switch (brand) {
                    case "zjwellcom":
                        baseBluetoothPresenter = new Fingerprint_WeiEr_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MIX, address, "zjwellcom"));
                        break;
                }
                break;
        }
    }

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        return o2.rssi - o1.rssi;
    }

    public void destroy() {
        if (isSearching) {
            BluetoothClientManager.getClient().stopSearch();
        }
        if (baseBluetoothPresenter != null) {
            baseBluetoothPresenter.onDestroy();
        }
    }
}
