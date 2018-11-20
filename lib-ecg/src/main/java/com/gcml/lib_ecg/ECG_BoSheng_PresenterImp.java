package com.gcml.lib_ecg;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import com.borsam.borsamnetwork.util.PatientApi;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.gcml.lib_ecg.base.BorsamConfig;
import com.gcml.lib_ecg.base.IView;
import com.gcml.lib_ecg.base.UserInfoBean;
import com.gcml.lib_ecg.ecg.Bluetooth_Constants;
import com.google.gson.Gson;
import com.gzq.lib_core.utils.ByteUtils;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.SPUtil;
import com.gzq.lib_core.utils.StreamUtils;
import com.gzq.lib_core.utils.ThreadUtils;
import com.gzq.lib_core.utils.TimeUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_core.utils.WeakHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 博声心电仪
 * mac:50:65:83:8C:2C:A1
 * name:WeCardio STD
 */
@SuppressLint("LongLogTag")
public class ECG_BoSheng_PresenterImp {
    private static final String TAG = "ECG_BoSheng_PresenterImp";
    private final IView fragment;
    private BleDevice lockedDevice;
    private List<Integer> points;
    private TimeCount timeCount;
    private static boolean isMeasureEnd = false;
    private List<byte[]> bytesResult;
    private WeakHandler weakHandler;
    private static final int MESSAGE_DEAL_BYTERESULT = 1;
    //    private LoadingDialog mLoadingDialog;
    private boolean isLoginBoShengSuccess = false;
    private Context context;

    private boolean isDestroy;
    private String macAddress;


    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DEAL_BYTERESULT:
//                    mLoadingDialog = new LoadingDialog.Builder(baseContext)
//                            .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
//                            .setTipWord("正在分析数据...")
//                            .create();
//                    if (mLoadingDialog != null) {
//                        mLoadingDialog.show();
//                    }
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


    public ECG_BoSheng_PresenterImp(IView fragment, String macAddress, UserInfoBean user) {
        this.fragment = fragment;
        this.macAddress = macAddress;
        context = fragment.getThisContext();
        bytesResult = new ArrayList<>();
        points = new ArrayList<>();
        weakHandler = new WeakHandler(weakRunnable);
        timeCount = new TimeCount(30000, 1000, fragment, weakHandler);
        initNet();

        getUser(user);
        BleManager.getInstance().init((Application) fragment.getThisContext().getApplicationContext());
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(20 * 1000)//设置扫描超时
                .setDeviceName(true, BorsamConfig.deviceNames)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);

