package com.gzq.lib_bluetooth;

import android.text.TextUtils;
import android.util.Log;

import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

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
    private static final String TAG = "BluetoothSearchHelper";
    public void searchClassic(int periodMill, int times, SearchListener listener, String... names) {
        searchListener = listener;
        SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothClassicDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        BluetoothStore.getInstance().getClient().search(request, search);

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
        BluetoothStore.getInstance().getClient().search(request, search);
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
                    BluetoothStore.getInstance().getClient().stopSearch();
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
            Log.i(TAG, "onDeviceFounded: "+device.getName()+">>>>>"+device.getAddress());
            if (!isClear) {
                synchronized (BluetoothSearchHelper.this) {
                    for (String name : bleNames) {
                        String deviceName = device.getName();
                        if (TextUtils.isEmpty(deviceName)) {
                            continue;
                        }
                        if (searchListener != null && deviceName.contains(name)) {
                            //搜索到了就停止搜索
                            BluetoothStore.getInstance().getClient().stopSearch();
                            isFindOne = true;
                            searchListener.obtainDevice(device.device);
                        }
                    }
                }
            }
        }

        @Override
        public void onSearchStopped() {
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
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
        }
    }

    public synchronized void clear() {
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getInstance().getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
