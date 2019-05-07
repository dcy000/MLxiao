package com.gcml.module_video.video;

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

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_video.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;
@Route(path = "/video/list/activity")
public class VideoListActivity extends ToolbarBaseActivity {

    ViewPager vpVideo;
    RadioGroup rgHealthVideo;

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
        vpVideo=findViewById(R.id.vp_video);
        rgHealthVideo=findViewById(R.id.rg_health_video);
        mToolbar.setVisibility(View.VISIBLE);
//        mTitleText.setText(R.string.title_health_class);
        mTitleText.setText(R.string.title_health_class);
//        mRightView.setImageResource(R.drawable.icon_wifi);
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
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "欢迎观看健康课堂");
        findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.view_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
            }
        });
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
        if (checkedId == R.id.rb_video_hypertension) {
            position = 0;

        } else if (checkedId == R.id.rb_video_stroke) {
            position = 1;

        } else if (checkedId == R.id.rb_video_psychosis) {
            position = 2;

        } else if (checkedId == R.id.rb_video_palsy) {
            position = 3;

        } else if (checkedId == R.id.rb_video_device_show) {
            position = 4;

        } else {
        }
        return position;
    }

    @Override
    protected void onDestroy() {
        Handlers.bg().removeCallbacksAndMessages(null);
        Handlers.ui().removeCallbacksAndMessages(null);
        MLVoiceSynthetize.destory();
        super.onDestroy();
    }

}