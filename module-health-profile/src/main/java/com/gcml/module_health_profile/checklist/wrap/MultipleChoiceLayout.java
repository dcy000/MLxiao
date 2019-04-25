package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;

import java.util.List;

/**
 * Created by lenovo on 2019/4/24.
 */

public class MultipleChoiceLayout<T> extends LinearLayout {
    private LinearLayout linearLayout;
    private EqualFlowLayout equalFlowLayout;

    public MultipleChoiceLayout(Context context) {
        super(context);
        init();
    }

    public MultipleChoiceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultipleChoiceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_single_choice, null);
        linearLayout = view.findViewById(R.id.ll_single_container);
        equalFlowLayout = view.findViewById(R.id.efl_container);
        addView(view);
    }

    public void addInput(EntryBoxLinearLayout layout) {
        linearLayout.addView(layout);
    }

    public void setData(List<T> data) {
        equalFlowLayout.setAdapte(new FlowAdapte<T>(data) {
            @Override
            public View onBindViewHolder(T item, int position) {
                CheckListInfoBean.TRdQuestion.TRdOption itemData = (CheckListInfoBean.TRdQuestion.TRdOption) item;
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.multy_choice_item, null);
                TextView name = inflate.findViewById(R.id.tv_multy_item_name);
                name.setText(itemData.optionName);
                ImageView icon = inflate.findViewById(R.id.iv_multy_item_icon);

                inflate.setOnClickListener(v -> {
                    if (name.isSelected()) {
                        name.setSelected(false);
                        name.setTextColor(Color.parseColor("#ff333333"));
                        icon.setBackgroundColor(Color.parseColor("#ffff00ff"));
                    } else {
                        name.setSelected(true);
                        name.setTextColor(Color.parseColor("#ff3f88fc"));
                        icon.setBackgroundColor(Color.parseColor("#ff0000ff"));
                    }


                    if (TextUtils.equals("1", itemData.exclusiveStatus)) {
                        //排斥其他
                        int childCount = equalFlowLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            TextView name01 = equalFlowLayout.getChildAt(i).findViewById(R.id.tv_multy_item_name);
                            ImageView icon01 = inflate.findViewById(R.id.iv_multy_item_icon);
                            name01.setTextColor(Color.parseColor("#ff333333"));
                            icon01.setBackgroundColor(Color.parseColor("#ffff00ff"));
                        }
                    }
                });
                return inflate;
            }
        });
    }

}
