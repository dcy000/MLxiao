package com.example.han.referralproject.measure.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.measure.MeasureChooseReason;
import com.example.han.referralproject.measure.MeasureExceptionAdapter;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureXueyaWarningFragment extends Fragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.other_reason)
    TextView otherReason;
    @BindView(R.id.measure_normal)
    TextView measureNormal;
    Unbinder unbinder;
    @BindView(R.id.iv_top_left)
    ImageView ivTopLeft;
    @BindView(R.id.tv_top_left)
    TextView tvTopLeft;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_top_right)
    TextView tvTopRight;
    @BindView(R.id.iv_top_right)
    ImageView ivTopRight;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    private View view;

    private ArrayList<Integer> reasons;
    private String firstTitle = "主人，您的测量数据与标准值相差较大，您是否存在以下情况：";
    private String titleString = "主人，您最新的测量数据与历史数据存在较大差异，您是否存在以下情况：";
    private MeasureExceptionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_measurexueya_warning, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTopTitle.setText("测量异常");
        title.setText(titleString);
        ((DetectActivity)getActivity()).speak(titleString);
        reasons = new ArrayList<>();
        initData();
        list.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        list.addItemDecoration(new GridViewDividerItemDecoration(15, 25));
        list.setAdapter(adapter = new MeasureExceptionAdapter(R.layout.xuetang_result_item, reasons));
        ivTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReason.noReason();
            }
        });
        ivTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0://服用了降压药
                        chooseReason.hasReason(0);
                        break;
                    case 1://臂带佩戴不正确
                        chooseReason.hasReason(1);
                        break;
                    case 2://坐姿不正确
                        chooseReason.hasReason(2);
                        break;
                    case 3://测量过程说话了
                        chooseReason.hasReason(3);
                        break;
                    case 4://饮酒、咖啡之后
                        chooseReason.hasReason(4);
                        break;
                    case 5://沐浴之后
                        chooseReason.hasReason(5);
                        break;
                    case 6://运动之后
                        chooseReason.hasReason(6);
                        break;
                    case 7://饭后一小时
                        chooseReason.hasReason(7);
                        break;

                }
            }
        });
        otherReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReason.hasReason(-1);
            }
        });
        measureNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReason.noReason();
            }
        });
        return view;
    }

    private void initData() {
        reasons.add(R.drawable.measure_jyy_sel);
        reasons.add(R.drawable.measure_bd_sel);
        reasons.add(R.drawable.measure_zz_sel);
        reasons.add(R.drawable.measure_sh_sel);
        reasons.add(R.drawable.measure_ykf_sel);
        reasons.add(R.drawable.measure_my_sel);
        reasons.add(R.drawable.measure_yd_sel);
        reasons.add(R.drawable.measure_fh_sel);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private MeasureChooseReason chooseReason;

    public void setOnChooseReason(MeasureChooseReason chooseReason) {
        this.chooseReason = chooseReason;
    }

}
