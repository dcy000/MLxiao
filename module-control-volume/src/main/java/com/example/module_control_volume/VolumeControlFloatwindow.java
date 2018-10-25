package com.example.module_control_volume;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gcml.common.widget.base.dialog.DialogImage;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import java.lang.reflect.Field;

import static android.content.Context.AUDIO_SERVICE;

/**
 * @author gcml_gzq
 */
public class VolumeControlFloatwindow {
    /**
     * 获取设备当前音量
     */
    private static int currentVolume;
    private static TextView tvVolume;
    private static int maxVolume;
    private static ImageView icon_voice;
    private static AudioManager mAudioManager;
    private static SeekBar seekBar;

    public static void init(final Context application) {

        //初始化音频管理器
        mAudioManager = (AudioManager) application.getSystemService(AUDIO_SERVICE);
        //获取系统最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        final ImageView imageView = new ImageView(application);
        imageView.setClickable(true);
        imageView.setFocusableInTouchMode(false);
        imageView.setImageResource(R.drawable.volume_control_float_button);
        if (FloatWindow.get("volume_control") != null) {
            return;
        }
        FloatWindow
                .with(application)
                .setView(imageView)
                .setWidth(Screen.height, 0.1f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.1f)
                .setX(Screen.width, 0.8f)
                .setY(Screen.height, 0.3f)
                .setMoveType(MoveType.slide, 0, 0)
                .setMoveStyle(500, new BounceInterpolator())
                //此处可以指定具体Activity中不现实悬浮按钮
//                .setFilter(true, A_Activity.class, C_Activity.class)
                .setViewStateListener(viewStateListener)
                .setPermissionListener(permissionListener)
                .setTag("volume_control")
                .setDesktopShow(true)
                .build();
        FloatWindow.get("volume_control").show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                View inflate = LayoutInflater.from(application).inflate(R.layout.volume_control_layout, null);
                seekBar = inflate.findViewById(R.id.seekBar);
                icon_voice = inflate.findViewById(R.id.image_volume);
                tvVolume = inflate.findViewById(R.id.tv_volume);
                seekBar.setMax(maxVolume);
                seekBar.setProgress(currentVolume);
                tvVolume.setText((int) ((currentVolume / (float) maxVolume) * 100) + "%");
                String userId = userId(application);
                if (inflate.findViewById(R.id.ludashi) != null) {
                    inflate.findViewById(R.id.ludashi).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClassName("com.ludashi.benchmarkhd", "com.ludashi.benchmarkhd.MainActivity");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
                    });
                    inflate.findViewById(R.id.ludashi).setVisibility("123456".equals(userId) ? View.VISIBLE : View.GONE);
                }
                if (inflate.findViewById(R.id.robot) != null) {
                    inflate.findViewById(R.id.robot).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClassName("com.example.han.referralproject", "com.example.han.referralproject.WelcomeActivity");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
                    });
                    inflate.findViewById(R.id.robot).setVisibility("123456".equals(userId) ? View.VISIBLE : View.GONE);
                }
                FloatWindow.get("volume_control").hide();
                // 郭志强的锅
                DialogImage dialogImage = new DialogImage(application);

                dialogImage.setContentView(inflate);
                dialogImage.getWindow().setDimAmount(0);
                dialogImage.setCanceledOnTouchOutside(true);
                dialogImage.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialogImage.show();
                dialogImage.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        FloatWindow.get("volume_control").show();
                        imageView.setFocusable(true);

                    }
                });
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        currentVolume = progress;
                        if (currentVolume == 0) {
                            icon_voice.setImageResource(R.drawable.volume_control_icon_voice_mute);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        tvVolume.setText((int) ((currentVolume / (float) maxVolume) * 100) + "%");
                        if (currentVolume == 0) {
                            icon_voice.setImageResource(R.drawable.volume_control_icon_voice_mute);
                            return;
                        }
                        icon_voice.setImageResource(R.drawable.volume_control_icon_voice);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);

                    }
                });
            }
        });
    }

    private static final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail() {

        }
    };
    private static final ViewStateListener viewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int i, int i1) {
        }

        @Override
        public void onShow() {

        }

        @Override
        public void onHide() {

        }

        @Override
        public void onDismiss() {

        }

        @Override
        public void onMoveAnimStart() {

        }

        @Override
        public void onMoveAnimEnd() {

        }

        @Override
        public void onBackToDesktop() {

        }
    };

    private static String userId(Context application) {
        String userId = "";
        try {
            Class<?> class_MyApplication = Class.forName("com.example.han.referralproject.application.MyApplication");
            Field field_userId = class_MyApplication.getField("userId");
            field_userId.setAccessible(true);
            Object obj = field_userId.get(application);
            userId = obj.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static void setVolumSB(int progress) {
        if (tvVolume!=null){
            tvVolume.setText((int) ((progress / (float) maxVolume) * 100) + "%");
        }
        if (seekBar!=null){
            seekBar.setProgress(progress);
        }
        if (icon_voice!=null){
            if (progress == 0) {
                icon_voice.setImageResource(R.drawable.volume_control_icon_voice_mute);
                return;
            }
            icon_voice.setImageResource(R.drawable.volume_control_icon_voice);
        }
        if (mAudioManager!=null){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
        }
    }

}
