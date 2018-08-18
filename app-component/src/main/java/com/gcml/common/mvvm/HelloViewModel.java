package com.gcml.common.mvvm;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class HelloViewModel extends BaseViewModel {
    private ObservableField<String> hello = new ObservableField<>();

    public HelloViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> getHello() {
        return hello;
    }
}
