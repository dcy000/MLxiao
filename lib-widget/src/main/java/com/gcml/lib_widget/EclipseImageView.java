package com.gcml.lib_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 19:22
 * created by:gzq
 * description:自定义有点击效果的ImageView
 */
@SuppressLint("AppCompatCustomView")
public class EclipseImageView extends ImageView {
    private boolean touchEffect = true;
    public final float[] BG_PRESSED = new float[]{1, 0, 0, 0, -50, 0, 1,
            0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
    public final float[] BG_NOT_PRESSED = new float[]{1, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

    public EclipseImageView(Context context) {
        super(context);
    }

    public EclipseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EclipseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        updateView(pressed);
        super.setPressed(pressed);
    }

    /**
     * 根据是否按下去来刷新bg和src
     *
     * @param pressed
     */
    private void updateView(boolean pressed) {
        //如果没有点击效果
        if (!touchEffect) {
            return;
        }//end if
        Drawable background = this.getBackground();
        if (background == null) {
            return;
        }
        if (pressed) {//点击
            /**
             * 通过设置滤镜来改变图片亮度@minghao
             */
            this.setDrawingCacheEnabled(true);
            this.setColorFilter(new ColorMatrixColorFilter(BG_PRESSED));
            background.setColorFilter(new ColorMatrixColorFilter(BG_PRESSED));
        } else {//未点击
            this.setColorFilter(new ColorMatrixColorFilter(BG_NOT_PRESSED));
            background.setColorFilter(
                    new ColorMatrixColorFilter(BG_NOT_PRESSED));
        }
    }

}
