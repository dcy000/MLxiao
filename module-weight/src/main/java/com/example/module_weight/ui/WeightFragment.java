package com.example.module_weight.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.module_weight.R;
import com.example.module_weight.presenter.WeightPresenter;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class WeightFragment extends BaseBluetoothFragment implements IBluetoothView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvTizhong;
    protected TextView mTvTizhi;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
        } else if (i == R.id.btn_video_demo) {
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            if (mTvTizhong != null) {
                mTvTizhong.setText("0.00");
            }
        } else if (datas.length == 1) {
            if (mTvTizhong != null) {
                mTvTizhong.setText(datas[0]);
            }
        } else if (datas.length == 3) {
            if (mTvTizhong != null) {
                mTvTizhong.setText(datas[2]);

                float weight = Float.parseFloat(datas[2]);

                UserInfoBean user = Box.getSessionManager().getUser();
                String height = user.height;
                if (TextUtils.isEmpty(height)) {
                    ToastUtils.showShort("请去个人中心完善您的身高信息");
                    MLVoiceSynthetize.startSynthesize("主人，您本次测量体重" + datas[2] + "公斤。" +
                            "主人，请去个人中心完善您的身高信息，小E才能为您计算体质。");
                    return;
                }
                float parseFloat = Float.parseFloat(height);
                String tizhi = String.format("%.2f", weight / (parseFloat * parseFloat / 10000));
                mTvTizhi.setText(tizhi);
                MLVoiceSynthetize.startSynthesize("主人，您本次测量体重" + datas[2] + "公斤，体质" + tizhi);
            }
        }
    }

    @Override
    public void updateState(String state) {
        if (stateUpdate2Activity != null) {
            stateUpdate2Activity.onStateChanged(state);
        }
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(state);
    }

    @Override
    public boolean isUploadData() {
        return true;
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.bluetooth_fragment_weight;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvTizhong = view.findViewById(R.id.tv_tizhong);
        mTvTizhi = view.findViewById(R.id.tv_tizhi);
        mTvTizhong.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvTizhi.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public IPresenter obtainPresenter() {
        return new WeightPresenter(this);
    }
}
