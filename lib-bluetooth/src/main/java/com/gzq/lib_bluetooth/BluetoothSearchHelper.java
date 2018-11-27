package com.gzq.lib_bluetooth;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        searchListener = listener;
        final SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothClassicDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                BluetoothStore.getClient().search(request, search);
            }
        }).subscribeOn(Schedulers.single());


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
            Timber.i(device.getName() + ">>>======>>>>" + device.getAddress());
            if (!isClear) {
                synchronized (BluetoothSearchHelper.this) {
                    for (String name : bleNames) {
                        String deviceName = device.getName();
                        if (TextUtils.isEmpty(deviceName)) {
                            continue;
                        }
                        if (searchListener != null && deviceName.contains(name)) {
                            //搜索到了就停止搜索
                            BluetoothStore.getClient().stopSearch();
                            isFindOne = true;
                            searchListener.obtainDevice(device.device);
                        }
                    }
                }
            }
        }

        @Override
        public void onSearchStopped() {
            Timber.i(">>>>>=======>>>>bluetooth stopped>>>Thread:"+Thread.currentThread().getName());
            Timber.i("bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
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
            Timber.i(">>>>>=======>>>>bluetooth canceled");
            Timber.i("bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
        }
    }

    public synchronized void clear() {
        Timber.i("BluetoothSearchHelper>>>>===>>>clear");
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
