package com.medlink.danbogh.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;


public class BloodPressureSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    //定义刻度的范围
    private int mValueRange = 5;
    //定义一个盘快的范围
    private RectF mRange = new RectF();
    //定义温度计的宽度和中心宽度
    private int mWidth;
    private int mHeight;
    private int mCenterWidth;
    private int mCenterHeight;
    //定义总的宽度

    //定义温度计刻度总长度
    private int mTotalValueLength;

    //定义一下水银的宽度
    private int mMercuryWidth;
    //十的倍数的线长度
    private int mMaxLineLength;
    //五的倍数的线的长度
    private int mMidLineLength;
    //其他刻度线的长度
    private int mMinLineLength;
    //刻度间隔
    private int mUnitLength;
    //定义温度计距离画布的上宽度
    private int mValueTop;

    private int mBgColor = Color.parseColor("#ffffff");

    //绘制线条的画笔
    private Paint mLinePaint;
    private int mLineColor = Color.BLACK;
    private int lineWidth = 1;
    //绘制文本的画笔
    private Paint mTextPaint;
    private int textColor = Color.BLACK;

    private Paint mValuePaint;
    private int valueColor = Color.parseColor("#3F86FC");

    //设置温度上升的速度
    private volatile float mSpeed = 0;

    //设置背景图
    private Bitmap mBgBitmap;

    /**
     * 定义初始温度，当前显示正在变化也就是显示的温度，还有目标温度
     * 其中，初始温度不变，
     * 当前温度是有程序根据不同的速度和目标温度计算出来的，
     * 目标温度则是由仪器传送过来的数据
     */
    private float mBeginValue = (float) 0;
    private int mEndValue = 300;
    private volatile float mCurrentValue = (float) 60;
    private float mTargetValue = 0;

    /**
     * 定义每一毫秒绘制的次数
     */
    private int mspt = 50;

    //设置文字的大小
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 25, getResources().getDisplayMetrics());
    private float mSymbolTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 35, getResources().getDisplayMetrics());
    private float mShowSymbolTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 45, getResources().getDisplayMetrics());
    /**
     * 用户绘制的线程
     */
    private Thread mThread;
    /**
     * 根据目标温度改变要显示的当前温度的线程
     */
    private Thread mChangeTemperatureThread;

    /**
     * 设置一个标志位，用于线程的开启还是关闭的标志
     *
     * @param context
     */
    private Boolean isRunning;
    private DecimalFormat fomat;//格式化float

    public BloodPressureSurfaceView(Context context) {
        this(context, null);
    }

    public BloodPressureSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    protected void onMeasure(int with, int height) {
        super.onMeasure(with, height);
//        mWidth = getMeasuredWidth() / 2;
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //这里先把中心设置在屏幕的中心
        mCenterWidth = mWidth / 2;
        mCenterHeight = mHeight / 2;
        //设置水银的宽度,暂时设置为总宽度的十五分之一
        mMercuryWidth = mWidth / 15;
        mMinLineLength = mMercuryWidth;
        mMidLineLength = mMinLineLength * 8 / 5;
        mMaxLineLength = mMidLineLength * 3 / 2;
        //temperatureAllLong表示温度刻度总长度
        mValueTop = mHeight / 20;
        mTotalValueLength = mHeight - mValueTop * 4;
        //设置刻度间隔,包含了刻度线的长度
        mUnitLength = mTotalValueLength / mValueRange / 10;//表示一个温度十个刻度
//        mValueTop = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化画笔
        mLinePaint = new Paint();
        //去锯齿
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth);
        //初始化画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setShader(null);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setColor(valueColor);

        //初始化温度计的范围
        mRange = new RectF(0, 0, mWidth, mHeight);
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        //不断进行绘制
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            //这里控制一下，一秒绘制二十次。也就是五十毫秒绘制一次
            long sleepMs = mspt - end + start;
            if (sleepMs > 0) {
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            //这里要判断是不是为空，之因为在用户点击了home以后，可能已经执行到这里
            if (mCanvas != null) {
                drawBg();
                drawValue();
            }
        } catch (Throwable ignore) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawValue() {
        //这里控制水银的上升速度
        //这里要除以20，是因为血压计的刻度值，到温度计的刻度值，是二十倍的换算，主要是偷懒
        float distance = Math.abs(mTargetValue - mCurrentValue) / 60;
        /**
         * //这里定义一个boolean来控制是使用加法还是减法，其中true表示当前温度小于
         * 目标温度，要使用加法，false表示当前温度大于目标温度，要使用减法。
         */
        boolean addOrSub = mCurrentValue < mTargetValue;
        if (distance == 0 || distance <= 0.005) {
            mSpeed = 0;
            mCurrentValue = mTargetValue;
        } else {
            if (distance > 2) {
                mSpeed = (float) 0.03;
            } else {
                if (distance > 1) {
                    mSpeed = (float) 0.025;
                } else {
                    if (distance > 0.5) {
                        mSpeed = (float) 0.015;
                    } else {
                        if (distance > 0.3) {
                            mSpeed = (float) 0.012;
                        } else {
                            if (distance > 0.2) {
                                mSpeed = (float) 0.009;
                            } else {
                                mSpeed = (float) 0.008;
                            }
                        }
                    }
                }
            }
        }
        if (addOrSub) {
            mCurrentValue += mSpeed * 100;
        } else {
            mCurrentValue -= mSpeed * 100;
        }

//        这里主要是对温度的显示，画矩形的过程中，唯一改变的就是Top这一个值了
        //这里(mCurrentValue-mBeginValue)/20表示的是刻度值的换算，从温度计过度到血压计的刻度值
        float nextValue = Math.abs(mCurrentValue - mTargetValue) > 0.8 ? mCurrentValue : mTargetValue;
        mCanvas.drawRect(mCenterWidth - mMercuryWidth / 2,
                (mUnitLength) * 10 * (mValueRange) + mValueTop * 2 -
                        (nextValue - mBeginValue) / 60 * 10 * mUnitLength,
                mCenterWidth + mMercuryWidth / 2,
                (mUnitLength) * 10 * (mValueRange) + mValueTop * 2,
                mValuePaint);
    }

    private void drawBg() {
        mCanvas.drawColor(mBgColor);
        //画右边的刻度
        //定义每一个长刻度的高度
        float unitHeight = (mUnitLength) * 10;
        for (int i = 0; i < mValueRange; i++) {
            mCanvas.drawLine(mCenterWidth + mMercuryWidth / 2,
                    unitHeight * i + mValueTop * 2,//这里加上两倍的上距离
                    mCenterWidth + mMercuryWidth / 2 + mMidLineLength,
                    unitHeight * i + mValueTop * 2, mLinePaint);
            for (int j = 1; j <= 9; j++) {
                if (j == 5) {
                    mCanvas.drawLine(mCenterWidth + mMercuryWidth / 2,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2,
                            mCenterWidth + mMercuryWidth / 2 + mMaxLineLength,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2, mLinePaint);
                    mCanvas.drawText(mEndValue - 30 - i * 60 + "", mCenterWidth + mMercuryWidth / 2 + mMaxLineLength + mMinLineLength / 3,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2 + mTextPaint.getTextSize() / 2, mTextPaint);
                } else {
                    mCanvas.drawLine(mCenterWidth + mMercuryWidth / 2,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2,
                            mCenterWidth + mMercuryWidth / 2 + mMinLineLength,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2, mLinePaint);
                }

            }

        }
        //画左边的刻度
        for (int i = 0; i < mValueRange; i++) {
            mCanvas.drawLine(mCenterWidth - mMercuryWidth / 2,
                    unitHeight * i + mValueTop * 2,
                    mCenterWidth - mMercuryWidth / 2 - mMaxLineLength,
                    unitHeight * i + mValueTop * 2, mLinePaint);
            if (mEndValue - i * 60 > 99)
                mCanvas.drawText(mEndValue - i * 60 + "", mCenterWidth - (mMercuryWidth / 2 + mMaxLineLength + mMinLineLength / 3) - mTextPaint.getTextSize() * 3 / 2,
                        unitHeight * i + mTextPaint.getTextSize() / 2 + mValueTop * 2, mTextPaint);
            else if (mEndValue - i * 60 > 9)
                mCanvas.drawText(mEndValue - i * 60 + "", mCenterWidth - (mMercuryWidth / 2 + mMaxLineLength + mMinLineLength / 3) - mTextPaint.getTextSize(),
                        unitHeight * i + mTextPaint.getTextSize() / 2 + mValueTop * 2, mTextPaint);
            else if (mEndValue - i * 60 >= 0)
                mCanvas.drawText(mEndValue - i * 60 + "", mCenterWidth - (mMercuryWidth / 2 + mMaxLineLength + mMinLineLength / 3) - mTextPaint.getTextSize() / 100,
                        unitHeight * i + mTextPaint.getTextSize() / 2 + mValueTop * 2, mTextPaint);

            for (int j = 1; j <= 9; j++) {
                if (j == 5) {
                    mCanvas.drawLine(mCenterWidth - mMercuryWidth / 2,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2,
                            mCenterWidth - mMercuryWidth / 2 - mMidLineLength,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2, mLinePaint);
                } else {
                    mCanvas.drawLine(mCenterWidth - mMercuryWidth / 2,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2,
                            mCenterWidth - mMercuryWidth / 2 - mMinLineLength,
                            unitHeight * i + j * (mUnitLength) + mValueTop * 2, mLinePaint);
                }

            }
            //画最后一个刻度
            if (i == mValueRange - 1) {
                mCanvas.drawLine(mCenterWidth - mMercuryWidth / 2,
                        unitHeight * (i + 1) + mValueTop * 2,
                        mCenterWidth - mMercuryWidth / 2 - mMaxLineLength,
                        unitHeight * (i + 1) + mValueTop * 2, mLinePaint);
                mCanvas.drawText(mEndValue - (i + 1) * 60 + "", mCenterWidth - (mMercuryWidth / 2 + mMaxLineLength + mMinLineLength / 3) - mTextPaint.getTextSize(),
                        unitHeight * (i + 1) + mTextPaint.getTextSize() / 2 + mValueTop * 2, mTextPaint);
            }
        }
        //画园
        mCanvas.drawCircle(mCenterWidth,
                unitHeight * (mValueRange) + mValueTop * 2 + mMercuryWidth,
                mMercuryWidth * 3 / 2, mValuePaint);
    }

    private float mTrueValue = 0;

    public void setTargetValue(float value) {
        mTrueValue = value;
        if (value < 0) {
            value = 0;
        }
        if (value > mEndValue) {
            value = mEndValue;
        }
        this.mTargetValue = value;
    }
}