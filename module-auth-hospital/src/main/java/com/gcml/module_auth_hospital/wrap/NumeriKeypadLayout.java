package com.gcml.module_auth_hospital.wrap;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gcml.module_auth_hospital.R;

public class NumeriKeypadLayout extends FrameLayout implements View.OnClickListener {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0, tvx, tvxx;

    public NumeriKeypadLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public NumeriKeypadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumeriKeypadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //  mValuePaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/DINEngschrift-Alternate.otf"));
    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_number_keypad, null);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);
        tv4 = view.findViewById(R.id.tv_4);
        tv5 = view.findViewById(R.id.tv_5);
        tv6 = view.findViewById(R.id.tv_6);
        tv7 = view.findViewById(R.id.tv_7);
        tv8 = view.findViewById(R.id.tv_8);
        tv9 = view.findViewById(R.id.tv_9);
        tv0 = view.findViewById(R.id.tv_0);
        tvx = view.findViewById(R.id.tv_x);
        tvxx = view.findViewById(R.id.tv_xx);
        tv1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv3.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv4.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv5.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv6.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv7.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv8.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv9.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tv0.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tvx.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));
        tvxx.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/DIN-Bold.otf"));

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        tv0.setOnClickListener(this);
        tvx.setOnClickListener(this);
        tvxx.setOnClickListener(this);
        addView(view);
    }

    private String text = "";
    private boolean clearAll;

    @Override

    public void onClick(View v) {
        if (v == tv1 || v == tv2 || v == tv3
                || v == tv4 || v == tv5 || v == tv6
                || v == tv7 || v == tv8 || v == tv9
                || v == tv0 || v == tvx
        ) {
            if (onTextChageListener != null) {
                String clickNmber = ((TextView) v).getText().toString();
                if (this.text.length() < 18) {
                    this.text = this.text + clickNmber;
                }
                onTextChageListener.onTextChange(text);
            }

        } else if (v == tvxx) {
            if (onTextChageListener != null) {
                if (clearAll) {
                    text = "";
                    onTextChageListener.onTextChange("");
                } else {
                    if (TextUtils.isEmpty(text)) {
                        text = "";
                        onTextChageListener.onTextChange("");
                    } else {
                        String substring = text.substring(0, text.length() - 1);
                        text = substring;
                        onTextChageListener.onTextChange(substring);
                    }
                }

            }
        }
    }

    public void showX(boolean showX) {
        if (showX) {
            tvx.setText("X");
        } else {
            tvx.setText("");
        }
    }

    public void clearAll(boolean clearAll) {
        this.clearAll = clearAll;
    }

    public String getText() {
        return text;
    }

    public interface OnTextChageListener {
        void onTextChange(String text);
    }

    private OnTextChageListener onTextChageListener;

    public void setOnTextChageListener(OnTextChageListener onTextChageListener) {
        this.onTextChageListener = onTextChageListener;
    }
}
