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
public class SanheyiFragment extends Fragment {
    @BindView(R.id.iv_xuetang)
    ImageView ivXuetang;
    @BindView(R.id.iv_niaosuan)
    ImageView ivNiaosuan;
    @BindView(R.id.iv_danguchun)
    ImageView ivDanguchun;
    @BindView(R.id.tv_san_tag)
    TextView tvSanTag;
    @BindView(R.id.tv_san_one)
    TextView tvSanOne;
    @BindView(R.id.rl_xuetang)
    RelativeLayout rlXuetang;
    @BindView(R.id.tv_san_tag_two)
    TextView tvSanTagTwo;
    @BindView(R.id.tv_san_two)
    TextView tvSanTwo;
    @BindView(R.id.rl_niaosuan)
    RelativeLayout rlNiaosuan;
    @BindView(R.id.tv_san_tag_three)
    TextView tvSanTagThree;
    @BindView(R.id.tv_san_three)
    TextView tvSanThree;
    @BindView(R.id.rl_danguchun)
    RelativeLayout rlDanguchun;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_video)
    Button btnVideo;
    @BindView(R.id.ll_sanheyi)
    LinearLayout llSanheyi;
    Unbinder unbinder;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sanheyi, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((OnMeasureActivity)getActivity()).setOnMeasureListener(new MeasureListener() {
            @Override
            public void onSuccess(String data1, String data2, String data3, String tip) {
                if (data1!=null)
                    tvSanOne.setText(data1);
                if (data2!=null)
                    tvSanTwo.setText(data2);
                if (data3 != null)
                    tvSanThree.setText(data3);
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
