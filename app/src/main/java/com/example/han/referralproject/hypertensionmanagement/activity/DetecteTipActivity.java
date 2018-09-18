package com.example.han.referralproject.hypertensionmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.han.referralproject.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class DetecteTipActivity extends AppCompatActivity {

    private String fromeWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detecte_tip);
        fromeWhere = getIntent().getStringExtra("fromeWhere");
        initEvent();
    }

    Disposable d;

    private void initEvent() {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        DetecteTipActivity.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        toDetecte();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dispose();
                    }

                    @Override
                    public void onComplete() {
                        dispose();
                    }
                });
    }

    private void toDetecte() {
        Timber.d("timer--3秒--时间就到");
        switch (fromeWhere) {
            case "0":

                break;
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    private void dispose() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}
