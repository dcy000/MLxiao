package com.example.han.referralproject.settting.wrap;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/3/27.
 */

public class ItemView extends LinearLayout implements View.OnClickListener {
    IcClickListener listener;

    public void setListener(IcClickListener listener) {
        this.listener = listener;
    }

    public void setTextColor(int white) {
        text.setTextColor(Color.WHITE);
    }

    public void setTextSize(int i) {
        text.setTextSize(14);
    }

    public String getText() {
        return text.getText().toString();
    }

    public void showICon(boolean b) {
        iv.setVisibility(b ? VISIBLE : GONE);
    }


    public interface IcClickListener {
        void onIcClick(View v);
    }

    private ImageView iv;
    private TextView text;

    public ItemView(Context context) {
        super(context);
        initView(context);
    }


    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.key_item, null);
        iv = (ImageView) view.findViewById(R.id.iv);
        iv.setOnClickListener(this);
        text = (TextView) view.findViewById(R.id.text);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onIcClick(this);
        }
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
