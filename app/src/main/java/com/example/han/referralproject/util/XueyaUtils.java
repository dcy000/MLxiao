package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dcy on 2017/12/23.
 */

public class XueyaUtils {
    public static BluetoothSocket socket;
    public static boolean isSuccess;
    public static List<byte[]> listNum = new ArrayList<>();
    public static boolean isEnd;

    /**
     *
     * @param device
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public static void connect(BluetoothDevice device, final Handler handler) throws IOException {
        listNum.clear();
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        Log.i("mylog", "1111111111111111111111");
        socket = device.createRfcommSocketToServiceRecord(uuid);
        if (!socket.isConnected()) {
            socket.connect();
            Log.e("西恩血压计连接", "connect: "+socket.toString());
        }
        if (socket.isConnected()) {
            isSuccess = true;
        } else {
            isSuccess = false;
            socket.connect();
        }
        Log.i("mylog", "2222222222222222222");
        isEnd = false;
        //handler.sendEmptyMessage(14);
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                // 发送启动命令
                OutputStream os;
                try {
                    os = socket.getOutputStream();
                    byte[] b = { (byte) 0xff, (byte) 0xff, 0x05, 0x01,
                            (byte) 0xfa };
                    os.write(b, 0, b.length);
                    Log.i("mylog", "33333333333333333333333");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream is;
                try {
                    is = socket.getInputStream();
                    // 定义一个数据，来接收数据
                    byte[] buffer = new byte[255];
                    // 定义int型变量，代表字节数
                    int bytes = 0;
                    if (is == null) {
                        return;
                    }
                    // 执行循环
                    while (!isEnd) {
                        try {
                            if (socket != null) {
                                Log.i("mylog", "AAAAAAA--------->>>>>>>>>>"
                                        + bytes);
                                // 如果读取的字节数大于0，表示有数据存在
                                bytes = is.read(buffer);
                                if (bytes > 0 && bytes == 73) {
                                    // 定义新的字符数据，长度为读取的数据的长度
                                    byte[] byte_data = new byte[bytes];
                                    // 通过循环将buffer里面的数据复制到byte_data数组中
                                    for (int i = 0; i < bytes; i++) {
                                        byte_data[i] = buffer[i];
                                    }
                                    // 将byte_data数组转换成字符串
                                    listNum.add(byte_data);
                                    if (listNum != null) {
                                        byte[] result = listNum.get(0);
                                        if (result[2] == 73 && result[3] == 3) {
                                            // 测量返回成功关闭流
                                            try {
                                                is.close();
                                            } catch (IOException ae) {
                                                ae.printStackTrace();
                                            }
                                            isEnd = true;
                                            socket.close();
                                            // dialog.cancel();
                                            Message msg = handler
                                                    .obtainMessage();
                                            msg.what = 13;
                                            msg.obj = result;
                                            handler.sendMessage(msg);
                                            break;
                                        }
                                    }
                                } else if (bytes > 0 && bytes == 6) {
                                    // 定义新的字符数据，长度为读取的数据的长度
                                    byte[] byte_data = new byte[bytes];
                                    // 通过循环将buffer里面的数据复制到byte_data数组中
                                    for (int i = 0; i < bytes; i++) {
                                        byte_data[i] = buffer[i];
                                    }
                                    String abc = "";
                                    for (int j = 0; j < byte_data.length; j++) {
                                        abc += byte_data[j];
                                    }
                                    System.out
                                            .println("CCCCCCCCCCCCCCCCCCCCC----------->>>>>"
                                                    + abc);
                                    if (byte_data[2] == 6 && byte_data[3] == 7) {
                                        try {
                                            is.close();
                                        } catch (IOException ae) {
                                            ae.printStackTrace();
                                        }
                                        isEnd = true;
                                        socket.close();
                                        // dialog.cancel();
                                        if (byte_data[4] == 0) {
                                            handler.sendEmptyMessage(4);
                                        } else if (byte_data[4] == 1) {
                                            handler.sendEmptyMessage(5);
                                        } else if (byte_data[4] == 2) {
                                            handler.sendEmptyMessage(6);
                                        } else {
                                            handler.sendEmptyMessage(2);
                                        }
                                        break;
                                    }
                                    // else{
                                    // try {
                                    // is.close();
                                    // } catch (IOException ae) {
                                    // ae.printStackTrace();
                                    // }
                                    // socket.close();
                                    // handler.sendEmptyMessage(3);
                                    // }
                                } else if (bytes > 0 && bytes == 10) {
                                    // 定义新的字符数据，长度为读取的数据的长度
                                    byte[] byte_data = new byte[bytes];
                                    // 通过循环将buffer里面的数据复制到byte_data数组中
                                    for (int i = 0; i < bytes; i++) {
                                        byte_data[i] = buffer[i];
                                    }

                                    Message msg = handler.obtainMessage();
                                    msg.what = 12;
                                    msg.obj = byte_data;
                                    handler.sendMessage(msg);

                                } else if (bytes > 0 && bytes == 5) {
                                    // 定义新的字符数据，长度为读取的数据的长度
                                    byte[] byte_data = new byte[bytes];
                                    // 通过循环将buffer里面的数据复制到byte_data数组中
                                    for (int i = 0; i < bytes; i++) {
                                        byte_data[i] = buffer[i];
                                    }
                                    if (byte_data[3] == 4) {
                                        try {
                                            is.close();
                                        } catch (IOException ae) {
                                            ae.printStackTrace();
                                        }
                                        isEnd = true;
                                        socket.close();
                                        handler.sendEmptyMessage(7);

                                    }
                                }

                            }
                        } catch (IOException a) {
                            try {
                                is.close();
                            } catch (IOException ae) {
                                ae.printStackTrace();
                            }
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        th.start();
    }

    public static void stopThread(){
        isEnd = true;
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
