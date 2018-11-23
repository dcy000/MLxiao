package com.example.han.referralproject.new_music;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AlertDialog;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.NetworkUtils;


/**
 * Created by hzwangchenyan on 2017/1/20.
 */
public abstract class PlayMusic implements IExecutor<Music> {
    protected SupportActivity mActivity;
    protected Music music;
    private int mTotalStep;
    protected int mCounter = 0;

    public PlayMusic(SupportActivity activity, int totalStep) {
        mActivity = activity;
        mTotalStep = totalStep;
    }

    @Override
    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkPlay = (boolean) KVUtils.get(Box.getString(R.string.mp_setting_key_mobile_network_play), false);
        if (!NetworkUtils.isWifiConnected() && !mobileNetworkPlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.mp_tips);
            builder.setMessage(R.string.mp_play_tips);
            builder.setPositiveButton(R.string.mp_play_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    KVUtils.put(Box.getString(R.string.mp_setting_key_mobile_network_play), true);
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
