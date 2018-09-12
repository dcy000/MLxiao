package com.gcml.health.measure.health_report_form;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportParseBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

import java.util.ArrayList;
import java.util.List;

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
    private String type;
    private FirstReportReceiveBean firstReportBean;
    private Drawable drawableUp;
    private Drawable drawableDown;
    private Drawable drawableWarning;
    private String result;
    private String advice;
    private int riskLevel=1;
    private List<FirstReportReceiveBean.FactorListBeanX> factorList;

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
        if (bundle!=null){
            type = bundle.getString(HealthReportFormActivity.KEY_TYPE);
            firstReportBean = bundle.getParcelable(HealthReportFormActivity.KEY_DATA);

            mTvResultFlag.setText(type);
            //0:达标，1：不达标
            List<FirstReportReceiveBean.ReportListBean> reportList = firstReportBean.getReportList();
            for (FirstReportReceiveBean.ReportListBean reportListBean:reportList){
                String controlStatus = reportListBean.getControlStatus();
                String illnessName = reportListBean.getIllnessName();
                if (illnessName.equals(type)){
                    if ("0".equals(controlStatus)){
                        mTvResult1.setText("您的"+type+"控制较好,");
                        mTvResult2.setText("达标");
                    }else{
                        mTvResult1.setText("您的"+type+"控制较差,");
                        mTvResult2.setText("未达标");
                    }
                    factorList=reportListBean.getFactorList();
                    riskLevel=reportListBean.getRiskLevel();
                    result=reportListBean.getResult();
                    advice=reportListBean.getAdvice();
                    break;
                }

            }
            switch (riskLevel) {
                case 1:
                    mTvBottom.setText("您未来"+type+"发病等级为低风险。小E给您的建议，");
                    break;
                case 2:
                    mTvBottom.setText("您未来"+type+"发病等级为较低风险。小E给您的建议，");
                    break;
                case 3:
                    mTvBottom.setText("您未来"+type+"发病等级为中等风险。小E给您的建议，");
                    break;
                case 4:
                    mTvBottom.setText("您未来"+type+"发病等级为较高风险。小E给您的建议，");
                    break;
                case 5:
                    mTvBottom.setText("您未来"+type+"发病等级为高风险。小E给您的建议，");
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
        }else{
            ToastUtils.showShort("传递数据失败");
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
