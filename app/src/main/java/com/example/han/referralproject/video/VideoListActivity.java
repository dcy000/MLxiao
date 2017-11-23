package com.example.han.referralproject.video;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VideoListActivity extends AppCompatActivity {

    @BindView(R.id.vp_video)
    ViewPager vpVideo;
    @BindView(R.id.rb_video_hypertension)
    RadioButton rbHypertension;
    @BindView(R.id.rb_video_stroke)
    RadioButton rbStroke;
    @BindView(R.id.rb_video_psychosis)
    RadioButton rbPsychosis;
    @BindView(R.id.rb_video_palsy)
    RadioButton rbPalsy;
    @BindView(R.id.rg_health_video)
    RadioGroup rgHealthVideo;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.fl_toolbar)
    FrameLayout flToolbar;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mUnbinder = ButterKnife.bind(this);

    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {

    }

    @OnClick(R.id.iv_home)
    public void onIvHomeClicked() {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
