package com.gcml.module_inquiry.inquiry.ui.fragment.base;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 2019/3/21.
 */

@SuppressLint("ValidFragment")
public abstract class InquiryBaseFrament extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected void config() {
    }

    protected abstract int layoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected ChildActionListenerAdapter listenerAdapter;

    public void setListenerAdapter(ChildActionListenerAdapter listenerAdapter) {
        this.listenerAdapter = listenerAdapter;
    }

}
