package com.example.han.referralproject.new_music;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.gcml.common.utils.network.NetUitls;


/**
 * Created by hzwangchenyan on 2017/1/20.
 */
public abstract class PlayMusic implements IExecutor<Music> {
    private Context mActivity;
    protected Music music;
    private int mTotalStep;
    protected int mCounter = 0;

    public PlayMusic(Context activity, int totalStep) {
        mActivity = activity;
        mTotalStep = totalStep;
    }

    @Override
    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        try {
            boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
            if (NetUitls.isAvailable() && !mobileNetworkPlay) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.mp_tips);
                builder.setMessage(R.string.mp_play_tips);
                builder.setPositiveButton(R.string.mp_play_tips_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Preferences.saveMobileNetworkPlay(true);
                        getPlayInfoWrapper();
                    }
                });
                builder.setNegativeButton(R.string.mp_cancel, null);
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
                getPlayInfoWrapper();
            }
        } catch (Exception e) {
            getPlayInfoWrapper();
        }

    }

    private void getPlayInfoWrapper() {
        onPrepare();
        getPlayInfo();
    }

    protected abstract void getPlayInfo();

    protected void checkCounter() {
        mCounter++;
        if (mCounter == mTotalStep) {
            onExecuteSuccess(music);
        }
    }
}
