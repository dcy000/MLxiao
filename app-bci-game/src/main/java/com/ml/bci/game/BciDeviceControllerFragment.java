package com.ml.bci.game;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ml.bci.game.common.utils.T;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;


/**
 * Bci device controller for connecting or close device, and listening signal from device
 */
public class BciDeviceControllerFragment extends Fragment {

    public static final String TAG = "BciDeviceController";

    private int subjectContactQuality_last = -1; /* start with impossible value */
    private int subjectContactQuality_cnt = 200; /* start over the limit, so it gets reported the 1st time */

    private double task_famil_baseline, task_famil_cur, task_famil_change;
    private boolean task_famil_first = true;
    private double task_diff_baseline, task_diff_cur, task_diff_change;
    private boolean task_diff_first = true;

    private final BciSignalObservable mBciSignalObservable = new BciSignalObservable();
    private volatile TGDevice tgDevice;

    public BciDeviceControllerFragment() {

    }

    public void register(BciSignalObservable.Observer observer) {
        mBciSignalObservable.registerObserver(observer);
    }

    public void unregister(BciSignalObservable.Observer observer) {
        mBciSignalObservable.unregisterObserver(observer);
    }

    public void unregisterAll() {
        mBciSignalObservable.unregisterAll();
    }

    public void connectDevice() {
        if (tgDevice == null) {
            synchronized (this) {
                if (tgDevice == null) {
                    tgDevice = initTgDevice();
                }
            }
        }
        if (tgDevice != null
                && tgDevice.getState() != TGDevice.STATE_CONNECTING
                && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
            tgDevice.connect(true);
        }
    }

    private TGDevice initTgDevice() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        FragmentActivity activity = getActivity();

