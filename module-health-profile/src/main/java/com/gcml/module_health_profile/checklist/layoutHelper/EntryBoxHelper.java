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
    private String dateType;
    private Boolean title;
    private String questionId;//用于取答案 数据提交
    private int titleLevel;//对应ui上输入框 层级


    private EntryBoxLinearLayout.OnInputClickListener onInputClickListener;


    private EntryBoxHelper(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.unit = builder.unit;
        this.layout = builder.layout;
        this.dateType = builder.dateType;
        this.title = builder.title;
        this.questionId = builder.questionId;
        this.onInputClickListener = builder.onInputClickListener;
        this.titleLevel = builder.titleLevel;


        layout.name(builder.name);
        layout.unit(builder.unit);
        layout.setOnInputClickListener(builder.onInputClickListener);
        layout.dataype(builder.dateType);//在 setOnInputClickListener 后
        layout.requestionType(builder.title);
//        layout.setTag(builder.questionId);
        layout.setTitleLevel(builder.titleLevel);
    }

    private String name() {
        return name;
    }

    public String value() {
        return layout.value();
    }

    public String questionId() {
        return questionId;
    }

    private String unit() {
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
        private String questionId;
        private EntryBoxLinearLayout.OnInputClickListener onInputClickListener;
        private int titleLevel;


        public Builder(EntryBoxLinearLayout layout) {
            this.layout = layout;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder titleLevel(int titleLevel) {
            this.titleLevel = titleLevel;
            return this;
        }

        public Builder questionId(String questionId) {
            this.questionId = questionId;
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
