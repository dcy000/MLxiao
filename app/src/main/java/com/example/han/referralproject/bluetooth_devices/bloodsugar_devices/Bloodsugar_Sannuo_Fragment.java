package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.AllMeasureActivity;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.util.ToastTool;

public class Bloodsugar_Sannuo_Fragment extends Fragment implements IView {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.bloodsugar_sannuo_fragment, container, false);
            new Bloodsugar_Sannuo_PresenterImp(this, new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, ""));
        }
        return view;
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length==0) {
            return;
        }


    }

    @Override
    public void updateState(String state) {
        ToastTool.showShort(state);
        ((AllMeasureActivity) getActivity()).speak(state);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
