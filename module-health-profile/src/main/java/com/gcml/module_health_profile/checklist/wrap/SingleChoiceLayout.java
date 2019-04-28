package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by lenovo on 2019/4/24.
 * 单选多选
 */

public class SingleChoiceLayout<T> extends LinearLayout {

    private LinearLayout linearLayout;
    private EqualFlowLayout equalFlowLayout;
    private List<CheckListInfoBean.TRdQuestion.TRdOption> data;

    public SingleChoiceLayout(Context context) {
        super(context);
        init();
    }

    public SingleChoiceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleChoiceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        this.data = (List<CheckListInfoBean.TRdQuestion.TRdOption>) data;
        equalFlowLayout.setAdapte(new FlowAdapte<T>(data) {
            @Override
            public View onBindViewHolder(T item, int position) {
                CheckListInfoBean.TRdQuestion.TRdOption itemData = (CheckListInfoBean.TRdQuestion.TRdOption) item;

                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.single_choice_item, null);
                TextView name = inflate.findViewById(R.id.tv_single_item_name);
                name.setText(itemData.optionName);
                ImageView icon = inflate.findViewById(R.id.iv_single_item_icon);

                inflate.setOnClickListener(v -> {
                    /*if (name.isSelected()) {
                        name.setSelected(false);
                        name.setTextColor(Color.parseColor("#ff333333"));
                        icon.setBackgroundColor(Color.parseColor("#ff999999"));
                    }

                    else {
                        name.setSelected(true);
                        name.setTextColor(Color.parseColor("#ff3f88fc"));
                        icon.setBackgroundColor(Color.parseColor("#ff000000"));
                    }*/

                    if (!v.isSelected()) {
                        v.setSelected(true);
                        name.setTextColor(Color.parseColor("#ff3f88fc"));
                        icon.setBackgroundResource(R.drawable.item_single_choice_blue);


                        //排斥其他
                        int childCount = equalFlowLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            if (position == i) {
                                continue;
                            }

                            TextView name01 = equalFlowLayout.getChildAt(i).findViewById(R.id.tv_single_item_name);
                            ImageView icon01 = equalFlowLayout.getChildAt(i).findViewById(R.id.iv_single_item_icon);
                            equalFlowLayout.getChildAt(i).setSelected(false);
                            name01.setTextColor(Color.parseColor("#ff333333"));
                            icon01.setBackgroundResource(R.drawable.item_single_choice_gray);
                        }
                    }
                });
                return inflate;
            }
        });
    }

    public String optionId() {
        int childCount = equalFlowLayout.getChildCount();
        String optionId = "";
        for (int i = 0; i < childCount; i++) {
            boolean selected = equalFlowLayout.getChildAt(i).isSelected();
            if (selected) {
                optionId = data.get(i).optionId;
                break;
            }
        }
        return optionId;
    }
}
