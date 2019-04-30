package com.gcml.module_factory_test.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_factory_test.R;

/**
 * Created by lenovo on 2018/3/27.
 */

public class ItemView extends LinearLayout implements View.OnClickListener {
    IcClickListener listener;

    public void setListener(IcClickListener listener) {
        this.listener = listener;
    }

    public void setTextColor(int white) {
        text.setTextColor(white);
    }

    public void setTextSize(int i) {
        text.setTextSize(i);
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
        View view = View.inflate(context, R.layout.factory_key_item, null);
        iv = view.findViewById(R.id.iv);
        iv.setOnClickListener(this);
        text = view.findViewById(R.id.text);
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
