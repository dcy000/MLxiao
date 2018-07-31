package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;

/**
 *
 */
public class HealthFirstTipsFragment extends Fragment {

    private TextView tvTpis;
    private ImageView ivRight;

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
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).speak(getResources().getString(R.string.health_first_detect_tips));
            activity.getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navToNext();
                }
            }, 3000);
        }
    }

    public void navToNext() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + "/" + R.raw.tips_xueya);
        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            FragmentManager fm = getFragmentManager();
            if (fm != null) {
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment;
                fragment = new HealthBloodDetectionUiFragment();
//            fragment = new HealthWeightDetectionUiFragment();
                transaction.replace(R.id.fl_container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }

        }
    }
}
