package com.gcml.module_detection.followup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.R;
import com.gcml.module_detection.net.DetectionRepository;
import com.gcml.module_detection.risk_assessment.ChooseDevicesActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/module/detection/followup/activity")
public class FollowupActivity extends ToolbarBaseActivity {

    private int index = 0;
    private MyActivityCallback callback = new MyActivityCallback();
    private TextView mTvTips;
    private String rdRecordId;
    private ArrayList<Integer> data = new ArrayList<>();
    private String healthRecordId;
    private String typeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        initView();
        AppManager.getAppManager().addActivity(this);
        healthRecordId = getIntent().getStringExtra("healthRecordId");
        rdRecordId = getIntent().getStringExtra("rdRecordId");
        typeString = getIntent().getStringExtra("typeString");
        getDetectDevices();
    }

    private void getDetectDevices() {
        DetectionRepository.getDevices(rdRecordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> deviceBeans) {
                        dealDevices(deviceBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("获取测量设备出错");
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dealDevices(List<String> deviceBeans) {
        //去重
        Set<Integer> integerArrayList = new HashSet<>();
        //0血压 01左侧血压 02右侧血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 10腰围 11呼吸频率 12身高 13心率
        for (String dev : deviceBeans) {
            switch (dev) {
                case "0":
                    integerArrayList.add(0);
                    break;
                case "01":
                    integerArrayList.add(0);
                    break;
                case "02":
                    integerArrayList.add(0);
                    break;
                case "1":
                    integerArrayList.add(1);
                    break;
                case "2":
                    integerArrayList.add(4);
                    break;
                case "3":
                    integerArrayList.add(3);
                    break;
                case "4":
                    integerArrayList.add(2);
                    break;
                case "6":
                    integerArrayList.add(5);
                    break;
                case "7":
                    integerArrayList.add(6);
                    break;
                case "8":
                    integerArrayList.add(7);
                    break;
                case "9":
                    break;
                case "10":
                    //腰围
                    break;
                case "11":
                    //呼吸家
                    break;
                case "12":
                    integerArrayList.add(8);
                    break;
                case "13":
                    break;
            }
        }
        data.addAll(integerArrayList);
        dealData();
    }

    private void dealData() {

        if (data != null && data.size() > 0) {
            if (index == data.size()) {
                //最后一项
                uploadData();
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
                case 8:
                    Routerfit.register(AppRouter.class).skipFollowupHeightActivity(callback);
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
                if (data instanceof DetectionDataBean) {
                    datas.add((DetectionDataBean) data);
                }
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

    private List<DetectionDataBean> datas = new ArrayList<>();

    private void uploadData() {
        ArrayList<DetectionData> resultDatas = new ArrayList<>();
        for (DetectionDataBean bean : datas) {
            resultDatas.addAll(bean.getDetectionDataList());
        }
        DetectionRepository.postHealthRecordMeasureData(healthRecordId, resultDatas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Routerfit.register(AppRouter.class).skipOutputResultActivity(rdRecordId, healthRecordId, typeString);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
