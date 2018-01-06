package com.witspring.view.pageflow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * @author Created by Goven on 16/12/25 下午5:25
 * @email gxl3999@gmail.com
 */
public class PageFlowIndicator extends View {

    private float mRadius = 8.0f;
    private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
    private int mCurrentPage;
    private PageScrollView pageView;

    private static final int PAGE_COLOR = Color.parseColor("#dadada");
    private static final int FILL_COLOR = Color.parseColor("#ff7608");

    public PageFlowIndicator(Context context) {
        this(context, null);
    }

    public PageFlowIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageFlowIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
        mPaintPageFill.setStyle(Paint.Style.FILL);
        mPaintPageFill.setColor(PAGE_COLOR);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(FILL_COLOR);
    }

    public void setSelectedColor(int color) {
        mPaintFill.setColor(color);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pageView == null) {
            return;
        }
        final int count = pageView.getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentPage(count - 1);
            return;
        }
        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;

        longSize = getWidth();
        longPaddingBefore = getPaddingLeft();
        longPaddingAfter = getPaddingRight();
        shortPaddingBefore = getPaddingTop();

        final float threeRadius = mRadius * 4;
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset = longPaddingBefore + mRadius;
        longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);
        float dX;
        float dY;
        float pageFillRadius = mRadius;
        //Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            dX = drawLong;
            dY = shortOffset;
            // Only paint fill if not completely transparent
            if (mPaintPageFill.getAlpha() > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
            }
        }
        //Draw the filled circle according to the current scroll
        float cx =  mCurrentPage * threeRadius;
        dX = longOffset + cx;
        dY = shortOffset;
        canvas.drawCircle(dX, dY, mRadius, mPaintFill);
    }

    void setPageView(PageScrollView view) {
        if (pageView == view) {
            return;
        }
        pageView = view;
        invalidate();
    }

    public PageScrollView getPageView() {
        return pageView;
    }

    public void setCurrentPage(int pageIndex) {
        mCurrentPage = pageIndex;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (pageView == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = pageView.getCount();
            result = (int) (getPaddingLeft() + getPaddingRight()
                    + (count * 2 * mRadius) + (count - 1) * mRadius + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}