package com.example.han.referralproject.health;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.ml.edu.common.widget.recycleyview.CenterScrollListener;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;
import com.ml.rulerview.RulerView;

import java.util.ArrayList;
import java.util.List;

public class HealthSportsDiaryActivity extends BaseActivity {

    private RulerView rvRuler;
    private TextView tvRulerIndicator;
    private TextView tvConfirm;
    private TextView tvCancel;
    private TextView tvTopic;
    private TextView tvRulerTopic;
    private RecyclerView rvItems;
    private Adapter mAdapter;
    private RecyclerView rvUnits;
    private HealthSaltDiaryActivity.UnitAdapter mUnitAdapter;
    private List<String> units;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_sports_diary);
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
        tvTopic.setText("选择今天的运动");
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(this);
        lm.setMinScale(1.0f);
        lm.setItemSpace(0);
        lm.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        mAdapter = new Adapter(getModels());
        rvItems.setLayoutManager(lm);
        rvItems.setAdapter(mAdapter);
        tvRulerTopic.setText("选择运动时间");
        rvRuler.setValue(0, 0, 120, 1);
        rvRuler.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                int selectedPosition = mUnitAdapter.getSelectedPosition();
                if (selectedPosition % 2 == 0) {
                    tvRulerIndicator.setText(value + "min");
                } else {
                    tvRulerIndicator.setText(value * 60 + "min");
                }

            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), HealthDrinkDiaryActivity.class);
                startActivity(intent2);
                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), HealthSaltDiaryActivity.class);
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
                if (position % 2 == 0) {
                    rvRuler.setValue(0, 0, 120, 1);
                } else {
                    rvRuler.setValue(0, 0, 10, 0.1f);
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
        units.add("分钟");
        units.add("小时");
        return units;
    }


    private List<String> mModels;

    public List<String> getModels() {
        if (mModels != null) {
            return mModels;
        }
        mModels = new ArrayList<>();
        mModels.add("慢跑");
        mModels.add("羽毛球");
        mModels.add("快跑");
        mModels.add("散步");
        mModels.add("太极拳");
        mModels.add("广场舞");
        mModels.add("篮球");
        mModels.add("气功");
        mModels.add("健身操");
        mModels.add("乒乓球");
        mModels.add("高尔夫球");
        return mModels;
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class Adapter extends RecyclerView.Adapter<VH> {
        private List<String> mModels;
        private List<Integer> bgReses;

        public Adapter(List<String> models) {
            mModels = models;
            bgReses = new ArrayList<>();
            bgReses.add(R.drawable.health_diary_item_bg_red);
            bgReses.add(R.drawable.health_diary_item_bg_blue);
            bgReses.add(R.drawable.health_diary_item_bg_yellow);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.health_item_diary, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            String model = mModels.get(position);
            vh.tvItem.setText(model);
            vh.tvItem.setBackgroundResource(bgReses.get(position % 3));
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvItem;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvItem = (TextView) itemView.findViewById(R.id.health_diary_tv_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
