package com.gcml.health.measure.test;

import android.bluetooth.BluetoothDevice;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.lib_widget.CircleImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

public class TestWuhuaqiActivity extends ToolbarBaseActivity implements ITestView {
    private CircleImageView mSwtich;
    private TextView mTime;
    private VideoView mVideo;
    private SocketPresenter socketPresenter;
    private int currentTime = 0;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wuhuaqi);
        initView();
        mTitleText.setText("雾 化 器 测 量");
        initBluetooth();
        playVideo();
    }

    private void initBluetooth() {
        socketPresenter = new SocketPresenter(this);
    }


    private void startTime(int start) {
        Observable.intervalRange(start, Integer.MAX_VALUE, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        TestWuhuaqiActivity.this.disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        currentTime = aLong.intValue();
                        mTime.setText(secToTime(aLong.intValue()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        Observable.interval(1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//
//                    }
//                });
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    private void playVideo() {
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
        });
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test_video);
//        mVideo.setVideoURI(uri);
        mVideo.start();
    }

    private void initView() {
        mSwtich = (CircleImageView) findViewById(R.id.swtich);
        mSwtich.setOnClickListener(this);
        mTime = (TextView) findViewById(R.id.time);
        mVideo = (VideoView) findViewById(R.id.video);
    }

    public void openOrClose() {
        if (socketPresenter != null && socketPresenter.isConnected()) {
            socketPresenter.openOrClose();
        }
    }

    @Override
    public void openedState() {
        mSwtich.setSelected(true);
        startTime(currentTime);

    }

    @Override
    public void closeedState() {
        mSwtich.setSelected(false);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.swtich) {
            openOrClose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideo.stopPlayback();
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {

    }
}
