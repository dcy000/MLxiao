package com.gcml.module_health_profile.bracelet.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;

/**
 * Created by lenovo on 2019/2/20.
 */

public class CorrelationNumberItemLayout extends LinearLayout {

    private TextView name, phone;

    public CorrelationNumberItemLayout(Context context) {
        super(context);
        initView();
    }

    public CorrelationNumberItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_correalation_item, null);
        name = view.findViewById(R.id.tv_item_name);
        phone = view.findViewById(R.id.tv_item_phone);
        addView(view);
    }

    public void setItemName(String itemName) {
        if (TextUtils.isEmpty(itemName)) {
            return;
        }
        name.setText(itemName);
    }

    public void setItemPhone(String itemPhone) {
        if (TextUtils.isEmpty(itemPhone)) {
            return;
        }
        phone.setText(itemPhone);
    }
}
