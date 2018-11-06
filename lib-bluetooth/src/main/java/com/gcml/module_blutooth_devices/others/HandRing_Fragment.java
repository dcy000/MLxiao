package com.gcml.module_blutooth_devices.others;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.gcml.module_blutooth_devices.utils.UtilsManager;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/5 9:10
 * created by: gzq
 * description: TODO
 */
public class HandRing_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    /**
     * 12345
     */
    private TextView mTvResultSteps;
    /**
     * 10.2
     */
    private TextView mTvResultKilometer;
    /**
     * 186
     */
    private TextView mTvResultCalorie;
    /**
     * 88
     */
    private TextView mTvResultRate;
    /**
     * 98
     */
    private TextView mTvResultHighRate;
    /**
     * 64
     */
    private TextView mTvResultLowRate;
    /**
     * 3
     */
    private TextView mTvResultShallowSleep;
    /**
     * 98
     */
    private TextView mTvResultDeepSleep;
    /**
     * 64
     */
    private TextView mTvResultAwakeTimes;
    private HandRing_Tongleda_PresenterImp bluetoothPresenter;
    private Bundle bundle;
    private SearchWithDeviceGroupHelper helper;
//    private LoadingDialog mLoadingDialog;
    /**
     * 运动历史
     */
    private TextView mTvTitleSportsHistory;
    /**
     * 心率历史
     */
    private TextView mTvTitleRateHistory;
    /**
     * 睡眠历史
     */
    private TextView mTvTitleSleepHistory;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_handring;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mTvResultSteps = (TextView) view.findViewById(R.id.tv_result_steps);
        mTvResultSteps.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultKilometer = (TextView) view.findViewById(R.id.tv_result_kilometer);
        mTvResultKilometer.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultCalorie = (TextView) view.findViewById(R.id.tv_result_calorie);
        mTvResultCalorie.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultRate = (TextView) view.findViewById(R.id.tv_result_rate);
        mTvResultRate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultHighRate = (TextView) view.findViewById(R.id.tv_result_high_rate);
        mTvResultHighRate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultLowRate = (TextView) view.findViewById(R.id.tv_result_low_rate);
        mTvResultLowRate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultShallowSleep = (TextView) view.findViewById(R.id.tv_result_shallow_sleep);
        mTvResultShallowSleep.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultDeepSleep = (TextView) view.findViewById(R.id.tv_result_deep_sleep);
        mTvResultDeepSleep.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvResultAwakeTimes = (TextView) view.findViewById(R.id.tv_result_awake_times);
        mTvResultAwakeTimes.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvTitleSportsHistory = (TextView) view.findViewById(R.id.tv_title_sports_history);
        mTvTitleSportsHistory.setOnClickListener(this);
        mTvTitleRateHistory = (TextView) view.findViewById(R.id.tv_title_rate_history);
        mTvTitleRateHistory.setOnClickListener(this);
        mTvTitleSleepHistory = (TextView) view.findViewById(R.id.tv_title_sleep_history);
        mTvTitleSleepHistory.setOnClickListener(this);
        this.bundle = bundle;
//        mLoadingDialog = new LoadingDialog.Builder(mContext)
//                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
//                .setTipWord("正在同步数据...")
//                .create();
        dealLogic();


    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_handring = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_HAND_RING, "");
        Logg.d(Bloodoxygen_Fragment.class, sp_handring);
        if (!TextUtils.isEmpty(sp_handring)) {
            String[] split = sp_handring.split(",");
            if (split.length == 2) {
                brand = split[0];
                address = split[1];
                chooseConnectType(address, brand);
                return;
            }
        }
        if (bundle != null) {
            address = bundle.getString(IPresenter.DEVICE_BLUETOOTH_ADDRESS);
            brand = bundle.getString(IPresenter.BRAND);
            chooseConnectType(address, brand);
            return;
        }
        chooseConnectType(address, brand);

    }

    private void chooseConnectType(String address, String brand) {
        Logg.d(Bloodoxygen_Fragment.class, "" + address + brand);
        if (TextUtils.isEmpty(address)) {
            if (helper == null) {
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_HAND_RING);
            }
            helper.start();
        } else {
            if (bluetoothPresenter != null) {
                bluetoothPresenter.checkBlueboothOpened();
                return;
            }
            switch (brand) {
                case "RB09_Heart":
                    bluetoothPresenter = new HandRing_Tongleda_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "RB09_Heart"));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void updateData(final String... datas) {
        if (datas == null) {
            return;
        }
        String flag = datas[0];
        switch (flag) {
            case HandRing_Tongleda_PresenterImp.FLAG_TIME:
                SystemClock.sleep(200);
                //同步完时间以后同步步数
                HandRing_Tongleda_PresenterImp.Commond.synStep();
                break;
            case HandRing_Tongleda_PresenterImp.FLAG_STEP:
                syncStepFinished(datas[1],datas[2],datas[3]);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResultSteps.setText(datas[1]);
                        mTvResultKilometer.setText(datas[2]);
                        mTvResultCalorie.setText(datas[3]);
                    }
                });
                SystemClock.sleep(200);
                //同步完步数以后同步心率
                HandRing_Tongleda_PresenterImp.Commond.synHeartRate();
                break;
            case HandRing_Tongleda_PresenterImp.FLAG_HEART_RATE:
                syncHeartRateFinish(datas[1],datas[2],datas[3]);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResultRate.setText(datas[1]);
                        mTvResultHighRate.setText(datas[2]);
                        mTvResultLowRate.setText(datas[3]);
                    }
                });
                SystemClock.sleep(200);
                //同步了心率之后同步睡眠
                HandRing_Tongleda_PresenterImp.Commond.synSleep();
                break;
            case HandRing_Tongleda_PresenterImp.FLAG_SLEEP:
                syncSleepFinished(datas[1],datas[2],datas[3]);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResultDeepSleep.setText(datas[1]);
                        mTvResultShallowSleep.setText(datas[1]);
                        mTvResultAwakeTimes.setText(datas[2]);
                    }
                });
//                if (mLoadingDialog != null) {
//                    mLoadingDialog.dismiss();
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
        if (state.equals(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected))) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mLoadingDialog != null) {
//                        mLoadingDialog.show();
//                    }
//                }
//            });
            //先同步时间
            HandRing_Tongleda_PresenterImp.Commond.synDateTime();
        }
    }

    @Override
    public Context getThisContext() {
        return mContext;
    }

    @Override
    public void onStop() {
        super.onStop();

//        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//        mLoadingDialog = null;

        if (bluetoothPresenter != null) {
            bluetoothPresenter.onDestroy();
            bluetoothPresenter = null;
        }
        if (helper != null) {
            helper.destroy();
            helper = null;
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_title_sports_history) {
            clickSportsHistory();
        } else if (i == R.id.tv_title_rate_history) {
            clickHeartRateHistory();
        } else if (i == R.id.tv_title_sleep_history) {
            clickSleepHistory();
        } else {
        }
    }

    protected void clickSportsHistory() {

    }

    protected void clickHeartRateHistory() {

    }

    protected void clickSleepHistory() {

    }
    @WorkerThread
    protected void syncStepFinished(String step,String km,String ka){

    }
    @WorkerThread
    protected void syncHeartRateFinish(String current,String high,String low){

    }
    @WorkerThread
    protected void syncSleepFinished(String deep,String low,String awake){

    }
}
