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
public class XueyangFragment extends Fragment {
    @BindView(R.id.iv_xueyang)
    ImageView ivXueyang;
    @BindView(R.id.test_xueyang)
    TextView testXueyang;
    @BindView(R.id.tv_xue_yang)
    TextView tvXueYang;
    @BindView(R.id.rl_xueyang)
    RelativeLayout rlXueyang;
    @BindView(R.id.test_maibo)
    TextView testMaibo;
    @BindView(R.id.tv_xueyang_pulse)
    TextView tvXueyangPulse;
    @BindView(R.id.rl_maibo)
    RelativeLayout rlMaibo;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_video)
    Button btnVideo;
    @BindView(R.id.ll_xueyang)
    LinearLayout llXueyang;
    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_xueyang, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((OnMeasureActivity)getActivity()).setOnMeasureListener(new MeasureListener() {
            @Override
            public void onSuccess(String data1, String data2, String data3, String tip) {
                if (data1!=null)
                    tvXueYang.setText(data1);
                if (data2!=null)
                    tvXueyangPulse.setText(data2);
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
