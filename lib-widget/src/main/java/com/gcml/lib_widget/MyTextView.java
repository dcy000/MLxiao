package com.gcml.lib_widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/10 18:16
 * created by:gzq
 * description:TODO
 */
@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    private final String namespace = "http://blog.csdn.net/Justin_Dong1122";
    private String text;
    private float textSize;
    private Paint paint1 = new Paint();
    private float paddingLeft;
    private float paddingRight;
    private float textShowWidth;
    private int textColor;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        textSize = attrs.getAttributeIntValue(namespace, "textSize", 18);
        textColor = attrs.getAttributeIntValue(namespace, "textColor", Color.BLACK);
        paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
        paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
        paint1.setTextSize(textSize);
        paint1.setColor(textColor);
        paint1.setAntiAlias(true);
        textShowWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() - paddingLeft - paddingRight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int lineCount = 0;
        if(text != null)
        {
            char[] textCharArray = text.toCharArray();
            // 已绘的宽度
            float drawedWidth = 0;
            float charWidth;
            for (int i = 0; i < textCharArray.length; i++) {
                charWidth = paint1.measureText(textCharArray, i, 1);
                if(textCharArray[i] == '\n' || textCharArray[i] == '\r')
                {
                    lineCount++;
                    drawedWidth = 0;
                }
                if (textShowWidth - drawedWidth < charWidth) {
                    lineCount++;
                    drawedWidth = 0;
                }
                canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,(lineCount + 1) * textSize, paint1);
                drawedWidth += charWidth;
            }
            setHeight((lineCount + 1) * (int) textSize + 5);
        }
    }

    @Override
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

}

