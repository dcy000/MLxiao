package com.gcml.module_blutooth_devices.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.lib_utils.permission.PermissionsManager;
import com.gcml.lib_utils.permission.PermissionsResultAction;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Kangtai_PresenterImp;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Self_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_KN550_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Self_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_PresenterImp;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_YuWell_PresenterImp;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_GlucWell_PresenterImp;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Sannuo_PresenterImp;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Self_PresenterImp;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_BoSheng_PresenterImp;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerprint_WeiEr_PresenterImp;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Self_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Ailikang_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fudakang_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Meidilian_PresenterImp;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Zhiziyun_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Bodivis_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Self_PresenterImp;
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
    private static final String[] BLOODOXYGEN_BRANDS = {"POD", "iChoice", "SpO2080971"};
    private static final String[] BLOODPRESSURE_BRANDS = {"eBlood-Pressure", "Yuwell", "Dual-SPP", "iChoice", "KN-550BT 110"};
    private static final String[] BLOODSUGAR_BRANDS = {"Bioland-BGM", "BLE-Glucowell", "BDE_WEIXIN_TTM"};
    private static final String[] TEMPERATURE_BRANDS = {"AET-WD", "ClinkBlood", "MEDXING-IRT", "FSRKB-EWQ01"};
    private static final String[] WEIGHT_BRANDS = {"VScale", "SHHC-60F1", "iChoice", "SENSSUN_CLOUD", "000FatScale01"};
    private static final String[] ECG_BRANDS = {"WeCardio STD", "A12-B"};
    private static final String[] FINGERPRINT_BRANDS = {"zjwellcom"};
    private static final String[] OTHERS_BRANDS = {"BeneCheck GL"};
    private List<SearchResult> devices;
    private int measureType;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private IView view;
    private boolean isSearching = false;
    private MySearchResponse mySearchResponse;
    private static final String TAG = "SearchWithDeviceGroupHe";
    /**
     * 蓝牙连接的敏感权限
     */
    private static final String DANGET_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;

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
            case IPresenter.MEASURE_OTHERS:
                search(OTHERS_BRANDS);
                break;
            default:
                break;
        }
    }

    private final PermissionsResultAction permissionsResultAction = new PermissionsResultAction() {
        @Override
        public void onGranted() {
            Log.i(TAG, "onGranted: 用户同意权限请求");
        }

        @Override
        public void onDenied(String permission) {
            Log.e(TAG, "onDenied: 拒绝了用户请求");
        }
    };

    /**
     * 默认搜索经典蓝牙3秒，搜索ble蓝牙8秒,8秒之后进行信号强度排序，如果有多个可连设备，这连信号最强的设备,
     * 如果没有任何可连设备，则启动第二8秒进行搜索，以此类推。
     *
     * @param brands
     */
    private void search(final String[] brands) {
        if (mySearchResponse == null) {
            mySearchResponse = new MySearchResponse(brands);
        }
        if (!PermissionsManager.getInstance().hasPermission(view.getThisContext(), DANGET_PERMISSION)) {
            if (view instanceof Activity) {
                PermissionsManager.getInstance()
                        .requestPermissionsIfNecessaryForResult(((Activity) view),
                                new String[]{DANGET_PERMISSION}, permissionsResultAction);
            } else if (view instanceof Fragment) {
                PermissionsManager.getInstance()
                        .requestPermissionsIfNecessaryForResult(((Fragment) view),
                                new String[]{DANGET_PERMISSION}, permissionsResultAction);
            }
        }
        final SearchRequest searchRequest = new SearchRequest.Builder()
                .searchBluetoothClassicDevice(3000, 1)
                .searchBluetoothLeDevice(8000, 1)
                .build();
        if (mySearchResponse != null) {
            BluetoothClientManager.getClient().search(searchRequest, mySearchResponse);
        }
    }

    class MySearchResponse implements SearchResponse {
        private final String[] brands;

        public MySearchResponse(String[] brands) {
            this.brands = brands;
        }

        @Override
        public void onSearchStarted() {
            isSearching = true;
            Log.i(TAG, "onSearchStarted: ");
        }

        @Override
        public void onDeviceFounded(SearchResult searchResult) {
            String name = searchResult.getName();
            String address = searchResult.getAddress();
            Log.i(TAG, "》》》" + name + "》》》" + address);
            if (!TextUtils.isEmpty(name)) {
                for (String s : brands) {
                    if (name.startsWith(s) && !devices.contains(searchResult)) {
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
            } else {
                final SearchRequest searchRequest = new SearchRequest.Builder()
                        .searchBluetoothClassicDevice(3000, 1)
                        .searchBluetoothLeDevice(8000, 1)
                        .build();
                if (mySearchResponse != null) {
                    BluetoothClientManager.getClient().search(searchRequest, mySearchResponse);
                }
            }
        }

        @Override
        public void onSearchCanceled() {
            isSearching = false;
            Log.i(TAG, "onSearchCanceled: ");
        }
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
                    default:
                        break;
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                if ("eBlood-Pressure".equals(brand)) {
                    baseBluetoothPresenter = new Bloodpressure_Self_PresenterImp(view,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "eBlood-Pressure"));

                } else if (brand.startsWith("Yuwell")) {
                    baseBluetoothPresenter = new Bloodpressure_YuWell_PresenterImp(view,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Yuwell"));

                } else if ("iChoice".equals(brand)) {
                    baseBluetoothPresenter = new Bloodpressure_Chaosi_PresenterImp(view,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));

                } else if ("KN-550BT 110".equals(brand)) {
                    baseBluetoothPresenter = new Bloodpressure_KN550_PresenterImp(view,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "KN-550BT 110"));

                } else if ("Dual-SPP".equals(brand)) {
                    baseBluetoothPresenter = new Bloodpressure_Xien_PresenterImp(view,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Dual-SPP"));
                } else {

                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                switch (brand) {
                    case "Bioland-BGM":
                        baseBluetoothPresenter = new Bloodsugar_Self_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Bioland-BGM"));
                        break;
                    case "BLE-Glucowell":
                        baseBluetoothPresenter = new Bloodsugar_GlucWell_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BLE-Glucowell"));
                        break;
                    case "BDE_WEIXIN_TTM":
                        baseBluetoothPresenter = new Bloodsugar_Sannuo_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BDE_WEIXIN_TTM"));
                        break;
                    default:
                        break;
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                switch (brand) {
                    case "POD":
                        baseBluetoothPresenter = new Bloodoxygen_Self_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "POD"));
                        break;
                    case "iChoice":
                        baseBluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                        break;
                    case "SpO2080971":
                        baseBluetoothPresenter = new Bloodoxygen_Kangtai_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SpO2080971"));
                        break;
                    default:
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
                    case "000FatScale01":
                        baseBluetoothPresenter = new Weight_Self_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "000FatScale01"));
                        break;
                    default:
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
                    default:
                        break;
                }
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                switch (brand) {
                    case "zjwellcom":
                        baseBluetoothPresenter = new Fingerprint_WeiEr_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MIX, address, "zjwellcom"));
                        break;
                    default:
                        break;
                }
                break;
            case IPresenter.MEASURE_OTHERS:
                switch (brand) {
                    case "BeneCheck GL-0F8B0C":
                        baseBluetoothPresenter = new ThreeInOne_Self_PresenterImp(view,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BeneCheck GL-0F8B0C"));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 按降序排列
     *
     * @param o1
     * @param o2
     * @return
     */
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
            baseBluetoothPresenter = null;
        }
        mySearchResponse = null;
    }
}
