package com.witspring.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * 自定义文字、图片具体的单项选择框
 * @author Created by Goven on 14-10-27 下午6:06
 * @email gxl3999@gmail.com
 */
public class CenterRadioButton extends RadioButton {

    private Drawable mButtonDrawable;
    private int mButtonResource;

    public CenterRadioButton(Context context) {
        super(context);
    }

    public CenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setButtonDrawable(int resid) {
        if (resid != 0 && resid == mButtonResource) {
            return;
        }

        mButtonResource = resid;

        Drawable d = null;
        if (mButtonResource != 0) {
            d = getResources().getDrawable(mButtonResource);
        }
        setButtonDrawable(d);
    }

    @Override
    public void setButtonDrawable(Drawable d) {
        if (d != null) {
            if (mButtonDrawable != null) {
                mButtonDrawable.setCallback(null);
                unscheduleDrawable(mButtonDrawable);
            }
            d.setCallback(this);
            d.setState(getDrawableState());
            d.setVisible(getVisibility() == VISIBLE, false);
            mButtonDrawable = d;
            mButtonDrawable.setState(null);
            setMinHeight(mButtonDrawable.getIntrinsicHeight());
        }
        refreshDrawableState();
    }

    // 核心代码部分
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final Drawable buttonDrawable = mButtonDrawable;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();
            final int width = buttonDrawable.getIntrinsicWidth();
            int y = 0;
            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }
            int x = 0;
            x = (getWidth() - width) / 2;
            buttonDrawable.setBounds(x, y, x + width, y + height);
            buttonDrawable.draw(canvas);
        }
    }
}
