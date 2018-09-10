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
 * created on 2018/9/10 18:53
 * created by:gzq
 * description:TODO
 */
@SuppressLint("AppCompatCustomView")
public class MyTextView2 extends TextView {
    private final String namespace = "http://www.angellecho.com/";
    private String text;
    private float textSize;
    private float paddingLeft;
    private float paddingRight;
    private float marginLeft;
    private float marginRight;
    private int textColor;


    private Paint paint1 = new Paint();
    private float textShowWidth;
    public MyTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res/android", "text");
        textSize = attrs.getAttributeIntValue(namespace, "textSize", 15);
        textColor = attrs.getAttributeIntValue(namespace, "textColor", Color.WHITE);
        paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
        paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
        marginLeft = attrs.getAttributeIntValue(namespace, "marginLeft", 0);
        marginRight = attrs.getAttributeIntValue(namespace, "marginRight", 0);
        paint1.setTextSize(textSize);
        paint1.setColor(textColor);
        paint1.setAntiAlias(true);
        textShowWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() - paddingLeft - paddingRight - marginLeft - marginRight;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int lineCount = 0;
        text = this.getText().toString();//.replaceAll("\n", "\r\n");
        if(text==null)return;
        char[] textCharArray = text.toCharArray();
        // 已绘的宽度
        float drawedWidth = 0;
        float charWidth;
        for (int i = 0; i < textCharArray.length; i++) {
            charWidth = paint1.measureText(textCharArray, i, 1);

            if(textCharArray[i]=='\n'){
                lineCount++;
                drawedWidth = 0;
                continue;
            }
            if (textShowWidth - drawedWidth < charWidth) {
                lineCount++;
                drawedWidth = 0;
            }
            canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
                    (lineCount + 1) * textSize, paint1);
            drawedWidth += charWidth;
        }
        setHeight((lineCount + 1) * (int) textSize + 5);
    }
}
