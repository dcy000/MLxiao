package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;

public class HealthBloodDetectionIntelligentFragment extends HealthBloodDetectionFragment {

    private ImageView ivRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.tips_xueya);
        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            startDetection();
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        tvDetectionAgain.setVisibility(View.GONE);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDetectionStepChanged(detectionStep);
    }

    private SparseIntArray highPressures = new SparseIntArray();
    private SparseIntArray lowPressures = new SparseIntArray();
    private SparseIntArray pulses = new SparseIntArray();

    private View.OnClickListener actionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startDetection();
        }
    };

    @Override
    protected void onBloodResult(int highPressure, int lowPressure, int pulse) {
        highPressures.put(detectionStep, highPressure);
        lowPressures.put(detectionStep, lowPressure);
        pulses.put(detectionStep, pulse);
        dispatchNextDetectionStep();
    }

    /**
     *
     */
    @IntDef({
            DetectionStep.LEFT_1,
            DetectionStep.LEFT_2,
            DetectionStep.LEFT_3,
            DetectionStep.RIGHT_1,
            DetectionStep.RIGHT_2,
            DetectionStep.RIGHT_3,
            DetectionStep.DONE
    })
    private @interface DetectionStep {
        int LEFT_1 = 1;
        int LEFT_2 = 2;
        int LEFT_3 = 3;
        int RIGHT_1 = 4;
        int RIGHT_2 = 5;
        int RIGHT_3 = 6;
        int DONE = 7;
    }

    @DetectionStep
    private int detectionStep = DetectionStep.LEFT_1;

    private void dispatchNextDetectionStep() {
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                notifyDetectionStepChanged(DetectionStep.LEFT_2);
                break;
            case DetectionStep.LEFT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.LEFT_1)
                        - highPressures.get(DetectionStep.LEFT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.LEFT_1)
                        - lowPressures.get(DetectionStep.LEFT_2))) {
                    notifyDetectionStepChanged(DetectionStep.LEFT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                }
                break;
            case DetectionStep.LEFT_3:
                notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                break;
            case DetectionStep.RIGHT_1:
                notifyDetectionStepChanged(DetectionStep.RIGHT_2);
                break;
            case DetectionStep.RIGHT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.RIGHT_1)
                        - highPressures.get(DetectionStep.RIGHT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.RIGHT_1)
                        - lowPressures.get(DetectionStep.RIGHT_2))) {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.DONE);
                }
                break;
            case DetectionStep.RIGHT_3:
                notifyDetectionStepChanged(DetectionStep.DONE);
                break;

        }
    }

    private void notifyDetectionStepChanged(@DetectionStep int detectionStep) {
        this.detectionStep = detectionStep;
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                showTips(
                        getString(R.string.health_tips_left_1),
                        "2–3次",
                        R.color.f56c6c,
                        actionOnClickListener);
                break;
            case DetectionStep.LEFT_2:
                showTips(
                        getString(R.string.health_tips_left_2),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.LEFT_3:
                showTips(
                        getString(R.string.health_tips_left_3),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_1:
                showTips(
                        getString(R.string.health_tips_right_1),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_2:
                showTips(
                        getString(R.string.health_tips_right_2),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_3:
                showTips(
                        getString(R.string.health_tips_right_3),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.DONE:
                break;
        }
    }

    private void showTips(
            String tips,
            String colorText,
            @ColorRes int color,
            View.OnClickListener actionOnClickListener) {
        CommonTipsDialogFragment df = CommonTipsDialogFragment.newInstance(tips, colorText, color);
        df.setActionOnClickListener(actionOnClickListener);
        FragmentManager fm = getChildFragmentManager();
        String tag = CommonTipsDialogFragment.class.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        df.show(fm, tag);
    }
}
