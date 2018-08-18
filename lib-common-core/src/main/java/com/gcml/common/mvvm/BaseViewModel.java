package com.gcml.common.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends AndroidViewModel {

    private CompositeDisposable mDisposables = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
}
