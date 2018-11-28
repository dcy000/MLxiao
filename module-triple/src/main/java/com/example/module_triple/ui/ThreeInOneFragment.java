package com.example.module_triple.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_triple.R;
import com.example.module_triple.presenter.TriplePresenter;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/1 14:15
 * created by:gzq
 * description:TODO
 */
public class ThreeInOneFragment extends BaseBluetoothFragment implements View.OnClickListener, IBluetoothView {
    /**
     * 历史记录
     */
    protected TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    protected TextView mBtnVideoDemo;
    /**
     * 0
     */
    private TextView mTvGaoya;
    /**
     * 0
     */
    private TextView mTvDiya;
    /**
     * 0
     */
    private TextView mTvMaibo;
    /**
     * &lt;3.9
     */
    protected TextView mTitle11;
    /**
     * 3.9~6.1
     */
    protected TextView mTitle12;
    /**
     * &gt;6.1
     */
    protected TextView mTitle13;
    protected TextView mTitle1;
    private int sugarTime;


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
            if (datas[0].equals("bloodsugar")) {
                mTvGaoya.setText(datas[1]);
                MLVoiceSynthetize.startSynthesize("主人，您本次测量血糖" + datas[1]);
            } else if (datas[0].equals("bua")) {
                mTvDiya.setText(datas[1]);
                MLVoiceSynthetize.startSynthesize("主人，您本次测量尿酸" + datas[1]);
            } else if (datas[0].equals("cholesterol")) {
                mTvMaibo.setText(datas[1]);
                MLVoiceSynthetize.startSynthesize("主人，您本次测量胆固醇" + datas[1]);
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
        return R.layout.bluetooth_fragment_three_in_one;
    }

    @Override
    public void initParams(Bundle bundle) {
        if (bundle != null) {
            sugarTime = bundle.getInt("selectMeasureSugarTime", 0);
        }
    }

    @Override
    public void initView(View view) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvMaibo = view.findViewById(R.id.tv_maibo);
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTitle1 = (TextView) view.findViewById(R.id.title1);
        mTitle11 = (TextView) view.findViewById(R.id.title1_1);
        mTitle12 = (TextView) view.findViewById(R.id.title1_2);
        mTitle13 = (TextView) view.findViewById(R.id.title1_3);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new TriplePresenter(this, sugarTime);
    }

    public interface MeasureItemChanged {
        void onChanged(int position);
    }

    protected MeasureItemChanged measureItemChanged;

    public void setOnMeasureItemChanged(MeasureItemChanged measureItemChanged) {
        this.measureItemChanged = measureItemChanged;
    }
}
