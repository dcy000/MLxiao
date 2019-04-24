package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;

import java.util.List;

/**
 * Created by lenovo on 2019/4/24.
 */

public class SingleChoiceLayout<T> extends LinearLayout {

    private LinearLayout linearLayout;
    private EqualFlowLayout equalFlowLayout;

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
        equalFlowLayout.setAdapte(new FlowAdapte<T>(data) {
            @Override
            public View onBindViewHolder(T item, int position) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.single_choice_item, null);
                TextView name = inflate.findViewById(R.id.tv_single_item_name);
                if (position == 1) {
                    name.setText(position + "条目条目条目条目条目条目条目条目条目条目条目条目条目条目");
                } else if (position == 7) {
                    name.setText(position + "条目条目条目条目条目条目条目");
                } else
                    name.setText(position + "条目");
                return inflate;
            }
        });
    }


}
