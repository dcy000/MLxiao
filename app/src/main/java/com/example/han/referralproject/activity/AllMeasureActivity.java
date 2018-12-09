package com.example.han.referralproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.module_blood_oxygen.ui.BloodoxygenFragment;
import com.example.module_blood_pressure.ui.BloodpressureFragment;
import com.example.module_blood_sugar.ui.BloodsugarFragment;
import com.example.module_blood_sugar.ui.SelectSugarDetectionTimeFragment;
import com.example.module_triple.ui.ThreeInOneFragment;
import com.example.module_weight.ui.WeightFragment;
import com.gcml.lib_video_ksyplayer.MeasureVideoPlayActivity;
import com.gcml.lib_video_ksyplayer.VideoConstants;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gcml.module_temperature.ui.TemperatureFragment;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_bluetooth.DeviceBean;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.utils.BluetoothUtils;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.KVUtils;

public class AllMeasureActivity extends ToolbarBaseActivity implements BaseBluetoothFragment.StateUpdate2Activity, BaseBluetoothFragment.FragmentReplaced {
    private String measureType;
    private BaseBluetoothFragment baseFragment;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_all_measure;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    private void checkVideo() {
        Uri uri;
        switch (measureType) {
            case BluetoothConstants.KEY_TEMPERATURE:
                //体温测量
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                MeasureVideoPlayActivity.startActivity(this, uri, null, "耳温测量演示视频");
                break;
            case BluetoothConstants.KEY_SPHYGMOMANOMETER:
                //血压
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                MeasureVideoPlayActivity.startActivity(this, uri, null, "血压测量演示视频");
                break;
            case BluetoothConstants.KEY_BLOOD_GLUCOSE_METER:
                //血糖
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                MeasureVideoPlayActivity.startActivity(this, uri, null, "血糖测量演示视频");
                break;
            case BluetoothConstants.KEY_OXIMETER:
                //血氧
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                MeasureVideoPlayActivity.startActivity(this, uri, null, "血氧测量演示视频");
                break;
            case BluetoothConstants.KEY_TRIPLE:
                //三合一
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                MeasureVideoPlayActivity.startActivity(this, uri, null, "三合一测量演示视频");
                break;
            case BluetoothConstants.KEY_WEIGHING_SCALE:
                loadLayout();
                break;
            default:
                break;
        }
    }

    private void loadLayout() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (measureType) {
            case BluetoothConstants.KEY_TEMPERATURE:
                baseFragment = new TemperatureFragment();
                mTitleText.setText("体 温 测 量");
                break;
            case BluetoothConstants.KEY_SPHYGMOMANOMETER:
                baseFragment = new BloodpressureFragment();
                mTitleText.setText("血 压 测 量");
                break;
            case BluetoothConstants.KEY_OXIMETER:
                baseFragment = new BloodoxygenFragment();
                mTitleText.setText("血 氧 测 量");
                break;
            case BluetoothConstants.KEY_BLOOD_GLUCOSE_METER:
                baseFragment = new SelectSugarDetectionTimeFragment();
                mTitleText.setText("血 糖 测 量");
                break;
            case BluetoothConstants.KEY_WEIGHING_SCALE:
                baseFragment = new WeightFragment();
                mTitleText.setText("体 重 测 量");
                break;
            case BluetoothConstants.KEY_TRIPLE:
                baseFragment = new SelectSugarDetectionTimeFragment();
                mTitleText.setText("三 合 一 测 量");
                break;
        }
        addFragment(fragmentTransaction);
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            measureType = extras.getString(BluetoothConstants.TYPE_OF_MEASURE);
        }
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        checkVideo();
    }

    @NonNull
    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private void addFragment(FragmentTransaction fragmentTransaction) {
        if (baseFragment != null) {
            baseFragment.setStateUpdate2ActivityListener(this);
            baseFragment.setFragmentReplacedListener(this);
            fragmentTransaction.replace(R.id.frame, baseFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected void backLastActivity() {
        super.backLastActivity();
        finish();
    }

    @Override
    protected void backMainActivity() {
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        untieDevice();
                    }
                }).show();
    }

    private void untieDevice() {
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        BluetoothUtils.unpairDevice();
        DeviceBean entity = KVUtils.getEntity(measureType, DeviceBean.class);
        BluetoothUtils.clearBluetoothCache(entity);
        KVUtils.removeValue(measureType);
    }

    @Override
    public void onStateChanged(String state) {
        if (TextUtils.equals(state, Box.getString(R.string.bluetooth_connected))) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_connected);
        } else if (TextUtils.equals(state, Box.getString(R.string.bluetooth_device_disconnected))) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        }
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof SelectSugarDetectionTimeFragment && bundle != null) {
            if (TextUtils.equals(measureType, BluetoothConstants.KEY_BLOOD_GLUCOSE_METER)) {
                baseFragment = new BloodsugarFragment();
            } else if (TextUtils.equals(measureType, BluetoothConstants.KEY_TRIPLE)) {
                baseFragment = new ThreeInOneFragment();
            }
            if (baseFragment != null) {
                baseFragment.setArguments(bundle);
            }
            addFragment(getSupportFragmentManager().beginTransaction());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            String result = data.getStringExtra(VideoConstants.KEY_EVENT_VIDEO);
            switch (result) {
                case VideoConstants.PRESSED_BUTTON_BACK:
                    break;
                case VideoConstants.PRESSED_BUTTON_SKIP:
                    loadLayout();
                    break;
                case VideoConstants.VIDEO_PLAY_END:
                    loadLayout();
                    break;
            }
        }
    }
}
