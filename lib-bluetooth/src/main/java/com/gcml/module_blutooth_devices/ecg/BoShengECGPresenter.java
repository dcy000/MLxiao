package com.gcml.module_blutooth_devices.ecg;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.View;

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
import com.gcml.common.constant.EUserInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.CheckUserInfoProviderImp;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.data.StreamUtils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.common.utils.thread.ThreadUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.sjtu.yifei.route.Routerfit;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class BoShengECGPresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;

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
    private String userName;
    private static final String TAG = "BoShengECGPresenter";
    private int lgoinTimes;
    private boolean isDestroyed;
    DetectionData detectionData = new DetectionData();
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
                                if (!UserSpHelper.isNoNetwork()) {
                                    uploadDatas(result);
                                } else {
                                    if (mLoadingDialog != null) {
                                        mLoadingDialog.dismiss();
                                    }
                                    ToastUtils.showLong("无网模式下，博声无检测报告，建议在有网模式下测量博声心电!");
                                }
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
    public BoShengECGPresenter(SupportActivity activity, IBluetoothView baseView, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.activity.getLifecycle().addObserver(this);
        initParam();
        handleBosheng(address);
    }

    private void handleBosheng(String address) {
        Timber.w("bt ---> startNotify: address = %s", address);
        BluetoothStore.getClient().notify(address, BorsamConfig.COMMON_RECEIVE_ECG_SUUID,
                BorsamConfig.COMMON_RECEIVE_ECG_CUUID, new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        if (!isMeasureEnd) {
                            bytesResult.add(value);
                            detectionData.setInit(false);
                            detectionData.setEcgData(value);
                            baseView.updateData(detectionData);
                            BluetoothStore.instance.detection.postValue(detectionData);
                        }
                    }

                    @Override
                    public void onResponse(int code) {
                        if (code == 0) {
                            if (timeCount != null) {
                                timeCount.start();
                            }
                            //连接成功之后再登录博声后台
                            getUser();
                        } else {
                            ToastUtils.showLong("检测不到您的心跳。请按如下操作：1.清洁仪器；2.按照仪器上的指示图操作；3.手指用力贴紧仪器");
                            if (timeCount != null) {
                                timeCount.cancel();
                            }
                        }
                    }
                });
    }

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
        weakHandler = new WeakHandler(weakRunnable);
        timeCount = new TimeCount(30000, 1000, baseView, weakHandler);
    }

    private void getUser() {
        Routerfit.register(AppRouter.class)
                .getCheckUserInfoProvider()
                .check(new CheckUserInfoProviderImp.CheckUserInfo() {
                    @Override
                    public void complete(UserEntity userEntity) {
                        phone = userEntity.phone;
                        birth = userEntity.birthday;
                        sex = userEntity.sex;
                        userName = userEntity.name;
                        initNet();
                        getNetConfig();
                    }

                    @Override
                    public void incomplete(UserEntity entity, List<EUserInfo> args, String s) {
                        showNotMsgDiaglog(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }
                }, EUserInfo.NAME, EUserInfo.PHONE, EUserInfo.BIRTHDAY, EUserInfo.GENDER);
    }

    private void showNotMsgDiaglog(String s) {
        if (!isDestroyed) {
            BaseNiceDialog dialog = NiceDialog.init()
                    .setLayoutId(R.layout.dialog_not_person_msg)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                            holder.setText(R.id.txt_msg, "心电测量必须先完善" + s + "等信息，方便我们为您生成心电报告。");
                            holder.setOnClickListener(R.id.btn_neg, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    activity.finish();
                                }
                            });
                            holder.setOnClickListener(R.id.btn_pos, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Routerfit.register(AppRouter.class).skipUserInfoActivity();
                                }
                            });
                        }
                    })
                    .setWidth(700)
                    .setHeight(350);
            if (baseView instanceof Fragment) {
                dialog.show(((Fragment) baseView).getFragmentManager());
            } else if (baseView instanceof FragmentActivity) {
                dialog.show(((FragmentActivity) baseView).getSupportFragmentManager());
            }
        }
    }

    private void getNetConfig() {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.getConfig())
                .enqueue(new HttpCallback<BorsamResponse<Config>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Config> configBorsamResponse) {
                        if (configBorsamResponse.getCode() == 0) {
                            //这里必须设置
                            PatientApi.config = configBorsamResponse.getEntity();
                            //注册
                            registAccount(DataUtils.hideMobilePhone4(phone), BluetoothConstants.BoSheng.BoSheng_USER_PASSWORD);
                        } else {
                            ToastUtils.showShort("get config error");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        onErrorMsg();
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        onErrorMsg();
                    }
                });
    }

    private void registAccount(final String phone, final String password) {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.register(phone, password))
                .enqueue(new HttpCallback<BorsamResponse<RegisterResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<RegisterResult> registerResultBorsamResponse) {
                        if (registerResultBorsamResponse != null) {
                            login(phone, password);
                        } else {
                            if (baseView != null)
                                baseView.updateState("心电后台发生异常");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        onErrorMsg();
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        onErrorMsg();
                    }
                });
    }

    //博声登录
    private void login(String phone, String password) {
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.login(phone, password)).enqueue(
                new HttpCallback<BorsamResponse<LoginResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<LoginResult> loginResultBorsamResponse) {
                        if (loginResultBorsamResponse.getCode() != 0) {
                            ToastUtils.showShort(loginResultBorsamResponse.getMsg());
                        } else {
                            PatientApi.userId = loginResultBorsamResponse.getEntity().getUser().getId();
                            PatientApi.token = loginResultBorsamResponse.getEntity().getToken();
                            isLoginBoShengSuccess = true;
                            alterPersonInfo();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        //尝试调用3次登录接口，如果还是不能登录则打印错误信息
                        if (lgoinTimes < 3) {
                            lgoinTimes++;
                            login(phone, password);
                        } else {
                            onErrorMsg();
                        }
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                        //尝试调用3次登录接口，如果还是不能登录则打印错误信息
                        if (lgoinTimes < 3) {
                            lgoinTimes++;
                            login(phone, password);
                        } else {
                            onErrorMsg();
                        }
                    }
                });
    }

    //修改个人信息 （性别 0:未设置 1:女 2:男 3:其他）
    private void alterPersonInfo() {
        //我们系统中的年龄大于真实年龄1岁
        int birthday = (int) (TimeUtils.string2Milliseconds(String.valueOf(Integer.parseInt(birth) + 10000), new SimpleDateFormat("yyyyMMdd")) / 1000);
        int sexInt = 0;
        if (sex.equals("男")) {
            sexInt = 2;
        } else if (sex.equals("女")) {
            sexInt = 1;
        }
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.modifyPatient(userName, "", sexInt, birthday))
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
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        ToastUtils.showShort("分析数据失败");
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
                        Timber.w(addRecordResultBorsamResponse.toString());
                        AddRecordResult entity = addRecordResultBorsamResponse.getEntity();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        if (entity == null) {
                            ToastUtils.showShort("分析异常，请重新测量");
                            return;
                        }
                        BoShengResultBean boShengResultBean = new Gson().fromJson(entity.getExt(), BoShengResultBean.class);
                        if (boShengResultBean == null) {
                            ToastUtils.showShort("分析异常，请重新测量");
                            return;
                        }
                        List<BoShengResultBean.AvgbeatsBean> avgbeats = boShengResultBean.getAvgbeats();
                        if (avgbeats == null || avgbeats.size() == 0) {
                            ToastUtils.showShort("分析异常，请重新测量");
                            return;
                        }
                        BoShengResultBean.AvgbeatsBean avgbeatsBean = avgbeats.get(0);
                        if (avgbeatsBean == null) {
                            ToastUtils.showShort("分析异常，请重新测量");
                            return;
                        }
                        detectionData.setInit(false);
                        detectionData.setEcgData(null);
                        detectionData.setResultUrl(entity.getFile_report());
                        detectionData.setEcgFlag(boShengResultBean.getStop_light());
                        detectionData.setResult(boShengResultBean.getFindings());
                        detectionData.setHeartRate(avgbeatsBean.getHR());
                        baseView.updateData(detectionData);
                        BluetoothStore.instance.detection.postValue(detectionData);
                    }

                    @Override
                    public void onError(Exception e) {
                        ToastUtils.showShort("分析异常，请重新测量");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.showShort("分析异常，请重新测量");
                    }
                });

    }


    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        timeCount = null;
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
        private WeakReference<IBluetoothView> fragment;
        private WeakReference<WeakHandler> weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, IBluetoothView fragment, WeakHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = new WeakReference<>(fragment);
            this.weakHandler = new WeakReference<>(weakHandler);
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMeasureEnd = true;
            if (fragment.get() != null) {
                detectionData.setInit(false);
                detectionData.setEcgData(null);
                detectionData.setEcgTips("测量结束");
                fragment.get().updateData(detectionData);
            }

            if (activity != null) {
                mLoadingDialog = new LoadingDialog.Builder(activity)
                        .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在分析数据...")
                        .create();
                if (mLoadingDialog != null) {
                    mLoadingDialog.show();
                }
                if (weakHandler.get() != null) {
                    weakHandler.get().sendEmptyMessage(MESSAGE_DEAL_BYTERESULT);
                }
            }
        }


        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (fragment.get() != null) {
                detectionData.setInit(false);
                detectionData.setEcgData(null);
                detectionData.setEcgTips("距离测量结束还有" + millisUntilFinished / 1000 + "s");
                fragment.get().updateData(detectionData);
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        isDestroyed = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        isDestroyed = false;
    }

    private void onErrorMsg() {
        if (baseView != null)
            baseView.updateState("心电后台发生异常");
    }
}
