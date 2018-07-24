package com.example.module_control_volume;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gcml.lib_utils.service.ProcessUtils;
import com.gcml.lib_utils.ui.dialog.DialogImage;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import static android.content.Context.AUDIO_SERVICE;

public class VolumeControlFloatwindow {
    // 获取设备当前音量
    private static int currentVolume;

    public static void init(final Context application) {

        //初始化音频管理器
        final AudioManager mAudioManager = (AudioManager) application.getSystemService(AUDIO_SERVICE);
        //获取系统最大音量
        final int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        final ImageView imageView = new ImageView(application);
        imageView.setClickable(true);
        imageView.setFocusableInTouchMode(false);
        imageView.setImageResource(R.drawable.volume_control_float_button);
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
                .setViewStateListener(new ViewStateListener() {
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
                })
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                })
                .setTag("volume_control")
                .setDesktopShow(false)
                .build();
        FloatWindow.get("volume_control").show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                View inflate = LayoutInflater.from(application).inflate(R.layout.volume_control_layout, null);
                SeekBar seekBar = inflate.findViewById(R.id.seekBar);
                final ImageView icon_voice = inflate.findViewById(R.id.image_volume);
                final TextView tvVolume = inflate.findViewById(R.id.tv_volume);
                seekBar.setMax(maxVolume);
                seekBar.setProgress(currentVolume);
                tvVolume.setText((int) ((currentVolume / (float) maxVolume) * 100) + "%");
                FloatWindow.get("volume_control").hide();
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
                        tvVolume.setText((int) ((progress / (float) maxVolume) * 100) + "%");
                        if (progress == 0) {
                            icon_voice.setImageResource(R.drawable.volume_control_icon_voice_mute);
                            return;
                        }
                        icon_voice.setImageResource(R.drawable.volume_control_icon_voice);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                    }
                });
            }
        });
    }

}
