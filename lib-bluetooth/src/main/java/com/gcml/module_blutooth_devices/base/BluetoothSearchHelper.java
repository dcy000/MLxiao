package com.gcml.module_blutooth_devices.base;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 根据蓝牙名字搜索蓝牙
 */
public class BluetoothSearchHelper {
    private MySearch search;
    private SearchListener searchListener;
    private boolean isOnSearching = false;
    private boolean isClear = false;
    private long searchTime = 0L;
    private boolean isFindOne = false;

    @SuppressLint("CheckResult")
    public void searchClassic(int periodMill, int times, SearchListener listener, String... names) {
        Timber.w("bt ---> startScan: type = classic names = %s", names);
        searchListener = listener;
        final SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothClassicDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        BluetoothStore.getClient().search(request, search);
    }

    public void searchBle(int periodMill, int times, SearchListener listener, String... names) {
        Timber.w("bt ---> startScan: type = ble names = %s", names);
        searchListener = listener;
        SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothLeDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        BluetoothStore.getClient().search(request, search);
    }

    class MySearch implements SearchResponse {
        private String[] bleNames;

        public MySearch(String[] names) {
            bleNames = names;
        }

        @Override
        public void onSearchStarted() {
            searchTime = System.currentTimeMillis();
            synchronized (BluetoothSearchHelper.this) {
                if (isClear) {
                    BluetoothStore.getClient().stopSearch();
                    return;
                }
            }
            Timber.i("BaseBluetooth>>>>>>=====>>>bluetooth start>>>>");
            isOnSearching = true;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(true);
            }
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            Timber.w("bt ---> Scan onDeviceFounded: name = %s, address = %s", device.getName(), device.getAddress());
            if (!isClear) {
                if (searchListener != null) {
                    searchListener.onNewDeviceFinded(device.device);
                } else {
                    Timber.w("bt ---> Scan onDeviceFounded: searchListener = null");
                }
                synchronized (BluetoothSearchHelper.this) {
                    for (String name : bleNames) {
                        String deviceName = device.getName();
                        if (TextUtils.isEmpty(deviceName)) {
                            break;
                        }
                        if (searchListener != null && deviceName.contains(name)) {
                            isFindOne = true;
                            searchListener.obtainDevice(device.device);
                            break;
                        }
                    }
                }
            } else {
                Timber.w("bt ---> Scan onDeviceFounded: is Cleard");
            }
        }

        @Override
        public void onSearchStopped() {
            Timber.w("bt ---> Scanned %s ms", (System.currentTimeMillis() - searchTime));
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
            if (searchListener != null && !isClear && !isFindOne) {
                searchListener.noneFind();
            }
        }

        @Override
        public void onSearchCanceled() {
            Timber.w("bt ---> Scan canceled");
            Timber.w("bt ---> Scanned %s ms", (System.currentTimeMillis() - searchTime));
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
        }
    }

    public synchronized void stop() {
        Timber.w("bt ---> Scan stop");
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
    }

    public synchronized void clear() {
        Timber.w("bt ---> clear");
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
