package com.example.han.referralproject.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/3/6.
 */

public class ExpandableTextView extends LinearLayout {
    public TextView mTextView;
    private TextView mOpenBtn;
    public boolean isOpen = false;
    private int foldLines = 3; //大于3行的时候折叠
    private int lineCounts;
    private ClickListner clickListner;

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onclick(boolean open);
    }

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void initView() {

        lineCounts = mTextView.getLineCount();
        if (lineCounts <= foldLines) {
            mOpenBtn.setVisibility(GONE);
        }
        if (isOpen && mTextView.getHeight() != lineCounts * mTextView.getLineHeight()) {
            mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
        } else if (!isOpen && mTextView.getHeight() != foldLines * mTextView.getLineHeight()) {
            mTextView.setHeight(mTextView.getLineHeight() * foldLines);
        }
        mOpenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListner == null) {
                    return;
                }
                if (isOpen) {
                    mTextView.setHeight(mTextView.getLineHeight() * foldLines);
                    isOpen=false;
                    clickListner.onclick(false);

                } else {
                    mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
                    clickListner.onclick(true);
                    isOpen=true;
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mTextView == null || mOpenBtn == null) {
            mTextView = (TextView) getChildAt(0);
            mOpenBtn = (TextView) getChildAt(1);
        }
    }
}
