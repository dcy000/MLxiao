package com.example.han.referralproject.health_manager_program;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.intelligent_diagnosis.IChangToolbar;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import timber.log.Timber;

public class MedicinePlanFragment extends Fragment {
    private View view;
    private IChangToolbar iChangToolbar;
    /**
     * 根据您的情况，可以选择一种药物治疗2-4周，如血压未明显降低可换 另外降压药。具体用药请咨询医生为准。
     */
    private TextView mTvTitle;
    /**
     * 常用药物
     */
    private TextView mTvHeadlineInfluencingFactors;
    private TextView mView1;
    private RecyclerView mFactorsList;
    /**
     * 建议您健康饮食，合理运动，根据康复疗程进行生活干预。
     */
    private TextView mTvAdvice;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_medicine_plan, container, false);
            getData();
        }
        initView(view);
        return view;
    }

    private void getData() {
        OkGo.<String>get(NetworkApi.Medicine_Program + "100034" + "/")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                MedicineBean data = new Gson().fromJson(object.optJSONObject("data")
                                        .toString(), MedicineBean.class);
                                dealData(data);
                            } else if (object.optInt("code") == 500) {
                                ToastUtils.showShort("暂无推荐");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        Timber.e(response.body());
                    }
                });
    }

    private void dealData(MedicineBean data) {
        if (data == null) {
            return;
        }
        mTvTitle.setText(data.getAdvice());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mFactorsList.setLayoutManager(gridLayoutManager);
        List<String> drugs = data.getDrugs();
        mFactorsList.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.factor_item, drugs) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.text, s);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }

//            ((TreatmentPlanActivity) getActivity()).speak("主人，根据您的情况，" +
//                    "我们给你推荐了以下相关药物。具体用药请以专业医生为准。我们建议您健康饮食，" +
//                    "合理运动，根据康复疗程进行生活干预。");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = view.findViewById(R.id.tv_headline_influencing_factors);
        mView1 = view.findViewById(R.id.view_1);
        mFactorsList = view.findViewById(R.id.factors_list);
        mTvAdvice = view.findViewById(R.id.tv_advice);
    }
}
