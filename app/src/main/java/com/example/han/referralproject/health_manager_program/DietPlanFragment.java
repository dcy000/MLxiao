package com.example.han.referralproject.health_manager_program;

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
import com.example.han.referralproject.intelligent_diagnosis.DailyRecommendIntake;
import com.example.han.referralproject.intelligent_diagnosis.FoodMateratilDetail;
import com.example.han.referralproject.intelligent_diagnosis.FoodMaterialDetailActivity;
import com.example.han.referralproject.intelligent_diagnosis.IChangToolbar;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class DietPlanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView intakeSalt;
    private TextView intakeOil;
    private TextView intakeDrink;
    private TextView intakeSmoke;
    private TextView tvSalt;
    private TextView tvOil;
    private TextView tvDrink;
    private TextView tvSmoke;
    private TextView tvTuijian;
    private TextView tvMore;
    private RecyclerView foodMaterialList;
    private BaseQuickAdapter<FoodMateratilDetail, BaseViewHolder> adapter;
    private List<FoodMateratilDetail> mData;
    private List<FoodMateratilDetail> cacheDatas;
    private boolean isMore = false;
    private IChangToolbar iChangToolbar;
    private String TAG = "DietPlanFragment";
    private DailyRecommendIntake topData;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_diet_plan, container, false);
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

        foodMaterialList.setHasFixedSize(true);
        foodMaterialList.setNestedScrollingEnabled(false);
        foodMaterialList.setLayoutManager(manager);
        foodMaterialList.addItemDecoration(new GridViewDividerItemDecoration(16, 24));
        adapter = new BaseQuickAdapter<FoodMateratilDetail, BaseViewHolder>(R.layout.food_materatil_item, mData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FoodMateratilDetail foodMateratilDetail) {
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
        foodMaterialList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                startActivity(new Intent(getActivity(), FoodMaterialDetailActivity.class).putExtra("data", mData.get(i)));
            }
        });
    }

    private void getData() {
        Log.e(TAG, "getDataCache: ");
        OkGo.<String>get(NetworkApi.Daily_Recommended_Intake)
                .params("userId", MyApplication.getInstance().userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                DailyRecommendIntake data = new Gson().fromJson(object.optJSONObject("data").toString(), DailyRecommendIntake.class);
                                dealTopData(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
        OkGo.<String>get(NetworkApi.Daily_Food_Recommendation)
                .params("userId", MyApplication.getInstance().userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                List<FoodMateratilDetail> datas = new Gson().fromJson(object.optJSONArray("data").toString(), new TypeToken<List<FoodMateratilDetail>>() {
                                }.getType());
                                dealBottomData(datas);
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

    private void dealBottomData(List<FoodMateratilDetail> datas) {
        if (datas == null) {
            return;
        }
        cacheDatas = datas;
        if (!isMore && datas.size() > 5) {
            for (int i = 0; i < 5; i++) {
                mData.add(datas.get(i));
            }
        } else {
            tvMore.setVisibility(View.GONE);
            mData.addAll(datas);
        }
        adapter.notifyDataSetChanged();
    }

    private void dealTopData(DailyRecommendIntake data) {
        if (data == null) {
            return;
        }
        topData = data;
        String naSalt = data.getNaSalt();
        if (!TextUtils.isEmpty(naSalt)) {
            tvSalt.setText(naSalt);
        }
        String grease = data.getGrease();
        if (!TextUtils.isEmpty(grease)) {
            tvOil.setText(grease);
        }
        String drink = data.getDrink();
        if (!TextUtils.isEmpty(drink)) {
            tvDrink.setText(drink);
        }
        String smoke = data.getSmoke();
        if (!TextUtils.isEmpty(smoke)) {
            tvSmoke.setText(smoke);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
//            if (topData != null && cacheDatas != null) {
//                ((TreatmentPlanActivity) getActivity()).speak("主人，您本周每日食盐量应少于" + topData.getNaSalt()
//                        + "，油脂应少于" + topData.getGrease() + ",饮酒应少于" + topData.getDrink() + "，每日应" + topData.getSmoke()
//                        + "。为了您的健康，我们还为您推荐了以下的食材，请每天至少食用其中的两种");
//            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {
        mData = new ArrayList<>();
        intakeSalt = view.findViewById(R.id.intake_salt);
        intakeOil = view.findViewById(R.id.intake_oil);
        intakeDrink = view.findViewById(R.id.intake_drink);
        intakeSmoke = view.findViewById(R.id.intake_smoke);
        tvSalt = view.findViewById(R.id.tv_salt);
        tvOil = view.findViewById(R.id.tv_oil);
        tvDrink = view.findViewById(R.id.tv_drink);
        tvSmoke = view.findViewById(R.id.tv_smoke);
        tvTuijian = view.findViewById(R.id.tv_tuijian);
        tvMore = view.findViewById(R.id.tv_more);
        tvMore.setOnClickListener(this);
        foodMaterialList = view.findViewById(R.id.food_material_list);
        tvSalt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvOil.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvDrink.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
//        tvSmoke.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                mData.clear();
                if (isMore && cacheDatas.size() > 5) {
                    isMore = false;
                    for (int i = 0; i < 5; i++) {
                        mData.add(cacheDatas.get(i));
                    }
                    tvMore.setText("更多");
                } else {
                    isMore = true;
                    mData.addAll(cacheDatas);
                    tvMore.setText("收起");
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