        if (!TextUtils.isEmpty(this.macAddress)) {
            connectDevice(macAddress);
        } else {
            searchDevices();
        }
    }

    private void getUser(UserInfoBean user) {
        getNetConfig(user.getPhone(), user.getBirth(), user.getName(), user.getSex());
    }

    private void initNet() {
        BorsamHttpUtil.getInstance().initialization(Bluetooth_Constants.BoSheng.BoSheng_APP_ID,
                Bluetooth_Constants.BoSheng.BoSheng_URL, Bluetooth_Constants.BoSheng.BoSheng_PASSWORD,
                new Converter() {
                    @Override
                    public <T> T converter(String json, Class<T> clazz) {
                        return new Gson().fromJson(json, clazz);
                    }
                });
        BorsamHttpUtil.getInstance().setLogEnable(false);

    }

    private void getNetConfig(final String phone, final String birth, final String name, final String sex) {
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.getConfig())
                .enqueue(new HttpCallback<BorsamResponse<Config>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Config> configBorsamResponse) {
                        if (configBorsamResponse.getCode() == 0) {
                            //这里必须设置
                            PatientApi.config = configBorsamResponse.getEntity();
                            //注册
                            registAccount(DataUtils.hideMobilePhone4(phone),
                                    Bluetooth_Constants.BoSheng.BoSheng_USER_PASSWORD, birth, name, sex);
                        } else {
                            Toast.makeText(context, "get config error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        Log.e(TAG, "onFailure: " + responseMessage);
                    }
                });
    }

    private void registAccount(final String username, final String password, final String birth, final String name, final String sex) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }

        BorsamHttpUtil.getInstance().add(TAG, PatientApi.register(username, password))
                .enqueue(new HttpCallback<BorsamResponse<RegisterResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<RegisterResult> registerResultBorsamResponse) {
                        if (registerResultBorsamResponse == null) {
                        } else {
                            RegisterResult entity = registerResultBorsamResponse.getEntity();
                            login(username, password,name,sex,birth);
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        Log.e(TAG, "onFailure: " + responseMessage);
                    }
                });
    }

    //博声上传头像
    private void uploadHead() {
        BorsamHttpUtil.getInstance()
                .add(TAG, PatientApi.uploadFace(context.getResources().openRawResource(R.raw.gcml_head)))
                .enqueue(new HttpCallback<BorsamResponse<Face>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Face> faceBorsamResponse) {
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                });
    }

    //博声登录
    private void login(String username, String password, final String name, final String sex, final String birth) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }
        BorsamHttpUtil.getInstance()
                .add(TAG, PatientApi.login(username, password)).enqueue(
                new HttpCallback<BorsamResponse<LoginResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<LoginResult> loginResultBorsamResponse) {
                        if (loginResultBorsamResponse.getCode() != 0) {
                            Toast.makeText(context, loginResultBorsamResponse.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            PatientApi.userId = loginResultBorsamResponse.getEntity().getUser().getId();
                            PatientApi.token = loginResultBorsamResponse.getEntity().getToken();
                            isLoginBoShengSuccess = true;
                        }

                        int birthday = (int) (TimeUtils.string2Milliseconds(birth, new SimpleDateFormat("yyyyMMdd")) / 1000);
                        int sexInt = 0;
                        if (sex.equals("男")) {
                            sexInt = 2;
                        } else if (sex.equals("女")) {
                            sexInt = 1;
                        }
                        alertPersonInfo(name, "", sexInt, birthday);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: " + e.getMessage() + ":" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        Log.e(TAG, "onFailure: " + responseMessage);
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

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.e(TAG, "onFailure: " + s);
                    }
                });

    }

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
                String name = bleDevice.getName();

                if (!TextUtils.isEmpty(name) && name.contains("WeCardio")) {
                    BleManager.getInstance().cancelScan();
                    connectDevice(bleDevice.getMac());
                }
            }
        });
    }

    public void connectDevice(String macAddress) {
        BleManager.getInstance().connect(macAddress, bleGattCallback);
    }

    private final BleGattCallback bleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {

        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            connectSuccessed(bleDevice);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            fragment.updateState(context.getString(R.string.bluetooth_device_disconnected));
            isMeasureEnd = true;
            timeCount.cancel();
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
        fragment.updateState(context.getString(R.string.bluetooth_device_connected));
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_ECG, address.getName() + "," + address.getMac());
        lockedDevice = address;
        BleManager.getInstance().notify(address, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString(), new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        timeCount.start();
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        if (!isMeasureEnd) {
                            bytesResult.add(data);
                            fragment.updateData(ByteUtils.byteToString(data));
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
                        Log.i(TAG, "onSuccess: " + uploadFileResultBorsamResponse.toString());
                        String file_no = uploadFileResultBorsamResponse.getEntity().getFile_no();
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

    //增加记录  文件编号、测量时间，设备类型（1.单导，2.双导）、当前健康状况
    private void addRecord(final String fileNo, int testTime, int type, String condition) {
        BorsamHttpUtil.getInstance().add(TAG, PatientApi.addRecord(fileNo, testTime, type, condition))
                .enqueue(new HttpCallback<BorsamResponse<AddRecordResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<AddRecordResult> addRecordResultBorsamResponse) {
                        AddRecordResult entity = addRecordResultBorsamResponse.getEntity();
                        Log.i(TAG, "onSuccess: 分析数据" + entity.getExt() + "\n----Report:"
                                + entity.getFile_report() + "\n-----Url:" + entity.getFile_url());
                        fragment.updateData(fileNo, entity.getExt(), entity.getFile_report());
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        fragment.updateData(fileNo, null, null);
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
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }

    public void onDestroy() {
        isDestroy = true;
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
    }
}
