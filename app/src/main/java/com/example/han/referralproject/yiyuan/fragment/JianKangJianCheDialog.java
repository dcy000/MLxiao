package com.example.han.referralproject.yiyuan.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.han.referralproject.R;
import com.example.han.referralproject.yiyuan.bean.MainTiZHiDialogBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/5/26.
 */

public class JianKangJianCheDialog extends DialogFragment {

    @BindView(R.id.jiankang_tijian)
    ImageView jiankangTijian;
    @BindView(R.id.gaoxueya_tijian)
    ImageView gaoxueyaTijian;
    @BindView(R.id.tangniaobing_tijian)
    ImageView tangniaobingTijian;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    private ClickItemListener listener;
    private List<MainTiZHiDialogBean> data;

    public void setListener(ClickItemListener listener, List<MainTiZHiDialogBean> data) {
        this.listener = listener;
        this.data = data;
    }

    @OnClick({R.id.jiankang_tijian, R.id.gaoxueya_tijian, R.id.tangniaobing_tijian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jiankang_tijian:
                clickItem(data.get(0).name);
                break;
            case R.id.gaoxueya_tijian:
                clickItem(data.get(1).name);
                break;
            case R.id.tangniaobing_tijian:
                clickItem(data.get(2).name);
                break;
        }
    }

    private void clickItem(String itemName) {
        if (listener != null) {
            listener.onJianceItemClick(itemName);
        }
    }


    interface ClickItemListener {
        void onJianceItemClick(String name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jiankan_jiance_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Glide.with(this).load(data.get(0).iconId).into(jiankangTijian);
        Glide.with(this).load(data.get(1).iconId).into(gaoxueyaTijian);
        Glide.with(this).load(data.get(2).iconId).into(tangniaobingTijian);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}


