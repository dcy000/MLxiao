package com.gcml.module_health_profile.checklist.config;

import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;

/**
 * Created by lenovo on 2019/4/23.
 */

public class EntryBoxHelper {
    private EntryBoxLinearLayout layout;
    private String name;
    private String value;
    private String unit;

    public EntryBoxHelper(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.unit = builder.unit;
        this.layout = builder.layout;

        layout.name(builder.name);
        layout.unit(builder.unit);
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

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder layout(EntryBoxLinearLayout layout) {
            this.layout = layout;
            return this;
        }

        public EntryBoxHelper build() {
            return new EntryBoxHelper(this);
        }

    }
}
