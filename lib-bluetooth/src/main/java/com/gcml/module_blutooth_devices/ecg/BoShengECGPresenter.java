package com.gcml.module_blutooth_devices.ecg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothGatt;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.borsam.ble.BorsamConfig;
import com.borsam.borsamnetwork.bean.AddRecordResult;
import com.borsam.borsamnetwork.bean.BorsamResponse;
import com.borsam.borsamnetwork.bean.Config;
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
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.data.StreamUtils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.common.utils.thread.ThreadUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class BoShengECGPresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;

    private BleDevice lockedDevice;
    private List<Integer> points;
    private TimeCount timeCount;
    private boolean isMeasureEnd = false;
    private List<byte[]> bytesResult;
    private WeakHandler weakHandler;
    private static final int MESSAGE_DEAL_BYTERESULT = 1;
    private LoadingDialog mLoadingDialog;
    private boolean isLoginBoShengSuccess = false;
    private String phone;
    private String birth;
    private String sex;
    private static final String TAG = "BoShengECGPresenter";
    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DEAL_BYTERESULT:
                    Log.e(TAG, "handleMessage: " + bytesResult.size());
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
                                if (result.length == 0) {
                                    baseView.updateState("采集数据失败，请先清洁仪器，再次测量。");
                                    if (mLoadingDialog != null) {
                                        mLoadingDialog.dismiss();
                                    }
                                    return;
                                }
                                uploadDatas(result);
                            }
                        }
                    });
                    break;
            }
            return false;
        }
    };
    private Disposable disposable;

    @SuppressLint("RestrictedApi")
    public BoShengECGPresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);

        initParam();
        initNet();
        getUser();
        connect();

    }

    private void connect() {
        BleManager.getInstance().connect(address, bleGattCallback);
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
            isMeasureEnd = false;
            lockedDevice = bleDevice;
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_ECG, name + "," + address);

            BleManager.getInstance().notify(bleDevice, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                    BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString(), new BleNotifyCallback() {
                        @Override
                        public void onNotifySuccess() {
                            timeCount.start();
                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                            ToastUtils.showShort("数据传输出现异常");
                            timeCount.cancel();
                        }

                        @Override
                        public void onCharacteristicChanged(byte[] data) {
                            if (!isMeasureEnd) {
                                bytesResult.add(data);
                                baseView.updateData(ByteUtils.byteToString(data));
                            }
                        }
                    });
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            if (baseView instanceof Activity) {
                baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_disconnected));
            } else if (baseView instanceof Fragment) {
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_disconnected));
                }
            }
            isMeasureEnd = true;
            timeCount.cancel();

            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, 3000);
        }
    };

    private void initNet() {
        BorsamHttpUtil.getInstance().initialization(BluetoothConstants.BoSheng.BoSheng_APP_ID,
                BluetoothConstants.BoSheng.BoSheng_URL, BluetoothConstants.BoSheng.BoSheng_PASSWORD,
                new Converter() {
                    @Override
                    public <T> T converter(String json, Class<T> clazz) {
                        return new Gson().fromJson(json, clazz);
                    }
                });
        BorsamHttpUtil.getInstance().setLogEnable(false);

    }

    private void initParam() {
        bytesResult = new ArrayList<>();
        points = new ArrayList<>();
        weakHandler = new WeakHandler(weakRunnable);
        timeCount = new TimeCount(30000, 1000, baseView, weakHandler);

        BleManager.getInstance().init(activity.getApplication());
    }

    private void getUser() {

        CCResult result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
        Observable<UserEntity> rxUser = result.getDataItem("data");
        rxUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity != null) {
                            phone = userEntity.phone;
                            birth = userEntity.birthday;
                            sex = userEntity.sex;

                            if (TextUtils.isEmpty(birth) || TextUtils.isEmpty(userEntity.name) || TextUtils.isEmpty(sex)) {
                                ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                                return;
                            }
                            getNetConfig(phone, birth, userEntity.name, sex);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getNetConfig(final String phone, final String birth, final String name, final String sex) {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.getConfig())
                .enqueue(new HttpCallback<BorsamResponse<Config>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Config> configBorsamResponse) {
                        if (configBorsamResponse.getCode() == 0) {
                            //这里必须设置
                            PatientApi.config = configBorsamResponse.getEntity();
                            //注册
                            registAccount(DataUtils.hideMobilePhone4(phone),
                                    BluetoothConstants.BoSheng.BoSheng_USER_PASSWORD, birth, name, sex);
                        } else {
                            ToastUtils.showShort("get config error");
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

    private void registAccount(final String username, final String password, final String birth, final String name, final String sex) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }

        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.register(username, password))
                .enqueue(new HttpCallback<BorsamResponse<RegisterResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<RegisterResult> registerResultBorsamResponse) {
                        if (registerResultBorsamResponse == null) {
                        } else {
                            RegisterResult entity = registerResultBorsamResponse.getEntity();
                            if (entity == null) {
                                //该账号已经注册过
                                login(username, password);
                            } else {
                                //注册成功后进行两个操作：1.登录；2：修改个人信息
                                login(username, password);
                                int birthday = (int) (TimeUtils.string2Milliseconds(birth, new SimpleDateFormat("yyyyMMdd")) / 1000);
                                int sexInt = 0;
                                if (sex.equals("男")) {
                                    sexInt = 2;
                                } else if (sex.equals("女")) {
                                    sexInt = 1;
                                }
                                alertPersonInfo(name, "", sexInt, birthday);
                            }
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                    }
                });
    }

    //博声登录
    private void login(String username, String password) {
        if (DataUtils.isNullString(username) || DataUtils.isNullString(password)) {
            return;
        }
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.login(username, password)).enqueue(
                new HttpCallback<BorsamResponse<LoginResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<LoginResult> loginResultBorsamResponse) {
                        if (loginResultBorsamResponse.getCode() != 0) {
                            ToastUtils.showShort(loginResultBorsamResponse.getMsg());
                        } else {
                            PatientApi.userId = loginResultBorsamResponse.getEntity().getUser().getId();
                            PatientApi.token = loginResultBorsamResponse.getEntity().getToken();
                            isLoginBoShengSuccess = true;
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                    }
                });
    }

    //修改个人信息 （性别 0:未设置 1:女 2:男 3:其他）
    private void alertPersonInfo(String firstName, String sencondName, int sex, int birthday) {
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.modifyPatient(firstName, sencondName, sex, birthday))
                .enqueue(new HttpCallback<BorsamResponse>() {
                    @Override
                    public void onSuccess(BorsamResponse borsamResponse) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                });

    }

    //上传数据到博声后台
    private void uploadDatas(byte[] stream) {
        if (!isLoginBoShengSuccess) {
            ToastUtils.showShort("分析数据失败");
            return;
        }
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.uploadFile(StreamUtils.bytes2InputStream(stream)))
                .enqueue(new HttpCallback<BorsamResponse<UploadFileResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<UploadFileResult> uploadFileResultBorsamResponse) {
                        try {
                            String file_no = uploadFileResultBorsamResponse.getEntity().getFile_no();
                            addRecord(file_no, (int) (System.currentTimeMillis() / 1000), 1, "");
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        ToastUtils.showShort("分析数据失败");
                    }
                });
    }

    //增加记录  文件编号、测量时间，设备类型（1.单导，2.双导）、当前健康状况
    private void addRecord(final String fileNo, int testTime, int type, String condition) {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.addRecord(fileNo, testTime, type, condition))
                .enqueue(new HttpCallback<BorsamResponse<AddRecordResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<AddRecordResult> addRecordResultBorsamResponse) {
                        AddRecordResult entity = addRecordResultBorsamResponse.getEntity();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        BoShengResultBean boShengResultBean = new Gson().fromJson(entity.getExt(), BoShengResultBean.class);
                        baseView.updateData(fileNo, entity.getFile_report(), boShengResultBean.getStop_light() + "", boShengResultBean.getFindings(), boShengResultBean.getAvgbeats().get(0).getHR() + "");
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


    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
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
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = null;

        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }

    class TimeCount extends CountDownTimer {
        private IBluetoothView fragment;
        private WeakHandler weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, IBluetoothView fragment, WeakHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
            this.weakHandler = weakHandler;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMeasureEnd = true;
            fragment.updateData("tip", "测量结束");
            if (activity != null) {
                mLoadingDialog = new LoadingDialog.Builder(activity)
                        .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在分析数据...")
                        .create();
                if (mLoadingDialog != null) {
                    mLoadingDialog.show();
                }
                weakHandler.sendEmptyMessage(MESSAGE_DEAL_BYTERESULT);
            }
        }


        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }
}
