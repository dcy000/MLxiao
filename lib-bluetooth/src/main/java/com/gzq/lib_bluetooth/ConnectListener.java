package com.gzq.lib_bluetooth;

public interface ConnectListener {
    void success(String address);
    void failed();
    void disConnect();
}
