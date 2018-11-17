package com.example.han.referralproject.health;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.ItemsModel;
import com.gzq.lib_core.utils.UiUtils;

import java.util.ArrayList;

public class HealthDiaryItemsFragment extends Fragment {
    public static final String TAG = "HealthDiaryItemsFragment";

    private ItemsModel mModel;

    private View mView;
    private TextView tvItemsTitle;
    private FrameLayout flItemsContainer;
    private TextView tvItemsMore;
    private ArrayList<String> theItems;
    private ArrayList<String> moreItems;
    private WheelPicker mWheelPicker;

    public HealthDiaryItemsFragment() {

    }

    public static HealthDiaryItemsFragment newInstance(ItemsModel model) {
        HealthDiaryItemsFragment fragment = new HealthDiaryItemsFragment();
        Bundle args = new Bundle();
        args.putParcelable("itemsModel", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theItems = new ArrayList<>();
        moreItems = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mModel = arguments.getParcelable("itemsModel");
            if (mModel != null) {
                ArrayList<String> items = mModel.getItems();
                int size = items.size();
                for (int i = 0; i < size; i++) {
                    if (i >= 10) {
                        moreItems.add(items.get(i));
                    } else {
                        theItems.add(items.get(i));
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.health_fragment_diary_items, container, false);
        tvItemsTitle = (TextView) findViewById(R.id.health_diary_tv_items_title);
        flItemsContainer = (FrameLayout) findViewById(R.id.health_diary_fl_items_container);
        Context context = getContext();
        if (context == null) {
            return mView;
        }
        mWheelPicker = new WheelPicker(context);
        mWheelPicker.setVisibleItemCount(5);
        mWheelPicker.setItemTextSize(UiUtils.pt(70));
        mWheelPicker.setItemSpace(UiUtils.pt(50));
        mWheelPicker.setData(theItems);
        mWheelPicker.setOnItemSelectedListener(onItemSelectedListener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        flItemsContainer.addView(mWheelPicker, params);
        tvItemsMore = (TextView) findViewById(R.id.health_diary_tv_more);
        tvItemsMore.setOnClickListener(moreItemsOnClickListener);
        return mView;
    }

    private View.OnClickListener moreItemsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showMoreItems();
        }
    };

    private void showMoreItems() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm != null) {
            String tag = HealthDiaryMoreItemsFragment.class.getName() + mModel.getTitle();
            Fragment fragment = fm.findFragmentByTag(tag);
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment newFragment = HealthDiaryMoreItemsFragment.newInstance(mModel.getTitle(), theItems, moreItems);
            if (fragment == null) {
                transaction.add(android.R.id.content, newFragment, tag);
            } else {
                transaction.remove(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    private WheelPicker.OnItemSelectedListener onItemSelectedListener = new WheelPicker.OnItemSelectedListener() {
        @Override
        public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment != null) {
                ((HealthDiaryDetails2Fragment) parentFragment).onItemSelected((String) o);
            }
        }
    };

    public <V extends View> V findViewById(int id) {
        if (mView == null) {
            return null;
        }
        return mView.findViewById(id);
    }

    private LocalBroadcastManager lbm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lbm = LocalBroadcastManager.getInstance(context);
        lbm.registerReceiver(receiver, new IntentFilter("theItems"));
    }

    @Override
    public void onDetach() {
        lbm.unregisterReceiver(receiver);
        super.onDetach();
    }

    private MyReceiver receiver = new MyReceiver();

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String title = intent.getStringExtra("title");
            if ("theItems".equals(action)
                    && TextUtils.equals(title, mModel.getTitle())) {
                theItems = intent.getStringArrayListExtra("theItems");
                moreItems = intent.getStringArrayListExtra("moreItems");
                mWheelPicker.setData(theItems);
            }
        }
    }
}
