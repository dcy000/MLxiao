package com.gcml.health.assistant.page;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.common.LazyFragment;
import com.gcml.health.assistant.R;
import com.gcml.health.assistant.model.entity.RecommendEntity;

import java.util.ArrayList;

/**
 *
 */
public class RecommendFragment extends LazyFragment {


    private RecyclerView rvRecommend;

    private RecommendAdapter adapter;

    private ArrayList<RecommendEntity> recommendEntities = new ArrayList<>();

    public RecommendFragment() {
        // Required empty public constructor
    }

    public static RecommendFragment newInstance(int position) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.assistant_fragment_recommend, container, false);
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

    private class RecommendHolder extends RecyclerView.ViewHolder {

        public RecommendHolder(View itemView) {
            super(itemView);
        }

        public void onBind(int position) {

        }
    }

    private class RecommendAdapter extends RecyclerView.Adapter<RecommendHolder> {

        @NonNull
        @Override
        public RecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.assistant_item_recommend, parent, false);
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

}
