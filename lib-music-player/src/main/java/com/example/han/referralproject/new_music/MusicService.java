package com.example.han.referralproject.new_music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by gzq on 2018/1/23.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer = getMediaPlayer(this);
    private Music music;
    private AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind();
    }

    public void setMusicResourse(Music music) {
        this.music = music;
        if (music != null && !TextUtils.isEmpty(music.getPath())) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(music.getPath());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        //先请求焦点
        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (audioChange != null)
                    audioChange.onAudioFocusChange(audioManager, focusChange);
            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public long getCurrentTime() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }

    //音乐文件准备好了，可以开始播放
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (music==null){
            return;
        }
        music.setDuration(mp.getDuration());
        if (musicPreParedOk != null) {
            musicPreParedOk.prepared(music);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (musicFinish != null) {
            musicFinish.onFinish();
        }
    }

    public class MusicBind extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private MusicPreParedOk musicPreParedOk;

    public void setOnMusicPreparedListener(MusicPreParedOk musicPreParedOk) {
        this.musicPreParedOk = musicPreParedOk;
    }

    public interface MusicPreParedOk {
        void prepared(Music music);
    }

    private AudioChange audioChange;

    public void setOnnAudioFocusChangeListener(AudioChange audioChange) {
        this.audioChange = audioChange;
    }

    public interface AudioChange {
        void onAudioFocusChange(AudioManager manager, int focusChange);
    }

    public void setOnCompletion(MusicFinish musicFinish) {
        this.musicFinish = musicFinish;
    }

    private MusicFinish musicFinish;

    public interface MusicFinish {
        void onFinish();
    }


    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {

        }
        return mediaplayer;
    }
}
