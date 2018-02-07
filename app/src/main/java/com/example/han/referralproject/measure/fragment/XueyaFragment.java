package com.example.han.referralproject.measure.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.measure.MeasureListener;
import com.example.han.referralproject.measure.OnMeasureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class XueyaFragment extends Fragment {
    @BindView(R.id.iv_xueya1)
    ImageView ivXueya1;
    @BindView(R.id.iv_xueya2)
    ImageView ivXueya2;
    @BindView(R.id.iv_xueya3)
    ImageView ivXueya3;
    @BindView(R.id.high_pressure1)
    TextView highPressure1;
    @BindView(R.id.high_pressure)
    TextView highPressure;
    @BindView(R.id.rl_xueya_hight)
    RelativeLayout rlXueyaHight;
    @BindView(R.id.low_pressure1)
    TextView lowPressure1;
    @BindView(R.id.low_pressure)
    TextView lowPressure;
    @BindView(R.id.rl_xueya_low)
    RelativeLayout rlXueyaLow;
    @BindView(R.id.purse1)
    TextView purse1;
    @BindView(R.id.pulse)
    TextView pulse;
    @BindView(R.id.rl_maibo)
    RelativeLayout rlMaibo;
    @BindView(R.id.history1)
    Button history1;
    @BindView(R.id.xueya_video)
    Button xueyaVideo;
    @BindView(R.id.ll_xueya)
    LinearLayout llXueya;
    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xueya, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((OnMeasureActivity) getActivity()).setOnMeasureListener(new MeasureListener() {
            @Override
            public void onSuccess(String data1, String data2, String data3, String tip) {
                if (data1 != null)
                    highPressure.setText(data1);
                if (data2 != null)
                    lowPressure.setText(data2);
                if (data3 != null)
                    pulse.setText(data3);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
