package com.example.han.referralproject.intelligent_diagnosis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class New_WeeklyReport3Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tabMbYan;
    private TextView tabMbYundong;
    private TextView tabMbTizhong;
    private TextView tabMbYinjiu;
    private TextView tabSjYan;
    private TextView tabSjYundong;
    private TextView tabSjTizhong;
    private TextView tabSjYinjiu;
    private ImageView imgYan;
    private TextView pcYan;
    private ImageView imgYundong;
    private TextView pcYundong;
    private ImageView imgTizhong;
    private TextView pcTizhong;
    private ImageView imgYinjiu;
    private TextView pcYinjiu;
    private RxTextRoundProgressBar rpbSum;
    private TextView tvProgress3;
    private RadarChart chart;
    /**
     * 治疗方案
     */
    private TextView mTreatmentPlan;
    /**
     * 查看完成
     */
    private TextView mViewCompetion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.new_fragment_weekly_report3, container, false);
            initView(view);
            getData();
        }
        return view;
    }

    private void getData() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) - 7);
        long weekAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.Life_Therapy)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", weekAgoTime)
                .params("num", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("生活疗法", "onSuccess: " + response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                List<LifeTherapy> data = new Gson().fromJson(object.optJSONArray("data").toString(), new TypeToken<List<LifeTherapy>>() {
                                }.getType());
                                dealData(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealData(List<LifeTherapy> data) {
        if (data == null) {
            return;
        }
        LifeTherapy lifeTherapy = data.get(0);
        if (lifeTherapy != null) {
            double naSaltTarget = lifeTherapy.getNaSaltTarget();
            double naSalt = lifeTherapy.getNaSalt();
            double sportTimeTarget = lifeTherapy.getSportTimeTarget();
            double sportTime = lifeTherapy.getSportTime();
            double weightTarget = lifeTherapy.getWeightTarget();
            double weight = lifeTherapy.getWeight();
            double wineDrinkTarget = lifeTherapy.getWineDrinkTarget();
            double wineDrink = lifeTherapy.getWineDrink();
            double pcNaSalt = naSalt - naSaltTarget;
            double pcSportTime = sportTime - sportTimeTarget;
            double pcWeight = weight - weightTarget;
            double pcWineDrink = wineDrink - wineDrinkTarget;

            tabMbYan.setText("<"+String.format("%.2f", naSaltTarget));
            tabMbYundong.setText(">"+String.format("%.2f", sportTimeTarget));
            tabMbTizhong.setText("<"+String.format("%.2f", weightTarget));
            tabMbYinjiu.setText("<"+String.format("%.0f", wineDrinkTarget));

            tabSjYan.setText(String.format("%.2f", naSalt));
            tabSjYundong.setText(String.format("%.2f", sportTime));
            tabSjTizhong.setText(String.format("%.2f", weight));
            tabSjYinjiu.setText(String.format("%.0f", wineDrink));

            if (pcNaSalt > 0) {
                imgYan.setImageResource(R.drawable.red_up);
                imgYan.setVisibility(View.VISIBLE);
                pcYan.setText(String.format("%.2f", pcNaSalt));
            } else {
                imgYan.setVisibility(View.GONE);
                pcYan.setText("√");
                pcYan.setTextColor(Color.parseColor("#3CD478"));
            }
            if (pcSportTime < 0) {
                imgYundong.setImageResource(R.drawable.red_up);
                imgYundong.setVisibility(View.VISIBLE);
                pcYundong.setText(String.format("%.2f", -pcSportTime));
            } else {
                imgYundong.setVisibility(View.GONE);
                pcYundong.setText("√");
                pcYundong.setTextColor(Color.parseColor("#3CD478"));
            }

            if (pcWeight > 0) {
                imgTizhong.setImageResource(R.drawable.red_up);
                imgTizhong.setVisibility(View.VISIBLE);
                pcTizhong.setText(String.format("%.2f", pcWeight));
            } else {
                imgTizhong.setVisibility(View.GONE);
                pcTizhong.setText("√");
                pcTizhong.setTextColor(Color.parseColor("#3CD478"));
            }

            if (pcWineDrink > 0) {
                imgYinjiu.setImageResource(R.drawable.red_up);
                imgYinjiu.setVisibility(View.VISIBLE);
                pcYinjiu.setText(String.format("%.0f", pcWineDrink));
            } else {
                imgYinjiu.setVisibility(View.GONE);
                pcYinjiu.setText("√");
                pcYinjiu.setTextColor(Color.parseColor("#3CD478"));
            }

            int completion = lifeTherapy.getCompletion();
            rpbSum.setMax(100);
            rpbSum.setProgress(completion);
            tvProgress3.setText(completion + "%");

            setChart(((float) weightTarget), ((float) sportTimeTarget), ((float) wineDrinkTarget), ((float) naSaltTarget), ((float) weight), ((float) sportTime), ((float) wineDrink), ((float) naSalt));
        }
    }

    private void setChart(float mb_tizhong, float mb_yundong, float mb_yinjiu, float mb_yan, float sj_tizhong, float sj_yundong, float sj_yinjiu, float sj_yan) {
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.parseColor("#999999"));
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.parseColor("#999999"));
        chart.setWebAlpha(100);
        chart.setExtraOffsets(50, 50, 50, 50);

        MarkerView mv = new RadarMarkerView(getActivity(), R.layout.radar_markerview);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        setData(mb_tizhong, mb_yundong, mb_yinjiu, mb_yan, sj_tizhong, sj_yundong, sj_yinjiu, sj_yan);

        chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(16f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[]{"体重（Kg）", "运动（min）", "饮酒（ml）", "盐（g）"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.parseColor("#333333"));

        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(4, false);
        yAxis.setTextSize(16f);
        yAxis.setDrawLabels(false);
    }

    public void setData(float mb_tizhong, float mb_yundong, float mb_yinjiu, float mb_yan, float sj_tizhong, float sj_yundong, float sj_yinjiu, float sj_yan) {
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        entries1.add(new RadarEntry(mb_tizhong));
        entries1.add(new RadarEntry(mb_yundong));
        entries1.add(new RadarEntry(mb_yinjiu));
        entries1.add(new RadarEntry(mb_yan));
        entries2.add(new RadarEntry(sj_tizhong));
        entries2.add(new RadarEntry(sj_yundong));
        entries2.add(new RadarEntry(sj_yinjiu));
        entries2.add(new RadarEntry(sj_yan));
        RadarDataSet set1 = new RadarDataSet(entries1, "");
//        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.parseColor("#49DF84"));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);
        //左下角指示器样式
        set1.setFormLineWidth(0f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
        set1.setFormSize(0f);

        RadarDataSet set2 = new RadarDataSet(entries2, "");
//        set2.setColor(Color.rgb(121, 162, 175));


        set2.setFillColor(Color.parseColor("#FF5747"));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(1f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        //左下角指示器样式
        set2.setFormLineWidth(0f);
        set2.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
        set2.setFormSize(0f);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setDrawValues(false);

        chart.setData(data);
        chart.invalidate();
    }

    private void initView(View view) {
        tabMbYan = (TextView) view.findViewById(R.id.tab_mb_yan);
        tabMbYundong = (TextView) view.findViewById(R.id.tab_mb_yundong);
        tabMbTizhong = (TextView) view.findViewById(R.id.tab_mb_tizhong);
        tabMbYinjiu = (TextView) view.findViewById(R.id.tab_mb_yinjiu);
        tabSjYan = (TextView) view.findViewById(R.id.tab_sj_yan);
        tabSjYundong = (TextView) view.findViewById(R.id.tab_sj_yundong);
        tabSjTizhong = (TextView) view.findViewById(R.id.tab_sj_tizhong);
        tabSjYinjiu = (TextView) view.findViewById(R.id.tab_sj_yinjiu);
        imgYan = (ImageView) view.findViewById(R.id.img_yan);
        pcYan = (TextView) view.findViewById(R.id.pc_yan);
        imgYundong = (ImageView) view.findViewById(R.id.img_yundong);
        pcYundong = (TextView) view.findViewById(R.id.pc_yundong);
        imgTizhong = (ImageView) view.findViewById(R.id.img_tizhong);
        pcTizhong = (TextView) view.findViewById(R.id.pc_tizhong);
        imgYinjiu = (ImageView) view.findViewById(R.id.img_yinjiu);
        pcYinjiu = (TextView) view.findViewById(R.id.pc_yinjiu);
        rpbSum = (RxTextRoundProgressBar) view.findViewById(R.id.rpb_sum);
        tvProgress3 = (TextView) view.findViewById(R.id.tv_progress3);
        chart = (RadarChart) view.findViewById(R.id.chart);
        mTreatmentPlan = (TextView) view.findViewById(R.id.treatment_plan);
        mTreatmentPlan.setOnClickListener(this);
        mViewCompetion = (TextView) view.findViewById(R.id.view_competion);
        mViewCompetion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.treatment_plan:
                startActivity(new Intent(getActivity(),TreatmentPlanActivity.class));
                break;
            case R.id.view_competion:
                getActivity().finish();
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
