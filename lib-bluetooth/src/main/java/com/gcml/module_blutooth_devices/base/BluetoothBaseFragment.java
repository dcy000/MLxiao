package com.gcml.module_blutooth_devices.base;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.common.utils.click.ClickEventListener;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenPresenter;
import com.gcml.module_blutooth_devices.dialog.BluetoothDialog;
import com.gcml.module_blutooth_devices.dialog.ChooseBluetoothDevice;

public abstract class BluetoothBaseFragment extends Fragment implements IBluetoothView, ChooseBluetoothDevice {
    protected View view = null;
    protected boolean isMeasureFinishedOfThisTime = false;
    protected Context mContext;
    protected Activity mActivity;
    protected BaseBluetooth basePresenter;
    protected BluetoothDialog bluetoothDialog;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //全局控制上下文 子类统一使用该引用，避免异步任务结束后getActivity引起的空指针异常，但同时这种做法有内存泄漏的风险
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(initLayout(), container, false);
            initView(view, getArguments());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        basePresenter = obtainPresenter();
    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {
        if (bluetoothDialog != null) {
            bluetoothDialog.addDevice(device);
        }
    }

    public void showBluetoothDialog() {
        if (bluetoothDialog == null) {
            bluetoothDialog = new BluetoothDialog(mContext, this, IPresenter.MEASURE_BLOOD_OXYGEN);
            bluetoothDialog.setChooseBluetoothDeviceListener(this);
        }
        if (!bluetoothDialog.isShowing()) {
            bluetoothDialog.show();
            if (basePresenter != null && !basePresenter.isOnSearching()) {
                basePresenter.startDiscovery(null);
            }
        } else {
            if (bluetoothDialog != null) {
                bluetoothDialog.dismiss();
            }
            bluetoothDialog = null;
        }
    }

    @Override
    public void choosed(BluetoothDevice device) {
        if (basePresenter != null) {
            basePresenter.startConnect(device);
        }
    }

    @Override
    public void autoConnect() {
        if (basePresenter != null) {
            basePresenter.startDiscovery(null);
        }
    }

    @Override
    public void cancelSearch() {
        if (basePresenter != null) {
            basePresenter.stopDiscovery();
        }
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    protected BaseBluetooth obtainPresenter() {
        return null;
    }

    protected abstract int initLayout();

    protected abstract void initView(View view, Bundle bundle);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            view = null;
        }
        isMeasureFinishedOfThisTime = false;
        dealVoiceAndJump = null;
        fragmentChanged = null;
    }

    /**
     * Find出来的View，自带防抖功能
     */
    public <T extends View> T findClickView(int id) {

        T viewA = (T) view.findViewById(id);
        viewA.setOnClickListener(new ClickEventListener(this));
        return viewA;
    }

    protected DealVoiceAndJump dealVoiceAndJump;

    public void setOnDealVoiceAndJumpListener(DealVoiceAndJump dealVoiceAndJump) {
        this.dealVoiceAndJump = dealVoiceAndJump;
    }

    protected FragmentChanged fragmentChanged;

    public void setOnFragmentChangedListener(FragmentChanged fragmentChanged) {
        this.fragmentChanged = fragmentChanged;
    }

    protected ThisFragmentDatas fragmentDatas;

    public void setOnThisFragmentDataChangedListener(ThisFragmentDatas fragmentDatas) {
        this.fragmentDatas = fragmentDatas;
    }

    protected void clickHealthHistory(View view) {
    }

    protected void clickVideoDemo(View view) {
    }

    protected void onMeasureFinished(String... results) {
    }

    protected void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(getContext())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }

    protected void showUploadDataFailedDialog(String[] mResults) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_first_diagnosis_upload_failed)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.getView(R.id.btn_neg).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickHealthHistory(null);
                                dialog.dismiss();
                            }
                        });
                        holder.getView(R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onMeasureFinished(mResults);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getFragmentManager());
    }
}
