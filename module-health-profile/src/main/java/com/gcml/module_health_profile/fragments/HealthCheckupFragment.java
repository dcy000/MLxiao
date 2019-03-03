package com.gcml.module_health_profile.fragments;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.divider.LinearLayoutDividerItemDecoration;
import com.gcml.common.http.ApiException;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_profile.HealthProfileActivity;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.gcml.module_health_profile.webview.AddHealthProfileActivity;
import com.gcml.module_health_profile.webview.SeeHealthCheckupWebActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthCheckupFragment extends RecycleBaseFragment implements View.OnClickListener {
    /**
     * 立即开始
     */
    private TextView mBtnNewRecord;
    private RecyclerView mRv;
    private List<HealthRecordBean> mData;
    private String recordId;
    private BaseQuickAdapter<HealthRecordBean, BaseViewHolder> adapter;
    private ImageView mIvCircle;
    /**
     * 体检
     */
    private TextView mTvCTitle;
    /**
     * 距离上次体检已过去10天
     */
    private TextView mTvCContent;
    private ConstraintLayout mClHead;
    /**
     * 体检记录
     */
    private TextView mTvTitle;
    private boolean isBuildHealthRecord;

    public static HealthCheckupFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        HealthCheckupFragment healthCheckupFragment = new HealthCheckupFragment();
        healthCheckupFragment.setArguments(bundle);
        return healthCheckupFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_checkup;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId = bundle.getString("recordId");
        mBtnNewRecord = (TextView) view.findViewById(R.id.btn_new_record);
        mBtnNewRecord.setOnClickListener(this);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mIvCircle = (ImageView) view.findViewById(R.id.iv_circle);
        mTvCTitle = (TextView) view.findViewById(R.id.tv_c_title);
        mTvCContent = (TextView) view.findViewById(R.id.tv_c_content);
        mClHead = (ConstraintLayout) view.findViewById(R.id.cl_head);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mRv.setOnClickListener(this);
        mData = new ArrayList<>();
        initRV();
        ((HealthProfileActivity) getActivity()).isBuildHealthRecord.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isBuildHealthRecord = aBoolean;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 40));
        mRv.setNestedScrollingEnabled(false);
        mRv.setAdapter(adapter = new BaseQuickAdapter<HealthRecordBean, BaseViewHolder>(R.layout.health_profile_item_checkup_record, mData) {
            @Override
            protected void convert(BaseViewHolder helper, HealthRecordBean item) {
                String createdTime = item.getCreatedTime();
                if (!TextUtils.isEmpty(createdTime)) {
                    String[] split = createdTime.split("\\s+");
                    if (split.length == 2) {
                        helper.setText(R.id.tv_time, split[0]);
                    } else {
                        helper.setText(R.id.tv_time, "未知");
                    }

                } else {
                    helper.setText(R.id.tv_time, "未知");
                }

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getActivity().startActivity(new Intent(getActivity(), SeeHealthCheckupWebActivity.class)
                        .putExtra("RdCordId", recordId)
                        .putExtra("HealthRecordId", mData.get(position).getRdUserRecordId()));
            }
        });
    }

    private void getData() {
        HealthProfileRepository repository = new HealthProfileRepository();
        repository.getHealthRecordList(recordId, UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<HealthRecordBean>>() {
                    @Override
                    public void onNext(List<HealthRecordBean> healthRecordBeans) {
                        if (healthRecordBeans == null || healthRecordBeans.size() == 0) {
                            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                            mRv.setVisibility(View.GONE);
                            mTvCContent.setText("您还未进行过体检");
                        } else {
                            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
                            mRv.setVisibility(View.VISIBLE);
                            long time = System.currentTimeMillis() - healthRecordBeans.get(0).getCreatedOn();
                            int days = (int) (time / (24 * 3600 * 1000));
                            mTvCContent.setText("距离上次体检已过去" + days + "天");
                            mData.clear();
                            mData.addAll(healthRecordBeans);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException exception = (ApiException) e;
                            if (exception.code() == 9002) {
                                view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                mRv.setVisibility(View.GONE);
                                mTvCContent.setText("您还未进行过体检");
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_new_record) {
            checkIsSignDoctor();
        } else {
        }
    }

    private void checkIsSignDoctor() {
        Observable<UserEntity> rxUsers = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        rxUsers.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
//                        if (TextUtils.isEmpty(user.doctorId)) {
//                            ToastUtils.showShort("请先签约医生");
//                            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "请先签约医生");
//                            return;
//                        }
                        if (!isBuildHealthRecord) {
                            ToastUtils.showShort("请先在居民健康档案中进行建档");
                            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "请先建立个人档案");
                            return;
                        }
                        getActivity().startActivity(new Intent(getActivity(), AddHealthProfileActivity.class)
                                .putExtra("RdCordId", recordId)
                                .putExtra("type", "健康体检")
                                .putExtra("title", "健 康 体 检 "));
                    }
                });
    }
}
