package com.gcml.task.ui;


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

import com.gcml.common.utils.UM;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;

import java.util.ArrayList;

/**
 * desc: 每日任务类型更多选项（最多支持8项任务类型添加） .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskDialyMoreFragment extends Fragment {

    private TranslucentToolBar mToolBar;
    private ArrayList<String> mTheItems;
    private ArrayList<String> mMoreItems;
    private View mView;
    private RecyclerView selectedRecycler;
    private RecyclerView unselectedRecycler;
    private TheAdapter mTheAdapter;
    private MoreAdapter mMoreAdapter;
    private String mTitle;

    public static TaskDialyMoreFragment newInstance(
            String title,
            ArrayList<String> theItems,
            ArrayList<String> moreItems) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("theItems", (ArrayList<String>) theItems.clone());
        args.putStringArrayList("moreItems", (ArrayList<String>) moreItems.clone());
        TaskDialyMoreFragment fragment = new TaskDialyMoreFragment();
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
        mView = inflater.inflate(R.layout.fragment_task_dialy_more, container, false);
        bindView();
        bindData();
        return mView;
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task_more_item);
        selectedRecycler = findViewById(R.id.rv_task_more_item_selected);
        unselectedRecycler = findViewById(R.id.rv_task_more_item_unselected);
    }

    private void bindData() {
        mToolBar.setData(UM.getString(R.string.title_daily_task), R.drawable.common_icon_back, UM.getString(R.string.toolbar_back), 0, UM.getString(R.string.finished), new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                removeSelf();
            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent("theItems");
                intent.putExtra("title", mTitle);
                intent.putStringArrayListExtra("theItems", mTheItems);
                intent.putStringArrayListExtra("moreItems", mMoreItems);
                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
                lbm.sendBroadcast(intent);
                removeSelf();
            }
        });
        GridLayoutManager selectedLm = new GridLayoutManager(getActivity(), 5);
        selectedLm.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager unselectedLm = new GridLayoutManager(getActivity(), 5);
        unselectedLm.setOrientation(LinearLayoutManager.VERTICAL);
        mTheAdapter = new TheAdapter();
        selectedRecycler.setLayoutManager(selectedLm);
        selectedRecycler.setAdapter(mTheAdapter);
        mMoreAdapter = new MoreAdapter();
        unselectedRecycler.setLayoutManager(unselectedLm);
        unselectedRecycler.setAdapter(mMoreAdapter);
    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (mView == null) {
            return null;
        }
        return (V) mView.findViewById(id);
    }

    private void removeSelf() {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }
    }

    private View.OnClickListener theItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = selectedRecycler.getChildAdapterPosition(v);
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
            int position = unselectedRecycler.getChildAdapterPosition(v);
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
            View view = inflater.inflate(R.layout.item_task_daily_item, viewGroup, false);
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
            View view = inflater.inflate(R.layout.item_task_daily_more, viewGroup, false);
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
