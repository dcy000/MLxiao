package com.example.han.referralproject.health;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.TargetModel;
import com.medlink.danbogh.utils.UiUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthAnalysisFragment extends Fragment {

    public static HealthAnalysisFragment newInstance() {
        Bundle args = new Bundle();
        HealthAnalysisFragment fragment = new HealthAnalysisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rvTargets;

    public HealthAnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.health_fragment_analysis, container, false);
        rvTargets = (RecyclerView) view.findViewById(R.id.health_diary_rv_targets);
        GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTargets.setLayoutManager(lm);
        rvTargets.setAdapter(new RecyclerView.Adapter<TargetHolder>() {
            @Override
            public TargetHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
                View view1 = inflater1.inflate(R.layout.health_item_target, viewGroup, false);
                return new TargetHolder(view1);
            }

            @Override
            public void onBindViewHolder(TargetHolder viewHolder, int position) {
                viewHolder.onBind(position);
            }

            @Override
            public int getItemCount() {
                return mTargetModels == null ? 0 : mTargetModels.size();
            }
        });
        return view;
    }

    private ArrayList<TargetModel> mTargetModels = new ArrayList<>();

    {
        TargetModel targetModel = new TargetModel();
        targetModel.setTitle("本周盐摄入量");
        targetModel.setTarget("少于40克");
        targetModel.setSource("摄入20克");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("本周饮酒量");
        targetModel.setTarget("少于100ml");
        targetModel.setSource("已饮50ml");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("本周运动时间");
        targetModel.setTarget("大于120分钟");
        targetModel.setSource("运动40分钟");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("体重");
        targetModel.setTarget("小于62Kg");
        targetModel.setSource("体重61kg");
        mTargetModels.add(targetModel);
    }

    private int[] backgroudReses = new int[]{
            R.drawable.health_ic_salt,
            R.drawable.health_ic_drink,
            R.drawable.health_ic_sports,
            R.drawable.health_ic_weight,
    };

    public class TargetHolder extends RecyclerView.ViewHolder {

        private TextView tvTarget;
        private TextView tvSource;
        private TextView tvTitle;

        public TargetHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.health_diary_tv_analysis_title);
            tvTarget = (TextView) itemView.findViewById(R.id.health_diary_tv_target);
            tvSource = (TextView) itemView.findViewById(R.id.health_diary_tv_source);
        }

        public void onBind(int position) {
            itemView.setBackgroundResource(backgroudReses[position % 4]);
            TargetModel model = mTargetModels.get(position);
            tvTitle.setText(model.getTitle());
            SpannableString targetStyle = new SpannableString(model.getTarget());
            targetStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            targetStyle.setSpan(new AbsoluteSizeSpan(UiUtils.pt(120)), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTarget.setText(targetStyle);
            SpannableString sourceStyle = new SpannableString(model.getTarget());
            sourceStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5747")), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvSource.setText(model.getSource());
        }
    }
}
