package com.gcml.lib_ecg.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ECGSingleGuideView extends View {
    private int largerGridColor;
    private int mediumGridColor;
    private int smallGridColor;
    private int dataColor;//心电折线图颜色
    private Paint largerGridPaint;
    private Paint mediumGridPaint;
    private Paint smallGridPaint;
    private Paint dataPaint;
    private float margin;
    private int xGridSize;//x轴大方格的数量
    private float smallGridLength;//小格的宽度
    private float gridLargerStrokeWidth;//最外边界线条宽度
    private float gridMediumStrokeWidth;//中间较粗线条宽度
    private float gridSmallStrokeWidth;//中间最细线条宽度
    private int xGridMediumStrokeCount;//x轴小格的数量
    private float dataStrokeWidth;//心电折线图的宽度
    private ArrayList<byte[]> data;
    private float defaultBaseLinewValue;
    private float baseLineValue;
    private int chartSpeed;
    private int gain;//实际测量数字和坐标数字的转换比率（越大图形波浮动越大 图形越不平滑）
    private int sampling;//采样率（这个需要在设备说明书中查看）
    private int adUnit;
    private Path dataPath;
    private float xDistance;
    private int xMaxPointCount;
    private float yBaseLineValue;
    private boolean reverse;
    private String brand;

    public ECGSingleGuideView(Context context) {
        this(context, null);
    }

    public ECGSingleGuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECGSingleGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.largerGridColor = Color.parseColor("#66767676");
        this.mediumGridColor = Color.parseColor("#66767676");
        this.smallGridColor = Color.parseColor("#66414141");
        this.dataColor = Color.parseColor("#00FF00");
        this.margin = 40.0F;
        this.xGridSize = 100;
        this.gridLargerStrokeWidth = 3.0F;
        this.gridMediumStrokeWidth = 2.0F;
        this.gridSmallStrokeWidth = 1.0F;
        this.dataStrokeWidth = 3.0F;
        this.data = new ArrayList();
        this.defaultBaseLinewValue = 3000.0F;
        this.baseLineValue = this.defaultBaseLinewValue;
        this.chartSpeed = 25;
        this.gain = 10;
        this.sampling = 200;
        this.adUnit = 840;
        this.brand = "BoSheng";//默认是博声的心电
        this.initialization();
    }

    private void onPreDraw() {
        this.xGridMediumStrokeCount = this.xGridSize / 5 - 1;
        this.smallGridLength = ((float) this.getWidth() - this.margin * 2.0F) / (float) this.xGridSize;
        this.xDistance = 1.0F / (float) this.sampling * (float) this.chartSpeed * this.smallGridLength;
        this.xMaxPointCount = (int) (((float) this.getWidth() - this.margin * 2.0F) / this.xDistance) / 10 * 10;
        this.yBaseLineValue = (float) (this.getHeight() / 2) - this.getYDistance(this.baseLineValue, this.defaultBaseLinewValue);
    }

    private void initialization() {
        this.largerGridPaint = new Paint(1);
        this.largerGridPaint.setStyle(Paint.Style.STROKE);
        this.largerGridPaint.setColor(this.largerGridColor);
        this.largerGridPaint.setStrokeWidth(this.gridLargerStrokeWidth);
        this.mediumGridPaint = new Paint(1);
        this.mediumGridPaint.setStyle(Paint.Style.FILL);
        this.mediumGridPaint.setColor(this.mediumGridColor);
        this.mediumGridPaint.setStrokeWidth(this.gridMediumStrokeWidth);
        this.smallGridPaint = new Paint(1);
        this.smallGridPaint.setStyle(Paint.Style.FILL);
        this.smallGridPaint.setColor(this.smallGridColor);
        this.smallGridPaint.setStrokeWidth(this.gridSmallStrokeWidth);
        this.dataPaint = new Paint(1);
        this.dataPaint.setStyle(Paint.Style.STROKE);
        this.dataPaint.setColor(this.dataColor);
        this.dataPaint.setStrokeWidth(this.dataStrokeWidth);
        this.dataPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.onPreDraw();
        this.drawGrid(canvas);
        this.drawData(canvas);
    }

    private void drawGrid(Canvas canvas) {
        this.drawLargerGrid(canvas);
        this.drawMediumStroke(canvas);
        this.drawSmallStroke(canvas);
    }

    private void drawData(Canvas canvas) {
        this.dataPath.rewind();
        int k = 0;
        int m = 0;

        if ("BoSheng".equals(brand)) {
            if (this.data.size() * 10 >= this.xMaxPointCount) {
                m = this.data.size() - this.data.size() * 10 % this.xMaxPointCount / 10;
            }
            for (int i = m; i < this.data.size(); ++i) {
                for (int j = 0; j < this.data.get(i).length; j += 2) {
                    int point = this.getBoShengValue(this.data.get(i)[j], this.data.get(i)[j + 1]);
                    if (k == 0) {
                        this.dataPath.moveTo(this.getX(k), this.getY(point));
                    } else {
                        this.dataPath.lineTo(this.getX(k), this.getY(point));
//                    Logg.e(ECGSingleGuideView.class,"点:("+getX(k)+","+getY(point)+")");
                    }

                    ++k;
                }
            }
        } else if ("ChaoSi".equals(brand)) {
            if (this.data.size() * 4 >= this.xMaxPointCount) {
                m = this.data.size() - this.data.size() * 4 % this.xMaxPointCount / 4;
            }
            for (int i = m; i < this.data.size(); ++i) {
                byte[] byte_single = data.get(i);
                //第一组4个点
                for (int j = 6; j <= 9; j++) {
                    int point = ((byte_single[j] & 0xff) << 2) + (((byte_single[10] & 0xff) >> (-2 * j + 18)) & 0x3);
                    if (k == 0) {
                        this.dataPath.moveTo(this.getX(k), this.getY(point));
                    } else {
                        this.dataPath.lineTo(this.getX(k), this.getY(point));
                    }
                    ++k;
                }
//                //第二组4个点
//                for (int j=23;j<=26;j++){
//                    int point = ((byte_single[j] & 0xff) << 2) + (((byte_single[27] & 0xff) >> (-2 * j + 52)) & 0x3);
//                    if (k == 0) {
//                        this.dataPath.moveTo(this.getX(k), this.getY(point));
//                    } else {
//                        this.dataPath.lineTo(this.getX(k), this.getY(point));
//                    }
//                    ++k;
//                }
//                //第三组4个点
//                for (int j=40;j<=43;j++){
//                    int point = ((byte_single[j] & 0xff) << 2) + (((byte_single[44] & 0xff) >> (-2 * j + 86)) & 0x3);
//                    if (k == 0) {
//                        this.dataPath.moveTo(this.getX(k), this.getY(point));
//                    } else {
//                        this.dataPath.lineTo(this.getX(k), this.getY(point));
//                    }
//                    ++k;
//                }
//                //第四组4个点
//                for (int j=57;j<=60;j++){
//                    int point = ((byte_single[j] & 0xff) << 2) + (((byte_single[61] & 0xff) >> (-2 * j + 120)) & 0x3);
//                    if (k == 0) {
//                        this.dataPath.moveTo(this.getX(k), this.getY(point));
//                    } else {
//                        this.dataPath.lineTo(this.getX(k), this.getY(point));
//                    }
//                    ++k;
//                }

            }
        }
        canvas.drawPath(this.dataPath, this.dataPaint);
    }

    private float getX(int k) {
        return this.margin + (float) k * this.xDistance;
    }

    private float getY(int value) {
        float yDistance = (float) (this.reverse ? 1 : -1) * this.getYDistance((float) value, this.baseLineValue);
        return this.yBaseLineValue - yDistance;
    }

    private float getYDistance(float value, float refer) {
        return (value - refer) / (float) this.adUnit * (float) this.gain * this.smallGridLength;
    }

    private int getBoShengValue(byte lowByte, byte highByte) {
        int v1 = (highByte & 255) << 8;
        int v2 = lowByte & 255;
        return v1 + v2;
    }

    private void drawSmallStroke(Canvas canvas) {
        float yLineStartY = this.margin;
        float yLineEndY = (float) this.getHeight() - yLineStartY;
        float xLineStartX = yLineStartY;
        float xLineEndX = (float) this.getWidth() - yLineStartY;

        for (int i = 0; i < this.xGridSize; ++i) {
            if (i % 5 != 0) {
                canvas.drawLine(this.smallGridLength * (float) i + this.margin, yLineStartY, this.smallGridLength * (float) i + this.margin, yLineEndY, this.smallGridPaint);
            }
        }

        float xLineStartY = (float) this.getHeight() - this.margin;
        int i = 0;

        while (true) {
            do {
                if (xLineStartY <= this.smallGridLength + this.margin) {
                    return;
                }

                xLineStartY -= this.smallGridLength;
                ++i;
            } while (i % 5 == 0 && xLineStartY >= this.margin + 3.0F * this.smallGridLength);

            canvas.drawLine(xLineStartX, xLineStartY, xLineEndX, xLineStartY, this.smallGridPaint);
        }
    }

    private void drawMediumStroke(Canvas canvas) {
        float yLineStartY = this.margin;
        float yLineEndY = (float) this.getHeight() - yLineStartY;
        float xLineStartX = yLineStartY;
        float xLineEndX = (float) this.getWidth() - yLineStartY;
        float distance = 5.0F * this.smallGridLength;

        for (int i = 0; i < this.xGridMediumStrokeCount; ++i) {
            canvas.drawLine(distance * (float) (i + 1) + this.margin, yLineStartY, distance * (float) (i + 1) + this.margin, yLineEndY, this.mediumGridPaint);
        }

        for (float xLineStartY = (float) this.getHeight() - this.margin - distance; xLineStartY >= this.margin + this.smallGridLength * 3.0F; xLineStartY -= distance) {
            canvas.drawLine(xLineStartX, xLineStartY, xLineEndX, xLineStartY, this.mediumGridPaint);
        }

    }

    private void drawLargerGrid(Canvas canvas) {
        RectF gridStorke = new RectF(this.margin, this.margin, (float) this.getRight() - this.margin, (float) this.getBottom() - this.margin);
        canvas.drawRoundRect(gridStorke, 10.0F, 10.0F, this.largerGridPaint);
    }

    public int getLargerGridColor() {
        return this.largerGridColor;
    }

    public ECGSingleGuideView setLargerGridColor(int largerGridColor) {
        this.largerGridColor = largerGridColor;
        this.largerGridPaint.setColor(largerGridColor);
        this.postInvalidate();
        return this;
    }

    public int getMediumGridColor() {
        return this.mediumGridColor;
    }

    public ECGSingleGuideView setMediumGridColor(int mediumGridColor) {
        this.mediumGridColor = mediumGridColor;
        this.mediumGridPaint.setColor(mediumGridColor);
        this.postInvalidate();
        return this;
    }

    public int getSmallGridColor() {
        return this.smallGridColor;
    }

    public ECGSingleGuideView setSmallGridColor(int smallGridColor) {
        this.smallGridColor = smallGridColor;
        this.smallGridPaint.setColor(smallGridColor);
        this.postInvalidate();
        return this;
    }

    public void addData(byte[] data) {
        if ("ChaoSi".equals(brand)){
            if (data != null && data.length >=11) {
                if (this.data != null) {
                    this.data.add(data);
                }
                this.postInvalidate();
            }
        }else if ("BoSheng".equals(brand)){
            if (data != null && data.length >= 20) {
                if (this.data != null) {
                    this.data.add(data);
                }

                this.postInvalidate();
            }
        }
    }

    public ArrayList<byte[]> getData() {
        return this.data;
    }

    public byte[] getDataByteArray() {
        byte[] bytes = new byte[this.data.size() * 20];

        for (int i = 0; i < this.data.size(); ++i) {
            for (int j = 0; j < this.data.get(i).length; ++j) {
                bytes[i * 20 + j] = this.data.get(i)[j];
            }
        }

        return bytes;
    }

    public int getDataColor() {
        return this.dataColor;
    }

    public ECGSingleGuideView setDataColor(int dataColor) {
        this.dataColor = dataColor;
        this.dataPaint.setColor(dataColor);
        this.postInvalidate();
        return this;
    }

    public float getMargin() {
        return this.margin;
    }

    public ECGSingleGuideView setMargin(float margin) {
        this.margin = margin;
        this.postInvalidate();
        return this;
    }

    public float getGridLargerStrokeWidth() {
        return this.gridLargerStrokeWidth;
    }

    public ECGSingleGuideView setGridLargerStrokeWidth(float gridLargerStrokeWidth) {
        this.gridLargerStrokeWidth = gridLargerStrokeWidth;
        this.postInvalidate();
        return this;
    }

    public float getGridMediumStrokeWidth() {
        return this.gridMediumStrokeWidth;
    }

    public ECGSingleGuideView setGridMediumStrokeWidth(float gridMediumStrokeWidth) {
        this.gridMediumStrokeWidth = gridMediumStrokeWidth;
        this.postInvalidate();
        return this;
    }

    public float getGridSmallStrokeWidth() {
        return this.gridSmallStrokeWidth;
    }

    public ECGSingleGuideView setGridSmallStrokeWidth(float gridSmallStrokeWidth) {
        this.gridSmallStrokeWidth = gridSmallStrokeWidth;
        this.postInvalidate();
        return this;
    }

    public float getDataStrokeWidth() {
        return this.dataStrokeWidth;
    }

    public ECGSingleGuideView setDataStrokeWidth(float dataStrokeWidth) {
        this.dataStrokeWidth = dataStrokeWidth;
        this.postInvalidate();
        return this;
    }

    public float getBaseLineValue() {
        return this.baseLineValue;
    }

    public ECGSingleGuideView setBaseLineValue(float baseLineValue) {
        this.baseLineValue = baseLineValue;
        this.postInvalidate();
        return this;
    }

    public int getChartSpeed() {
        return this.chartSpeed;
    }

    public ECGSingleGuideView setChartSpeed(int chartSpeed) {
        this.chartSpeed = chartSpeed;
        this.postInvalidate();
        return this;
    }

    public int getGain() {
        return this.gain;
    }

    public ECGSingleGuideView setGain(int gain) {
        this.gain = gain;
        this.postInvalidate();
        return this;
    }

    public int getSampling() {
        return this.sampling;
    }

    public ECGSingleGuideView setSampling(int sampling) {
        this.sampling = sampling;
        this.postInvalidate();
        return this;
    }

    public int getAdUnit() {
        return this.adUnit;
    }

    public ECGSingleGuideView setAdUnit(int adUnit) {
        this.adUnit = adUnit;
        this.postInvalidate();
        return this;
    }

    public int getxGridSize() {
        return this.xGridSize;
    }

    public ECGSingleGuideView setxGridSize(int xGridSize) {
        this.xGridSize = xGridSize;
        this.postInvalidate();
        return this;
    }

    public float getDefaultBaseLinewValue() {
        return this.defaultBaseLinewValue;
    }

    public ECGSingleGuideView setDefaultBaseLinewValue(float defaultBaseLinewValue) {
        if (this.baseLineValue == this.defaultBaseLinewValue) {
            this.baseLineValue = defaultBaseLinewValue;
        }

        this.defaultBaseLinewValue = defaultBaseLinewValue;
        this.postInvalidate();
        return this;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        this.postInvalidate();
    }

    public boolean getReverse() {
        return this.reverse;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
