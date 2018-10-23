package com.gcml.health.measure.health_report_form;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportParseBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.littlejie.circleprogress.CircleProgress;

import java.util.ArrayList;
import java.util.List;

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
    private String type;
    private FirstReportReceiveBean firstReportBean;
    private Drawable drawableUp;
    private Drawable drawableDown;
    private Drawable drawableWarning;
    private String result;
    private String advice;
    private String morbidity;
    private int riskLevel=1;
    private List<FirstReportReceiveBean.FactorListBeanX> factorList;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_report_form2;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mCpChart = (CircleProgress) view.findViewById(R.id.cp_chart);
        mCpChart.setMaxValue(30);
        mTvResultProbability = (TextView) view.findViewById(R.id.tv_result_probability);
        mClLeft = (ConstraintLayout) view.findViewById(R.id.cl_left);
        mTvIndex = (TextView) view.findViewById(R.id.tv_index);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvRange = (TextView) view.findViewById(R.id.tv_range);
        mRvReport = (RecyclerView) view.findViewById(R.id.rv_report);
        mClRight = (ConstraintLayout) view.findViewById(R.id.cl_right);
        mTvBottom = (TextView) view.findViewById(R.id.tv_bottom);
        mTvSeeDetail = (TextView) view.findViewById(R.id.tv_see_detail);
        mTvSeeDetail.setOnClickListener(this);

        // 找到资源图片
        drawableUp = getResources().getDrawable(R.drawable.health_measure_ic_danger_up);
        // 这一步必须要做，否则不会显示。// 设置图片宽高
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        // 找到资源图片
        drawableDown = getResources().getDrawable(R.drawable.health_measure_ic_danger_down);
        // 这一步必须要做，否则不会显示。// 设置图片宽高
        drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(), drawableDown.getMinimumHeight());
        // 找到资源图片
        drawableWarning = getResources().getDrawable(R.drawable.health_measure_danger_warning);
        // 这一步必须要做，否则不会显示。// 设置图片宽高
        drawableWarning.setBounds(0, 0, drawableWarning.getMinimumWidth(), drawableWarning.getMinimumHeight());
        setData(bundle);
    }

    private void setData(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getString(HealthReportFormActivity.KEY_TYPE);
            firstReportBean = bundle.getParcelable(HealthReportFormActivity.KEY_DATA);
            List<FirstReportReceiveBean.ReportListBean> reportList = firstReportBean.getReportList();

            for (FirstReportReceiveBean.ReportListBean reportListBean:reportList){
                if (reportListBean.getIllnessName().equals(type)){
                    factorList=reportListBean.getFactorList();
                    result=reportListBean.getResult();
                    advice=reportListBean.getAdvice();
                    morbidity=reportListBean.getMorbidity();
                    riskLevel=reportListBean.getRiskLevel();
                    break;
                }
            }
            mTvResultProbability.setText("您的" + type + "发病率为" + morbidity + "%");
            switch (riskLevel) {
                case 1:
                    mCpChart.setValueWithString("低风险");
                    mTvBottom.setText("您未来"+type+"发病等级为低风险。小E给您的建议，");
                    mCpChart.setValue(6.0f);
                    safeGetColorArrays(R.array.circle_progress_color_0DD192);
                    break;
                case 2:
                    mCpChart.setValueWithString("较低风险");
                    mTvBottom.setText("您未来"+type+"发病等级为较低风险。小E给您的建议，");
                    mCpChart.setValue(12.0f);
                    safeGetColorArrays(R.array.circle_progress_color_FFCE00);
                    break;
                case 3:
                    mCpChart.setValueWithString("中等风险");
                    mTvBottom.setText("您未来"+type+"发病等级为中等风险。小E给您的建议，");
                    mCpChart.setValue(18.0f);
                    safeGetColorArrays(R.array.circle_progress_color_F3AA43);
                    break;
                case 4:
                    mCpChart.setValueWithString("较高风险");
                    mTvBottom.setText("您未来"+type+"发病等级为较高风险。小E给您的建议，");
                    mCpChart.setValue(24.0f);
                    safeGetColorArrays(R.array.circle_progress_color_F08A40);
                    break;
                case 5:
                    mCpChart.setValueWithString("高风险");
                    mTvBottom.setText("您未来"+type+"发病等级为高风险。小E给您的建议，");
                    mCpChart.setValue(30.0f);
                    safeGetColorArrays(R.array.circle_progress_color_F56C6B);
                    break;
                default:
                    break;
            }
            List<FirstReportParseBean> firstReportParseBeans = new ArrayList<>();
            for (FirstReportReceiveBean.FactorListBeanX xxxx : factorList) {
                for (FirstReportReceiveBean.ListBean beanXXXX : xxxx.getList()) {
                    FirstReportParseBean firstReportParseBean = new FirstReportParseBean();
                    firstReportParseBean.setAnomalyStatus(beanXXXX.getAnomalyStatus());
                    firstReportParseBean.setFactorCode(beanXXXX.getFactorCode());
                    firstReportParseBean.setFactorName(xxxx.getFactorName());
                    firstReportParseBean.setFatherCode(beanXXXX.getFatherCode());
                    firstReportParseBean.setHdFactorRecordId(beanXXXX.getHdFactorRecordId());
                    firstReportParseBean.setHdHealthSurveyId(beanXXXX.getHdHealthSurveyId());
                    firstReportParseBean.setReference(xxxx.getReference());
                    firstReportParseBean.setScore(beanXXXX.getScore());
                    firstReportParseBean.setUserId(beanXXXX.getUserId());
                    firstReportParseBean.setValue(beanXXXX.getValue());
                    firstReportParseBeans.add(firstReportParseBean);
                }
            }
            setRecycleview(firstReportParseBeans);
        } else {
            ToastUtils.showShort("传递的数据未获取到");
        }

    }
    private void safeGetColorArrays(int colorArray){
        Resources resources = getResources();
        if (resources!=null){
            int[] intArray = resources.getIntArray(colorArray);
            if (intArray!=null){
                mCpChart.setGradientColors(intArray);
            }

        }
    }
    private void setRecycleview(List<FirstReportParseBean> factorList) {
        mRvReport.setLayoutManager(new LinearLayoutManager(mContext));
        mRvReport.setAdapter(new BaseQuickAdapter<FirstReportParseBean, BaseViewHolder>(R.layout.health_measure_item_risk_assessment, factorList) {
            @Override
            protected void convert(BaseViewHolder helper, FirstReportParseBean item) {
                int adapterPosition = helper.getAdapterPosition();
                String anomalyStatus = item.getAnomalyStatus();
                String factorName = null;
                if (adapterPosition > 0) {
                    factorName = factorList.get(adapterPosition - 1).getFactorName();
                }
                if (!TextUtils.isEmpty(factorName)) {
                    if (!factorName.equals(item.getFactorName())) {
                        helper.setText(R.id.tv_left, item.getFactorName());
                    }
                } else {
                    helper.setText(R.id.tv_left, item.getFactorName());
                }
                helper.setText(R.id.tv_middle, item.getValue());
                helper.setText(R.id.tv_right, item.getReference());
                if ("0".equals(anomalyStatus)) {
                    ((TextView) helper.getView(R.id.tv_middle)).setTextColor(Color.parseColor("#333333"));
                    ((TextView) helper.getView(R.id.tv_middle)).setCompoundDrawables(null, null, null, null);
                } else if ("1".equals(anomalyStatus)) {
                    ((TextView) helper.getView(R.id.tv_middle)).setCompoundDrawables(null, null, drawableWarning, null);
                } else if ("2".equals(anomalyStatus)) {
                    ((TextView) helper.getView(R.id.tv_middle)).setCompoundDrawables(null, null, drawableUp, null);
                } else if ("3".equals(anomalyStatus)) {
                    ((TextView) helper.getView(R.id.tv_middle)).setCompoundDrawables(null, null, drawableDown, null);
                }else{

                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_see_detail) {
            HealthReportFormDetailActivity.startActivity(mContext,result,advice);
        } else {
        }
    }
}
