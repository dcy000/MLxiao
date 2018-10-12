package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.data.AppManager;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodsugarFragment;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的血糖测量界面
 */
public class BloodsugarManagerActivity extends BaseManagementActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BloodsugarManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
        jump2MeasureVideoPlayActivity(uri, "血糖测量演示视频");
        super.dealLogic();
    }

    private void initFragment() {
        mTitleText.setText("血 糖 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new SingleMeasureBloodsugarFragment();
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnFragmentChangedListener(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOnlyShowBtnHealthRecord", true);
        baseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commitAllowingStateLoss();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void untieDevice() {
        super.untieDevice();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        //血糖
        String nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR);
        ((Bloodsugar_Fragment) baseFragment).onStop();
        ((Bloodsugar_Fragment) baseFragment).dealLogic();
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
