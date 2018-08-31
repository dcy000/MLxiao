package com.gcml.health.measure.health_report_form;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.health.measure.widget.ColorfulProgressBar;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 18:15
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormFragment1 extends BluetoothBaseFragment {


    private View view;
    /**
     * 疾病风险评估结果
     */
    private TextView mTvResultTitle;
    /**
     * 糖尿病
     */
    private TextView mTvTitleBloodsugar;
    private ColorfulProgressBar mCpbBloodsugar;
    /**
     * 已确诊
     */
    private TextView mTvBloodsugarSure;
    /**
     * 高血压
     */
    private TextView mTvTitleBloodpressure;
    private ColorfulProgressBar mCpbBloodpressure;
    /**
     * 已确诊
     */
    private TextView mTvBloodpressureSure;
    /**
     * 肥胖症
     */
    private TextView mTvTitleObesity;
    private ColorfulProgressBar mCpbObesity;
    /**
     * 已确诊
     */
    private TextView mTvObesitySure;
    /**
     * 心血管病
     */
    private TextView mTvTitleAngiocardiopathy;
    private ColorfulProgressBar mCpbAngiocardiopathy;
    /**
     * 已确诊
     */
    private TextView mTvAngiocardiopathySure;
    private ConstraintLayout mClLeft;
    /**
     * 指标
     */
    private TextView mTvIndex;
    /**
     * 结果
     */
    private TextView mTvResult;
    /**
     * 范围
     */
    private TextView mTvRange;
    private RecyclerView mRvReport;
    private ConstraintLayout mClRight;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_report_form1;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mTvResultTitle = (TextView) view.findViewById(R.id.tv_result_title);
        mTvTitleBloodsugar = (TextView) view.findViewById(R.id.tv_title_bloodsugar);
        mCpbBloodsugar = (ColorfulProgressBar) view.findViewById(R.id.cpb_bloodsugar);
        mTvBloodsugarSure = (TextView) view.findViewById(R.id.tv_bloodsugar_sure);
        mTvTitleBloodpressure = (TextView) view.findViewById(R.id.tv_title_bloodpressure);
        mCpbBloodpressure = (ColorfulProgressBar) view.findViewById(R.id.cpb_bloodpressure);
        mTvBloodpressureSure = (TextView) view.findViewById(R.id.tv_bloodpressure_sure);
        mTvTitleObesity = (TextView) view.findViewById(R.id.tv_title_obesity);
        mCpbObesity = (ColorfulProgressBar) view.findViewById(R.id.cpb_obesity);
        mTvObesitySure = (TextView) view.findViewById(R.id.tv_obesity_sure);
        mTvTitleAngiocardiopathy = (TextView) view.findViewById(R.id.tv_title_angiocardiopathy);
        mCpbAngiocardiopathy = (ColorfulProgressBar) view.findViewById(R.id.cpb_angiocardiopathy);
        mTvAngiocardiopathySure = (TextView) view.findViewById(R.id.tv_angiocardiopathy_sure);
        mClLeft = (ConstraintLayout) view.findViewById(R.id.cl_left);
        mTvIndex = (TextView) view.findViewById(R.id.tv_index);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvRange = (TextView) view.findViewById(R.id.tv_range);
        mRvReport = (RecyclerView) view.findViewById(R.id.rv_report);
        mClRight = (ConstraintLayout) view.findViewById(R.id.cl_right);
        setData();
    }

    private void setData() {
        mCpbBloodsugar.setLevel(5);
        mTvBloodsugarSure.setVisibility(View.GONE);
        mCpbBloodpressure.setLevel(4);
        mTvBloodpressureSure.setVisibility(View.GONE);
        mCpbObesity.setLevel(2);
        mTvObesitySure.setVisibility(View.GONE);
        mCpbAngiocardiopathy.setVisibility(View.GONE);

    }
}
