package com.example.han.referralproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothBean implements Parcelable{
    private String bluetoothName;
    private String name;
    private String address;
    private int  measureType;

    public BluetoothBean() {
    }

    public BluetoothBean(String bluetoothName, String name, String address, int measureType) {
        this.bluetoothName = bluetoothName;
        this.name = name;
        this.address = address;
        this.measureType = measureType;
    }

    protected BluetoothBean(Parcel in) {
        bluetoothName = in.readString();
        name = in.readString();
        address = in.readString();
        measureType = in.readInt();
    }

    public static final Creator<BluetoothBean> CREATOR = new Creator<BluetoothBean>() {
        @Override
        public BluetoothBean createFromParcel(Parcel in) {
            return new BluetoothBean(in);
        }

        @Override
        public BluetoothBean[] newArray(int size) {
            return new BluetoothBean[size];
        }
    };

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMeasureType() {
        return measureType;
    }

    public void setMeasureType(int measureType) {
        this.measureType = measureType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bluetoothName);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(measureType);
    }
}
