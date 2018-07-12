package com.gcml.module_blutooth_devices.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    protected View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(initLayout(), container, false);
            initView(view, getArguments());
        }
        return view;
    }

    protected abstract int initLayout();

    protected abstract void initView(View view, Bundle bundle);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    protected DealVoiceAndJump dealVoiceAndJump;

    public void setOnDealVoiceAndJumpListener(DealVoiceAndJump dealVoiceAndJump) {
        this.dealVoiceAndJump = dealVoiceAndJump;
    }
}
