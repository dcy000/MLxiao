package com.gcml.mod_hyper_manager.ui.plan;

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
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.utils.RxUtils;
import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.bean.MedicineBean;
import com.gcml.mod_hyper_manager.net.HyperRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MedicinePlanFragment extends Fragment {
    private View view;
    private IChangToolbar iChangToolbar;
    /**
     * 根据您的情况，可以选择一种药物治疗2-4周，如血压未明显降低可换 另外降压药。具体用药请咨询签约医生为准。
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
        new HyperRepository()
                .getMedicineProgram(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<MedicineBean>() {
                    @Override
                    public void onNext(MedicineBean medicineBean) {
                        dealData(medicineBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            if (((ApiException) e).code() == 500) {
                                mTvTitle.setText("您的各项指标均在正常范围内，暂无药物方案推荐。");
                                view.findViewById(R.id.layout_empty_data).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.layout_empty_data)
                                        .findViewById(R.id.btn_go).setVisibility(View.GONE);
                            }
                        } else {
                            Timber.e(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

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

//            ((TreatmentPlanActivity) getActivity()).speak("根据您的情况，" +
//                    "我们给你推荐了以下相关药物。具体用药请以专业签约医生为准。我们建议您健康饮食，" +
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
