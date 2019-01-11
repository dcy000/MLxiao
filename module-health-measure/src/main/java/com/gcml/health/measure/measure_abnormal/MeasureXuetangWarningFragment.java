package com.gcml.health.measure.measure_abnormal;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.health.measure.R;
import com.gcml.health.measure.divider.GridViewDividerItemDecoration;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;

import java.util.ArrayList;


/**
 * Created by gzq on 2018/2/7.
 */

public class MeasureXuetangWarningFragment extends HealthMeasureAbnormalBaseFragment implements View.OnClickListener {
    private String titleString = "主人，您最新的测量数据与历史数据存在较大差异，您是否存在以下情况：";
    private ArrayList<Integer> reasons;
    private BaseQuickAdapter<Integer, BaseViewHolder> adapter;
    /**
     * 主人，您最新的测量数据与历史数据存在较大偏差，您是否在测量时存在以下情况
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
        initData();
        initAdapter();

    }


    private void initAdapter() {
        mList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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
    }

    private void initData() {
        reasons = new ArrayList<>();
        reasons.add(R.drawable.health_measure_ic_xzsj);
        reasons.add(R.drawable.health_measure_ic_dydx);
        reasons.add(R.drawable.health_measure_ic_gq);
        reasons.add(R.drawable.health_measure_ic_bltj);
        reasons.add(R.drawable.health_measure_ic_cx);
        reasons.add(R.drawable.health_measure_ic_qj);
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
