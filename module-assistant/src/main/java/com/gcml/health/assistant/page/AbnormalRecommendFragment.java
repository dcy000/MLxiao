package com.gcml.health.assistant.page;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.LazyFragment;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.health.assistant.R;
import com.gcml.health.assistant.model.AssistantRepository;
import com.gcml.health.assistant.model.entity.AbnormalEntity;
import com.gcml.health.assistant.model.entity.AbnormalRecommendEntity;
import com.gcml.health.assistant.model.entity.AbnormalTaskList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class AbnormalRecommendFragment extends LazyFragment {


    private RecyclerView rvRecommend;

    private RecommendAdapter adapter;

    private AssistantRepository repository = new AssistantRepository();

    private ArrayList<AbnormalRecommendEntity> recommendEntities = new ArrayList<>();
    private int position;
    private AbnormalEntity abnormal;

    int taskId;

    public AbnormalRecommendFragment() {
        // Required empty public constructor
    }

    public static AbnormalRecommendFragment newInstance(int position, AbnormalEntity abnormalEntity) {
        AbnormalRecommendFragment fragment = new AbnormalRecommendFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("abnormal", abnormalEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position", 0);
            abnormal = arguments.getParcelable("abnormal");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.abnormal_fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRecommend = (RecyclerView) view.findViewById(R.id.rvRecommend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommend.setLayoutManager(layoutManager);
        adapter = new RecommendAdapter();
        rvRecommend.setAdapter(adapter);
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();
        getData();
    }

    private void getData() {
        if (abnormal == null) {
            ToastUtils.showShort("请求异常");
            return;
        }

        repository.abnormalRecommends(abnormal.getId())
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
                .subscribe(new DefaultObserver<List<AbnormalRecommendEntity>>() {
                    @Override
                    public void onNext(List<AbnormalRecommendEntity> abnormalRecommendList) {
                        show(abnormalRecommendList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    private void show(List<AbnormalRecommendEntity> abnormalRecommendList) {
        if (abnormalRecommendList.size() == 0) {
            ToastUtils.showShort("当前没有建议!");
        }
        recommendEntities.clear();
        recommendEntities.addAll(abnormalRecommendList);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            int position = rvRecommend.getChildAdapterPosition(v);
            AbnormalRecommendEntity entity = recommendEntities.get(position);

            startActivity(new Intent(activity, AbnormalRecommendDetailsActivity.class)
                    .putExtra("abnormalRecommend", entity)
                    .putExtra("taskId", taskId)
                    .putExtra("abnormal", abnormal)
            );
        }
    };

    private class RecommendHolder extends RecyclerView.ViewHolder {

        private final TextView tvItemTag;
        private final TextView tvItemLabel;
        private final TextView tvItemContent;
        private final TextView tvItemCount;

        public RecommendHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(onItemClickListener);
            tvItemTag = (TextView) itemView.findViewById(R.id.tvItemTag);
            tvItemLabel = (TextView) itemView.findViewById(R.id.tvItemLabel);
            tvItemContent = (TextView) itemView.findViewById(R.id.tvItemContent);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
        }

        public void onBind(int position) {
            setMarginIfNeed(position);
            AbnormalRecommendEntity entity = recommendEntities.get(position);
            tvItemTag.setText(entity.getTag());
            tvItemLabel.setText(entity.getAdviceName());
            tvItemContent.setText(entity.getAdviceMsg());
            tvItemCount.setText(entity.getCountDesc());
        }

        private int smallMargin = UiUtils.pt(8);
        private int largeMargin = UiUtils.pt(48);

        private void setMarginIfNeed(int position) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int leftMargin = position == 0 ? largeMargin : smallMargin;
            int rightMargin = position == adapter.getItemCount() - 1 ? largeMargin : smallMargin;
            boolean changed = false;
            if (layoutParams.leftMargin != leftMargin) {
                layoutParams.leftMargin = leftMargin;
                changed = true;
            }
            if (layoutParams.rightMargin != rightMargin) {
                layoutParams.rightMargin = rightMargin;
                changed = true;
            }
            if (changed) {
                itemView.setLayoutParams(layoutParams);
            }
        }
    }

    private class RecommendAdapter extends RecyclerView.Adapter<RecommendHolder> {

        @NonNull
        @Override
        public RecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.abnormal_item_recommend, parent, false);
            return new RecommendHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecommendHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return recommendEntities == null ? 0 : recommendEntities.size();
        }
    }


    public void showLoading(String tips) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity instanceof ToolbarBaseActivity) {
            ((ToolbarBaseActivity) activity).showLoading(tips);
        }
    }

    public void dismissLoading() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity instanceof ToolbarBaseActivity) {
            ((ToolbarBaseActivity) activity).dismissLoading();
        }
    }
}
