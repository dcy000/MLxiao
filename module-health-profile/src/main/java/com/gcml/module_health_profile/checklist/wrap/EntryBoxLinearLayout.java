package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.checklist.config.EntryBoxHelper;

/**
 * Created by lenovo on 2019/4/23.
 */

public class EntryBoxLinearLayout extends LinearLayout {
    private EntryBoxHelper boxConfig;
    private TextView name;
    private TextView unit;
    private EditText value;

 /*   public void setBoxConfig(EntryBoxHelper boxConfig) {
        this.boxConfig = boxConfig;
        if (boxConfig != null) {
            name.setText(boxConfig.name());
            unit.setText(boxConfig.unit());
        }
    }*/


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

}
