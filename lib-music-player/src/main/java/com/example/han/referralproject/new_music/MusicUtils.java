package com.example.han.referralproject.new_music;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.provider.MediaStore;

import com.gcml.common.recommend.bean.get.Music;
import com.gcml.common.utils.display.ToastUtils;

import java.util.List;


/**
 * 歌曲工具类
 * Created by wcy on 2015/11/27.
 */
public class MusicUtils {
    private static final String SELECTION = MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " + MediaStore.Audio.AudioColumns.DURATION + " >= ?";

    /**
     * 扫描歌曲
     */
//    public static void scanMusic(Context context, List<Music> musicList) {
//        musicList.clear();
//
//        long filterSize = ParseUtils.parseLong(Preferences.getFilterSize()) * 1024;
//        long filterTime = ParseUtils.parseLong(Preferences.getFilterTime()) * 1000;
//
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                new String[]{
//                        BaseColumns._ID,
//                        MediaStore.Audio.AudioColumns.IS_MUSIC,
//                        MediaStore.Audio.AudioColumns.TITLE,
//                        MediaStore.Audio.AudioColumns.ARTIST,
//                        MediaStore.Audio.AudioColumns.ALBUM,
//                        MediaStore.Audio.AudioColumns.ALBUM_ID,
//                        MediaStore.Audio.AudioColumns.DATA,
//                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
//                        MediaStore.Audio.AudioColumns.SIZE,
//                        MediaStore.Audio.AudioColumns.DURATION
//                },
//                SELECTION,
//                new String[]{
//                        String.valueOf(filterSize),
//                        String.valueOf(filterTime)
//                },
//                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//        if (cursor == null) {
//            return;
//        }
//
//        int i = 0;
//        while (cursor.moveToNext()) {
//            // 是否为音乐，魅族手机上始终为0
//            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
//            if (!SystemUtils.isFlyme() && isMusic == 0) {
//                continue;
//            }
//
//            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
//            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
//            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
//            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
//            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
//            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
//            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
//            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//
//            Music music = new Music();
//            music.setId(id);
//            music.setType(Music.Type.LOCAL);
//            music.setTitle(title);
//            music.setArtist(artist);
//            music.setAlbum(album);
//            music.setAlbumId(albumId);
//            music.setDuration(duration);
//            music.setPath(path);
//            music.setFileName(fileName);
//            music.setFileSize(fileSize);
//            if (++i <= 20) {
//                // 只加载前20首的缩略图
//                CoverLoader.getInstance().loadThumbnail(music);
//            }
//            musicList.add(music);
//        }
//        cursor.close();
//    }
    public static Uri getMediaStoreAlbumCoverUri(long albumId) {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, albumId);
    }

    public static boolean isAudioControlPanelAvailable(Context context) {
        return isIntentAvailable(context, new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL));
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        return context.getPackageManager().resolveActivity(intent, PackageManager.GET_RESOLVED_FILTER) != null;
    }


    public static void searchAndPlayMusic(final Context context, String keyword) {
        HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response == null || response.getSong() == null) {
                    ToastUtils.showShort("暂时无法播放");
                    return;
                }
                List<SearchMusic.Song> songs = response.getSong();
                Activity activity = (Activity) context;
                new PlaySearchedMusic(activity, songs.get(0)) {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onExecuteSuccess(Music music) {
                        //跳转到音乐播放界面去
                        context.startActivity(new Intent(context, MusicPlayActivity.class)
                                .putExtra("music", music));
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        ToastUtils.showShort("暂时无法播放");
                    }
                }.execute();

            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.showShort("暂时无法播放");
            }
        });
    }


}
