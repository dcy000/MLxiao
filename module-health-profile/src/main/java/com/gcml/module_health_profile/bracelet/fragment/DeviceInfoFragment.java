package com.gcml.module_health_profile.bracelet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_profile.R;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class DeviceInfoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView deviceName;
    private TextView deviceImei;
    private TextView deviceOwner;
    private TextView bindPhone;

    public DeviceInfoFragment() {
    }

    public static DeviceInfoFragment newInstance(String param1, String param2) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_device_info, container, false);
        deviceName = inflate.findViewById(R.id.tv_deviece_name);
        deviceImei = inflate.findViewById(R.id.tv_device_imei);
        deviceOwner = inflate.findViewById(R.id.tv_device_owner);
        bindPhone = inflate.findViewById(R.id.tv_bind_phone);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        CCResult result;
        Observable<UserEntity> rxUser;
        result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
        rxUser = result.getDataItem("data");
        rxUser.subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        deviceImei.setText(user.watchCode);
                        deviceOwner.setText(user.name);
                        bindPhone.setText(user.phone);
                    }
                });
    }

    private List<String> falseData() {
        return Arrays.asList("小米手环", "SDJK_SDJD_SDJD_SUIO", "张辉", "1865521286");
    }

}
