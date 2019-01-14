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
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.homepage.HospitalMainActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.measure.MeasureChooseReason;
import com.example.han.referralproject.measure.MeasureExceptionAdapter;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gzq on 2018/2/7.
 */

public class MeasureXuetangFragment extends Fragment {
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
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.other_reason)
    TextView otherReason;
    @BindView(R.id.measure_normal)
    TextView measureNormal;
    Unbinder unbinder;
    private View view;
    private String titleString = "主人，您最新的测量数据与历史数据存在较大差异，您是否存在以下情况：";
    private ArrayList<Integer> reasons;
    private MeasureExceptionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_measurexuetang_warning, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTopTitle.setText("测量异常");
        title.setText(titleString);
        ((DetectActivity) getActivity()).speak(titleString);
        reasons = new ArrayList<>();
        initData();
        list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        list.addItemDecoration(new GridViewDividerItemDecoration(15, 25));
        list.setAdapter(adapter = new MeasureExceptionAdapter(R.layout.xuetang_result_item, reasons));

        ivTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReason != null) {
                    chooseReason.noReason();
                }
            }
        });
        ivTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HospitalMainActivity.class));
                getActivity().finish();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (chooseReason == null) {
                    return;
                }
                switch (position) {
                    case 0://选择时间错误
                        chooseReason.hasReason(0);
                        break;
                    case 1://未擦掉第一滴血
                        chooseReason.hasReason(1);
                        break;
                    case 2://试纸过期
                        chooseReason.hasReason(2);
                        break;
                    case 3://血液暴露时间太久
                        chooseReason.hasReason(3);
                        break;
                    case 4://彩雪方法不对
                        chooseReason.hasReason(4);
                        break;
                    case 5://血糖仪未清洁
                        chooseReason.hasReason(5);
                        break;

                }
            }
        });
        otherReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReason != null) {
                    chooseReason.hasReason(-1);
                }
            }
        });
        measureNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReason != null) {
                    chooseReason.noReason();
                }
            }
        });
        return view;
    }

    private void initData() {
        reasons.add(R.drawable.measure_xzsj_sel);
        reasons.add(R.drawable.measure_dydx_sel);
        reasons.add(R.drawable.measure_gq_sel);
        reasons.add(R.drawable.measure_bltj_sel);
        reasons.add(R.drawable.measure_cx_sel);
        reasons.add(R.drawable.measure_qj_sel);
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
