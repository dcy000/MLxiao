package com.gcml.lib_sub_bluetooth;

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
            if (BuildConfig.DEBUG) {
                Log.i("BluetoothSearching", ">>>>>=======>>>>bluetooth started!!!!");
            }
            searchTime = System.currentTimeMillis();
            synchronized (BluetoothSearchHelper.this) {
                if (isClear) {
                    BluetoothStore.getClient().stopSearch();
                    return;
                }
            }
            isOnSearching = true;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(true);
            }
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            Log.i("BluetoothSearching", device.getName() + ">>>======>>>>" + device.getAddress());
            if (BuildConfig.DEBUG) {
            }
            if (!isClear) {
                if (searchListener != null) {
                    searchListener.onNewDeviceFinded(device.device);
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
            }
        }

        @Override
        public void onSearchStopped() {
            if (BuildConfig.DEBUG) {
                Log.i("BluetoothSearching", ">>>>>=======>>>>bluetooth stopped>>>Thread:" + Thread.currentThread().getName());
                Log.i("BluetoothSearching", "bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
            }
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
            if (BuildConfig.DEBUG) {
                Log.i("BluetoothSearching", ">>>>>=======>>>>bluetooth canceled");
                Log.i("BluetoothSearching", "bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
            }
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
        }
    }

    public synchronized void clear() {
        if (BuildConfig.DEBUG) {
            Log.i("BluetoothSearching", "BluetoothSearchHelper>>>>===>>>clear");
        }
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
