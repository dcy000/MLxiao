package com.gcml.health.measure.first_diagnosis;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.FirstDiagnosisBean;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodOxygenDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.FragmentChanged;

import java.util.ArrayList;
import java.util.List;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:52
 * created by:gzq
 * description:TODO
 */
public class FirstDiagnosisActivity extends ToolbarBaseActivity implements FragmentChanged {
    private List<FirstDiagnosisBean> firstDiagnosisBeans;
    private FrameLayout mFrame;
    private int showPosition = 0;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);
        initView();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HealthECGDetectionFragment()).commit();
//        initFirstDiagnosis();
//        checkVideo(showPosition);
    }

    private void checkVideo(int showPosition) {
        Uri videoUri = firstDiagnosisBeans.get(showPosition).getVideoUri();
        String videoTitle = firstDiagnosisBeans.get(showPosition).getVideoTitle();
        if (videoUri!=null){
            jump2MeasureVideoPlayActivity(videoUri,videoTitle);
        }else{
            showFragment(showPosition);
        }
    }

    private void showFragment(int showPosition) {
        String fragmentTag = firstDiagnosisBeans.get(showPosition).getFragmentTag();
        BluetoothBaseFragment fragment=null;
        switch (fragmentTag) {
            case "HealthFirstTipsFragment":
                fragment=new HealthFirstTipsFragment();
                break;
            case "HealthBloodDetectionUiFragment":
                fragment=new HealthBloodDetectionUiFragment();
                break;
            case "HealthBloodOxygenDetectionFragment":
                fragment=new HealthBloodOxygenDetectionFragment();
                break;
            case "HealthTemperatureDetectionFragment":
                fragment=new HealthTemperatureDetectionFragment();
                break;
            default:
                break;
        }
        fragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    /**
     * 将需要测量的设备放在一个有序集合中，按照先后顺序来配置即可
     */
    private void initFirstDiagnosis() {
        firstDiagnosisBeans = new ArrayList<>();
        FirstDiagnosisBean firstTip = new FirstDiagnosisBean(new ArrayList<>(),HealthFirstTipsFragment.class.getSimpleName(),null,null);
        firstDiagnosisBeans.add(firstTip);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
        FirstDiagnosisBean bloodpressure = new FirstDiagnosisBean(new ArrayList<>(),
                HealthBloodDetectionUiFragment.class.getSimpleName(),uri,"测量血压演示视频");
        firstDiagnosisBeans.add(bloodpressure);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
        FirstDiagnosisBean bloodoxygen = new FirstDiagnosisBean(new ArrayList<>(),
                HealthBloodOxygenDetectionFragment.class.getSimpleName(),uri,"测量血氧演示视频");
        firstDiagnosisBeans.add(bloodoxygen);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
        FirstDiagnosisBean temperature = new FirstDiagnosisBean(new ArrayList<>(),
                HealthTemperatureDetectionFragment.class.getSimpleName(),uri,"测量耳温演示视频");
        firstDiagnosisBeans.add(temperature);

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
                        showFragment(showPosition);
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        showFragment(showPosition);
                        break;
                    default:
                }
            }
        });
    }
    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        showPosition++;
        checkVideo(showPosition);
    }
}
