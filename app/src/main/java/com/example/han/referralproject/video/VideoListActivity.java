package com.example.han.referralproject.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.gcml.lib_utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Handlers;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoListActivity extends BaseActivity {

    @BindView(R.id.vp_video)
    ViewPager vpVideo;
    @BindView(R.id.rg_health_video)
    RadioGroup rgHealthVideo;
    private Unbinder mUnbinder;

    public static void launch(Context context, int position) {
        Intent intent = new Intent(context, VideoListActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mToolbar.setVisibility(View.VISIBLE);
//        mTitleText.setText(R.string.title_health_class);
        mTitleText.setText(R.string.title_health_class);
//        mRightView.setImageResource(R.drawable.icon_wifi);
        mUnbinder = ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);

        rgHealthVideo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                vpVideo.setCurrentItem(providePosition(checkedId));
            }
        });
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rgHealthVideo.check(provideCheckedId(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpVideo.setOffscreenPageLimit(3);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mFragments.add(VideoListFragment.newInstance(i));
        }
        vpVideo.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments == null ? 0 : mFragments.size();
            }
        });
        rgHealthVideo.check(provideCheckedId(position));

//        mRightView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoListActivity.this, WifiConnectActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"主人，欢迎观看健康课堂");
    }

    private List<VideoListFragment> mFragments;

    private int provideCheckedId(int position) {
        int checkedId = R.id.rb_video_hypertension;
        switch (position) {
            case 0:
                checkedId = R.id.rb_video_hypertension;
                break;
            case 1:
                checkedId = R.id.rb_video_stroke;
                break;
            case 2:
                checkedId = R.id.rb_video_psychosis;
                break;
            case 3:
                checkedId = R.id.rb_video_palsy;
                break;
            case 4:
                checkedId = R.id.rb_video_device_show;
                break;
            default:
                break;
        }
        return checkedId;
    }

    private int providePosition(int checkedId) {
        int position = 0;
        switch (checkedId) {
            case R.id.rb_video_hypertension:
                position = 0;
                break;
            case R.id.rb_video_stroke:
                position = 1;
                break;
            case R.id.rb_video_psychosis:
                position = 2;
                break;
            case R.id.rb_video_palsy:
                position = 3;
                break;
            case R.id.rb_video_device_show:
                position = 4;
                break;
            default:
                break;
        }
        return position;
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        Handlers.bg().removeCallbacksAndMessages(null);
        Handlers.ui().removeCallbacksAndMessages(null);
        MLVoiceSynthetize.destory();
        super.onDestroy();
    }

}