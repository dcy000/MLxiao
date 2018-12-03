package com.example.han.referralproject.health;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.TargetModel;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_core.utils.UiUtils;

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
        Box.getRetrofit(API.class)
                .getWeeklyReport(Box.getUserId(), "1")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WeekReportModel>() {
                    @Override
                    public void onNext(WeekReportModel weekReportModel) {
                        if (weekReportModel == null
                                || getActivity() == null
                                || getActivity().isFinishing()
                                || getActivity().isDestroyed()) {
                            return;
                        }
                        onWeekReport(weekReportModel);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        if (getActivity() == null
                                || getActivity().isFinishing()
                                || getActivity().isDestroyed()) {
                            return;
                        }
                        onWeekReport(null);
                        ToastUtils.showShort(ex.message);

                    }
                });
    }

    private void onWeekReport(WeekReportModel response) {
        if (response == null) {
            response = new WeekReportModel();
        }
        if (response.currentWeek == null) {
            response.currentWeek = new WeekReportModel.CurrentWeek();
        }
        if (response.lastWeek == null) {
            response.lastWeek = new WeekReportModel.LastWeek();
        }
        mTargetModels.clear();
        TargetModel targetModel = new TargetModel();
        targetModel.setTitle("本周盐摄入量");
        if (response.lastWeek.nam == null || response.lastWeek.nam.equals("-1")) {
            response.lastWeek.nam = "42.00";
        }
        if (response.currentWeek.na == null || response.currentWeek.na.equals("-1")) {
            response.currentWeek.na = "0.00";
        }
        String target = Float.parseFloat(response.currentWeek.na) < Float.parseFloat(response.lastWeek.nam) ? "少于" : "少于";
        targetModel.setTarget(target + response.lastWeek.nam + "克");
        targetModel.setTargetLength(response.lastWeek.nam.length());
        targetModel.setSourceLength(response.currentWeek.na == null ? 0 : response.currentWeek.na.length());
        targetModel.setSource("摄入" + response.currentWeek.na + "克");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("本周酒精摄入量");
        if (response.lastWeek.drinkm == null || response.lastWeek.drinkm.equals("-1")) {
            response.lastWeek.drinkm = "0.00";
        }
        if (response.currentWeek.drink == null || response.currentWeek.drink.equals("-1")) {
            response.currentWeek.drink = "0.00";
        }
        target = Float.parseFloat(response.currentWeek.drink) < Float.parseFloat(response.lastWeek.drinkm) ? "少于" : "少于";
        targetModel.setTarget(target + response.lastWeek.drinkm + "ml");
        targetModel.setTargetLength(response.lastWeek.drinkm.length());
        targetModel.setSourceLength(response.lastWeek.drinkm.length());
        targetModel.setSource("已饮" + response.currentWeek.drink + "ml");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        if (response.lastWeek.sportsm == null || response.lastWeek.sportsm.equals("-1")) {
            response.lastWeek.sportsm = "100.00";
        }
        if (response.currentWeek.sports == null || response.currentWeek.sports.equals("-1")) {
            response.currentWeek.sports = "0.00";
        }
        target = Float.parseFloat(response.currentWeek.sports) < Float.parseFloat(response.lastWeek.sportsm) ? "大于" : "大于";
        targetModel.setTitle("本周运动时间");
        targetModel.setTarget(target + response.lastWeek.sportsm + "分钟");
        targetModel.setTargetLength(response.lastWeek.sportsm.length());
        targetModel.setSourceLength(response.currentWeek.sports.length());
        targetModel.setSource("运动" + response.currentWeek.sports + "分钟");
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.setTitle("体重");
        float targetWeight = 0.00f;
        float weight = 0.00f;
        if (response.lastWeek.bmim == null || response.lastWeek.bmim.equals("-1")) {
            response.lastWeek.bmim = "0.00";
        }
        if (response.lastWeek.bmis == null || response.lastWeek.bmis.equals("-1")) {
            response.lastWeek.bmis = "0.00";
        }
        float bmi = Float.parseFloat(response.lastWeek.bmis);
        float targetBmi = Float.parseFloat(response.lastWeek.bmim);
        UserInfoBean user = Box.getSessionManager().getUser();
        float height = TextUtils.isEmpty(user.height) ? 0 : Float.parseFloat(user.height);

        if (height > 0) {
            targetWeight = targetBmi / height / height * 10000;
            weight = bmi / height / height * 10000;
        }
        target = bmi < targetBmi ? "少于" : "少于";
        String s = String.valueOf(targetWeight);
        String s1 = String.valueOf(weight);
        targetModel.setTarget(target + s + "kg");
        targetModel.setTargetLength(s.length());
        targetModel.setSourceLength(s1.length());
        targetModel.setSource("体重" + s1 + "kg");
        mTargetModels.add(targetModel);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<TargetModel> mTargetModels = new ArrayList<>();

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
            int startSource = 2;
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
            tvSource.setText(sourceStyle);
        }
    }
}
