package com.gzq.lib_bluetooth;

public interface ConnectListener {
    void success(String name, String address);

    void failed();

    void disConnect(String address);
}
