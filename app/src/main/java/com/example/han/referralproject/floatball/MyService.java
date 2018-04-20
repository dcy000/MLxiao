package com.example.han.referralproject.floatball;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class MyService extends Service implements DragFloatActionButton.onClickListener, SeekBar.OnSeekBarChangeListener {
    public static BaseActivity context;
    private int maxVolume;
    private int currentVolume;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showDialog();
        initPupubWindow();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void showDialog() {
        DragFloatActionButton floatBtn = new DragFloatActionButton(this);
        floatBtn.setImageResource(R.drawable.icons);
        floatBtn.setListener(this);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//         设置LayoutParams(全局变量）相关参数
        WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        /**
         * 注意，flag的值可以为：
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
         * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦
         * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
         */
        // 调整悬浮窗口至左上角，便于调整坐标
        windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        windowManagerParams.x = 0;
        windowManagerParams.y = 0;
//         设置悬浮窗口长宽数据
//        floatBtn.measure(0, 0);
        int originWidth = DensityUtil.dip2px(this, 58L);
        floatBtn.setOriginWidth(originWidth);
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        // 显示myFloatView图像
        windowManager.addView(floatBtn, windowManagerParams);
    }


    @Override
    public void onclick() {
        context.showWindow(popupWindow);
    }

    public static void StartMe(BaseActivity context) {
        MyService.context = context;
        Intent service = new Intent(context, MyService.class);
        context.startService(service);
    }

    PopupWindow popupWindow;

    private void initPupubWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
        }
        popupWindow.setWidth(960);
        popupWindow.setHeight(480);
        View view = LayoutInflater.from(this).inflate(R.layout.assistive_touch_inflate_layout, null);
        SeekBar sb = view.findViewById(R.id.seek);
        setSeekbar(sb);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
    }

    AudioManager mAudioManager;

    private void setSeekbar(SeekBar seekBar) {
        //初始化音频管理器
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //获取系统最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), AudioManager.FLAG_PLAY_SOUND);

    }


}
