package com.example.han.referralproject.bluetooth_devices.base;

import com.inuker.bluetooth.library.search.SearchResult;

public class BluetoothDevice {
    private int currentState= IPresenter.DEVICE_INITIAL;//默认是未知状态
    private SearchResult searchResult;

    public BluetoothDevice() {
    }

    public BluetoothDevice(int currentState, SearchResult searchResult) {
        this.currentState = currentState;
        this.searchResult = searchResult;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
