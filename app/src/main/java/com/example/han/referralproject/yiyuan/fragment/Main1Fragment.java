package com.example.han.referralproject.yiyuan.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/5/17.
 */

public class Main1Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main1_fragment, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
