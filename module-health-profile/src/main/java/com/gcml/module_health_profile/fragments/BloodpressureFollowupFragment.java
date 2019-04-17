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
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_profile.HealthProfileActivity;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.gcml.module_health_profile.webview.AddHealthProfileActivity;
import com.gcml.module_health_profile.webview.SeeBloodpressureWebActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class BloodpressureFollowupFragment extends RecycleBaseFragment implements View.OnClickListener {
    private String recordId;
    private ImageView mIvCircle;
    /**
     * 高血压随访
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
     * 高血压随访记录
     */
    private TextView mTvTitle;
    private RecyclerView mRv;
    private BaseQuickAdapter<HealthRecordBean, BaseViewHolder> adapter;
    private List<HealthRecordBean> mData;
    private boolean isBuildHealthRecord = false;

    public static BloodpressureFollowupFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);

        BloodpressureFollowupFragment bloodpressureFollowupFragment = new BloodpressureFollowupFragment();
        bloodpressureFollowupFragment.setArguments(bundle);
        return bloodpressureFollowupFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodpressure_followup;
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
                        helper.setText(R.id.tv_time, R.string.unknown);
                    }

                } else {
                    helper.setText(R.id.tv_time, R.string.unknown);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getActivity().startActivity(new Intent(getActivity(), SeeBloodpressureWebActivity.class)
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
                            mTvCContent.setText(R.string.no_hypertension_follow_up);
                        } else {
                            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
                            mRv.setVisibility(View.VISIBLE);
                            long time = System.currentTimeMillis() - healthRecordBeans.get(0).getCreatedOn();
                            int days = (int) (time / (24 * 3600 * 1000));
                            mTvCContent.setText(UM.getString(R.string.last_visit_has_passed) + days + UM.getString(R.string.unit_days));
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
                                mTvCContent.setText(R.string.no_hypertension_follow_up);
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
//                            MLVoiceSynthetize.startSynthesize(UM.getApp(), "请先签约医生");
//                            return;
//                        }
                        if (!isBuildHealthRecord) {
                            ToastUtils.showShort(R.string.resident_health_file);
                            MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.resident_health_file));
                            return;
                        }
                        getActivity().startActivity(new Intent(getActivity(), AddHealthProfileActivity.class)
                                .putExtra("RdCordId", recordId)
                                .putExtra("type", "高血压")
                                .putExtra("title", UM.getString(R.string.title_High_blood_pressure)));
                    }
                });
    }
}
