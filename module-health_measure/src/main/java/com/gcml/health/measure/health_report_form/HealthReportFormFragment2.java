package com.gcml.health.measure.health_report_form;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.littlejie.circleprogress.CircleProgress;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 18:15
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormFragment2 extends BluetoothBaseFragment implements View.OnClickListener {
    private CircleProgress mCpChart;
    /**
     * 您的高血压发病率为5%
     */
    private TextView mTvResultProbability;
    private ConstraintLayout mClLeft;
    /**
     * 危险因素
     */
    private TextView mTvIndex;
    /**
     * 结果
     */
    private TextView mTvResult;
    /**
     * 参考标准
     */
    private TextView mTvRange;
    private RecyclerView mRvReport;
    private ConstraintLayout mClRight;
    /**
     * 根据结果，您已经患有高血压。具体评估结果和建议
     */
    private TextView mTvBottom;
    /**
     * 请查看详情》
     */
    private TextView mTvSeeDetail;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_report_form2;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mCpChart = (CircleProgress) view.findViewById(R.id.cp_chart);
        mTvResultProbability = (TextView) view.findViewById(R.id.tv_result_probability);
        mClLeft = (ConstraintLayout) view.findViewById(R.id.cl_left);
        mTvIndex = (TextView) view.findViewById(R.id.tv_index);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvRange = (TextView) view.findViewById(R.id.tv_range);
        mRvReport = (RecyclerView) view.findViewById(R.id.rv_report);
        mClRight = (ConstraintLayout) view.findViewById(R.id.cl_right);
        mTvBottom = (TextView) view.findViewById(R.id.tv_bottom);
        mTvSeeDetail = (TextView) view.findViewById(R.id.tv_see_detail);
        setData();
        mTvSeeDetail.setOnClickListener(this);
    }

    private void setData() {
        mCpChart.setValueWithString("低风险");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_see_detail) {
        } else {
        }
    }
}
