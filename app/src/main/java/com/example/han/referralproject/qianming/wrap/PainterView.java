package com.example.han.referralproject.qianming.wrap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PainterView extends View {
    private Paint mPaint;
    private Path mPath;
    private float mlastX;//上一次事件的终点x
    private float mLastY;//上一次事件的终点y


    public PainterView(Context context) {
        this(context, null);
    }

    public PainterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PainterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mlastX = x;
                mLastY = y;
                return true;//消费按下事件,则整个时间序列都将交给该view处理
            case MotionEvent.ACTION_MOVE:
                float endX = (mlastX + x) / 2;//结束点x
                float endY = (mLastY + y) / 2;//结束点y
                mPath.quadTo(mlastX, mLastY, endX, endY);//上一次的终点作为起点,上一个点作为控制点,中间点作为终点
                mlastX = x;//记录上一次的操作点
                mLastY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }


    /**
     * 设置画笔颜色
     *
     * @param penColorId
     */
    public void setPenColorRes(@ColorRes int penColorId) {
        int color = getResources().getColor(penColorId);
        mPaint.setColor(color);
    }

    /**
     * 设置画笔颜色
     *
     * @param penColor
     */
    public void setPenColor(@ColorInt int penColor) {
        mPaint.setColor(penColor);
    }

    /**
     * 设置画笔宽度
     *
     * @param penWidth
     */
    public void setPenWidth(float penWidth) {
        mPaint.setStrokeWidth(penWidth);
    }

    /**
     * 重置,即擦除所有内容
     */
    public void clear() {
        mPath.reset();//重置为空白路径
        invalidate();//绘制空白路径
    }

    /**
     * 生成快照
     *
     * @return 返回当前的快照
     */
    public Bitmap creatBitmap() {
        return convertViewToBitmap(this, getWidth(), getHeight());
    }

    public Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_4444);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_4444, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                int color = mBitmap.getPixel(j, i);
                if (color == oldColor) {
                    mBitmap.setPixel(j, i, newColor);
                }

            }
        }
        return mBitmap;
    }

}  