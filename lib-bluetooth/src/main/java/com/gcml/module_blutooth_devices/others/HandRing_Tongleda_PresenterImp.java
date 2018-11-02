package com.gcml.module_blutooth_devices.others;

import android.annotation.SuppressLint;
import android.util.Log;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.yc.pedometer.info.BPVOneDayInfo;
import com.yc.pedometer.info.StepOneDayAllInfo;
import com.yc.pedometer.sdk.BLEServiceOperate;
import com.yc.pedometer.sdk.BloodPressureChangeListener;
import com.yc.pedometer.sdk.BluetoothLeService;
import com.yc.pedometer.sdk.DataProcessing;
import com.yc.pedometer.sdk.ICallback;
import com.yc.pedometer.sdk.ICallbackStatus;
import com.yc.pedometer.sdk.RateChangeListener;
import com.yc.pedometer.sdk.ServiceStatusCallback;
import com.yc.pedometer.sdk.SleepChangeListener;
import com.yc.pedometer.sdk.StepChangeListener;
import com.yc.pedometer.sdk.UTESQLOperate;
import com.yc.pedometer.sdk.WriteCommandToBLE;
import com.yc.pedometer.utils.CalendarUtils;
import com.yc.pedometer.utils.GlobalVariable;

import java.util.List;
import java.util.logging.Handler;

/**
 * 同乐达手环
 * name:RB09_Heart
 * mac:78:02:B7:27:8A:74
 */
