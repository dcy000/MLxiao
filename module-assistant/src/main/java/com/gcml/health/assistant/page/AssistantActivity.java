package com.gcml.health.assistant.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.assistant.R;
import com.gcml.health.assistant.model.AssistantRepository;
import com.gcml.health.assistant.model.entity.AbnormalEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AssistantActivity extends ToolbarBaseActivity {

    private TextView tvAbnormalLabel;
    private TextView tvAbnormalTips;
    private TextView tvRecommendLabel;
    private TextView tvRecommendTips;
    private RecyclerView rvAbnormal;
    private AbnormalAdapter adapter;
    private ArrayList<AbnormalEntity> abnormalEntities = new ArrayList<>();
    private ArrayList<RecommendFragment> recommendFragments = new ArrayList<>();

    private AssistantRepository repository = new AssistantRepository();

    private int selected = 0;
    private int last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_activity_assistant);
        mTitleText.setText("智 能 辅 助 诊 疗");

        tvAbnormalLabel = (TextView) findViewById(R.id.tvAbnormalLabel);
        tvAbnormalTips = (TextView) findViewById(R.id.tvAbnormalTips);
        tvRecommendLabel = (TextView) findViewById(R.id.tvRecommendLabel);
        tvRecommendTips = (TextView) findViewById(R.id.tvRecommendTips);
        rvAbnormal = (RecyclerView) findViewById(R.id.rvAbnormal);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAbnormal.setLayoutManager(layoutManager);
        adapter = new AbnormalAdapter();
        rvAbnormal.setAdapter(adapter);
        getData();
    }

    private void getData() {
        repository.abnormals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("加载中");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<AbnormalEntity>>() {
                    @Override
                    public void onNext(List<AbnormalEntity> abnormals) {
                        show(abnormals);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    private void show(List<AbnormalEntity> abnormals) {
        abnormalEntities.clear();
        abnormalEntities.addAll(abnormals);
        adapter.notifyDataSetChanged();
        int size = abnormalEntities.size();

        int fSize = recommendFragments.size();
        if (fSize > 0) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            for (int i = 0; i < fSize; i++) {
                RecommendFragment fragment = recommendFragments.get(i);
                if (fragment.isAdded()) {
                    transaction.remove(fragment);
                }
            }
            transaction.commitNowAllowingStateLoss();
        }

        recommendFragments.clear();

        for (int i = 0; i < size; i++) {
            recommendFragments.add(RecommendFragment.newInstance(i));
        }
        showRecommendFragment(selected, -1);
    }

    private void showRecommendFragment(int selected, int last) {
        if (recommendFragments.size() == 0) {
            return;
        }
        if (selected < 0 || selected >= recommendFragments.size()) {
            selected = 0;
        }

        String tag = RecommendFragment.class.getName() + selected;
        RecommendFragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment = (RecommendFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = recommendFragments.get(selected);
        }

        if (last >= 0 && last < recommendFragments.size()) {
            RecommendFragment lastF = recommendFragments.get(last);
            if (lastF.isAdded()) {
                transaction.hide(lastF);
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }
        transaction.commitNowAllowingStateLoss();
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvAbnormal.getChildAdapterPosition(v);
            last = selected;
            selected = position;

            rvAbnormal.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    showRecommendFragment(selected, last);
                }
            });
        }
    };

    private class AbnormalHolder extends RecyclerView.ViewHolder {

        private ImageView ivCheckBox;
        private TextView tvItemLabel;
        private TextView tvItemValue;
        private TextView tvItemUnit;

        public AbnormalHolder(View itemView) {
            super(itemView);
            ivCheckBox = (ImageView) itemView.findViewById(R.id.ivCheckBox);
            tvItemLabel = (TextView) itemView.findViewById(R.id.tvItemLabel);
            tvItemValue = (TextView) itemView.findViewById(R.id.tvItemValue);
            tvItemUnit = (TextView) itemView.findViewById(R.id.tvItemUnit);

            itemView.setOnClickListener(onItemClickListener);
        }

        public void onBind(int position) {
            if (selected < 0 || selected >= abnormalEntities.size()) {
                selected = 0;
            }
            AbnormalEntity entity = abnormalEntities.get(position);
            ivCheckBox.setSelected(selected == position);
            tvItemLabel.setText(entity.getLabel());
            tvItemValue.setText(entity.getValue());
            tvItemUnit.setText(entity.getUnit());
        }
    }

    private class AbnormalAdapter extends RecyclerView.Adapter<AbnormalHolder> {

        @NonNull
        @Override
        public AbnormalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.assistant_item_abnormal, parent, false);
            return new AbnormalHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AbnormalHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return abnormalEntities == null ? 0 : abnormalEntities.size();
        }
    }
}
