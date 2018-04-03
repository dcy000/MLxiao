package com.example.han.referralproject.health.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.ml.edu.common.widget.recycleyview.CenterScrollListener;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;
import com.ml.rulerview.RulerView;

import java.util.ArrayList;
import java.util.List;

public class HealthDrinkDiaryActivity extends BaseActivity {

    private RulerView rvRuler;
    private TextView tvRulerIndicator;
    private TextView tvTopic;
    private RecyclerView rvItems;
    private TextView tvRulerTopic;
    private HealthSportsDiaryActivity.Adapter mAdapter;
    private TextView tvConfirm;
    private TextView tvCancel;
    private RecyclerView rvUnits;
    private List<String> units;
    private HealthSaltDiaryActivity.UnitAdapter mUnitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_drink_diary);
        units = getUnits();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健  康  日  记");
        tvTopic = (TextView) findViewById(R.id.health_diary_tv_topic);
        rvItems = (RecyclerView) findViewById(R.id.health_diary_rv_items);
        tvRulerTopic = (TextView) findViewById(R.id.health_diary_tv_ruler_topic);
        rvRuler = (RulerView) findViewById(R.id.health_diary_rv_ruler);
        tvRulerIndicator = (TextView) findViewById(R.id.health_diary_tv_ruler_indicator);
        tvConfirm = (TextView) findViewById(R.id.health_diary_tv_confirm);
        tvCancel = (TextView) findViewById(R.id.health_diary_tv_cancel);
        tvTopic.setText("选择酒的种类");
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(this);
        lm.setMinScale(1.0f);
        lm.setItemSpace(0);
        lm.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        mAdapter = new HealthSportsDiaryActivity.Adapter(getModels());
        rvItems.setLayoutManager(lm);
        rvItems.setAdapter(mAdapter);
        tvRulerTopic.setText("选择饮酒量");
        rvRuler.setValue(0, 0, 10, 0.1f);
        rvRuler.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                int position = mUnitAdapter.getSelectedPosition();
                tvRulerIndicator.setText(value + units.get(position));
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), HealthSportsDiaryActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        rvUnits = (RecyclerView) findViewById(R.id.health_diary_rv_units);
        rvUnits.addOnScrollListener(new CenterScrollListener());
        mUnitAdapter = new HealthSaltDiaryActivity.UnitAdapter(units);
        OverFlyingLayoutManager lm2 = new OverFlyingLayoutManager(this);
        lm2.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        lm2.setMinScale(1.0f);
        lm2.setItemSpace(0);
        lm2.setOnPageChangeListener(new OverFlyingLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mUnitAdapter.setSelectedPosition(position);
                if (position % 3 == 0) {
                    rvRuler.setValue(0, 0, 10, 0.1f);
                } else if (position % 3 == 1){
                    rvRuler.setValue(0, 0, 1000, 20);
                } else {
                    rvRuler.setValue(0, 0, 5, 0.1f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rvUnits.setAdapter(mUnitAdapter);
        rvUnits.setLayoutManager(lm2);
    }

    private List<String> getUnits() {
        List<String> units = new ArrayList<>();
        units.add("杯");
        units.add("毫升");
        units.add("瓶");
        return units;
    }

    private List<String> mModels;

    public List<String> getModels() {
        if (mModels != null) {
            return mModels;
        }
        mModels = new ArrayList<>();
        mModels.add("啤酒");
        mModels.add("白酒");
        mModels.add("黄酒");
        mModels.add("红酒");
        return mModels;
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
