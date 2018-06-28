package com.example.han.referralproject.intelligent_diagnosis;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class SportPlanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView intakeSalt;
    private TextView intakeOil;
    private TextView intakeDrink;
    private TextView intakeSmoke;
    private TextView exerciseIntensity;
    private TextView maximumHeartRate;
    private TextView exerciseFrequency;
    private TextView runDuration;
    private TextView tvTuijian;
    private TextView moreExercise;
    private RecyclerView exerciseList;
    private BaseQuickAdapter<SportPlan.SportListBean, BaseViewHolder> adapter;
    private List<SportPlan.SportListBean> mData;
    private boolean isMore = false;
    private List<SportPlan.SportListBean> cacheDatas;
    private IChangToolbar iChangToolbar;
    private String TAG = "SportPlanFragment";
    private SportPlan data;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.activity_sport, container, false);
            initView(view);
            setAdapter();
            getData();
        }
        return view;
    }

    private void setAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        exerciseList.setHasFixedSize(true);
        exerciseList.setNestedScrollingEnabled(false);
        exerciseList.setLayoutManager(manager);
        exerciseList.addItemDecoration(new GridViewDividerItemDecoration(16, 24));
        adapter = new BaseQuickAdapter<SportPlan.SportListBean, BaseViewHolder>(R.layout.food_materatil_item, mData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, SportPlan.SportListBean foodMateratilDetail) {
                String imgUrl = foodMateratilDetail.getImgUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    Glide.with(getContext().getApplicationContext())
                            .load(imgUrl)
                            .into((ImageView) baseViewHolder.getView(R.id.food_img));
                }
                String name = foodMateratilDetail.getName();
                if (!TextUtils.isEmpty(name)) {
                    baseViewHolder.setText(R.id.food_nick, name);
                }
            }
        };
        exerciseList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                startActivity(new Intent(getActivity(), SportPlanDetailActivity.class)
                        .putExtra("data", mData.get(i)));
            }
        });
    }

    private void getData() {
        Log.e(TAG, "getData: ");
        OkGo.<String>get(NetworkApi.SportHealthPlan)
                .params("userId", MyApplication.getInstance().userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                SportPlan data = new Gson().fromJson(object.optJSONObject("data").toString(), SportPlan.class);
                                dealData(data);
                            }else if (object.optInt("code")==500){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    private void dealData(SportPlan data) {
        if (data == null) {
            return;
        }
        this.data = data;
        String sportLevel = data.getSportLevel();
        if (!TextUtils.isEmpty(sportLevel)) {
            exerciseIntensity.setText(sportLevel);
        }
        String sportRate = data.getSportRate();
        if (!TextUtils.isEmpty(sportRate)) {
            maximumHeartRate.setText(sportRate);
        }
        int sportWeekTime = data.getSportWeekTime();
        runDuration.setText(sportWeekTime + "");
        String sportTime = data.getSportTime();
        if (!TextUtils.isEmpty(sportTime)) {
            exerciseFrequency.setText(sportTime.charAt(2) + "");
        }
        List<SportPlan.SportListBean> sportList = data.getSportList();
        if (sportList != null) {
            cacheDatas = sportList;
            if (!isMore && sportList.size() > 5) {

                for (int i = 0; i < 5; i++) {
                    mData.add(sportList.get(i));
                }
            } else {
                moreExercise.setVisibility(View.GONE);
                mData.addAll(sportList);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: ");
            if (data != null) {
                ((TreatmentPlanActivity) getActivity())
                        .speak("主人，为了您的健康，小依为您推荐了下面的运动项目，请选择自己喜欢的、合适的项目进行锻炼。" +
                                "小依建议您运动" + data.getSportTime() + "，运动时候的心率应小于" + data.getSportRate() + "，运动强度应较" + data.getSportLevel());
            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {
        mData = new ArrayList<>();
        intakeSalt = (TextView) view.findViewById(R.id.intake_salt);
        intakeOil = (TextView) view.findViewById(R.id.intake_oil);
        intakeDrink = (TextView) view.findViewById(R.id.intake_drink);
        intakeSmoke = (TextView) view.findViewById(R.id.intake_smoke);
        exerciseIntensity = (TextView) view.findViewById(R.id.exercise_intensity);
        maximumHeartRate = (TextView) view.findViewById(R.id.maximum_heart_rate);
        exerciseFrequency = (TextView) view.findViewById(R.id.exercise_frequency);
        runDuration = (TextView) view.findViewById(R.id.run_duration);
        tvTuijian = (TextView) view.findViewById(R.id.tv_tuijian);
        moreExercise = (TextView) view.findViewById(R.id.more_exercise);
        moreExercise.setOnClickListener(this);
        exerciseList = (RecyclerView) view.findViewById(R.id.exercise_list);
        maximumHeartRate.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        exerciseFrequency.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        runDuration.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_exercise:
                mData.clear();
                if (isMore && cacheDatas.size() > 5) {
                    isMore = false;
                    for (int i = 0; i < 5; i++) {
                        mData.add(cacheDatas.get(i));
                    }
                    moreExercise.setText("更多");
                } else {
                    isMore = true;
                    mData.addAll(cacheDatas);
                    moreExercise.setText("收起");
                }
                adapter.notifyDataSetChanged();
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
