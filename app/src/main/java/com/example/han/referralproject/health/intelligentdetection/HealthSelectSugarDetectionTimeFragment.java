package com.example.han.referralproject.health.intelligentdetection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;

public class HealthSelectSugarDetectionTimeFragment extends Fragment {

    private ImageView mIvEmptyStomach;
    private ImageView mIvTwoHours;
    private ImageView mIvOtherTime;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private int layoutId() {
        return R.layout.health_fragment_select_sugar_time;
    }

    private void initView(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.iv_top_right).setVisibility(View.GONE);
        mIvEmptyStomach = (ImageView) view.findViewById(R.id.iv_empty_stomach);
        mIvTwoHours = (ImageView) view.findViewById(R.id.iv_two_hours);
        mIvOtherTime = (ImageView) view.findViewById(R.id.iv_other_time);
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_xuetang);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .remove(HealthSelectSugarDetectionTimeFragment.this)
                            .commitAllowingStateLoss();
                }
            }
        });
        mIvEmptyStomach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionListener != null) {
                    onActionListener.onAction(ACTION_EMPTY_STOMACH);
                }
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .remove(HealthSelectSugarDetectionTimeFragment.this)
                            .commitAllowingStateLoss();
                }
            }
        });
        mIvTwoHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionListener != null) {
                    onActionListener.onAction(ACTION_TWO_HOURS);
                }
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .remove(HealthSelectSugarDetectionTimeFragment.this)
                            .commitAllowingStateLoss();
                }
            }
        });
        mIvOtherTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionListener != null) {
                    onActionListener.onAction(ACTION_OTHER_TIME);
                }
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .remove(HealthSelectSugarDetectionTimeFragment.this)
                            .commitAllowingStateLoss();
                }
            }
        });
    }

    public static final int ACTION_EMPTY_STOMACH = 0;
    public static final int ACTION_TWO_HOURS = 2;
    public static final int ACTION_OTHER_TIME = 3;

    public interface OnActionListener {
        void onAction(int action);
    }

    private OnActionListener onActionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Fragment parentFragment = getParentFragment();
            if (parentFragment != null && parentFragment instanceof OnActionListener) {
                onActionListener = (OnActionListener) parentFragment;
                return;
            }
            onActionListener = (OnActionListener) context;
        } catch (Throwable e) {
            e.printStackTrace();
            onActionListener = null;
        }
    }

    @Override
    public void onDetach() {
        onActionListener = null;
        super.onDetach();
    }
}
