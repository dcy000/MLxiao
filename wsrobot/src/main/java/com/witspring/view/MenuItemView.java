package com.witspring.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

/**
 * @author Created by Goven on 2017/11/5 下午10:05
 * @email gxl3999@gmail.com
 */
public class MenuItemView extends android.support.v7.widget.AppCompatTextView {

    public MenuItemView(Context context) {
        super(context);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIconAndText(@Nullable int resId, @Nullable String text) {
        setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), resId, null), null, null, null);
        setText(text);
    }

}
