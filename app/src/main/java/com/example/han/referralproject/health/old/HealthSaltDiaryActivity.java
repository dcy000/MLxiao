package com.example.han.referralproject.health.old;

import android.content.Intent;
import android.graphics.Color;
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

public class HealthSaltDiaryActivity extends BaseActivity {

    private RulerView rvRuler;
    private TextView tvRulerIndicator;
    private TextView tvConfirm;
    private TextView tvCancel;
    private TextView tvSaltG;

    private List<String> mUnits;
    private RecyclerView rvUnits;
    private UnitAdapter mUnitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_salt_diary);
        mUnits = getUnits();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健  康  日  记");
        tvSaltG = (TextView) findViewById(R.id.health_diary_tv_salt_g);
        rvRuler = (RulerView) findViewById(R.id.health_diary_rv_ruler);
        tvRulerIndicator = (TextView) findViewById(R.id.health_diary_tv_ruler_indicator);
        tvConfirm = (TextView) findViewById(R.id.health_diary_tv_confirm);
        tvCancel = (TextView) findViewById(R.id.health_diary_tv_cancel);
        rvRuler.setValue(0, 0, 10, 0.1f);
        rvRuler.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                int selectedPosition = mUnitAdapter.getSelectedPosition();
                tvRulerIndicator.setText(value + mUnits.get(selectedPosition));
                if (selectedPosition == 0) {
                    tvSaltG.setText(value * 2 + "g");
                } else {
                    tvSaltG.setText(value  + "g");
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), HealthSportsDiaryActivity.class);
                startActivity(intent2);
                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvUnits = (RecyclerView) findViewById(R.id.health_diary_rv_units);
        rvUnits.addOnScrollListener(new CenterScrollListener());
        mUnitAdapter = new UnitAdapter(mUnits);
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(this);
        lm.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        lm.setMinScale(1.0f);
        lm.setItemSpace(0);
        lm.setOnPageChangeListener(new OverFlyingLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mUnitAdapter.setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rvUnits.setAdapter(mUnitAdapter);
        rvUnits.setLayoutManager(lm);
    }

    public List<String> getUnits() {
        List<String> units = new ArrayList<>();
        units.add("勺");
        units.add("克");
        return units;
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class UnitAdapter extends RecyclerView.Adapter<UnitVH> {
        private List<String> mModels;

        public UnitAdapter(List<String> models) {
            mModels = models;
        }

        private volatile int selectedPosition;

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public UnitVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.health_item_unit, parent, false);
            return new UnitVH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(UnitVH vh, int position) {
            String model = mModels.get(position);
            vh.tvItem.setText(model);
            if (position == selectedPosition) {
                vh.tvItem.setTextColor(Color.parseColor("#3F86FC"));
            } else {
                vh.tvItem.setTextColor(Color.parseColor("#FFFFFF"));

            }
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    static class UnitVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvItem;

        public UnitVH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvItem = (TextView) itemView.findViewById(R.id.health_diary_tv_item_unit);
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
        setDisableWakeup(true);
        super.onResume();
    }
}
