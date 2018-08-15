package com.gcml.module_blutooth_devices.ecg_devices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.borsam.ble.BorsamConfig;
import com.borsam.borsamnetwork.bean.AddRecordResult;
import com.borsam.borsamnetwork.bean.BorsamResponse;
import com.borsam.borsamnetwork.bean.Config;
import com.borsam.borsamnetwork.bean.Face;
import com.borsam.borsamnetwork.bean.LoginResult;
import com.borsam.borsamnetwork.bean.RegisterResult;
import com.borsam.borsamnetwork.bean.UploadFileResult;
import com.borsam.borsamnetwork.http.BorsamHttpUtil;
import com.borsam.borsamnetwork.http.Converter;
import com.borsam.borsamnetwork.http.HttpCallback;
import com.borsam.borsamnetwork.http.Request;
import com.borsam.borsamnetwork.util.PatientApi;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.data.StreamUtils;
import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.handler.WeakHandler;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPManifest;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

/**
 * 博声心电仪
 * mac:50:65:83:8C:2C:A1
 * name:WeCardio STD
 */
@SuppressLint("LongLogTag")
public class ECG_BoSheng_PresenterImp extends BaseBluetoothPresenter {
    private static final String TAG = "ECG_BoSheng_PresenterImp";
    private BleDevice lockedDevice;
    private List<Integer> points;
    private TimeCount timeCount;
    private static boolean isMeasureEnd = false;
    private List<byte[]> bytesResult;
    private WeakHandler weakHandler;
    private static final int MESSAGE_DEAL_BYTERESULT = 1;
    private boolean isLoginBoShengSuccess = false;
    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DEAL_BYTERESULT:
                    LoadingProgressUtils.showViewWithLabel(baseContext, LoadingProgressUtils.LABEL.ON_ANALYSIS_DATA);
                    ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<byte[]>() {
                        @Nullable
                        @Override
                        public byte[] doInBackground() {
                            int length_byte = 0;
                            for (int i = 0; i < bytesResult.size(); i++) {
                                length_byte += bytesResult.get(i).length;

                            }
                            byte[] all_byte = new byte[length_byte];
                            int countLength = 0;
                            for (int i = 0; i < bytesResult.size(); i++) {
                                byte[] b = bytesResult.get(i);
                                System.arraycopy(b, 0, all_byte, countLength, b.length);
                                countLength += b.length;
                            }
                            return all_byte;
                        }

                        @Override
                        public void onSuccess(@Nullable byte[] result) {
                            if (result != null) {
                                uploadDatas(result);
                            }
                        }
                    });
                    break;
            }
            return false;
        }
    };

    public ECG_BoSheng_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        bytesResult = new ArrayList<>();
        points = new ArrayList<>();
        weakHandler = new WeakHandler(weakRunnable);
        timeCount = new TimeCount(30000, 1000, fragment, weakHandler);
        initNet();
        getNetConfig();

        BleManager.getInstance().init((Application) fragment.getThisContext().getApplicationContext());
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(20 * 1000)//设置扫描超时
                .setDeviceName(true, BorsamConfig.deviceNames)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        searchDevices();
    }

    private void initNet() {
        BorsamHttpUtil.getInstance().initialization(Bluetooth_Constants.BoSheng.BoSheng_APP_ID,
                Bluetooth_Constants.BoSheng.BoSheng_URL, Bluetooth_Constants.BoSheng.BoSheng_PASSWORD,
                new Converter() {
                    @Override
                    public <T> T converter(String json, Class<T> clazz) {
                        Timber.e("初始化博声成功");
                        return new Gson().fromJson(json, clazz);
                    }
                });
        BorsamHttpUtil.getInstance().setLogEnable(false);

    }

    private void getNetConfig() {
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.getConfig())
                .enqueue(new HttpCallback<BorsamResponse<Config>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Config> configBorsamResponse) {
                        if (configBorsamResponse.getCode() == 0) {
                            //这里必须设置
                            PatientApi.config = configBorsamResponse.getEntity();
                            //注册
                            String userPhone = SPManifest.getUserPhone();
                            if (TextUtils.isEmpty(userPhone)){
                                userPhone="18888888888";
                            }
                            registAccount(DataUtils.hideMobilePhone4(userPhone),
                                    Bluetooth_Constants.BoSheng.BoSheng_USER_PASSWORD);
                        } else {
                            Toast.makeText(baseContext, "get config error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {

                    }
                });
    }

    private void registAccount(final String username, final String password) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.register(username, password))
                .enqueue(new HttpCallback<BorsamResponse<RegisterResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<RegisterResult> registerResultBorsamResponse) {
                        if (registerResultBorsamResponse == null) {
                            Timber.e("registerResultBorsamResponse==null");
                        } else {
                            RegisterResult entity = registerResultBorsamResponse.getEntity();
                            if (entity == null) {
                                Timber.e("entity==null");
                                //该账号已经注册过
                                login(username, password);
                            } else {
                                Timber.e("注册成功.注册id" + entity.getId());
                                //注册成功后进行两个操作：1.登录；2：修改个人信息
                                login(username, password);
                                String name;
                                int sex = 0, birth = 0;

                                String userName = SPManifest.getUserName();
                                if (TextUtils.isEmpty(userName)) {
                                    name = "未知";
                                } else {
                                    name = userName;
                                }
                                String userSex = SPManifest.getUserSex();
                                if (TextUtils.isEmpty(userSex)) {
                                    //未知
                                    sex = 0;
                                } else {
                                    if ("男".equals(userSex)) {
                                        sex = 2;
                                    }
                                    if ("女".equals(userSex)) {
                                        sex = 1;
                                    }
                                }
                                String userAge = SPManifest.getUserAge();
                                if (TextUtils.isEmpty(userAge)) {
                                    birth = 0;
                                } else {
                                    try {
                                        int i = Integer.parseInt(userAge);
                                        Calendar instance = Calendar.getInstance();
                                        int i1 = instance.get(Calendar.YEAR);
                                        //生日换算成秒
                                        birth = (int) (TimeUtils.date2Milliseconds(
                                                TimeUtils.string2Date((i1 - i) + "-01-01",
                                                        new SimpleDateFormat("yyyy-MM-dd"))) / 1000);
                                    } catch (NumberFormatException e) {
                                        Timber.e("年龄不是整数");
                                    }
                                }
                                Timber.e("姓名："+name+"年龄："+birth+"性别："+sex);
                                alertPersonInfo(name, "", sex, birth);
                            }
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e("注册失败" + e.getMessage());
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        Timber.e("注册失败：" + responseCode + "--" + responseMessage);
                    }
                });
    }

    //博声上传头像
    private void uploadHead() {
        BorsamHttpUtil.getInstance()
                .add(TAG, PatientApi.uploadFace(baseContext.getResources().openRawResource(R.raw.gcml_head)))
                .enqueue(new HttpCallback<BorsamResponse<Face>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Face> faceBorsamResponse) {
                        Timber.e("上传头像成功" + faceBorsamResponse.getEntity().getFace_url());
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Timber.e("上传头像失败：" + i + "--" + s);
                    }
                });
    }

    //博声登录
    private void login(String username, String password) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }
        BorsamHttpUtil.getInstance()
                .add(TAG, PatientApi.login(username, password)).enqueue(
                new HttpCallback<BorsamResponse<LoginResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<LoginResult> loginResultBorsamResponse) {
                        if (loginResultBorsamResponse.getCode() != 0) {
                            Toast.makeText(baseContext, loginResultBorsamResponse.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            PatientApi.userId = loginResultBorsamResponse.getEntity().getUser().getId();
                            PatientApi.token = loginResultBorsamResponse.getEntity().getToken();
                            Timber.e("登录成功");
                            isLoginBoShengSuccess = true;
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e("登录失败" + e.getMessage());
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        Timber.e("登录失败:" + responseCode + responseMessage);
                    }
                });
    }

    //修改个人信息 （性别 0:未设置 1:女 2:男 3:其他）
    private void alertPersonInfo(String firstName, String sencondName, int sex, int birthday) {
        BorsamHttpUtil.getInstance()
                .add(TAG, PatientApi.modifyPatient(firstName, sencondName, sex, birthday))
                .enqueue(new HttpCallback<BorsamResponse>() {
                    @Override
                    public void onSuccess(BorsamResponse borsamResponse) {
                        Timber.e("修改个人信息成功");
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    @Override
    public void searchDevices() {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.e(TAG, "onScanFinished: ");
            }

            @Override
            public void onScanStarted(boolean success) {
                Log.e(TAG, "onScanStarted: " + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String mac = bleDevice.getMac();
                if (mac.equals(targetAddress)) {
                    BleManager.getInstance().cancelScan();
                    connectDevice(mac);
                }
            }
        });
    }

    @Override
    public void connectDevice(String macAddress) {
        BleManager.getInstance().connect(macAddress, bleGattCallback);
    }

    private final BleGattCallback bleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            Logg.e(ECG_BoSheng_PresenterImp.class, "开始连接");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接失败");
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接成功");
            connectSuccessed(bleDevice);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接中断");
            if (baseView instanceof Activity) {
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
            } else if (baseView instanceof Fragment) {
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
                }
            }
            isMeasureEnd = true;
            timeCount.cancel();
            Logg.e(ECG_BoSheng_PresenterImp.class, Thread.currentThread().getName());
            if (!isDestroy) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connectDevice(device.getMac());
            }
        }
    };

    protected void connectSuccessed(BleDevice address) {
        isMeasureEnd = false;
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_ECG, address.getName() + "," + address.getMac());
        lockedDevice = address;
        BleManager.getInstance().notify(address, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString(), new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Logg.e(ECG_BoSheng_PresenterImp.class, "打开通道成功");
                        timeCount.start();
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Logg.e(ECG_BoSheng_PresenterImp.class, "打开通道失败");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        if (!isMeasureEnd) {

                            if (baseView == null || data == null) {
                                Logg.e(ECG_BoSheng_PresenterImp.class, "baseView=" + baseView + "----datas=" + data);
                            }
                            bytesResult.add(data);
                            baseView.updateData(ByteUtils.byteToString(data));
                        }
                    }
                });
    }


    //上传数据到博声后台
    private void uploadDatas(byte[] stream) {
        if (!isLoginBoShengSuccess) {
            ToastUtils.showShort("分析数据失败");
            return;
        }
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.uploadFile(StreamUtils.bytes2InputStream(stream)))
                .enqueue(new HttpCallback<BorsamResponse<UploadFileResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<UploadFileResult> uploadFileResultBorsamResponse) {
                        String file_no = uploadFileResultBorsamResponse.getEntity().getFile_no();
                        Timber.e("文件编号：" + file_no);
                        addRecord(file_no, (int) (System.currentTimeMillis() / 1000), 1, "测试");
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.showShort("分析数据失败");
                    }
                });
    }
    /**{
     "avgbeats": [{
     "BeatData": [-3, -2, -4, -3, -4, -6, -7, -6, -5, -7, -7, -6, -5, -5, 0, 0, -2, 0, 1, -2, -1, 0, 0, 1, 0, 0, -1, -2, -2, -1, 0, 0, -2, -2, -2, -2, -1, -2, -2, 0, 0, 0, -2, 0, -2, -1, 0, 1, 2, 1, 5, 7, 9, 10, 9, 7, 7, 1, -1, -4, -6, -6, -6, -4, -3, -5, -8, -16, -21, -25, -5, 31, 82, 142, 192, 219, 210, 177, 127, 70, 33, 8, -3, -7, -14, -22, -25, -25, -25, -25, -29, -28, -25, -23, -23, -20, -19, -16, -17, -15, -19, -15, -12, -8, -8, -7, -5, -1, -2, 0, 0, 2, 0, 5, 5, 5, 9, 11, 11, 12, 13, 17, 20, 19, 20, 20, 18, 16, 16, 10, 5, 1, 0, -3, -3, -1, -6, -6, -6, -8, -8, -7, -5, -6, -2, -3, -5, -5, -6, -4, -5, -7, -7, -4, -2, -2, -4, -2, 0, 0, -2, -2, -3, -2, -1, -2, -2, -4, -4, -4, -7, -6, -5, -5, -4, -3, -3, -2, -1, 1, 2, 3, 1, 1, -1, 0, -3, -3, -6, -4, -3, -4, -3, -4, -4, -3, 0, 1, 0, 0],
     "HR": 69,
     "Joff": 84,
     "PR": 125,
     "Poff": 55,
     "QRS": 90,
     "QT": 314,
     "QTC": 337,
     "Qoff": 66,
     "RR": 870,
     "Roff": 80,
     "ST": 125,
     "TEoff": 129,
     "TPoff": 0,
     "hz": 200
     }],
     "findings": "1.心率:69 bpm, 正常范围.\n2.心律失常(也可能是由于信号干扰导致误判).\n3.QRS宽度正常.\n4.QTC正常.\n(以上诊断为自动分析结果,请联系医生)\n",
     "qrs": [{
     "RR": 97,
     "flag": 1,
     "offset": 65
     }, {
     "RR": 164,
     "flag": 1,
     "offset": 229
     }, {
     "RR": 166,
     "flag": 1,
     "offset": 395
     }, {
     "RR": 145,
     "flag": 1,
     "offset": 540
     }, {
     "RR": 168,
     "flag": 1,
     "offset": 708
     }, {
     "RR": 182,
     "flag": 1,
     "offset": 890
     }, {
     "RR": 173,
     "flag": 1,
     "offset": 1063
     }, {
     "RR": 188,
     "flag": 1,
     "offset": 1251
     }, {
     "RR": 166,
     "flag": 1,
     "offset": 1417
     }, {
     "RR": 148,
     "flag": 1,
     "offset": 1565
     }, {
     "RR": 164,
     "flag": 1,
     "offset": 1729
     }, {
     "RR": 174,
     "flag": 1,
     "offset": 1903
     }, {
     "RR": 189,
     "flag": 1,
     "offset": 2092
     }, {
     "RR": 179,
     "flag": 1,
     "offset": 2271
     }, {
     "RR": 188,
     "flag": 1,
     "offset": 2459
     }, {
     "RR": 160,
     "flag": 1,
     "offset": 2619
     }, {
     "RR": 160,
     "flag": 1,
     "offset": 2779
     }, {
     "RR": 196,
     "flag": 1,
     "offset": 2975
     }, {
     "RR": 188,
     "flag": 1,
     "offset": 3163
     }, {
     "RR": 166,
     "flag": 1,
     "offset": 3329
     }, {
     "RR": 160,
     "flag": 1,
     "offset": 3489
     }, {
     "RR": 181,
     "flag": 1,
     "offset": 3670
     }, {
     "RR": 192,
     "flag": 1,
     "offset": 3862
     }, {
     "RR": 187,
     "flag": 1,
     "offset": 4049
     }, {
     "RR": 181,
     "flag": 1,
     "offset": 4230
     }, {
     "RR": 162,
     "flag": 1,
     "offset": 4392
     }, {
     "RR": 171,
     "flag": 1,
     "offset": 4563
     }, {
     "RR": 188,
     "flag": 1,
     "offset": 4751
     }, {
     "RR": 190,
     "flag": 1,
     "offset": 4941
     }, {
     "RR": 179,
     "flag": 1,
     "offset": 5120
     }, {
     "RR": 155,
     "flag": 1,
     "offset": 5275
     }, {
     "RR": 161,
     "flag": 1,
     "offset": 5436
     }, {
     "RR": 182,
     "flag": 1,
     "offset": 5618
     }, {
     "RR": 180,
     "flag": 1,
     "offset": 5798
     }],
     "stop_light": 1
     }*/
    //增加记录  文件编号、测量时间，设备类型（1.单导，2.双导）、当前健康状况
    private void addRecord(final String fileNo, int testTime, int type, String condition) {
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.addRecord(fileNo, testTime, type, condition))
                .enqueue(new HttpCallback<BorsamResponse<AddRecordResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<AddRecordResult> addRecordResultBorsamResponse) {
                        AddRecordResult entity = addRecordResultBorsamResponse.getEntity();
                        Timber.e("分析数据：" + entity.getExt());

                        LoadingProgressUtils.dismissView();
                        baseView.updateData(fileNo, entity.getFile_url(), entity.getFile_report());
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        baseView.updateData(fileNo, null, null);
                    }
                });

    }


    static class TimeCount extends CountDownTimer {
        private IView fragment;
        private WeakHandler weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, IView fragment, WeakHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
            this.weakHandler = weakHandler;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMeasureEnd = true;
            fragment.updateData("tip", "测量结束");
            weakHandler.sendEmptyMessage(MESSAGE_DEAL_BYTERESULT);
        }


        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            Logg.e(ECG_BoSheng_PresenterImp.class, millisUntilFinished / 1000 + "");
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    public void onDestroy() {
        if (lockedDevice != null) {
            BleManager.getInstance().stopNotify(lockedDevice, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                    BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString());
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        isMeasureEnd = false;
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
        }
        super.onDestroy();
    }
}
