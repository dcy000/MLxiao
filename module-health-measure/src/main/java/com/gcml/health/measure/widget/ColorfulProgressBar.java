package com.gcml.health.measure.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.health.measure.R;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 18:37
 * created by:gzq
 * description:TODO
 */
public class ColorfulProgressBar extends FrameLayout {

    private int level = 0;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private int[] colors;

    public ColorfulProgressBar(@NonNull Context context) {
        this(context, null);
    }

    public ColorfulProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void setData() {
        Timber.e("level==" + level);
        if (level > 0) {
            for (int i = 0; i < 5; i++) {
                if (i <= level - 1) {
                    if (i == 0) {
                        view1.setBackgroundColor(colors[0]);
                    } else if (i == 1) {
                        view2.setBackgroundColor(colors[1]);
                    } else if (i == 2) {
                        view3.setBackgroundColor(colors[2]);
                    } else if (i == 3) {
                        view4.setBackgroundColor(colors[3]);
                    } else if (i == 4) {
                        view5.setBackgroundColor(colors[4]);
                    }
                } else {
                    if (i == 0) {
                        view1.setBackgroundColor(getResources().getColor(R.color.color_eeeeee));
                    } else if (i == 1) {
                        view2.setBackgroundColor(getResources().getColor(R.color.color_eeeeee));
                    } else if (i == 2) {
                        view3.setBackgroundColor(getResources().getColor(R.color.color_eeeeee));
                    } else if (i == 3) {
                        view4.setBackgroundColor(getResources().getColor(R.color.color_eeeeee));
                    } else if (i == 4) {
                        view5.setBackgroundColor(getResources().getColor(R.color.color_eeeeee));
                    }
                }
            }
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.health_measure_layout_colorful_progressbar, this);
        colors = context.getResources().getIntArray(R.array.array_color_progressbar);
        view1 = view.findViewById(R.id.view_1);
        view2 = view.findViewById(R.id.view_2);
        view3 = view.findViewById(R.id.view_3);
        view4 = view.findViewById(R.id.view_4);
        view5 = view.findViewById(R.id.view_5);

    }

    public void setLevel(int level) {
        Timber.e("level++" + level);
        this.level = level;
        setData();
    }
}
