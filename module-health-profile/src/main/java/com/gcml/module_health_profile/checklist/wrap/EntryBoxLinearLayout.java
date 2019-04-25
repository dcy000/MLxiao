package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lenovo on 2019/4/23.
 */

public class EntryBoxLinearLayout extends LinearLayout {
    private TextView name;
    private TextView unit;
    private EditText value;
    private RelativeLayout rlContainer;

    /*1文字 2时间 3数字 4地址 ,*/

    public EntryBoxLinearLayout(Context context) {
        super(context);
        init();
    }

    public EntryBoxLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EntryBoxLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.entry_box_linearlayout, null);
        name = view.findViewById(R.id.tv_question_name);
        unit = view.findViewById(R.id.tv_question_unit);
        value = view.findViewById(R.id.et_value);
        rlContainer = view.findViewById(R.id.rl_entry_box);
        addView(view);
    }

    public void name(String name) {
        this.name.setText(name);
    }

    public void unit(String unit) {
        this.unit.setText(unit);
    }

    public String value() {
        return value.getText().toString().trim();
    }

    /*1文字 2时间 3数字 4地址 ,*/
    public void dataype(String type) {
        if (TextUtils.equals("1", type)) {
            value.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (TextUtils.equals("2", type)) {
            value.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (TextUtils.equals("3", type)) {
            value.setInputType(InputType.TYPE_CLASS_DATETIME);
        } else if (TextUtils.equals("4", type)) {
            value.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public void requestionType(Boolean title) {
        if (title) {
            rlContainer.setVisibility(GONE);
            rlContainer.setBackgroundResource(0);
        } else {
            rlContainer.setVisibility(VISIBLE);

            //控制背景 边框
            rlContainer.setBackgroundResource(R.drawable.common_bg_input_gray);
        }
    }


}
