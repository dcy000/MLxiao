package com.gcml.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gcml.common.business.R;

public final class ShadowLayout extends FrameLayout {
    private int shadowColor;
    private float shadowRadius;
    private float cornerRadius;
    private float dx;
    private float dy;
    private int theBackgroundColor;
    private boolean invalidateShadowOnSizeChanged;
    private boolean forceInvalidateShadow;

    public final int getShadowColor() {
        return this.shadowColor;
    }

    public final void setShadowColor(int var1) {
        this.shadowColor = var1;
    }

    public final float getShadowRadius() {
        return this.shadowRadius;
    }

    public final void setShadowRadius(float var1) {
        this.shadowRadius = var1;
    }

    public final float getCornerRadius() {
        return this.cornerRadius;
    }

    public final void setCornerRadius(float var1) {
        this.cornerRadius = var1;
    }

    public final float getDx() {
        return this.dx;
    }

    public final void setDx(float dx) {
        this.dx = dx;
    }

    public final float getDy() {
        return this.dy;
    }

    public final void setDy(float dy) {
        this.dy = dy;
    }

    public final int getBackgroundColor() {
        return this.theBackgroundColor;
    }

    public final void setBackgroundColor(int theBackgroundColor) {
        this.theBackgroundColor = theBackgroundColor;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (this.getBackground() == null || this.invalidateShadowOnSizeChanged || this.forceInvalidateShadow)) {
            this.forceInvalidateShadow = false;
            this.setBackgroundCompat(w, h);
        }

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.forceInvalidateShadow) {
            this.forceInvalidateShadow = false;
            this.setBackgroundCompat(right - left, bottom - top);
        }

    }

    public final void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        this.invalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    public final void invalidateShadow() {
        this.forceInvalidateShadow = true;
        this.requestLayout();
        this.invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        this.initAttributes(context, attrs);
        this.refreshPadding();
    }

    public final void refreshPadding() {
        int xPadding = (int) (this.shadowRadius + Math.abs(this.dx));
        int yPadding = (int) (this.shadowRadius + Math.abs(this.dy));
        this.setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void setBackgroundCompat(int w, int h) {
        Drawable background = getBackground();
        if (background instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) background;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmapDrawable.setCallback(null);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        Bitmap bitmap = null;
        try {
            bitmap = this.createShadowBitmap(w, h, this.cornerRadius, this.shadowRadius, this.dx, this.dy, this.shadowColor, 0);
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            this.setBackground((Drawable) drawable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = this.getTypedArray(context, attrs, R.styleable.ShadowLayout);
        if (attr != null) {
            try {
                this.cornerRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_cornerRadius, 0.0F);
                this.shadowRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_shadowRadius, 0.0F);
                this.dx = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_dx, 0.0F);
                this.dy = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_dy, 0.0F);
                this.shadowColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_shadowColor, Color.parseColor("#22000000"));
                this.theBackgroundColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_backgroundColor, Integer.MIN_VALUE);
            } finally {
                attr.recycle();
            }
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius, float dx, float dy, int shadowColor, int fillColor) {
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                (float) shadowWidth - shadowRadius,
                (float) shadowHeight - shadowRadius);
        if (dy > 0f) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < (float) 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0f) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < (float) 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(fillColor);
        paint.setStyle(Style.FILL);
        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, paint);
        if (this.theBackgroundColor != Integer.MIN_VALUE) {
            paint.clearShadowLayer();
            paint.setColor(this.theBackgroundColor);
            RectF backgroundRect = new RectF(
                    (float) this.getPaddingLeft(),
                    (float) this.getPaddingTop(),
                    (float) (this.getWidth() - this.getPaddingRight()),
                    (float) (this.getHeight() - this.getPaddingBottom())
            );
            canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint);
        }

        return output;
    }

    public ShadowLayout(Context context) {
        super(context);
        this.invalidateShadowOnSizeChanged = true;
        this.initView(context, (AttributeSet) null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.invalidateShadowOnSizeChanged = true;
        this.initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.invalidateShadowOnSizeChanged = true;
        this.initView(context, attrs);
    }

}

