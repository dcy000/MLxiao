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

        this.clearAll = builder.clearAll;

        this.onTextChageListener = builder.onTextChageListener;

        this.length = builder.length;
    }

    /**
     * 设置numberKeyPadlayout中的text值
     */
    public void setLayoutText(String text) {
        this.layout.setText(text);
    }

    /**
     * 设置inumberKeyPadlayout中的length值
     */
    public void setLayoutinputLength(int length) {
        this.layout.setLength(length);
    }

    public void showX(boolean showX) {
        this.layout.showX(showX);
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
            this.layout.setLength(length);
            return this;
        }

        public Builder showX(boolean showX) {
            this.showX = showX;

            this.layout.showX(showX);
            return this;
        }


        public Builder clearAll(boolean clearAll) {
            this.clearAll = clearAll;
            this.layout.clearAll(clearAll);
            return this;
        }

        public Builder textChageListener(NumeriKeypadLayout.OnTextChageListener onTextChageListener) {
            this.onTextChageListener = onTextChageListener;
            this.layout.setOnTextChageListener(onTextChageListener);
            return this;
        }

        public NumeriKeypadLayoutHelper build() {
            return new NumeriKeypadLayoutHelper(this);
        }

        public Builder newBuilder(Builder builder) {
            this.layout = builder.layout;

            this.showX = builder.showX;

            this.clearAll = builder.clearAll;

            this.onTextChageListener = builder.onTextChageListener;

            this.length = builder.length;
            return this;
        }
    }

    public String text() {
        return layout.getText();
    }


}