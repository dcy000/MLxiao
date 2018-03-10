package com.example.han.referralproject.settting.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/3/10.
 */

public class SportAdviceFragment extends Fragment {
    private TextView text;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sport_advice,container,false);
        return view;
    }
}
