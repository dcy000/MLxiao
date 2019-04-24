package com.gcml.module_health_profile.checklist.config;

import android.view.View;

import com.gcml.module_health_profile.checklist.bean.ItemBean;
import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.SingleChoiceLayout;
import com.gcml.module_health_profile.checklist.wrap.OutLayout;
import com.gcml.module_health_profile.checklist.wrap.SingleChoiceLayout;

import java.util.List;

/**
 * Created by lenovo on 2019/4/23.
 */

public class ChoiceInputLayoutHelper {
    private SingleChoiceLayout layout;
    private List<ItemBean> choices;

    public ChoiceInputLayoutHelper(Builder builder) {
        this.choices = builder.choices;
        this.layout = builder.layout;

        EntryBoxHelper helper=new EntryBoxHelper.Builder(new EntryBoxLinearLayout(layout.getContext())).name("").unit("").build();
        layout.addInput(helper.layout());

        layout.setData(choices);
    }

    public List<ItemBean> choices() {
        return choices;
    }

    public SingleChoiceLayout layout() {
        return layout;
    }

    public static class Builder {
        private SingleChoiceLayout layout;
        private List<ItemBean> choices;

        public Builder(SingleChoiceLayout layout) {
            this.layout = layout;
        }

        public Builder choices(List<ItemBean> choices) {
            this.choices = choices;
            return this;
        }

        public Builder layout(SingleChoiceLayout layout) {
            this.layout = layout;
            return this;
        }

        public ChoiceInputLayoutHelper build() {
            return new ChoiceInputLayoutHelper(this);
        }

    }
}
