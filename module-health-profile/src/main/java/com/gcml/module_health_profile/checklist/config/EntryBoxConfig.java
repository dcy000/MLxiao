package com.gcml.module_health_profile.checklist.config;

/**
 * Created by lenovo on 2019/4/23.
 */

public class EntryBoxConfig {
    private String name;
    private String value;
    private String unit;

    public EntryBoxConfig(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.unit = builder.unit;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public String unit() {
        return unit;
    }

    public static class Builder {
        public String name;
        public String value;
        public String unit;

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

        public EntryBoxConfig build() {
            return new EntryBoxConfig(this);
        }

    }
}
