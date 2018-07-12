/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcml.module_blutooth_devices.base.classic_bluetooth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import com.gcml.module_blutooth_devices.base.WeakHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 管理蓝牙设备连接，传输，读取等操作
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint("NewApi")
public class ClassicBluetoothService {
    private static final String TAG = "ClassicBluetoothService";
    private static final boolean D = true;
    // 通用UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter mAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private WeakHandler handler;
    private IClassicBluetoothCallBack callBack;
    // 标识当前的连接状态
    public static final int STATE_NONE = 0; //初始化状态
    public static final int STATE_LISTEN = 1; //监听尝试连接的设备
    public static final int STATE_CONNECTING = 2; //正在连接
    public static final int STATE_CONNECTED = 3; //连接成功
    public static final int STATE_CONNECT_FAIL = 4; //连接失败
    public static final int STATE_CAN_READ_WRITE = 5;//这个时候可以读取数据或者写入数据
    public static final int STATE_DISCONNECTED = 6;//连接断开

    /**
     * @param callBack 将信息传到UI线程的接口
     */
    public ClassicBluetoothService(IClassicBluetoothCallBack callBack) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        handler = new WeakHandler();
        this.callBack = callBack;
    }

    /**
     * 设置当前所处的状态
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D)
            Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * 得到当前的状态
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        if (D)
            Log.d(TAG, "start");

        // 如果当前正在尝试连接或者已经连接，先停止和断开他们
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_LISTEN);
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.call(STATE_LISTEN);
            }
        });
    }

    /**
     * 尝试连接设备
     *
     * @param device
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D)
            Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
        callBack.call(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket,
                                       BluetoothDevice device) {
        if (D)
            Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one
        // device

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        setState(STATE_CONNECTED);
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.call(STATE_CONNECTED);
            }
        });
    }

    /**
     * 停止所有线程
     */
    public synchronized void stop() {
        if (D)
            Log.d(TAG, "stop");
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
        callBack.call(STATE_NONE);
    }

    /**
     * 写入数据
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        }
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_CONNECT_FAIL);
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.call(STATE_CONNECT_FAIL);
            }
        });
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_DISCONNECTED);
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.call(STATE_DISCONNECTED);
            }
        });
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                for (int i = 0; i < 300; i++)
                    Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++)
                Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");
            //先停止搜索
            mAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

            } catch (IOException e) {
                e.printStackTrace();
                connectionFailed();
                // Close the socket
                try {
                    if (mmSocket != null) {
                        mmSocket.close();
                    }

                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                ClassicBluetoothService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (ClassicBluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                setState(STATE_CAN_READ_WRITE);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.call(STATE_CAN_READ_WRITE);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            boolean run = true;
            // Keep listening to the InputStream while connected
            while (run) {
                try {
                    // // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    callBack.writeData(bytes, buffer, mmOutStream);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    if (mmInStream != null) {
                        try {
                            mmInStream.close();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    connectionLost();
                    break;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG, "disconnected2", e);
                    if (mmInStream != null) {
                        try {
                            mmInStream.close();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    connectionLost();
                    run = false;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
