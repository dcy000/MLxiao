package com.gcml.module_detection.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.R;
import com.gcml.module_detection.wrap.BannerIndicatorView;

import java.util.ArrayList;

public class BloodsugarSearchFragment extends BluetoothBaseFragment {
    private TextView mTvTip;
    private ViewPager mViewpage;
    private TextView mMessage;
    private BannerIndicatorView indicator;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    int[] images = {R.drawable.ic_bloodsugar1, R.drawable.ic_bloodsugar2, R.drawable.ic_bloodsugar3, R.drawable.ic_bloodsugar4, R.drawable.ic_bloodsugar5, R.drawable.ic_bloodsugar6, R.drawable.ic_bloodsugar7};

    String[] tips = {
            "打开采血笔，安装采血针",
            "拧断采血针帽，不丢弃",
            "装上笔帽和连接筒，并旋转调整针的深度",
            "拉开采血笔，握住笔盖及笔杆滑动以至听到咔嚓的声音",
            "插入试纸，将试纸的电机感应区对着仪器的插入测试端口",
            "擦去第一滴血，取第二滴血进行检测",
            "测量完成后等待5秒，仪器显示结果后，机器人会自动连接蓝牙"

    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodsugar_search;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);
        mViewpage = (ViewPager) view.findViewById(R.id.viewpage);
        mMessage = (TextView) view.findViewById(R.id.message);
        indicator = view.findViewById(R.id.indicator);
        initFragments();
        initViewPage();

    }

    private void initFragments() {
        for (int i = 0; i < images.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("image", images[i]);
            ImageFragment fragment = new ImageFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    private void initViewPage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        indicator.setViewpager(mViewpage);
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMessage.setText(tips[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
