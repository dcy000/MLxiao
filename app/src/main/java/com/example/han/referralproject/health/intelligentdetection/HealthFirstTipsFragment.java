package com.example.han.referralproject.health.intelligentdetection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 *
 */
public class HealthFirstTipsFragment extends Fragment {

    private Callback mCallback;
    private TextView tvAction;
    private TextView tvTpis;

    public interface Callback {
        void onActionStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = ((Callback) context);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public HealthFirstTipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private int layoutId() {
        return R.layout.health_fragment_first_tips;
    }

    private void initView(View view, Bundle savedInstanceState) {
        tvTpis = view.findViewById(R.id.tv_tips);
        tvAction = view.findViewById(R.id.tv_action);
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onActionStart();
                }
            }
        });
    }
}
