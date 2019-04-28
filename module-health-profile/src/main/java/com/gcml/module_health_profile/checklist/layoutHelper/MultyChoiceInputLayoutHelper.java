package com.gcml.module_health_profile.checklist.layoutHelper;

import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;
import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.MultipleChoiceLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2019/4/23.
 */

public class MultyChoiceInputLayoutHelper<T> {
    private MultipleChoiceLayout layout;
    private List<T> choices;
    private String questionId;

    private MultyChoiceInputLayoutHelper(Builder builder) {
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
//        layout.setTag(questionId);
    }

    public List<T> choices() {
        return choices;
    }

    public String questionId() {
        return questionId;
    }

    private MultipleChoiceLayout layout() {
        return layout;
    }

    public String optionId() {
        return layout.optionId();
    }

    public List<CheckListInfoBean.TRdUserAnswer> options() {
        return layout.options();
    }

    public static class Builder<T> {
        private MultipleChoiceLayout layout;
        private List<T> choices;
        private String questionId;

        public Builder(MultipleChoiceLayout layout) {
            this.layout = layout;
        }

        public Builder choices(List<T> choices) {
            this.choices = choices;
            return this;
        }

        public Builder questionId(String questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder layout(MultipleChoiceLayout layout) {
            this.layout = layout;
            return this;
        }

        public MultyChoiceInputLayoutHelper build() {
            return new MultyChoiceInputLayoutHelper(this);
        }

    }
}
