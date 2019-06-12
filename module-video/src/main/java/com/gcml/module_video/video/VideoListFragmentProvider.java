package com.gcml.module_video.video;

import android.support.v4.app.Fragment;

import com.gcml.common.service.IVideoListFragmentProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/app/video/list/fragment/provider")
public class VideoListFragmentProvider implements IVideoListFragmentProvider {
    @Override
    public Fragment getVideoListFragment(int position) {
        return VideoListFragment.newInstance(position, null);
    }
}
