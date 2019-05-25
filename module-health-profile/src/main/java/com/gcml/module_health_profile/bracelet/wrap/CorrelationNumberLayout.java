package com.gcml.module_health_profile.bracelet.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

/**
 * Created by lenovo on 2019/2/20.
 */

public class CorrelationNumberLayout extends LinearLayout {

    private LinearLayout itemList;
    private TextView relationName;

    public CorrelationNumberLayout(Context context) {
        super(context);
        initView();
    }

    public CorrelationNumberLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_correalation, null);
        itemList = view.findViewById(R.id.ll_item_list);
        relationName = view.findViewById(R.id.tv_relation_name);
        addView(view);
    }

    public void setRelationName(String relationName) {
        if (TextUtils.isEmpty(relationName)) {
            return;
        }
        this.relationName.setText(relationName);
    }

    public void addItemView(CorrelationNumberItemLayout itemView) {
        itemList.addView(itemView);
    }


}