@SuppressLint("LongLogTag")
public class HandRing_Tongleda_PresenterImp extends BaseBluetoothPresenter
        implements StepChangeListener, BloodPressureChangeListener,
        RateChangeListener, SleepChangeListener,
        ServiceStatusCallback, ICallback {
    private static final String TAG = "HandRing_Tongleda_PresenterImp";
    private final BLEServiceOperate mBLEServiceOperate;
    private final DataProcessing mDataProcessing;
    private static WriteCommandToBLE mWriteCommand;
    private final UTESQLOperate mySQLOperate;
    private BluetoothLeService mBluetoothLeService;

    public HandRing_Tongleda_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        // 用于BluetoothLeService实例化准备,必须
        mBLEServiceOperate = BLEServiceOperate.getInstance(baseContext);
        mWriteCommand = WriteCommandToBLE.getInstance(baseContext);
        mDataProcessing = DataProcessing.getInstance(baseContext);
        mySQLOperate = UTESQLOperate.getInstance(baseContext);
        initListenter();
    }

    private void initListenter() {
        mBLEServiceOperate.setServiceStatusCallback(this);
        mDataProcessing.setOnStepChangeListener(this);
        mDataProcessing.setOnBloodPressureListener(this);
        mDataProcessing.setOnRateListener(this);
        mDataProcessing.setOnSleepChangeListener(this);
    }


    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    /**
     * 记步
     *
     * @param stepOneDayAllInfo
     */
    @Override
    public void onStepChange(StepOneDayAllInfo stepOneDayAllInfo) {
        Log.e(TAG, "onStepChange: " + stepOneDayAllInfo.toString());
    }

    /**
     * 血压
     *
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onBloodPressureChange(int i, int i1, int i2) {
        if (i2 == GlobalVariable.BLOOD_PRESSURE_TEST_FINISH) {
            List<BPVOneDayInfo> bloodPressureOneDayInfo = mySQLOperate.queryBloodPressureOneDayInfo(CalendarUtils.getCalendar(0));
            if (bloodPressureOneDayInfo != null && bloodPressureOneDayInfo.size() > 0) {
                int hightBloodPressure = bloodPressureOneDayInfo.get(bloodPressureOneDayInfo.size() - 1).getHightBloodPressure();
                int lowBloodPressure = bloodPressureOneDayInfo.get(bloodPressureOneDayInfo.size() - 1).getLowBloodPressure();
                ToastUtils.showShort("高压：" + hightBloodPressure + ",低压：" + lowBloodPressure);

            }
        }
    }

    /**
     * 心率
     *
     * @param i
     * @param i1
     */
    @Override
    public void onRateChange(int i, int i1) {

    }

    /**
     * 睡眠
     */
    @Override
    public void onSleepChange() {

    }

    @Override
    public void OnServiceStatuslt(int status) {
        Log.e(TAG, "OnServiceStatuslt: " + status);
        if (status == ICallbackStatus.BLE_SERVICE_START_OK) {
            if (mBluetoothLeService == null) {
                Log.e(TAG, "OnServiceStatuslt:mBluetoothLeService ");
                mBluetoothLeService = mBLEServiceOperate.getBleService();
                mBluetoothLeService.setICallback(this);
                //连接设备
                mBluetoothLeService.connect(targetAddress);
            }
        }
    }

    @Override
    public void OnResult(boolean result, int status) {
        Log.d(TAG, "result=" + result + ",status=" + status);

        switch (status) {
            case ICallbackStatus.OFFLINE_STEP_SYNC_OK:
                Log.d(TAG, "OnResult: 同步步数成功");
                break;
            case ICallbackStatus.OFFLINE_SLEEP_SYNC_OK:
                Log.d(TAG, "OnResult: 同步睡眠成功");
                break;
            case ICallbackStatus.SYNC_TIME_OK:
                // after set time
                Log.d(TAG, "OnResult: 同步时间成功");

                Commond.synStep();
                break;
            case ICallbackStatus.GET_BLE_VERSION_OK:
                // after read
                Log.d(TAG, "OnResult: 获取蓝牙版本成功");
                break;
            case ICallbackStatus.DISCONNECT_STATUS:
                Log.d(TAG, "OnResult: 蓝牙断开连接");
                break;
            case ICallbackStatus.CONNECTED_STATUS:
                Log.d(TAG, "OnResult: 蓝牙连接成功:" + Thread.currentThread().getName());
                break;
            case ICallbackStatus.DISCOVERY_DEVICE_SHAKE:
                Log.d(TAG, "摇一摇拍照");
                break;
            case ICallbackStatus.OFFLINE_RATE_SYNC_OK:
                Log.d(TAG, "OnResult: 心率同步成功");
                break;
            case ICallbackStatus.SET_METRICE_OK:
                // 设置公制单位成功
                break;
            case ICallbackStatus.SET_INCH_OK:
                //// 设置英制单位成功
                break;
            case ICallbackStatus.SET_FIRST_ALARM_CLOCK_OK:
                // 设置第1个闹钟OK
                break;
            case ICallbackStatus.SET_SECOND_ALARM_CLOCK_OK:
                //设置第2个闹钟OK
                break;
            case ICallbackStatus.SET_THIRD_ALARM_CLOCK_OK:
                // 设置第3个闹钟OK
                break;
            case ICallbackStatus.SEND_PHONE_NAME_NUMBER_OK:
                mWriteCommand.sendQQWeChatVibrationCommand(5);
                break;
            case ICallbackStatus.SEND_QQ_WHAT_SMS_CONTENT_OK:
                mWriteCommand.sendQQWeChatVibrationCommand(1);
                break;
            case ICallbackStatus.PASSWORD_SET:
                Log.d(TAG, "没设置过密码，请设置4位数字密码");
                break;
            case ICallbackStatus.PASSWORD_INPUT:
                Log.d(TAG, "已设置过密码，请输入已设置的4位数字密码");
                break;
            case ICallbackStatus.PASSWORD_AUTHENTICATION_OK:
                Log.d(TAG, "验证成功或者设置密码成功");
                break;
            case ICallbackStatus.PASSWORD_INPUT_AGAIN:
                Log.d(TAG, "验证失败或者设置密码失败，请重新输入4位数字密码，如果已设置过密码，请输入已设置的密码");
                break;
            case ICallbackStatus.OFFLINE_SWIM_SYNCING:
                Log.d(TAG, "游泳数据同步中");
                break;
            case ICallbackStatus.OFFLINE_SWIM_SYNC_OK:
                Log.d(TAG, "游泳数据同步完成");
                break;
            case ICallbackStatus.OFFLINE_BLOOD_PRESSURE_SYNCING:
                Log.d(TAG, "血压数据同步中");
                break;
            case ICallbackStatus.OFFLINE_BLOOD_PRESSURE_SYNC_OK:
                Log.d(TAG, "血压数据同步完成");
                break;
            case ICallbackStatus.OFFLINE_SKIP_SYNCING:
                Log.d(TAG, "跳绳数据同步中");
                break;
            case ICallbackStatus.OFFLINE_SKIP_SYNC_OK:
                Log.d(TAG, "跳绳数据同步完成");
                break;
            case ICallbackStatus.MUSIC_PLAYER_START_OR_STOP:
                Log.d(TAG, "音乐播放/暂停");
                break;
            case ICallbackStatus.MUSIC_PLAYER_NEXT_SONG:
                Log.d(TAG, "音乐下一首");
                break;
            case ICallbackStatus.MUSIC_PLAYER_LAST_SONG:
                Log.d(TAG, "音乐上一首");
                break;
            case ICallbackStatus.OPEN_CAMERA_OK:
                Log.d(TAG, "打开相机ok");
                break;
            case ICallbackStatus.CLOSE_CAMERA_OK:
                Log.d(TAG, "关闭相机ok");
                break;
            default:
                break;
        }
    }

    @Override
    public void OnDataResult(boolean b, int i, byte[] bytes) {

    }

    @Override
    public void onCharacteristicWriteCallback(int i) {

    }

    @Override
    public void onIbeaconWriteCallback(boolean b, int i, int i1, String s) {

    }

    @Override
    public void onQueryDialModeCallback(boolean b, int i, int i1, int i2) {

    }

    @Override
    public void onControlDialCallback(boolean b, int i, int i1) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBLEServiceOperate.disConnect();
        mBLEServiceOperate.unBindService();
    }

    public static class Commond {
        /**
         * @param height      身高 单位：cm
         * @param weight      体重 单位：Kg
         * @param lightoff    亮灯时间 单位：秒
         * @param targetSteps 目标步数
         * @param handon      抬手点亮开关
         * @param heartrate   心率超标提醒开关
         * @param heart       心率阈值
         */
        public static void setConfig(int height, int weight, int lightoff, int targetSteps, boolean handon, boolean heartrate, int heart) {
            mWriteCommand.sendStepLenAndWeightToBLE(heart, weight, lightoff,
                    targetSteps, true, true, heart);
        }

        /**
         * @param minute 单位：分钟 设置的时间至少30分钟
         * @param isOpen 是否打开久坐提醒 true:打开；false:关闭
         */
        public static void sedentaryReminder(int minute, boolean isOpen) {
            if (isOpen) {
                mWriteCommand.sendSedentaryRemindCommand(
                        GlobalVariable.OPEN_SEDENTARY_REMIND,
                        minute);
            } else {
                mWriteCommand.sendSedentaryRemindCommand(
                        GlobalVariable.CLOSE_SEDENTARY_REMIND, 0);
            }
        }

        /**
         * 打开或者关闭闹钟
         *
         * @param hour   24制
         * @param minute
         * @param isOpen true为打开，false为关闭
         */
        public static void openAlarm(int hour, int minute, boolean isOpen) {
            //最后一个参数为震动次数
            mWriteCommand.sendToSetAlarmCommand(1, GlobalVariable.EVERYDAY,
                    hour, minute, isOpen, 5);
        }

        /**
         * 同步心率
         */
        public static void synHeartRate() {
            mWriteCommand.syncAllRateData();
        }

        /**
         * 同步时间
         */
        public static void synDateTime() {
            mWriteCommand.syncBLETime();
        }

        /**
         * 同步步数
         */
        public static void synStep() {
            mWriteCommand.syncAllStepData();
        }

        /**
         * 同步睡眠
         */
        public static void synSleep() {
            mWriteCommand.syncAllSleepData();
        }

        /**
         * 同步血压
         */
        public static void synBloodpressure() {
            mWriteCommand.syncAllBloodPressureData();
        }

        /**
         * 测量血压
         *
         * @param isStart true:开始测量，false:停止测量
         */
        public static void startBloodpressure(boolean isStart) {
            if (isStart) {
                mWriteCommand.sendBloodPressureTestCommand(GlobalVariable.BLOOD_PRESSURE_TEST_START);
            } else {
                mWriteCommand.sendBloodPressureTestCommand(GlobalVariable.BLOOD_PRESSURE_TEST_STOP);
            }
        }

        /**
         * 测量心率
         *
         * @param isStart
         */
        public static void startHeartRate(boolean isStart) {
            if (isStart) {
                mWriteCommand.sendRateTestCommand(GlobalVariable.RATE_TEST_START);
            } else {
                mWriteCommand.sendRateTestCommand(GlobalVariable.RATE_TEST_STOP);
            }
        }

        /**
         * 获取蓝牙版本号
         */
        public static void getBleVersion() {
            mWriteCommand.sendToReadBLEVersion();
        }

        /**
         * 消息推送
         *
         * @param message
         * @param type    {@link GlobalVariable#TYPE_QQ}
         */
        public static void pushMessage(String message, int type) {
            mWriteCommand.sendTextToBle(message, type);
        }
    }
}
