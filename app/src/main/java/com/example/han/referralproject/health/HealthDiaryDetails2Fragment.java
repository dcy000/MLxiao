package com.example.han.referralproject.health;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.DetailsModel;
import com.example.han.referralproject.health.model.ItemsModel;

public class HealthDiaryDetails2Fragment extends Fragment
        implements HealthDiaryDetailsFragment.OnActionListener {

    private View mView;
    private ItemsModel mItemsModel;
    private DetailsModel mDetailsModel;

    private String mItem;
    private HealthDiaryDetailsFragment.OnActionListener mOnActionListener;

    public static HealthDiaryDetails2Fragment newInstance(
            ItemsModel itemsModel,
            DetailsModel detailsModel) {
        Bundle args = new Bundle();
        args.putParcelable("itemsModel", itemsModel);
        args.putParcelable("detailsModel", detailsModel);
        HealthDiaryDetails2Fragment fragment = new HealthDiaryDetails2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HealthDiaryDetails2Fragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnActionListener = (HealthDiaryDetailsFragment.OnActionListener) context;
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
        mView = inflater.inflate(R.layout.fragment_health_diary_sports, container, false);
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
            itemsFragment = HealthDiaryItemsFragment.newInstance(mItemsModel);
            transaction.add(R.id.health_diary_fl_container_items, itemsFragment, mItemsModel.getTitle());
        } else {
            transaction.show(itemsFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void addOrShowDetailFragment(FragmentManager fm) {
        String tag = HealthDiaryDetailsFragment.class.getName() + mDetailsModel.getWhat();
        Fragment detailFragment = fm.findFragmentByTag(tag);
        FragmentTransaction transaction1 = fm.beginTransaction();
        if (detailFragment == null) {
            detailFragment = HealthDiaryDetailsFragment.newInstance(mDetailsModel);
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
