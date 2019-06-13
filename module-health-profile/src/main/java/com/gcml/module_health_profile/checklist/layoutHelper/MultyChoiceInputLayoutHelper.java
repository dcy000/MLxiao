package com.gcml.module_health_profile.checklist.layoutHelper;

import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;
import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.MultipleChoiceLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/4/23.
 */

public class MultyChoiceInputLayoutHelper {
    private MultipleChoiceLayout layout;
    private List<CheckListInfoBean.TRdQuestion.TRdOption> choices;
    private List<EntryBoxHelper> entryBoxHelpers = new ArrayList<>();
    private String questionId;

    private MultyChoiceInputLayoutHelper(Builder builder) {
        this.choices = builder.choices;
        this.layout = builder.layout;
        this.questionId = builder.questionId;

        CheckListInfoBean.TRdQuestion.TRdOption tRdOption = choices.get(choices.size() - 1);
        List<CheckListInfoBean.TRdQuestion> questionList = tRdOption.questionList;
        if (questionList != null && questionList.size() != 0) {
            int size = questionList.size();
            for (int i = 0; i < size; i++) {
                EntryBoxHelper helper = new EntryBoxHelper
                        .Builder(new EntryBoxLinearLayout(layout.getContext()))
                        .title(false)
                        .name("")
                        .hint(tRdOption.optionName)
                        .questionId(questionList.get(i).questionId)
                        .build();
                entryBoxHelpers.add(helper);
                layout.addInput(helper.layout());
            }
        }

        layout.setData(choices);
//        layout.setTag(questionId);
    }

    public List<CheckListInfoBean.TRdQuestion.TRdOption> choices() {
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

    public List<EntryBoxHelper> inputBox() {
        return entryBoxHelpers;
    }

    public static class Builder {
        private MultipleChoiceLayout layout;
        private List<CheckListInfoBean.TRdQuestion.TRdOption> choices;
        private String questionId;

        public Builder(MultipleChoiceLayout layout) {
            this.layout = layout;
        }

        public Builder choices(List<CheckListInfoBean.TRdQuestion.TRdOption> choices) {
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
