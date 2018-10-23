package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.divider.GridViewDividerItemDecoration;
import com.gcml.health.measure.first_diagnosis.bean.ChooseDeviceBean;
import com.gcml.health.measure.first_diagnosis.bean.DeviceBean;
import com.gcml.health.measure.first_diagnosis.bean.PostDeviceBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/30 10:45
 * created by:gzq
 * description:TODO
 */
public class HealthChooseDevicesFragment extends BluetoothBaseFragment implements View.OnClickListener {
    /**
     * 确认
     */
    private TextView mBtnSure;
    private RecyclerView mRvDevices;
    private List<ChooseDeviceBean> deviceBeans;
    private BaseQuickAdapter<ChooseDeviceBean, BaseViewHolder> adapter;
    public static final String KEY_DEVICE_NUM = "key_device_num";
    private String userId="100206";

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_choose_devices;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mBtnSure = (TextView) view.findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
        mRvDevices = (RecyclerView) view.findViewById(R.id.rv_devices);
        initData();
        initRecycleview();
    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getContext(),"请选择您拥有的仪器，小易默认给您做了选择，如果没有该设备请再次点击该设备取消",false);
    }

    private void initRecycleview() {

        mRvDevices.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRvDevices.addItemDecoration(new GridViewDividerItemDecoration(24, 40));
        mRvDevices.setAdapter(adapter = new BaseQuickAdapter<ChooseDeviceBean, BaseViewHolder>(R.layout.health_measure_item_choose_device, deviceBeans) {
            @Override
            protected void convert(BaseViewHolder helper, ChooseDeviceBean item) {
                if (helper.getAdapterPosition() == 7) {
                    helper.getView(R.id.device_is_selected).setVisibility(View.GONE);
                }
                helper.getView(R.id.device_is_selected).setSelected(item.getChoosed());
                helper.setImageResource(R.id.device_image, item.getImageNormal());
                helper.setText(R.id.device_name, item.getDeviceName());

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 7) {
                    ToastUtils.showShort("敬请期待");
                    return;
                }
                ChooseDeviceBean chooseDeviceBean = deviceBeans.get(position);
                if (chooseDeviceBean.getChoosed()) {
                    chooseDeviceBean.setChoosed(false);
                    view.findViewById(R.id.device_is_selected).setSelected(false);
                } else {
                    chooseDeviceBean.setChoosed(true);
                    view.findViewById(R.id.device_is_selected).setSelected(true);
                }

                Timber.e(new Gson().toJson(deviceBeans));
            }
        });

    }

    @SuppressLint("CheckResult")
    private void initData() {
        this.deviceBeans = new ArrayList<>();
        //TODO:测试结束后打开下面的注释
        userId = UserSpHelper.getUserId();
        HealthMeasureRepository.getUserHasedDevices(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<List<DeviceBean>>() {
                    @Override
                    public void onNext(List<DeviceBean> deviceBeans) {
                        iniDevices(deviceBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iniDevices(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void iniDevices(List<DeviceBean> deviceBeans) {
        //血压计
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_bloodpressure, false, "血压仪", 1));
        //血糖仪
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_bloodsugar, false, "血糖仪", 5));
        //体温计
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_temperature, false, "体温计", 3));
        //血氧仪
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_bloodoxygen, false, "血氧仪", 2));
        //心电仪
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_ecg, false, "心电仪", 4));
        //三合一
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_threeinone, false, "三合一", 6));
        //体重秤
        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_weight, false, "体重秤", 7));
        //更多
//        this.deviceBeans.add(new ChooseDeviceBean(R.drawable.health_measure_image_more, false, "敬请期待", 0));
        if (deviceBeans != null) {
            for (DeviceBean deviceBean : deviceBeans) {
                switch (deviceBean.getName()) {
                    case "血压仪":
                        this.deviceBeans.get(0).setChoosed(true);
                        break;
                    case "血糖仪":
                        this.deviceBeans.get(1).setChoosed(true);
                        break;
                    case "体温计":
                        this.deviceBeans.get(2).setChoosed(true);
                        break;
                    case "血氧仪":
                        this.deviceBeans.get(3).setChoosed(true);
                        break;
                    case "心电仪":
                        this.deviceBeans.get(4).setChoosed(true);
                        break;
                    case "三合一":
                        this.deviceBeans.get(5).setChoosed(true);
                        break;
                    case "体重秤":
                        this.deviceBeans.get(6).setChoosed(true);
                        break;
                    default:
                        break;
                }
            }
        } else {
            //默认5件套
            this.deviceBeans.get(0).setChoosed(true);
            this.deviceBeans.get(1).setChoosed(true);
            this.deviceBeans.get(2).setChoosed(true);
            this.deviceBeans.get(3).setChoosed(true);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sure) {
            if (selectedDevices().size() == 0) {
                ToastUtils.showShort("请至少选择一个设备");
                return;
            }
            postDevices();

        } else {
        }
    }

    @SuppressLint("CheckResult")
    private void postDevices() {
        ArrayList<PostDeviceBean> postDeviceBeans = new ArrayList<>();
        for (ChooseDeviceBean bean : deviceBeans) {
            if (bean.getChoosed()) {
                PostDeviceBean postDeviceBean = new PostDeviceBean();
                postDeviceBean.setEquipmentId(DeviceUtils.getIMEI());
                postDeviceBean.setName(bean.getDeviceName());
                postDeviceBeans.add(postDeviceBean);
            }
        }

        HealthMeasureRepository.postUserHasedDevices(userId, postDeviceBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if (fragmentChanged != null) {
                            Bundle bundle = new Bundle();
                            bundle.putIntegerArrayList(KEY_DEVICE_NUM, selectedDevices());
                            fragmentChanged.onFragmentChanged(HealthChooseDevicesFragment.this, bundle);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("同步拥有的设备信息失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private ArrayList<Integer> selectedDevices() {
        ArrayList<Integer> deviceNum = new ArrayList<>();
        for (ChooseDeviceBean bean : deviceBeans) {
            if (bean.getChoosed()) {
                deviceNum.add(bean.getDeviceLevel());
            }
        }
        //TODO:默认体重必选,如果需要去掉，只需把下面代码去掉即可，其他地方不用修改
        if (!deviceNum.contains(7)){
            deviceNum.add(7);
        }
        return deviceNum;
    }

}
