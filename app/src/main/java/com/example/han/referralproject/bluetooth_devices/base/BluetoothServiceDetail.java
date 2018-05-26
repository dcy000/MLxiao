package com.example.han.referralproject.bluetooth_devices.base;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class BluetoothServiceDetail implements Serializable {
    public static final int INDICATE = 1;
    public static final int READ = 2;
    public static final int NOTIFY = 3;
    public static final int WRITE = 4;
    public static final int WRITE_NO_RESPONSE = 5;
    private String macAddress;
    private UUID service;
    private List<CharacteristicBean> characteristics;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public UUID getService() {
        return service;
    }

    public void setService(UUID service) {
        this.service = service;
    }

    public List<CharacteristicBean> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<CharacteristicBean> characteristics) {
        this.characteristics = characteristics;
    }

    public static class CharacteristicBean implements Serializable {
        private UUID uuid;
        private List<Integer> type;
        private List<String> datas;

        public List<String> getDatas() {
            return datas;
        }

        public void setDatas(List<String> datas) {
            this.datas = datas;
        }

        public List<Integer> getType() {
            return type;
        }

        public void setType(List<Integer> type) {
            this.type = type;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }
    }
}
