package com.example.han.referralproject.new_music;

import android.app.Activity;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


public abstract class PlaySearchedMusic extends PlayMusic {
    private SearchMusic.Song mSong;

    public PlaySearchedMusic(SupportActivity activity, SearchMusic.Song song) {
        super(activity, 2);
        mSong = song;
    }

    @Override
    protected void getPlayInfo() {
        String lrcFileName = FileUtils.getLrcFileName(mSong.getArtistname(), mSong.getSongname());
        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
        if (!lrcFile.exists()) {
            downloadLrc(lrcFile.getPath());
        } else {
            mCounter++;
        }

        music = new Music();
        music.setType(Music.Type.ONLINE);
        music.setTitle(mSong.getSongname());
        music.setArtist(mSong.getArtistname());

//        RetrofitUrlManager.getInstance().putDomain("music", "http://tingapi.ting.baidu.com/");
//        Box.getRetrofit(MusicAPI.class)
//                .getMusicDownloadInfo("baidu.ting.song.play",mSong.getSongid())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.<HttpCallback<DownloadInfo>>autoDisposeConverter(mActivity))
//                .subscribe(new CommonObserver<HttpCallback<DownloadInfo>>() {
//                    @Override
//                    public void onNext(HttpCallback<DownloadInfo> response) {
//                        if (response == null || response.getBitrate() == null) {
//                            onFail(null);
//                            return;
//                        }
//
//                        music.setPath(response.getBitrate().getFile_link());
//                        music.setDuration(response.getBitrate().getFile_duration() * 1000);
//                        checkCounter();
//                    }
//                });
        // 获取歌曲播放链接
        HttpClient.getMusicDownloadInfo(mSong.getSongid(), new HttpCallback<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo response) {
                if (response == null || response.getBitrate() == null) {
                    onFail(null);
                    return;
                }

                music.setPath(response.getBitrate().getFile_link());
                music.setDuration(response.getBitrate().getFile_duration() * 1000);
                checkCounter();
            }

            @Override
            public void onFail(Exception e) {
                onExecuteFail(e);
            }
        });
    }

    private void downloadLrc(final String filePath) {
        HttpClient.getLrc(mSong.getSongid(), new HttpCallback<Lrc>() {
            @Override
            public void onSuccess(Lrc response) {
                if (response == null || TextUtils.isEmpty(response.getLrcContent())) {
                    return;
                }

                FileUtils.saveLrcFile(filePath, response.getLrcContent());
            }

            @Override
            public void onFail(Exception e) {
            }

            @Override
            public void onFinish() {
                checkCounter();
            }
        });
    }
}
