package com.example.han.referralproject.inquiry.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.cc.CCVideoActions;
import com.example.han.referralproject.inquiry.fragment.HealthSelectSugarDetectionTimeFragment;
import com.example.han.referralproject.inquiry.fragment.HypertensionFollowUpFragment;
import com.example.han.referralproject.inquiry.fragment.SugarFollowUpFragment;
import com.example.han.referralproject.inquiry.fragment.WeightFollowUpFragment;
import com.example.han.referralproject.inquiry.model.SurveyBean;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/1/16.
 */

public class DetectionItemActivity extends AppCompatActivity implements FragmentChanged, DealVoiceAndJump {
    private int position = 0;
    private List<SurveyBean> followInfo = new ArrayList<>();
    private BluetoothBaseFragment posiontFragment;
    private Bundle data = new Bundle();
    private Bundle time = new Bundle();
    private TranslucentToolBar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dection_item);
        initTitle();
        initFollowDeviceInfo();
        showFragmentOrVideo(position);
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_dection_item);
        tb.setData("问 诊",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        backLastActivity();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    private void initFollowDeviceInfo() {
        SurveyBean xueyaYou = new SurveyBean("HypertensionFollowUpFragmentYou",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya),
                "血压测量演示视频"
        );
        followInfo.add(xueyaYou);

        SurveyBean xuanZheTime = new SurveyBean("HealthSelectSugarDetectionTimeFragment", null, null);
        followInfo.add(xuanZheTime);

        SurveyBean xuetang = new SurveyBean("SugarFollowUpFragment",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang),
                "血糖测量演示视频"
        );
        followInfo.add(xuetang);

        SurveyBean weight = new SurveyBean("WeightFollowUpFragment", null, null);
        followInfo.add(weight);
    }


    protected void backLastActivity() {
        position--;
        if (position > 0) {
            showFragment(position);
        } else {
            finish();
        }
    }

    protected void backMainActivity() {
        if (posiontFragment == null) {
            return;
        }
        if (posiontFragment instanceof HealthSelectSugarDetectionTimeFragment) {
            backMainActivity();
        } else {
            showRefreshBluetoothDialog();
        }
    }

    private void showFragmentOrVideo(int position) {
        Uri videoUri = followInfo.get(position).getVideoUri();
        String videoTitle = followInfo.get(position).getVideoTitle();
        if (videoUri != null) {
            jump2MeasureVideoPlayActivity(videoUri, videoTitle);
        } else {
            showFragment(position);
        }
    }

    private void showFragment(int position) {
        String fragmentTag = followInfo.get(position).getFragmentTag();
        switch (fragmentTag) {
            case "HypertensionFollowUpFragmentYou":
                tb.setTitle("血 压 测 量");
                posiontFragment = new HypertensionFollowUpFragment();
                tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
                break;
            case "SugarFollowUpFragment":
                tb.setTitle("血 糖 测 量");
                posiontFragment = new SugarFollowUpFragment();
                if (this.time != null)
                    posiontFragment.setArguments(time);
                tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
                break;
            case "HealthSelectSugarDetectionTimeFragment":
                tb.setTitle("血 糖 测 量");
                posiontFragment = new HealthSelectSugarDetectionTimeFragment();
                tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
                break;

            case "WeightFollowUpFragment":
                tb.setTitle("体 重 测 量");
                posiontFragment = new WeightFollowUpFragment();
                tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
                break;


            default:
                break;
        }

        if (posiontFragment != null) {
            posiontFragment.setOnFragmentChangedListener(this);
            posiontFragment.setOnDealVoiceAndJumpListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_dection_item, posiontFragment).commitAllowingStateLoss();
        } else {
            throw new IllegalArgumentException();
        }


    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        data.putAll(bundle);

        if (posiontFragment instanceof HealthSelectSugarDetectionTimeFragment) {
            this.time = bundle;
        }

        position++;

        if (position > followInfo.size() - 1) {
            Intent intent = new Intent(this, InquiryHeightActivity.class);
            intent.putExtras(getIntent());
            intent.putExtras(data);
            startActivity(intent);
            return;
        }
        //因为每一个Fragment中都有可能视频播放，所以应该先检查该Fragment中是否有视频播放
        showFragmentOrVideo(position);
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
                        posiontFragment.autoConnect();
                    }
                }).show();
    }

    @Override
    public void updateVoice(String voice) {
        ToastUtils.showShort(voice);
        String connect = getString(R.string.bluetooth_device_connected);
        String disconnect = getString(R.string.bluetooth_device_disconnected);
        if (TextUtils.equals(voice, connect)) {
            tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
        }
        if (TextUtils.equals(voice, disconnect)) {
            tb.setResIdRight(R.drawable.common_icon_bluetooth_break);
        }
    }

    @Override
    public void jump2HealthHistory(int measureType) {

    }

    @Override
    public void jump2DemoVideo(int measureType) {

    }

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
                        backLastActivity();
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        showFragment(position);
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        showFragment(position);
                        break;
                    default:
                }
            }
        });
    }
}
