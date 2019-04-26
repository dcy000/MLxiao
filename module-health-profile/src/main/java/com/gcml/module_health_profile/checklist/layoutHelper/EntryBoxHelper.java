package com.gcml.module_health_profile.checklist.layoutHelper;

import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;

/**
 * Created by lenovo on 2019/4/23.
 */

public class EntryBoxHelper {
    private EntryBoxLinearLayout layout;
    private String name;
    private String value;
    private String unit;
    /**
     * 数据类型
     */
    private String dateType;
    /**
     * 题
     */
    private Boolean title;

    private EntryBoxLinearLayout.OnInputClickListener onInputClickListener;


    private EntryBoxHelper(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.unit = builder.unit;
        this.layout = builder.layout;
        this.dateType = builder.dateType;
        this.title = builder.title;
        this.onInputClickListener = builder.onInputClickListener;

        layout.name(builder.name);
        layout.unit(builder.unit);
        layout.setOnInputClickListener(builder.onInputClickListener);
        layout.dataype(builder.dateType);//在 setOnInputClickListener 后
        layout.requestionType(builder.title);
    }

    public String name() {
        return name;
    }

    public String value() {
        return layout.value();
    }

    public String unit() {
        return unit;
    }

    public EntryBoxLinearLayout layout() {
        return layout;
    }


    public static class Builder {
        private EntryBoxLinearLayout layout;
        private String name;
        private String value;
        private String unit;
        private String dateType;
        private Boolean title;
        private EntryBoxLinearLayout.OnInputClickListener onInputClickListener;


        public Builder(EntryBoxLinearLayout layout) {
            this.layout = layout;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder dateType(String dateType) {
            this.dateType = dateType;
            return this;
        }

        public Builder inputListener(EntryBoxLinearLayout.OnInputClickListener onInputClickListener) {
            this.onInputClickListener = onInputClickListener;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder layout(EntryBoxLinearLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder title(Boolean title) {
            this.title = title;
            return this;
        }


        public EntryBoxHelper build() {
            return new EntryBoxHelper(this);
        }

    }
}
