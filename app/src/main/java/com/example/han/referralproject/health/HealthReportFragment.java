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
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.health.model.TargetModel;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthReportFragment extends Fragment {

    private RecyclerView.Adapter<TargetHolder> mAdapter;

    public static HealthReportFragment newInstance() {
        Bundle args = new Bundle();
        HealthReportFragment fragment = new HealthReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rvTargets;

    public HealthReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.health_fragment_report, container, false);
        rvTargets = (RecyclerView) view.findViewById(R.id.health_diary_rv_targets);
        view.findViewById(R.id.health_diary_tv_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        view.findViewById(R.id.health_diary_more_items_tv_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTargets.setLayoutManager(lm);
        mAdapter = new RecyclerView.Adapter<TargetHolder>() {
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
        };
        rvTargets.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkApi.getWeekReport(MyApplication.getInstance().userId,
                new NetworkManager.SuccessCallback<WeeklyReport>() {
                    @Override
                    public void onSuccess(WeeklyReport response) {
                        if (response == null
                                || getActivity() == null
                                || getActivity().isFinishing()
                                || getActivity().isDestroyed()) {
                            return;
                        }
                        onWeekReport(response);
                    }

                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        if (getActivity() == null
                                || getActivity().isFinishing()
                                || getActivity().isDestroyed()) {
                            return;
                        }
                        T.show(message);
                    }
                });
    }

    private void onWeekReport(WeeklyReport response) {
        mTargetModels.clear();
        TargetModel targetModel = new TargetModel();
        targetModel.setTitle("本周盐摄入量");
        String target = Float.parseFloat(response.nas) < Float.parseFloat(response.nam) ? "少于" : "大于";
        targetModel.setTarget(target + response.nam + "克");
        targetModel.setTargetLength(response.nam == null ? 0 : response.nam.length());
        targetModel.setSourceLength(response.nas == null ? 0 : response.nas.length());
        targetModel.setSource("摄入" + response.nas + "克");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("本周酒精摄入量");
        target = Float.parseFloat(response.drinks) < Float.parseFloat(response.drinkm) ? "少于" : "大于";
        targetModel.setTarget(target + Float.parseFloat(response.drinks) + "ml");
        targetModel.setTargetLength(response.drinkm == null ? 0 : response.drinkm.length());
        targetModel.setSourceLength(response.drinks == null ? 0 : response.drinks.length());
        targetModel.setSource("已饮" + Float.parseFloat(response.drinks) + "ml");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        target = Float.parseFloat(response.sportss) < Float.parseFloat(response.sportsm) ? "少于" : "大于";
        targetModel.setTitle("本周运动时间");
        targetModel.setTarget(target + Float.parseFloat(response.sportsm) + "分钟");
        targetModel.setTargetLength(response.sportsm == null ? 0 : response.sportsm.length());
        targetModel.setSourceLength(response.sportss == null ? 0 : response.sportss.length());
        targetModel.setSource("运动" + Float.parseFloat(response.sportss) + "分钟");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("本周BMI");
        target = Float.parseFloat(response.bmis) < Float.parseFloat(response.bmim) ? "少于" : "大于";
        targetModel.setTarget(target + Float.parseFloat(response.bmim));
        targetModel.setTargetLength(response.bmim == null ? 0 : response.bmim.length());
        targetModel.setSourceLength(response.bmis == null ? 0 : response.bmis.length());
        targetModel.setSource("BMI" + Float.parseFloat(response.bmis));
        mTargetModels.add(targetModel);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<TargetModel> mTargetModels = new ArrayList<>();
//
//    {
//        TargetModel targetModel = new TargetModel();
//        targetModel.setTitle("本周盐摄入量");
//        targetModel.setTarget("少于40克");
//        targetModel.setSource("摄入20克");
//        mTargetModels.add(targetModel);
//        targetModel = new TargetModel();
//        targetModel.setTitle("本周饮酒量");
//        targetModel.setTarget("少于100ml");
//        targetModel.setSource("已饮50ml");
//        mTargetModels.add(targetModel);
//        targetModel = new TargetModel();
//        targetModel.setTitle("本周运动时间");
//        targetModel.setTarget("大于120分钟");
//        targetModel.setSource("运动40分钟");
//        mTargetModels.add(targetModel);
//        targetModel = new TargetModel();
//        targetModel.setTitle("体重");
//        targetModel.setTarget("小于62Kg");
//        targetModel.setSource("体重61kg");
//        mTargetModels.add(targetModel);
//    }

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
            int startSource = position == 3 ? 3 : 2;
            tvTitle.setText(model.getTitle());
            SpannableString targetStyle = new SpannableString(model.getTarget());
            targetStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    2, 2 + model.getTargetLength(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            targetStyle.setSpan(new AbsoluteSizeSpan(UiUtils.pt(120)),
                    2, 2 + model.getTargetLength(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTarget.setText(targetStyle);
            SpannableString sourceStyle = new SpannableString(model.getSource());
            sourceStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5747")),
                    startSource, startSource + model.getSourceLength(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvSource.setText(model.getSource());
        }
    }
}
