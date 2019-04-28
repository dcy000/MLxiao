package com.gcml.module_health_profile.checklist.layoutHelper;

import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.SingleChoiceLayout;

import java.util.List;

/**
 * Created by lenovo on 2019/4/23.
 */

public class SingleChoiceInputLayoutHelper<T> {
    private SingleChoiceLayout layout;
    private List<T> choices;
    private String questionId;

    private SingleChoiceInputLayoutHelper(Builder builder) {
        this.choices = builder.choices;
        this.layout = builder.layout;
        this.questionId = builder.questionId;

        EntryBoxHelper helper = new EntryBoxHelper
                .Builder(new EntryBoxLinearLayout(layout.getContext()))
                .title(false)
                .name("")
                .unit("").build();
        layout.addInput(helper.layout());

        layout.setData(choices);
    }

    private List<T> choices() {
        return choices;
    }

    public SingleChoiceLayout layout() {
        return layout;
    }

    public String questionId() {
        return questionId;
    }

    public String optionId() {
        return layout.optionId();
    }

    public static class Builder<T> {
        private SingleChoiceLayout layout;
        private List<T> choices;
        private String questionId;

        public Builder(SingleChoiceLayout layout) {
            this.layout = layout;
        }

        public Builder choices(List<T> choices) {
            this.choices = choices;
            return this;
        }

        public Builder layout(SingleChoiceLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder questionId(String questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder layout(String questionId) {
            this.questionId = questionId;
            return this;
        }

        public SingleChoiceInputLayoutHelper build() {
            return new SingleChoiceInputLayoutHelper(this);
        }

    }
}
