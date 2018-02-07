package com.example.han.referralproject.measure.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class TizhongFragment extends Fragment {
    @BindView(R.id.test_tizhong)
    TextView testTizhong;
    @BindView(R.id.tv_tizhong)
    TextView tvTizhong;
    @BindView(R.id.rl_tizhong)
    RelativeLayout rlTizhong;
    @BindView(R.id.test_tizhi)
    TextView testTizhi;
    @BindView(R.id.tv_tizhi)
    TextView tvTizhi;
    @BindView(R.id.rl_tizhi)
    RelativeLayout rlTizhi;
    @BindView(R.id.btn_history)
    Button btnHistory;
    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tizhong, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((OnMeasureActivity)getActivity()).setOnMeasureListener(new MeasureListener() {
            @Override
            public void onSuccess(String data1, String data2, String data3, String tip) {
                if (data1!=null)
                    testTizhong.setText(data1);
                if (data2!=null)
                    testTizhi.setText(data2);
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
