package com.gcml.health.measure.measure_abnormal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.health.measure.R;
import com.gcml.health.measure.divider.GridViewDividerItemDecoration;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureXueyaWarningFragment extends HealthMeasureAbnormalBaseFragment implements View.OnClickListener {

    private ArrayList<Integer> reasons;
    private String titleString = "您最新的测量数据与历史数据存在较大差异，您是否存在以下情况：";
    /**
     * 您最新的测量数据与历史数据存在较大偏差，您是否在测量时存在以下情况
     */
    private TextView mTitle;
    private RecyclerView mList;
    /**
     * 其他原因
     */
    private TextView mOtherReason;
    /**
     * 测量正常
     */
    private TextView mMeasureNormal;
    private BaseQuickAdapter<Integer, BaseViewHolder> adapter;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_measurexueya_warning;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(titleString);
        mList = (RecyclerView) view.findViewById(R.id.list);
        mOtherReason = (TextView) view.findViewById(R.id.other_reason);
        mOtherReason.setOnClickListener(this);
        mMeasureNormal = (TextView) view.findViewById(R.id.measure_normal);
        mMeasureNormal.setOnClickListener(this);
        initData();
        initAdapter();
    }

    private void initAdapter() {
        mList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mList.addItemDecoration(new GridViewDividerItemDecoration(15, 25));
        mList.setAdapter(adapter = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.health_measure_item_abnormal, reasons) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                ((ImageView) helper.getView(R.id.title)).setImageResource(item);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (chooseReason == null) {
                    return;
                }
                switch (position) {
                    case 0:
                        //服用了降压药
                        chooseReason.hasReason(0);
                        break;
                    case 1:
                        //臂带佩戴不正确
                        chooseReason.hasReason(1);
                        break;
                    case 2:
                        //坐姿不正确
                        chooseReason.hasReason(2);
                        break;
                    case 3:
                        //测量过程说话了
                        chooseReason.hasReason(3);
                        break;
                    case 4:
                        //饮酒、咖啡之后
                        chooseReason.hasReason(4);
                        break;
                    case 5:
                        //沐浴之后
                        chooseReason.hasReason(5);
                        break;
                    case 6:
                        //运动之后
                        chooseReason.hasReason(6);
                        break;
                    case 7:
                        //饭后一小时
                        chooseReason.hasReason(7);
                        break;
                    default:
                        break;

                }
            }
        });
    }

    private void initData() {
        reasons = new ArrayList<>();
        reasons.add(R.drawable.health_measure_ic_jyy);
        reasons.add(R.drawable.health_measure_ic_bd);
        reasons.add(R.drawable.health_measure_ic_zs);
        reasons.add(R.drawable.health_measure_ic_sh);
        reasons.add(R.drawable.health_measure_ic_ykf);
        reasons.add(R.drawable.health_measure_ic_my);
        reasons.add(R.drawable.health_measure_ic_yd);
        reasons.add(R.drawable.health_measure_ic_fh_warning);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.other_reason) {
            if (chooseReason != null) {
                chooseReason.hasReason(-1);
            }

        } else if (i == R.id.measure_normal) {
            if (chooseReason != null) {
                chooseReason.noReason();
            }

        } else {

        }
    }
}
