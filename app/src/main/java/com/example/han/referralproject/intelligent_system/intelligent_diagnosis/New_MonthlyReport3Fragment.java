package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by Administrator on 2018/5/12.
 */

public class New_MonthlyReport3Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private RadioButton rbFirst;
    private RadioButton rbSecond;
    private RadioButton rbThree;
    private RadioButton rbFour;
    private RadioGroup rg;
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
    private TextView treatmentPlan;
    private TextView viewCompetion;
    private RadarChart chart;
    private List<LifeTherapy> cacheDatas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.new_fragment_monthly_report3, container, false);
            initView(view);
            getData();
        }
        return view;
    }

    private void getData() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.WEEK_OF_YEAR, curr.get(Calendar.WEEK_OF_YEAR) - 1);
        long monthAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.Life_Therapy)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", monthAgoTime)
                .params("num", "4")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                List<LifeTherapy> data = new Gson().fromJson(object.optJSONArray("data").toString(), new TypeToken<List<LifeTherapy>>() {
                                }.getType());
                                cacheDatas = data;
                                dealData(data, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealData(List<LifeTherapy> data, int radiobuttonPosition) {
        if (data == null) {
            return;
        }
        LifeTherapy lifeTherapy = data.get(radiobuttonPosition);
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

            tabMbYan.setText("<" + String.format("%.2f", naSaltTarget));
            tabMbYundong.setText(">" + String.format("%.2f", sportTimeTarget));
            tabMbTizhong.setText("<" + String.format("%.2f", weightTarget));
            tabMbYinjiu.setText("<" + String.format("%.0f", wineDrinkTarget));

            String yan_s = String.format("%.2f", naSalt);
            if ("0.00".equals(yan_s)){
                tabSjYan.setText("");
            }else {
                tabSjYan.setText(yan_s);
            }
            String yundong_s = String.format("%.2f", sportTime);
            if ("0.00".equals(yundong_s)) {
                tabSjYundong.setText("");
            }else {
                tabSjYundong.setText(yundong_s);
            }
            String tizhong_s = String.format("%.2f", weight);
            if ("0.00".equals(tizhong_s)) {
                tabSjTizhong.setText("");
            }else {
                tabSjTizhong.setText(tizhong_s);
            }
            String yinjiu_s = String.format("%.0f", wineDrink);
            if ("0".equals(yinjiu_s)) {
                tabSjYinjiu.setText("");
            }else {
                tabSjYinjiu.setText(yinjiu_s);
            }

            if (pcNaSalt > 0) {
                imgYan.setImageResource(R.drawable.red_up);
                imgYan.setVisibility(View.VISIBLE);
                pcYan.setVisibility(View.VISIBLE);
                pcYan.setText(String.format("%.2f", pcNaSalt));
                pcYan.setTextColor(Color.parseColor("#FF5747"));
            } else {
                imgYan.setVisibility(View.GONE);
                pcYan.setText("√");
                pcYan.setVisibility(View.VISIBLE);
                pcYan.setTextColor(Color.parseColor("#3CD478"));
            }
            if (pcSportTime < 0) {
                imgYundong.setImageResource(R.drawable.red_up);
                imgYundong.setVisibility(View.VISIBLE);
                pcYundong.setVisibility(View.VISIBLE);
                pcYundong.setText(String.format("%.2f", -pcSportTime));
                pcYundong.setTextColor(Color.parseColor("#FF5747"));
            } else {
                imgYundong.setVisibility(View.GONE);
                pcYundong.setText("√");
                pcYundong.setVisibility(View.VISIBLE);
                pcYundong.setTextColor(Color.parseColor("#3CD478"));
            }

            if (pcWeight > 0) {
                imgTizhong.setImageResource(R.drawable.red_up);
                imgTizhong.setVisibility(View.VISIBLE);
                pcTizhong.setVisibility(View.VISIBLE);
                pcTizhong.setText(String.format("%.2f", pcWeight));
                pcTizhong.setTextColor(Color.parseColor("#FF5747"));
            } else {
                imgTizhong.setVisibility(View.GONE);
                pcTizhong.setText("√");
                pcTizhong.setVisibility(View.VISIBLE);
                pcTizhong.setTextColor(Color.parseColor("#3CD478"));
            }

            if (pcWineDrink > 0) {
                imgYinjiu.setImageResource(R.drawable.red_up);
                imgYinjiu.setVisibility(View.VISIBLE);
                pcYinjiu.setVisibility(View.VISIBLE);
                pcYinjiu.setText(String.format("%.0f", pcWineDrink));
                pcYinjiu.setTextColor(Color.parseColor("#FF5747"));
            } else {
                imgYinjiu.setVisibility(View.GONE);
                pcYinjiu.setText("√");
                pcYinjiu.setVisibility(View.VISIBLE);
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
        chart.clear();
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.parseColor("#999999"));
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.parseColor("#999999"));
        chart.setWebAlpha(100);
//        chart.setExtraOffsets(50, 50, 50, 50);
        chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(16f);
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[]{"体重（Kg）", "运动（min）", "饮酒（ml）", "盐（g）"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(4, false);
        yAxis.setTextSize(16f);
        yAxis.setDrawLabels(false);

        setData(mb_tizhong, mb_yundong, mb_yinjiu, mb_yan, sj_tizhong, sj_yundong, sj_yinjiu, sj_yan);
    }

    public void setData(float mb_tizhong, float mb_yundong, float mb_yinjiu, float mb_yan, float sj_tizhong, float sj_yundong, float sj_yinjiu, float sj_yan) {
        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        entries1.add(new RadarEntry(mb_tizhong));
        entries1.add(new RadarEntry(mb_yundong));
        entries1.add(new RadarEntry(mb_yinjiu));
        entries1.add(new RadarEntry(mb_yan));
        entries2.add(new RadarEntry(sj_tizhong));
        entries2.add(new RadarEntry(sj_yundong));
        entries2.add(new RadarEntry(sj_yinjiu));
        entries2.add(new RadarEntry(sj_yan));
        RadarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (RadarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (RadarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(entries1);
            set2.setValues(entries2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {
            set1 = new RadarDataSet(entries1, "");
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

            set2 = new RadarDataSet(entries2, "");
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
        }

    }

    private void initView(View view) {
        rbFirst = (RadioButton) view.findViewById(R.id.rb_first);
        rbFirst.setChecked(true);
        rbFirst.setOnClickListener(this);
        rbSecond = (RadioButton) view.findViewById(R.id.rb_second);
        rbSecond.setOnClickListener(this);
        rbThree = (RadioButton) view.findViewById(R.id.rb_three);
        rbThree.setOnClickListener(this);
        rbFour = (RadioButton) view.findViewById(R.id.rb_four);
        rbFour.setOnClickListener(this);
        rg = (RadioGroup) view.findViewById(R.id.rg);
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
        treatmentPlan = (TextView) view.findViewById(R.id.treatment_plan);
        treatmentPlan.setOnClickListener(this);
        viewCompetion = (TextView) view.findViewById(R.id.view_competion);
        viewCompetion.setOnClickListener(this);
        chart = (RadarChart) view.findViewById(R.id.chart);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.treatment_plan:
                getActivity().startActivity(new Intent(getActivity(), TreatmentPlanActivity.class));
                break;
            case R.id.view_competion:
                getActivity().finish();
                break;
            case R.id.rb_first:
                dealData(cacheDatas, 0);
                break;
            case R.id.rb_second:
                dealData(cacheDatas, 1);
                break;
            case R.id.rb_three:
                dealData(cacheDatas, 2);
                break;
            case R.id.rb_four:
                dealData(cacheDatas, 3);
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
