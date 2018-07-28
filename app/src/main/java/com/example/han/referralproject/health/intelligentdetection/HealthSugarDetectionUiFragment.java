package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;

public class HealthSugarDetectionUiFragment extends HealthSugarDetectionFragment
        implements HealthSelectSugarDetectionTimeFragment.OnActionListener {

    private ImageView ivRight;
    private Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.tips_xuetang);
        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血糖测量演示视频");
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
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_xuetang);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
    }

    @Override
    protected void onSugarResult(float sugar) {

    }

    @Override
    public void onAction(int action) {

    }
}
