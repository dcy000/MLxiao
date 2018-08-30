package com.gcml.health.measure.health_report_form;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 18:15
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormFragment3 extends BluetoothBaseFragment implements View.OnClickListener {
    /**
     * 已确诊
     */
    private TextView mTvTitle;
    /**
     * 高血压
     */
    private TextView mTvResultFlag;
    /**
     * 您的血压控制较差，
     */
    private TextView mTvResult1;
    /**
     * 未达标
     */
    private TextView mTvResult2;
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
     * 根据结果，您已经患有高血压。具体评估结果和建议具体评估结果和建议具体评估结果和建议具体评估结果和建议
     */
    private TextView mTvBottom;
    /**
     * 请查看详情》
     */
    private TextView mTvSeeDetail;
    private Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_report_form3;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvResultFlag = (TextView) view.findViewById(R.id.tv_result_flag);
        mTvResult1 = (TextView) view.findViewById(R.id.tv_result_1);
        mTvResult2 = (TextView) view.findViewById(R.id.tv_result_2);
        mClLeft = (ConstraintLayout) view.findViewById(R.id.cl_left);
        mTvIndex = (TextView) view.findViewById(R.id.tv_index);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvRange = (TextView) view.findViewById(R.id.tv_range);
        mRvReport = (RecyclerView) view.findViewById(R.id.rv_report);
        mClRight = (ConstraintLayout) view.findViewById(R.id.cl_right);
        mTvBottom = (TextView) view.findViewById(R.id.tv_bottom);
        mTvSeeDetail = (TextView) view.findViewById(R.id.tv_see_detail);
        mTvSeeDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_see_detail) {
            HealthReportFormDetailActivity.startActivity(context);

        } else {
        }
    }
}
