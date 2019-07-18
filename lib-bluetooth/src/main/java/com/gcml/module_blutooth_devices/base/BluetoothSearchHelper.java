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
        String[] strings = names;
        Timber.w("bt ---> Scan searchClassic: names = %s", strings);
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
        String[] strings = names;
        Timber.w("bt ---> Scan searchBle: names = %s", strings);
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

            Timber.w("bt ---> Scan onSearchStarted: isClear = %s", isClear);
            synchronized (BluetoothSearchHelper.this) {
                if (isClear) {
                    BluetoothStore.getClient().stopSearch();
                    return;
                }
            }

            Timber.w("bt ---> Scan onSearchStarted: onSearching true, isClear = %s, searchListener = %s", isClear, searchListener);
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
                    Timber.w("bt ---> Scan onDeviceFounded: onNewDeviceFinded");
                    searchListener.onNewDeviceFinded(device.device);
                } else {
                    Timber.w("bt ---> Scan onDeviceFounded: searchListener = null");
                }
                synchronized (BluetoothSearchHelper.this) {
                    for (String name : bleNames) {
                        String deviceName = device.getName();
                        if (TextUtils.isEmpty(deviceName)) {
                            Timber.w("bt ---> Scan onDeviceFounded: deviceName = null");
                            break;
                        }
                        if (searchListener != null && deviceName.contains(name)) {
                            isFindOne = true;
                            Timber.w("bt ---> Scan onDeviceFounded: obtainedDevice = %s", deviceName);
                            Timber.w("bt ---> Scan stop");
                            BluetoothStore.getClient().stopSearch();
                            isOnSearching = false;
                            searchListener.obtainDevice(device.device);
                            break;
                        }
                    }
                }
            } else {
                Timber.w("bt ---> Scan onDeviceFounded: isClear = %s", isClear);
            }
        }

        @Override
        public void onSearchStopped() {
            Timber.w("bt ---> Scan onSearchStopped: Scanned %s ms", (System.currentTimeMillis() - searchTime));
            searchTime = 0L;
            isOnSearching = false;
            Timber.w("bt ---> Scan onSearchStopped: onSearching %s, searchListener = %s", false, searchListener);
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
            if (searchListener != null && !isClear && !isFindOne) {
                Timber.w("bt ---> Scan onSearchStopped: noneFind");
                searchListener.noneFind();
            }
        }

        @Override
        public void onSearchCanceled() {
            Timber.w("bt ---> Scan onSearchCanceled：Scanned %s ms", (System.currentTimeMillis() - searchTime));
            searchTime = 0L;
            isOnSearching = false;
            Timber.w("bt ---> Scan onSearchCanceled: onSearching %s, searchListener = %s", false, searchListener);
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
        Timber.w("bt ---> Scan clear");
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
