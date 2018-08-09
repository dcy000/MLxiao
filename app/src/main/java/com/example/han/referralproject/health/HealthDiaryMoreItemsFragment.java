package com.example.han.referralproject.health;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthDiaryMoreItemsFragment extends Fragment {

    private ArrayList<String> mTheItems;
    private ArrayList<String> mMoreItems;
    private View mView;
    private RecyclerView mRvTheItems;
    private RecyclerView mRvMoreItems;
    private TheAdapter mTheAdapter;
    private MoreAdapter mMoreAdapter;
    private String mTitle;

    public HealthDiaryMoreItemsFragment() {

    }

    public static HealthDiaryMoreItemsFragment newInstance(
            String title,
            ArrayList<String> theItems,
            ArrayList<String> moreItems) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("theItems", (ArrayList<String>) theItems.clone());
        args.putStringArrayList("moreItems", (ArrayList<String>) moreItems.clone());
        HealthDiaryMoreItemsFragment fragment = new HealthDiaryMoreItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString("title");
            mTheItems = arguments.getStringArrayList("theItems");
            mMoreItems = arguments.getStringArrayList("moreItems");
        }
        if (mTheItems == null) {
            mTheItems = new ArrayList<>();
        }
        if (mMoreItems == null) {
            mMoreItems = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.health_fragment_diary_more_items, container, false);
        initView();
        return mView;
    }

    private void initView() {
        GridLayoutManager theLm = new GridLayoutManager(getActivity(), 5);
        theLm.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager moreLm = new GridLayoutManager(getActivity(), 5);
        moreLm.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTheItems = findViewById(R.id.health_diary_rv_more_items_the_items);
        mRvMoreItems = findViewById(R.id.health_diary_rv_more_items_more_items);
        mTheAdapter = new TheAdapter();
        mRvTheItems.setLayoutManager(theLm);
        mRvTheItems.setAdapter(mTheAdapter);
        mMoreAdapter = new MoreAdapter();
        mRvMoreItems.setLayoutManager(moreLm);
        mRvMoreItems.setAdapter(mMoreAdapter);
        findViewById(R.id.health_diary_more_items_tv_top_back).setOnClickListener(backOnClickListener);
        findViewById(R.id.health_diary_more_items_tv_top_action).setOnClickListener(actionOnClickListener);
    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (mView == null) {
            return null;
        }
        return (V) mView.findViewById(id);
    }

    private View.OnClickListener actionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent("theItems");
            intent.putExtra("title", mTitle);
            intent.putStringArrayListExtra("theItems", mTheItems);
            intent.putStringArrayListExtra("moreItems", mMoreItems);
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(v.getContext());
            lbm.sendBroadcast(intent);
            removeSelf();
        }
    };

    private void removeSelf() {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }
    }

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           removeSelf();
        }
    };

    private View.OnClickListener theItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mRvTheItems.getChildAdapterPosition(v);
            String removed = mTheItems.remove(position);
            mMoreItems.add(removed);
            mTheAdapter.notifyDataSetChanged();
            mMoreAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener moreItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTheItems.size() >= 10) {
                return;
            }
            int position = mRvMoreItems.getChildAdapterPosition(v);
            String removed = mMoreItems.remove(position);
            mTheItems.add(removed);
            mMoreAdapter.notifyDataSetChanged();
            mTheAdapter.notifyDataSetChanged();
        }
    };

    private class TheHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public TheHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.health_diary_tv_the_item_content);
            itemView.setOnClickListener(theItemOnClickListener);
        }

        public void onBind(int position) {
            tvContent.setText(mTheItems.get(position));
        }
    }

    private class TheAdapter extends RecyclerView.Adapter<TheHolder> {

        @Override
        public TheHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.health_item_the_item, viewGroup, false);
            return new TheHolder(view);
        }

        @Override
        public void onBindViewHolder(TheHolder theHolder, int position) {
            theHolder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mTheItems == null ? 0 : mTheItems.size();
        }
    }

    private class MoreHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public MoreHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.health_diary_tv_more_item_content);
            itemView.setOnClickListener(moreItemOnClickListener);
        }

        public void onBind(int position) {
            itemView.setEnabled(mTheItems.size() < 10);
            tvContent.setEnabled(mTheItems.size() < 10);
            tvContent.setText(mMoreItems.get(position));
        }
    }

    private class MoreAdapter extends RecyclerView.Adapter<MoreHolder> {

        @Override
        public MoreHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.health_item_more_item, viewGroup, false);
            return new MoreHolder(view);
        }

        @Override
        public void onBindViewHolder(MoreHolder moreHolder, int position) {
            moreHolder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mMoreItems == null ? 0 : mMoreItems.size();
        }
    }
}
