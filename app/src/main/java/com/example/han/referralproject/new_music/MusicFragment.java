package com.example.han.referralproject.new_music;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.han.referralproject.R;
import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.wcy.lrcview.LrcView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnClickListener, MusicService.MusicPreParedOk, MusicService.MusicFinish {


    @BindView(R.id.iv_play_page_bg)
    ImageView ivPlayPageBg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.vp_play_page)
    ViewPager vpPlayPage;
    @BindView(R.id.il_indicator)
    IndicatorLayout ilIndicator;//指示器
    @BindView(R.id.iv_mode)
    ImageView ivMode;
    @BindView(R.id.iv_prev)
    ImageView ivPrev;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    Unbinder unbinder;
    private boolean isSongFinish=false;//标记一首歌是否完整播放完；

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
        view = inflater.inflate(R.layout.fragment_music, container, false);
        unbinder = ButterKnife.bind(this, view);

        ivGoBack.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        initViewPager();
        return view;
    }
    private void initViewPager() {
        //封面图
        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_page_cover, null);
        //歌词
        View lrcView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_page_lrc, null);
        //碟片
        mAlbumCoverView = (AlbumCoverView) coverView.findViewById(R.id.album_cover_view);
        //碟片下的歌词
        mLrcViewSingle = (LrcView) coverView.findViewById(R.id.lrc_view_single);
        //歌词
        mLrcViewFull = (LrcView) lrcView.findViewById(R.id.lrc_view_full);
        //音量调节键
        sbVolume = (SeekBar) lrcView.findViewById(R.id.sb_volume);

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        vpPlayPage.setAdapter(new PlayPagerAdapter(mViewPagerContent));
        ilIndicator.create(mViewPagerContent.size());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_go_back:
                getActivity().finish();
                break;
            case R.id.iv_play:
                if(musicService.isPlaying()){
                    musicService.pause();
                    ivPlay.setSelected(false);
                    mAlbumCoverView.pause();
                }else{
                    if (isSongFinish){
                        setLrc(music);
                        isSongFinish=false;
                        timeCount.start();
                    }
                    musicService.play();
                    ivPlay.setSelected(true);
                    mAlbumCoverView.start();
                }
                break;

        }
    }
    public void setMusicService(MusicService musicService){
        this.musicService=musicService;
        musicService.setOnMusicPreparedListener(this);
        musicService.setOnCompletion(this);
    }
    //音乐准备好了，可以开始播放了
    @Override
    public void prepared(Music music) {
        MusicFragment.this.music=music;
        //首先把标题、时间等设置到页面上去
        setMusicInfo(music);
        timeCount=new TimeCount(music.getDuration(),500);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        isSongFinish=true;
        if (ivPlay!=null) {
            ivPlay.setSelected(false);
        }
        if (mLrcViewSingle!=null) {
            mLrcViewSingle.updateTime(0);
        }
        if (mLrcViewFull!=null) {
            mLrcViewFull.updateTime(0);
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
