package com.medlink.danbogh.wakeup;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.ui.RecognizerDialog;

/**
 * Created by lenovo on 2017/11/23.
 */

public class MlRecognizerDialog extends RecognizerDialog {

    public MlRecognizerDialog(Context context, InitListener initListener) {
        super(context, initListener);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            View view = window.getDecorView();
            View textlink = view.findViewWithTag("textlink");
            if (textlink != null) {
                textlink.setVisibility(View.GONE);
            }
        }
    }
}
