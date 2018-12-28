package com.gcml.lib_sub_bluetooth;

public interface ConnectListener {
    void success(String name, String address);

    void failed();

    void disConnect(String address);
}
