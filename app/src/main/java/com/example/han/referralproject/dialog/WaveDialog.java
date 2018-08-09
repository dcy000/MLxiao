package com.example.han.referralproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.han.referralproject.R;

public class WaveDialog extends Dialog implements Runnable{
    private Context mContext;
    private VoiceLineView mVoiceLineView;
    private MediaRecorder mMediaRecorder;
    private boolean isAlive=true;

    public WaveDialog(Context context){
        super(context, R.style.XDialog_pup);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voiceinput_popwindow);
        getWindow().setGravity(Gravity.BOTTOM);
        mVoiceLineView = findViewById(R.id.voicLine);
        Thread thread = new Thread(this);
        thread.start();
    }

    public VoiceLineView getVoiceLineView(){
        return mVoiceLineView;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void run() {
        while (isAlive) {
            handler1.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAlive = false;
//        mMediaRecorder.release();
//        mMediaRecorder = null;
    }

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mMediaRecorder==null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            Log.e("音量大小",ratio+"");
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            mVoiceLineView.setVolume((int) (db));
        }
    };
}
