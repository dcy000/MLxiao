package com.example.han.referralproject.yiyuan.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require2.dialog.AlertDialog;
import com.example.han.referralproject.single_measure.SingleMeasureBloodpressureFragment;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.WenZhenBean;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WZPressureMeasureActivity extends BaseActivity implements SingleMeasureBloodpressureFragment.PostResultListener {

    public static final String PRESS_FALG_WZ = "WZ";
    public static final String PRESS_FALG = "PressureFlag";
    @BindView(R.id.container)
    FrameLayout container;
    private SingleMeasureBloodpressureFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wzpressure_measure);
        ButterKnife.bind(this);
        fillFragment();
        initToobar();
    }

    private void initToobar() {
        mToolbar.setVisibility(View.VISIBLE);
//        mRightView.
        mTitleText.setText("血压测量");
        mRightView.setImageResource(R.drawable.ic_blooth_beack);
    }

    @Override
    protected void backMainActivity() {
        new AlertDialog(WZPressureMeasureActivity.this)
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
        mRightView.setImageResource(R.drawable.ic_blooth_beack);
        //先清除已经绑定的设备
        unpairDevice();
        String nameAddress = null;
        //血压
        nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
        fragment.onStop();
        fragment.dealLogic();

        clearBluetoothCache(nameAddress);
    }

    private void clearBluetoothCache(String nameAddress) {
        if (!TextUtils.isEmpty(nameAddress)) {
            String[] split = nameAddress.split(",");
            if (split.length == 2 && !TextUtils.isEmpty(split[1])) {
                BluetoothClientManager.getClient().refreshCache(split[1]);
            }
        }
    }

    /**
     * 解除已配对设备
     */
    private void unpairDevice() {
        List<BluetoothDevice> devices = BluetoothUtils.getBondedBluetoothClassicDevices();
        for (BluetoothDevice device : devices) {
            try {
                Method m = device.getClass()
                        .getMethod("removeBond", (Class[]) null);
                m.invoke(device, (Object[]) null);
            } catch (Exception e) {

            }
        }

    }


    private void fillFragment() {
        fragment = new SingleMeasureBloodpressureFragment();

        Bundle args = new Bundle();
        args.putString(PRESS_FALG, PRESS_FALG_WZ);
        fragment.setArguments(args);

        fragment.setListener(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.container, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onNext(Object data, Bundle args) {
        postWenZhenData(true);
    }

    @Override
    public void onError(Object e, Bundle args) {

    }

    @Override
    public void onComplete(Bundle args) {

    }

    @Override
    public void clickLiftView(Bundle args) {
        if ("WZ".equals(args.get("PressureFlag"))) {
            postWenZhenData(false);
        }
    }

    private void postWenZhenData(final boolean testXueYa) {
        WenZhenBean bean = new WenZhenBean();
        bean.address = LocalShared.getInstance(this).getSignUpAddress();
        bean.allergicHistory = LocalShared.getInstance(this).getGuoMin();
        bean.diseasesHistory = LocalShared.getInstance(this).getJiBingShi();
        bean.equipmentId = LocalShared.getInstance(this).getEqID();
        bean.height = LocalShared.getInstance(this).getSignUpHeight() + "";
        bean.weight = LocalShared.getInstance(this).getSignUpWeight() + "";
        bean.hiUserInquiryId = "";
        String xueYa = LocalShared.getInstance(this).getXueYa();
        if (!testXueYa) {
            bean.highPressure = null;
            bean.lowPressure = null;
        } else {
            bean.highPressure = xueYa.split(",")[0];
            bean.lowPressure = xueYa.split(",")[1];
        }
        if ("男".equals(LocalShared.getInstance(this).getSex())) {
            bean.lastMensesTime = null;
            bean.pregnantState = null;
        } else {
            bean.lastMensesTime = LocalShared.getInstance(this).getYueJingDate();
            bean.pregnantState = LocalShared.getInstance(this).getHuaiYun();
        }
        bean.hypertensionState = "0";
        bean.userId = LocalShared.getInstance(this).getUserId();
        bean.weekDrinkState = LocalShared.getInstance(this).getIsDrinkOrNot();
        bean.wineType = LocalShared.getInstance(this).getDringInto();


        final Gson gson = new Gson();
        OkGo.<String>post(NetworkApi.Inquiry)
                .tag(this)
                .upJson(gson.toJson(bean))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        if (!TextUtils.isEmpty(result)) {
                            WenZhenReultBean reultBean = gson.fromJson(result, WenZhenReultBean.class);
                            if (reultBean.tag) {
                                T.show("提交成功");
                            } else {
                                T.show("提交失败");
                            }
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                        InquiryAndFileEndActivity.startMe(WZPressureMeasureActivity.this, "问诊");
                        finish();
                        ActivityHelper.finishAll();
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        T.show("网络繁忙");
                    }
                });
    }

}
