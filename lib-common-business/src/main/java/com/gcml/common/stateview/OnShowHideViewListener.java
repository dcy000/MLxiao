package com.gcml.common.stateview;

import android.view.View;

/**
 * Created by lenovo on 2019/2/22.
 */

public interface OnShowHideViewListener {
    void onShowView(View view, int id);

    void onHideView(View view, int id);
}
