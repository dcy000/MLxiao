package com.gcml.health.measure.health_report_form;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.gcml.health.measure.widget.ColorfulProgressBar;
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
public class HealthReportFormFragment1 extends BluetoothBaseFragment {

    private FirstReportReceiveBean firstReportBean;
    private Drawable drawableUp;
    private Drawable drawableDown;
    private Drawable drawableWarning;
    private RecyclerView mRvDisease;
    private RecyclerView mRvReport;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_report_form1;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mRvDisease = (RecyclerView) view.findViewById(R.id.rv_disease);
        mRvReport = (RecyclerView) view.findViewById(R.id.rv_report);
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
            firstReportBean = bundle.getParcelable(HealthReportFormActivity.KEY_DATA);
        } else {
            ToastUtils.showShort("传递的数据为空");
        }
        List<FirstReportReceiveBean.ReportListBean> reportList = firstReportBean.getReportList();

        mRvDisease.setLayoutManager(new LinearLayoutManager(mContext));
        mRvDisease.setAdapter(new BaseQuickAdapter<FirstReportReceiveBean.ReportListBean,
                BaseViewHolder>(R.layout.health_measure_item_disease,reportList) {
            @Override
            protected void convert(BaseViewHolder helper, FirstReportReceiveBean.ReportListBean item) {
                helper.setText(R.id.tv_disease_name,item.getIllnessName());
                ((ColorfulProgressBar) helper.getView(R.id.cb_disease_level)).setLevel(item.getRiskLevel());
                String illnessStatus = item.getIllnessStatus();
                if ("0".equals(illnessStatus)) {
                    ((ColorfulProgressBar) helper.getView(R.id.cb_disease_level)).setLevel(item.getRiskLevel());
                    helper.getView(R.id.tv_disease_sure).setVisibility(View.GONE);
                } else if ("1".equals(illnessStatus)) {
                    helper.getView(R.id.cb_disease_level).setVisibility(View.GONE);

                }
            }
        });

        List<FirstReportReceiveBean.FactorListBean> factorList = firstReportBean.getFactorList();
        List<FirstReportParseBean> firstReportParseBeans = new ArrayList<>();
        for (FirstReportReceiveBean.FactorListBean xxxx : factorList) {
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
}
