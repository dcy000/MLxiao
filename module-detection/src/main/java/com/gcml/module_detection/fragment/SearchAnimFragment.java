package com.gcml.module_detection.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.R;
import com.gcml.module_detection.wrap.WaveView;

public class SearchAnimFragment extends BluetoothBaseFragment {

    private WaveView mWaveView;
    private TextView mTvMainTitle;
    private TextView mTvSubtitle;
    private String mainTitle;
    private String subTitle;
    private int imgRes;
    private ImageView mIvImage;

    @Override
    protected int initLayout() {
        return R.layout.fragment_connect_anim;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mWaveView = view.findViewById(R.id.wave_view);
        mTvMainTitle = (TextView) view.findViewById(R.id.tv_main_title);
        mTvSubtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        mIvImage = view.findViewById(R.id.iv_image);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mainTitle = arguments.getString("mainTitle");
            subTitle = arguments.getString("subTitle");
            imgRes = arguments.getInt("imgRes");
        }
        if (!TextUtils.isEmpty(mainTitle)) {
            mTvMainTitle.setText(mainTitle);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            mTvSubtitle.setText(subTitle);
        }
        if (imgRes != 0) {
            mIvImage.setImageResource(imgRes);
        }
        mWaveView.setDuration(5000);
        mWaveView.setSpeed(600);
        mWaveView.setStyle(Paint.Style.FILL);
        mWaveView.setColor(Color.parseColor("#CC3F86FC"));
        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());

    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveView.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveView.stop();
    }
}
