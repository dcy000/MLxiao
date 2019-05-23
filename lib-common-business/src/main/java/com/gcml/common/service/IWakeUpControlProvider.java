package com.gcml.common.service;

import android.content.Context;

public interface IWakeUpControlProvider {
    void init(Context context);

    void enableWakeuperListening(boolean wake);
}
