package com.gcml.task.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.task.R;
import com.gcml.task.bean.DetailsModel;
import com.gcml.task.bean.ItemsModel;

/**
 * desc: 每日任务类型和每日任务量组合页面.
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskDialyGroupFragment extends Fragment
        implements TaskDialyDetailsFragment.OnActionListener {

    private View mView;
    private ItemsModel mItemsModel;
    private DetailsModel mDetailsModel;

    private String mItem;
    private TaskDialyDetailsFragment.OnActionListener mOnActionListener;

    public static TaskDialyGroupFragment newInstance(
            ItemsModel itemsModel,
            DetailsModel detailsModel) {
        Bundle args = new Bundle();
        args.putParcelable("itemsModel", itemsModel);
        args.putParcelable("detailsModel", detailsModel);
        TaskDialyGroupFragment fragment = new TaskDialyGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskDialyGroupFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnActionListener = (TaskDialyDetailsFragment.OnActionListener) context;
        } catch (Throwable e) {
            e.printStackTrace();
            mOnActionListener = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mItemsModel = arguments.getParcelable("itemsModel");
            mDetailsModel = arguments.getParcelable("detailsModel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_dialy_group, container, false);
        initView();
        return mView;
    }

    private void initView() {
        FragmentManager fm = getChildFragmentManager();
        addOrShowDetailFragment(fm);
        addOrShowItemsFragment(fm);
    }

    private void addOrShowItemsFragment(FragmentManager fm) {
        Fragment itemsFragment = fm.findFragmentByTag(mItemsModel.getTitle());
        FragmentTransaction transaction = fm.beginTransaction();
        if (itemsFragment == null) {
            itemsFragment = TaskDialyItemsFragment.newInstance(mItemsModel);
            transaction.add(R.id.health_diary_fl_container_items, itemsFragment, mItemsModel.getTitle());
        } else {
            transaction.show(itemsFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void addOrShowDetailFragment(FragmentManager fm) {
        String tag = TaskDialyDetailsFragment.class.getName() + mDetailsModel.getWhat();
        Fragment detailFragment = fm.findFragmentByTag(tag);
        FragmentTransaction transaction1 = fm.beginTransaction();
        if (detailFragment == null) {
            detailFragment = TaskDialyDetailsFragment.newInstance(mDetailsModel);
            transaction1.add(R.id.health_diary_fl_container_content, detailFragment, tag);
        } else {
            transaction1.show(detailFragment);
        }
        transaction1.commitAllowingStateLoss();
    }

    public void onItemSelected(String item) {
        mItem = item;
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        if (mOnActionListener != null) {
            mOnActionListener.onAction(what, selectedValue, unitPosition, mItem);
        }
    }

    @Override
    public void onDetach() {
        mOnActionListener = null;
        super.onDetach();
    }
}
