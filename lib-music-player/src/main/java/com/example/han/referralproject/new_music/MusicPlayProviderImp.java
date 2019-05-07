package com.example.han.referralproject.new_music;

import android.content.Context;
import android.content.Intent;

import com.gcml.common.service.IMusicPlayProvider;
import com.gcml.common.utils.UM;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/music/player/provider")
public class MusicPlayProviderImp implements IMusicPlayProvider {

    @Override
    public void searchAndPlayMusic(Context context, String musicName) {
        MusicUtils.searchAndPlayMusic(context, musicName);
    }
}