        if (adapter == null && activity != null) {
            T.show("无法获取蓝牙");

        }
        return new TGDevice(adapter, callbackHandler);
    }

    public void closeDevice() {
        if (tgDevice != null) {
            tgDevice.close();
            tgDevice = null;
        }
    }


    private void onBlinkChanged(int value) {
        mBciSignalObservable.notifyBlinkChanged(value);
    }

    private void onAttentionChanged(int value) {
        mBciSignalObservable.notifyAttentionChanged(value);
    }

    private Handler callbackHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            int arg1 = msg.arg1;
            switch (what) {
                case TGDevice.MSG_MODEL_IDENTIFIED:
                    Log.d(TAG, "handleMessage: MSG_MODEL_IDENTIFIED");
                    tgDevice.setBlinkDetectionEnabled(true);
                    tgDevice.setTaskDifficultyRunContinuous(true);
                    tgDevice.setTaskDifficultyEnable(true);
                    tgDevice.setTaskFamiliarityRunContinuous(true);
                    tgDevice.setTaskFamiliarityEnable(true);
                    tgDevice.setRespirationRateEnable(true); /// not allowed on EEG hardware, here to show the override message
                    break;
                case TGDevice.MSG_STATE_CHANGE:
                    String state;
                    switch (arg1) {
                        case TGDevice.STATE_IDLE:
                            state = "STATE_IDLE";
                            break;
                        case TGDevice.STATE_CONNECTING:
                            state = "STATE_CONNECTING";
                            break;
                        case TGDevice.STATE_CONNECTED:
                            state = "STATE_CONNECTING";
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            state = "STATE_NOT_FOUND. Check bluetooth!!!";
                            break;
                        case TGDevice.STATE_ERR_NO_DEVICE:
                            state = "STATE_ERR_NO_DEVICE. Check bluetooth!!!";
                            break;
                        case TGDevice.STATE_ERR_BT_OFF:
                            state = "STATE_ERR_BT_OFF. Turn on Bluetooth and try again!!!";
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            state = "STATE_DISCONNECTED.";
                            break;
                        default:
                            state = "" + arg1;
                            break;
                    }
                    Log.d(TAG, "handleMessage: MSG_STATE_CHANGE " + state);
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    if (subjectContactQuality_cnt >= 30 || arg1 != subjectContactQuality_last) {
                        String poor = arg1 == 0 ? " Good" : " Poor";
                        Log.d(TAG, "handleMessage: MSG_POOR_SIGNAL " + arg1 + poor);
                        subjectContactQuality_cnt = 0;
                        subjectContactQuality_last = arg1;
                    } else {
                        subjectContactQuality_cnt++;
                    }
                    break;
                case TGDevice.MSG_RAW_DATA:
//                    Log.d(TAG, "handleMessage: MSG_RAW_DATA ");
                    break;
                case TGDevice.MSG_ATTENTION:
                    onAttentionChanged(arg1);
                    Log.d(TAG, "handleMessage: MSG_ATTENTION " + arg1);
                    break;
                case TGDevice.MSG_MEDITATION:
                    Log.d(TAG, "handleMessage: MSG_MEDITATION " + arg1);
                    break;
                case TGDevice.MSG_EEG_POWER:
                    TGEegPower e = (TGEegPower) msg.obj;
                    if (e != null) {
                        Log.d(TAG, "handleMessage: MSG_MEDITATION MSG_EEG_POWER: " + e.delta + " theta: " + e.theta + " alpha1: " + e.lowAlpha + " alpha2: " + e.highAlpha + "\n");
                    }
                    break;
                case TGDevice.MSG_FAMILIARITY:
                    task_famil_cur = (Double) msg.obj;
                    String familiarity;
                    if (task_famil_first) {
                        task_famil_first = false;
                        familiarity = "start";
                    } else {
                        /*
                         * calculate the percentage change from the previous sample
                		 */
                        task_famil_change = calcPercentChange(task_famil_baseline, task_famil_cur);
                        if (task_famil_change > 500.0 || task_famil_change < -500.0) {
                            familiarity = "excessive range";
                        } else {
                            familiarity = String.valueOf(task_famil_change) + "%";
                        }
                    }
                    Log.d(TAG, "handleMessage: MSG_FAMILIARITY " + familiarity);
                    task_famil_baseline = task_famil_cur;
                    break;
                case TGDevice.MSG_DIFFICULTY:
                    task_diff_cur = (Double) msg.obj;
                    String difficulty;
                    if (task_diff_first) {
                        task_diff_first = false;
                        difficulty = "start";
                    } else {
                        /*
                         * calculate the percentage change from the previous sample
                		 */
                        task_diff_change = calcPercentChange(task_diff_baseline, task_diff_cur);
                        if (task_diff_change > 500.0 || task_diff_change < -500.0) {
                            difficulty = "excessive range";
                        } else {
                            difficulty = task_diff_change + "%";
                        }
                    }
                    Log.d(TAG, "handleMessage: MSG_DIFFICULTY " + difficulty);
                    task_diff_baseline = task_diff_cur;
                    break;
                case TGDevice.MSG_ZONE:
                    String zone;
                    switch (arg1) {
                        case 3:
                            zone = " Elite";
                            break;
                        case 2:
                            zone = " Intermediate";
                            break;
                        case 1:
                            zone = " Beginner";
                            break;
                        case 0:
                        default:
                            zone = " relax and try to focus";
                            break;
                    }
                    Log.d(TAG, "handleMessage: MSG_ZONE " + zone);
                    break;
                case TGDevice.MSG_BLINK:
                    onBlinkChanged(arg1);
                    Log.d(TAG, "handleMessage: MSG_BLINK " + arg1);
                    break;
                case TGDevice.MSG_ERR_CFG_OVERRIDE:
                    String errorMsg;
                    switch (arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            errorMsg = "blinkDetect";
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            errorMsg = "Familiarity";
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            errorMsg = "Difficulty";
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            errorMsg = "Positivity";
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            errorMsg = "RESPIRATIONRATE";
                            break;
                        default:
                            errorMsg = arg1 + "";
                            break;
                    }
                    Log.d(TAG, "handleMessage: MSG_ERR_CFG_OVERRIDE " + errorMsg);
                    break;
                case TGDevice.MSG_ERR_NOT_PROVISIONED:
                    String notProvided;
                    switch (arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            notProvided = "blinkDetect";
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            notProvided = "Familiarity";
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            notProvided = "Difficulty";
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            notProvided = "Positivity";
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            notProvided = "RESPIRATIONRATE";
                            break;
                        default:
                            notProvided = arg1 + "";
                            break;
                    }
                    Log.d(TAG, "handleMessage: MSG_ERR_NOT_PROVISIONED " + notProvided);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private double calcPercentChange(double baseline, double current) {
        double change;

        if (baseline == 0.0) baseline = 1.0; //don't allow divide by zero
        /*
         * calculate the percentage change
		 */
        change = current - baseline;
        change = (change / baseline) * 1000.0 + 0.5;
        change = Math.floor(change) / 10.0;
        return change;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDevice();
        mBciSignalObservable.unregisterAll();
    }
}