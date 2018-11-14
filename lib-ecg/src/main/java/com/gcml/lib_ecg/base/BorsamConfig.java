package com.gcml.lib_ecg.base;

import java.util.UUID;

public class BorsamConfig {
    public static String[] deviceNames = new String[]{"WeCardio STD", "WeCardio STDF", "Sanket one", "SimpleBLE", "Cardio002", "WeCardio PRO", "WeCardio PLUS", "WeCardio LTS", "WeCardio TTM", "WeCardio SPO2H", "Borsam WBP"};
    public static UUID COMMON_RECEIVE_ECG_SUUID = UUID.fromString("0000ffb1-0000-1000-8000-00805F9B34FB");
    public static UUID COMMON_RECEIVE_ECG_CUUID = UUID.fromString("0000ffb2-0000-1000-8000-00805F9B34FB");
    public static UUID COMMON_WRITE_COMMAND_SUUID = UUID.fromString("0000fff0-0000-1000-8000-00805F9B34FB");
    public static UUID COMMON_WRITE_COMMAND_CUUID = UUID.fromString("0000fff1-0000-1000-8000-00805F9B34FB");
    public static UUID LTS_RECEIVE_ECG_SUUID = UUID.fromString("0000ffd1-0000-1000-8000-00805F9B34FB");
    public static UUID LTS_RECEIVE_ECG_CUUID = UUID.fromString("0000ffd2-0000-1000-8000-00805F9B34FB");
    public static UUID STDF_RECEIVE_COMMAND_SUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805F9B34FB");
    public static UUID STDF_RECEIVE_COMMAND_CUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805F9B34FB");
    public static UUID BATTERY_SUUID = UUID.fromString("0000180f-0000-1000-8000-00805F9B34FB");
    public static UUID BATTERT_CUUID = UUID.fromString("00002a19-0000-1000-8000-00805F9B34FB");
    public static byte[] STDF_WRITE_COMMAND = new byte[]{-86, -86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public BorsamConfig() {
    }
}
