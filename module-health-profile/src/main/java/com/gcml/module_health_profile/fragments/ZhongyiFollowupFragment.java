package com.gcml.module_health_profile.fragments;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_profile.HealthManagementResultActivity;
import com.gcml.module_health_profile.HealthProfileActivity;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.TiZhiBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class ZhongyiFollowupFragment extends RecycleBaseFragment implements View.OnClickListener {
    private ImageView mIvCircle;
    /**
     * 中医体质辨识
     */
    private TextView mTvCTitle;
    /**
     * 距离上次体检已过去10天
     */
    private TextView mTvCContent;
    /**
     * 立即开始
     */
    private TextView mBtnNewRecord;
    private ConstraintLayout mClHead;
    /**
     * 中医体质辨识记录
     */
    private TextView mTvTitle;
    private RecyclerView mRv;
    private String recordId;
    private ArrayList<TiZhiBean> tiZhiBeans = new ArrayList<>();
    private BaseQuickAdapter<TiZhiBean, BaseViewHolder> adapter;
    private View noDataView;
    private Boolean isBuildHealthRecord;
    ;

    public static ZhongyiFollowupFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        ZhongyiFollowupFragment zhongyiFollowupFragment = new ZhongyiFollowupFragment();
        zhongyiFollowupFragment.setArguments(bundle);
        return zhongyiFollowupFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_zhongyi_followup;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId = bundle.getString("recordId");
        mIvCircle = (ImageView) view.findViewById(R.id.iv_circle);
        mTvCTitle = (TextView) view.findViewById(R.id.tv_c_title);
        mTvCContent = (TextView) view.findViewById(R.id.tv_c_content);
        mBtnNewRecord = (TextView) view.findViewById(R.id.btn_new_record);
        mBtnNewRecord.setOnClickListener(this);
        mClHead = (ConstraintLayout) view.findViewById(R.id.cl_head);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        noDataView = view.findViewById(R.id.empty_view);

        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new BaseQuickAdapter<TiZhiBean, BaseViewHolder>(R.layout.item_tizhi_history, tiZhiBeans) {
            @Override
            protected void convert(BaseViewHolder helper, TiZhiBean item) {
                TextView time = helper.getView(R.id.tv_time);
                TextView see = helper.getView(R.id.tv_see);
                try {
                    time.setText(TimeUtils.long2StringDate(Long.parseLong(item.modifiedOn)));
                } catch (Exception e) {
                }

              /*  see.setOnClickListener(new FilterClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("查看");
                    }
                }));*/
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), HealthManagementResultActivity.class);
                TiZhiBean value = tiZhiBeans.get(position);
                intent.putExtra("result_data", (Serializable) value.constitutionList);
                startActivity(intent);
            }
        });
        mRv.setAdapter(adapter);
        ((HealthProfileActivity) getActivity()).isBuildHealthRecord.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isBuildHealthRecord = aBoolean;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    HealthProfileRepository repository = new HealthProfileRepository();

    private void getData() {
        repository.getConstitution(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<TiZhiBean>>() {
                    @Override
                    public void onNext(List<TiZhiBean> data) {
                        tiZhiBeans.clear();
                        tiZhiBeans.addAll(data);
                        adapter.notifyDataSetChanged();

                        if (tiZhiBeans == null || tiZhiBeans.size() == 0) {
                            noDataView.setVisibility(View.VISIBLE);
                        } else {
                            noDataView.setVisibility(View.GONE);
                        }


                    }


                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException exception = (ApiException) e;
                            if (exception.code() == 9002) {
                                noDataView.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        ;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_new_record) {

            Routerfit.register(AppRouter.class)
                    .getUserProvider()
                    .getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity userEntity) {
                            if (!isBuildHealthRecord) {
                                ToastUtils.showShort("请先在居民健康档案中进行建档");
                                MLVoiceSynthetize.startSynthesize(UM.getApp(), "请先建立个人档案");
                                return;
                            }
                            Routerfit.register(AppRouter.class).skipOlderHealthManagementSerciveActivity();
                        }
                    });
        } else {
        }
    }
}
