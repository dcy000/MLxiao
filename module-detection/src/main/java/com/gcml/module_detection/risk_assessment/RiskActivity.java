package com.gcml.module_detection.risk_assessment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.R;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/module/detection/risk/activity")
public class RiskActivity extends ToolbarBaseActivity {

    private ArrayList<Integer> data;
    private int index = 0;
    private MyActivityCallback callback = new MyActivityCallback();
    private TextView mTvTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        initView();
        AppManager.getAppManager().addActivity(this);
        data = getIntent().getIntegerArrayListExtra("data");
        dealData();
    }

    private void dealData() {

        if (data != null) {
            if (index == data.size()) {
                //最后一项
                Routerfit.register(AppRouter.class).skipHealthReportFormActivity();
                AppManager.getAppManager().finishActivity(this);
                AppManager.getAppManager().finishActivity(ChooseDevicesActivity.class);
                return;
            }
            Integer integer = data.get(index);
            switch (integer) {
                case 0:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_PRESSURE, false, callback);
                    break;
                case 1:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_SUGAR, false, callback);
                    break;
                case 2:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_TEMPERATURE, false, callback);
                    break;
                case 3:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_WEIGHT, false, callback);
                    break;
                case 4:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_ECG, false, callback);
                    break;
                case 5:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_OXYGEN, false, callback);
                    break;
                case 6:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_CHOLESTEROL, false, callback);
                    break;
                case 7:
                    Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_URIC_ACID, false, callback);
                    break;
            }
        }
    }

    private void initView() {
        mTvTips = (TextView) findViewById(R.id.tv_tips);
    }

    class MyActivityCallback implements ActivityCallback {

        @Override
        public void onActivityResult(int result, Object data) {
            if (result == Activity.RESULT_OK) {
                mTvTips.setText("正在切换到下一项");
                index++;
                dealData();
            } else if (result == Activity.RESULT_CANCELED) {
                mTvTips.setText("正在回退到上一项");
                index--;
                if (index == -1) {
                    finish();
                } else {
                    dealData();
                }
            }
        }
    }
}
