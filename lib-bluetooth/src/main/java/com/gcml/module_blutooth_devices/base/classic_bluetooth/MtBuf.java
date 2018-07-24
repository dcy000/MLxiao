package com.gcml.module_blutooth_devices.base.classic_bluetooth;

import android.support.annotation.Nullable;
import android.util.Log;

import com.contec.cms50dj_jar.DeviceCommand;
import com.contec.cms50dj_jar.DevicePackManager;
import com.gcml.lib_utils.thread.ThreadUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MtBuf {
    private static final String TAG = "com.testBlueTooth.Mtbuf";
    private DevicePackManager m_DevicePackManager;
    private IBluetoothDataCallback iBluetoothDataCallback;

    public DevicePackManager getDevicePackManager() {
        return m_DevicePackManager;
    }

    public MtBuf(IBluetoothDataCallback iBluetoothDataCallback) {
        this.iBluetoothDataCallback = iBluetoothDataCallback;
        m_DevicePackManager = new DevicePackManager();
    }

    public synchronized void write(byte[] buf, int count, final OutputStream pOutputStream) {
        int _receiveNum = m_DevicePackManager.arrangeMessage(buf, count);
        switch (_receiveNum) {
            case 1:// 得到设备号 发送校时命令
                ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<Void>() {
                    @Nullable
                    @Override
                    public Void doInBackground() throws Throwable {
                        try {
                            pOutputStream.write(DeviceCommand.correctionDateTime());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(@Nullable Void result) {

                    }
                }, 500, TimeUnit.MILLISECONDS);
                break;
            case 2:// 对时成功
                ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<Void>() {
                    @Nullable
                    @Override
                    public Void doInBackground() throws Throwable {
                        try {
                            pOutputStream.write(DeviceCommand.setPedometerInfo("175", "75",
                                    0, 24, 10000, 1, 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(@Nullable Void result) {

                    }
                }, 500, TimeUnit.MILLISECONDS);
                break;
            case 3:// 对时失败 关闭socket

                break;
            case 4:// 无新数据;
                Log.e(TAG, "write: 没有新数据");
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
////						pOutputStream.write(DeviceCommand
////								.dayPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
                break;
            case 5: // 整个数据接收完成
                // 保存数据
//			saveSpo2Data();
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						pOutputStream.write(DeviceCommand
//								.dayPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 6:// 一包数据接收完毕 ；
                Log.e(TAG, "write: 一包数据接收完毕");
                List<byte[]> dataList = m_DevicePackManager.getDeviceData50dj().getmSp02DataList();
                if (dataList != null && dataList.size() > 0) {
                    byte[] bytes = dataList.get(dataList.size() - 1);
                    iBluetoothDataCallback.call(bytes[6] + "", bytes[7] + "", "year:" + bytes[0] + " month:" + bytes[1] + "  day:" + bytes[2] + " hour:" + bytes[3] + "  min:" + bytes[4] + "  second:" + bytes[5]);
                }
//                threadPoolTool.schedule(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(500);
//                            pOutputStream.write(DeviceCommand
//                                    .dataUploadSuccessCommand());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 500, TimeUnit.MILLISECONDS);
                break;
            case 7:// 接收失败
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 8:// 8:设置计步器 成功
                ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<Void>() {
                    @Nullable
                    @Override
                    public Void doInBackground() throws Throwable {
                        try {
                            pOutputStream.write(DeviceCommand.getDataFromDevice());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(@Nullable Void result) {

                    }
                }, 500, TimeUnit.MILLISECONDS);
                break;
            case 9:// 9: 设置计步器失败
                ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<Void>() {
                    @Nullable
                    @Override
                    public Void doInBackground() throws Throwable {
                        try {
                            Thread.sleep(500);
                            pOutputStream.write(DeviceCommand.getDataFromDevice());
                            Log.i(TAG, "设置计步器 失败    发送 请求血氧数据命令 ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(@Nullable Void result) {

                    }
                },500,TimeUnit.MILLISECONDS);
                break;
            case 10:// 以天为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataSuccessCommand());
//						Logg.i(TAG, "以天为单位计步器 数据 一包上传完成  发送上传完成命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 11:// 以天为单位计步器 数据上一包上传成功 请求下一包数据

//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataCommand());
//						Logg.i(TAG, "以天为单位计步器 数据上一包上传成功  请求下一包数据 ");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 12:// 以天为单位计步器 数据 全部 上传成功 请求以分为单位的数据
                // TODO 此处处理计步器一天为单位的数据
//			saveDaypedometerData();
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Logg.i(TAG, "以天为单位计步器 数据  全部   上传成功  请求以分为单位的数据");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
                break;
            case 13:// 以天为单位计步器 数据上传失败 请求以分为单位的数据
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Logg.i(TAG, " 以天为单位计步器 数据上传失败 请求以分为单位的数据");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 14:// 以分为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataSuccessCommand());
//						Logg.i(TAG, "以 分 为单位 计步器数据 一包上传完成 发送上传完成命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 15:// 以分为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Logg.i(TAG, "以分为单位计步器 数据 一包上传完成  发送请求下一包的命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
                break;
            case 16:
                // 以分为单位计步器 数据 全部 上传成功 关闭socket
                // TODO 存储以分为单位的数据
//			savePedometerMindata();
//			App_phms.getInstance().mEventBusPostOnBackGround
//					.postInMainThread(DeviceManager.m_DeviceBean);
                break;
            case 17:// 以天为单位 计步器无新数据
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

                break;
            case 18:// 以分为单位 计步器无新数据
                break;
            case 19:// 以分为单位 计步器数据上传失败
                break;
            case 20:// 请求下一包血氧数据
                byte[] bytes = m_DevicePackManager.getDeviceData50dj().getmSpoData();
                iBluetoothDataCallback.call(bytes[6] + "", bytes[7] + "", "year:" + bytes[0] + " month:" + bytes[1] + "  day:" + bytes[2] + " hour:" + bytes[3] + "  min:" + bytes[4] + "  second:" + bytes[5]);
                Log.e(TAG, "write: 全部血氧脉搏数据接收完毕");
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						Logg.i(TAG, " 请求下一包血氧数据   ");
//						SendCommand.send(DeviceCommand.getDataFromDevice());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
                break;
        }
    }
}
