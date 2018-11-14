package com.gzq.lib_core.http.upload;


import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by wzm on 2017/6/11.
 */

public class UploadOnSubscribe implements FlowableOnSubscribe<Integer> {

    private FlowableEmitter<Integer> mObservableEmitter;
    private long mSumLength = 0L;
    private long uploaded = 0L;

    private int mPercent = 0;

    public UploadOnSubscribe(long sumLength) {
        this.mSumLength = sumLength;
    }

    public void onRead(long read) {
        uploaded+=read;
        if(mSumLength <= 0) {
            onProgress(-1);
        } else {
            onProgress((int) (100*uploaded/ mSumLength));
        }
    }

    private void onProgress(int percent) {
        if (mObservableEmitter == null) {
            return;
        }
        if(percent == mPercent) {
            return;
        }
        mPercent = percent;
        if (percent >= 100) {
            percent = 100;
            mObservableEmitter.onNext(percent);
            mObservableEmitter.onComplete();
            return;
        }
        mObservableEmitter.onNext(percent);
    }

    @Override
    public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
        this.mObservableEmitter = e;
    }

}
