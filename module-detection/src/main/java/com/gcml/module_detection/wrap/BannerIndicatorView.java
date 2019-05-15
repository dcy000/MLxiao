package com.gcml.module_detection.wrap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Mrz
 * @date 2018/9/5 16:44
 */
public class BannerIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private int mCurrentPosition;
    private Paint mPaint;
    private Paint mPaint2;
    //导航点的直径
    private int r;
    private int circleCount;
    private int width;
    private int height;
    private Context mContext;
    private int center;
    private int distance = 3;//这边只能是奇数,不能是偶数,默认为3,就是2个圆之间间隔1个圆的距离

    public BannerIndicatorView(Context context) {
        super(context);
        this.mContext = context.getApplicationContext();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public BannerIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context.getApplicationContext();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#3F86FC"));
        mPaint.setTextSize(16);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint2=new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.parseColor("#DDDDDD"));
        mPaint2.setTextSize(16);
        mPaint2.setStyle(Paint.Style.FILL);

        r = dip2px(8);
    }


    public void setViewpager(ViewPager viewpager) {
        viewpager.addOnPageChangeListener(this);
        this.circleCount = viewpager.getAdapter().getCount();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中心店
        center = (width - circleCount * distance * r) / 2;
        drawDot(canvas);
        drawRect(canvas);
    }


    private void drawDot(Canvas canvas) {
        canvas.translate(center, (height - r) / 2);
        for (int i = 0; i < circleCount; i++) {
            int x = (distance / 2) * r + (i) * distance * r;
            canvas.drawCircle(x, 0, r / 2, mPaint2);
        }

    }

    private void drawRect(Canvas canvas) {
        int x1 = (int) (mCurrentPosition * distance * r - 0.5 * r);
        int x2 = (int) ((distance * mCurrentPosition + (distance - 1) + 0.5) * r);
        canvas.drawRoundRect(new RectF(x1, -r /
                2, x2, r / 2), r / 2, r / 2, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
