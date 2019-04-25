package com.gcml.module_health_profile.checklist.wrap;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;

/**
 * Created by lenovo on 2019/4/24.
 */

public class OutLayout extends LinearLayout {

    private TextView name;
    private FrameLayout container;

    public OutLayout(Context context) {
        super(context);
        init();
    }

    public OutLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.out_layout, null);
        name = view.findViewById(R.id.tv_out_name);
        container = view.findViewById(R.id.fl_out_container);
        addView(view);
    }

    public OutLayout name(String name) {
        if (name != null) {
            this.name.setText(name);
        }
        return this;
    }

    public OutLayout addLayout(View view) {
        if (view != null) {
            container.addView(view);
        }
        return this;
    }

    public OutLayout textViewMarginLeft(boolean margin) {
        int marginLeft;
        if (margin) {
            marginLeft = 100;
            name.setTextColor(Color.parseColor("#999999"));
        } else {
            marginLeft = 0;
            name.setTextColor(Color.parseColor("#333333"));
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) name.getLayoutParams();
        lp.setMargins(marginLeft, 0, 0, 0);

        return this;
    }

}
