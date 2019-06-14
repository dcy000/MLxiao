package com.gcml.module_health_profile.checklist.layoutHelper;

import android.view.View;

import com.gcml.module_health_profile.checklist.wrap.OutLayout;

/**
 * Created by lenovo on 2019/4/23.
 */

public class OutLayoutHelper {
    private OutLayout layout;
    private String name;
    private View rightChildView;

    private boolean marginLeft;

    private OutLayoutHelper(Builder builder) {
        this.name = builder.name;
        this.layout = builder.layout;
        this.rightChildView = builder.rightChildView;
        this.marginLeft = builder.marginLeft;

        layout.textViewMarginLeft(builder.marginLeft);
        layout.name(builder.name);
        layout.addLayout(builder.rightChildView);
    }

    public String name() {
        return name;
    }

    public OutLayout layout() {
        return layout;
    }

    public static class Builder {
        private OutLayout layout;
        private String name;
        private View rightChildView;

        private boolean marginLeft;

        public Builder(OutLayout layout) {
            this.layout = layout;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder rightView(View rightChildView) {
            this.rightChildView = rightChildView;
            return this;
        }

        public Builder marginLeft(boolean marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }

        public OutLayoutHelper build() {
            return new OutLayoutHelper(this);
        }

    }
}
