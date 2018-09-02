package com.gcml.health.measure.health_report_form;

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
    private FirstReportBean firstReportBean;
    private Drawable drawableUp;
    private Drawable drawableDown;
    private Drawable drawableWarning;

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
        //糖尿病
        String dm = firstReportBean.getDm().getIllnessStatus();
        int dmLevel = firstReportBean.getDm().getRiskLevel();
        //高血压
        String htn = firstReportBean.getHtn().getIllnessStatus();
        int htnLevel = firstReportBean.getHtn().getRiskLevel();
        //肥胖症
        String fat = firstReportBean.getFat().getIllnessStatus();
        int fatLevel = firstReportBean.getFat().getRiskLevel();
        //心血管
        String icvd = firstReportBean.getIcvd().getIllnessStatus();
        int icvdLevel = firstReportBean.getIcvd().getRiskLevel();
        //0是为未确诊，1是确诊
        if ("0".equals(dm)) {
            mCpbBloodsugar.setLevel(dmLevel);
            mTvBloodsugarSure.setVisibility(View.GONE);
        } else if ("1".equals(dm)) {
            mCpbBloodsugar.setVisibility(View.GONE);
        }
        if ("0".equals(htn)) {
            mCpbBloodpressure.setLevel(htnLevel);
            mTvBloodpressureSure.setVisibility(View.GONE);
        } else if ("1".equals(htn)) {
            mCpbBloodpressure.setVisibility(View.GONE);
        }
        if ("0".equals(fat)) {
            mCpbObesity.setLevel(fatLevel);
            mTvObesitySure.setVisibility(View.GONE);
        } else if ("1".equals(fat)) {
            mCpbObesity.setVisibility(View.GONE);
        }
        if ("0".equals(icvd)) {
            mCpbAngiocardiopathy.setLevel(icvdLevel);
            mTvAngiocardiopathySure.setVisibility(View.GONE);
        } else if ("1".equals(icvd)) {
            mCpbAngiocardiopathy.setVisibility(View.GONE);
        }


        List<FirstReportBean.FactorListBean> factorList = firstReportBean.getFactorList();
        List<FirstReportParseBean> firstReportParseBeans = new ArrayList<>();
        for (FirstReportBean.FactorListBean xxxx : factorList) {
            for (FirstReportBean.ListBean beanXXXX : xxxx.getList()) {
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
