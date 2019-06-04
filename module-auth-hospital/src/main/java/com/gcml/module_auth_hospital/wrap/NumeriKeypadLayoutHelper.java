package com.gcml.module_auth_hospital.wrap;

public class NumeriKeypadLayoutHelper {
    private NumeriKeypadLayout.OnTextChageListener onTextChageListener;
    private NumeriKeypadLayout layout;
    private boolean showX;
    private boolean clearAll;
    private int length;//输入的长度

    private NumeriKeypadLayoutHelper() {
    }

    public NumeriKeypadLayoutHelper(Builder builder) {
        this.layout = builder.layout;

        this.showX = builder.showX;
        this.layout.showX(builder.showX);

        this.clearAll = builder.clearAll;
        this.layout.clearAll(builder.clearAll);

        this.onTextChageListener = builder.onTextChageListener;
        this.layout.setOnTextChageListener(builder.onTextChageListener);

        this.length = builder.length;
        this.layout.setLength(builder.length);
    }

    public static class Builder {
        private NumeriKeypadLayout.OnTextChageListener onTextChageListener;
        private NumeriKeypadLayout layout;
        private boolean showX;
        private boolean clearAll;
        private int length;

        public Builder layout(NumeriKeypadLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder showX(boolean showX) {
            this.showX = showX;
            return this;
        }


        public Builder clearAll(boolean clearAll) {
            this.clearAll = clearAll;
            return this;
        }

        public Builder textChageListener(NumeriKeypadLayout.OnTextChageListener onTextChageListener) {
            this.onTextChageListener = onTextChageListener;
            return this;
        }

        public NumeriKeypadLayoutHelper build() {
            return new NumeriKeypadLayoutHelper(this);
        }
    }

    public String text() {
        return layout.getText();
    }

}
