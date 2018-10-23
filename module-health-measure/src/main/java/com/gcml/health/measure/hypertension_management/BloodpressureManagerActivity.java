package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.data.AppManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的血压测量界面
 */
public class BloodpressureManagerActivity extends BaseManagementActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BloodpressureManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
        jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
        super.dealLogic();
    }

    private void initFragment() {
        mTitleText.setText("血 压 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new HealthBloodDetectionOnlyOneFragment();
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnDealVoiceAndJumpListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commitAllowingStateLoss();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void untieDevice() {
        super.untieDevice();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        //血压
        String nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
        ((Bloodpressure_Fragment) baseFragment).onStop();
        ((Bloodpressure_Fragment) baseFragment).dealLogic();
        clearBluetoothCache(nameAddress);
    }

    @Override
    public void jump2HealthHistory(int measureType) {
        //点击了下一步
        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_MEASUREACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, uri)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        initFragment();
                        break;
                    default:
                }
            }
        });
    }
}
