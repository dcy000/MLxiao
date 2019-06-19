package com.gcml.module_detection.risk_assessment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.divider.GridViewDividerItemDecoration;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_detection.R;
import com.gcml.module_detection.bean.RiskChooseDeviceBean;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

@Route(path = "/module/detection/choose/devices")
public class ChooseDevicesActivity extends ToolbarBaseActivity {
    private FrameLayout mFlContainer;
    private TextView mBtnSure;
    private RecyclerView mRvDevices;
    private BaseQuickAdapter<RiskChooseDeviceBean, BaseViewHolder> adapter;
    private List<RiskChooseDeviceBean> deviceBeans = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_devices);
        initView();
        iniDevices();
        initRecycleview();
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        mBtnSure = (TextView) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
        mRvDevices = (RecyclerView) findViewById(R.id.rv_devices);
    }

    private void initRecycleview() {

        mRvDevices.setLayoutManager(new GridLayoutManager(this, 4));
        mRvDevices.addItemDecoration(new GridViewDividerItemDecoration(24, 40));
        mRvDevices.setAdapter(adapter = new BaseQuickAdapter<RiskChooseDeviceBean, BaseViewHolder>(R.layout.item_choose_device, deviceBeans) {
            @Override
            protected void convert(BaseViewHolder helper, RiskChooseDeviceBean item) {
                helper.getView(R.id.device_is_selected).setSelected(item.getChoosed());
                helper.setImageResource(R.id.device_image, item.getImageNormal());
                helper.setText(R.id.device_name, item.getDeviceName());

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RiskChooseDeviceBean chooseDeviceBean = deviceBeans.get(position);
                if (chooseDeviceBean.getChoosed()) {
                    chooseDeviceBean.setChoosed(false);
                    view.findViewById(R.id.device_is_selected).setSelected(false);
                } else {
                    chooseDeviceBean.setChoosed(true);
                    view.findViewById(R.id.device_is_selected).setSelected(true);
                }
            }
        });

    }

    private void iniDevices() {
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_bloodpressure, true, "血压", 0));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_bloodsugar, true, "血糖", 1));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_temper, true, "体温", 2));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_weight, true, "体重", 3));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_ecg, true, "心电", 4));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_bloodoxygen, true, "血氧", 5));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_chrol, true, "胆固醇", 6));
        this.deviceBeans.add(new RiskChooseDeviceBean(R.drawable.risk_uac, true, "血尿酸", 7));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_sure) {
            ArrayList<Integer> data = selectedDevices();
            if (data.size() == 0) {
                ToastUtils.showShort("至少选择一项");
                return;
            }
            Routerfit.register(AppRouter.class).skipRiskActivity(data);
        }
    }

    private ArrayList<Integer> selectedDevices() {
        ArrayList<Integer> deviceNum = new ArrayList<>();
        for (RiskChooseDeviceBean bean : deviceBeans) {
            if (bean.getChoosed()) {
                deviceNum.add(bean.getDeviceLevel());
            }
        }
        return deviceNum;
    }

}
