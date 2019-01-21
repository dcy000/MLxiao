package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.divider.LinearLayoutDividerItemDecoration;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;

import java.util.ArrayList;
import java.util.List;

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
        mData=new ArrayList<>();
        initRV();
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
                    String[] split = createdTime.split(" ");
                    if (split.length == 2) {
                        helper.setText(R.id.tv_time, split[0]);
                    } else {
                        helper.setText(R.id.tv_time, "未知");
                    }

                } else {
                    helper.setText(R.id.tv_time, "未知");
                }

                helper.getView(R.id.btn_see_detail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页面
                        ToastUtils.showShort("点我干啥");
                    }
                });
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
                            mTvCContent.setText("您还未进行过高血压随访");
                        } else {
                            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
                            mRv.setVisibility(View.VISIBLE);
                            long time = System.currentTimeMillis() - healthRecordBeans.get(0).getCreatedOn();
                            int days = (int) (time / (24 * 3600 * 1000));
                            mTvCContent.setText("距离上次随访已过去" + days + "天");
                            mData.addAll(healthRecordBeans);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

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
        } else {
        }
    }
}
