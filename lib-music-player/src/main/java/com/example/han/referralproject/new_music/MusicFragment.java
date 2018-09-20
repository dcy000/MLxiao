package com.example.han.referralproject.new_music;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import me.wcy.lrcview.LrcView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnClickListener, MusicService.MusicPreParedOk, MusicService.MusicFinish {

    ImageView ivPlayPageBg;

    TextView tvTitle;

    TextView tvArtist;

    ViewPager vpPlayPage;

    IndicatorLayout ilIndicator;//指示器

    ImageView ivMode;

    ImageView ivPrev;

    ImageView ivPlay;

    ImageView ivNext;
    LinearLayout llContent;
    ImageView ivGoBack;

    private boolean isSongFinish = false;//标记一首歌是否完整播放完；

    private View view;
    private MusicService musicService;
    private AlbumCoverView mAlbumCoverView;
    private LrcView mLrcViewSingle;
    private LrcView mLrcViewFull;
    private SeekBar sbVolume;
    private ArrayList<View> mViewPagerContent;
    private TimeCount timeCount;
    private Music music;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mp_fragment_music, container, false);
        ivPlayPageBg = view.findViewById(R.id.mp_iv_play_page_bg);
        tvTitle = view.findViewById(R.id.mp_tv_title);
        tvArtist = view.findViewById(R.id.mp_tv_artist);
        vpPlayPage = view.findViewById(R.id.mp_vp_play_page);
        ilIndicator = view.findViewById(R.id.mp_il_indicator);//指示器
        ivMode = view.findViewById(R.id.mp_iv_mode);
        ivPrev = view.findViewById(R.id.mp_iv_prev);
        ivPlay = view.findViewById(R.id.mp_iv_play);
        ivNext = view.findViewById(R.id.mp_iv_next);
        llContent = view.findViewById(R.id.mp_ll_content);
        ivGoBack = view.findViewById(R.id.mp_iv_go_back);
        ivGoBack.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        initViewPager();
        return view;
    }

    private void initViewPager() {
        //封面图
        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.mp_fragment_play_page_cover, null);
        //歌词
        View lrcView = LayoutInflater.from(getContext()).inflate(R.layout.mp_fragment_play_page_lrc, null);
        //碟片
        mAlbumCoverView = coverView.findViewById(R.id.mp_album_cover_view);
        //碟片下的歌词
        mLrcViewSingle = coverView.findViewById(R.id.mp_lrc_view_single);
        //歌词
        mLrcViewFull = lrcView.findViewById(R.id.mp_lrc_view_full);
        //音量调节键
        sbVolume = lrcView.findViewById(R.id.mp_sb_volume);

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        vpPlayPage.setAdapter(new PlayPagerAdapter(mViewPagerContent));
        ilIndicator.create(mViewPagerContent.size());
        ilIndicator.attachToViewPager(vpPlayPage);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mp_iv_go_back) {
            getActivity().finish();
        } else if (i == R.id.mp_iv_play) {
            if (musicService.isPlaying()) {
                musicService.pause();
                ivPlay.setSelected(false);
                mAlbumCoverView.pause();
            } else {
                if (isSongFinish) {
                    setLrc(music);
                    isSongFinish = false;
                    timeCount.start();
                }
                musicService.play();
                ivPlay.setSelected(true);
                mAlbumCoverView.start();
            }

        }
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
        musicService.setOnMusicPreparedListener(this);
        musicService.setOnCompletion(this);
    }

    //音乐准备好了，可以开始播放了
    @Override
    public void prepared(Music music) {
        MusicFragment.this.music = music;
        //首先把标题、时间等设置到页面上去
        setMusicInfo(music);
        timeCount = new TimeCount(music.getDuration(), 500);
        musicService.play();
        ivPlay.setSelected(true);
        mAlbumCoverView.start();
        timeCount.start();
    }

    private void setMusicInfo(Music musicInfo) {
        tvTitle.setText(musicInfo.getTitle());
        tvArtist.setText(musicInfo.getArtist());
        setCoverAndBg(musicInfo);
        setLrc(musicInfo);
    }

    //设置背景和封面
    private void setCoverAndBg(Music music) {
        mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music));
        ivPlayPageBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));
    }

    //设置歌词
    private void setLrc(final Music music) {
        if (music.getType() == Music.Type.LOCAL) {
            String lrcPath = FileUtils.getLrcFilePath(music);
            if (!TextUtils.isEmpty(lrcPath)) {
                loadLrc(lrcPath);
            } else {
                new SearchLrc(music.getArtist(), music.getTitle()) {
                    @Override
                    public void onPrepare() {
                        // 设置tag防止歌词下载完成后已切换歌曲
                        vpPlayPage.setTag(music);

                        loadLrc("");
                        setLrcLabel("正在搜索歌词");
                    }

                    @Override
                    public void onExecuteSuccess(@NonNull String lrcPath) {
                        if (vpPlayPage.getTag() != music) {
                            return;
                        }

                        // 清除tag
                        vpPlayPage.setTag(null);

                        loadLrc(lrcPath);
                        setLrcLabel("暂无歌词");
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        if (vpPlayPage.getTag() != music) {
                            return;
                        }
                        // 清除tag
                        vpPlayPage.setTag(null);

                        setLrcLabel("暂无歌词");
                    }
                }.execute();
            }
        } else {
            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
            loadLrc(lrcPath);
        }
    }

    //加载歌词
    private void loadLrc(String path) {
        File file = new File(path);
        mLrcViewSingle.loadLrc(file);
        mLrcViewFull.loadLrc(file);
    }

    private void setLrcLabel(String label) {
        mLrcViewSingle.setLabel(label);
        mLrcViewFull.setLabel(label);
    }

    //音乐播放结束
    @Override
    public void onFinish() {
        isSongFinish = true;
        if (ivPlay != null) {
            ivPlay.setSelected(false);
        }
        if (mLrcViewSingle != null) {
            mLrcViewSingle.updateTime(0);
        }
        if (mLrcViewFull != null) {
            mLrcViewFull.updateTime(0);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (mLrcViewSingle.hasLrc()) {
                mLrcViewSingle.updateTime(musicService.getCurrentTime());
                mLrcViewFull.updateTime(musicService.getCurrentTime());
            }
        }
    }
}
