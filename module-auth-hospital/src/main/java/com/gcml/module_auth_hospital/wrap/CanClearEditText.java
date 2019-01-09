package com.gcml.module_auth_hospital.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcml.common.utils.ui.UiUtils;
import com.gcml.module_auth_hospital.R;


/**
 * Created by lenovo on 2018/7/12.
 */

public class CanClearEditText extends LinearLayout {
    public ImageView ivDelete;
    public EditText tvPhone;
    private Context context;
    private boolean isChinese = false;

    public CanClearEditText(Context context) {
        this(context, null);
    }

    public CanClearEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.canclear_edittext_view, null);
        ivDelete= view.findViewById(R.id.iv_delete);
        tvPhone= view.findViewById(R.id.tv_phone);
        if (isChinese) {
            tvPhone.setHint("请输入您的真实姓名");
            tvPhone.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        initEvent();
        addView(view);
    }

    /**
     * 联动逻辑处理
     */
    private void initEvent() {
        tvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null) {
                    listener.onTextChange(s);
                }
                if (TextUtils.isEmpty(tvPhone.getText().toString())) {
                    ivDelete.setVisibility(GONE);
                } else {
                    ivDelete.setVisibility(VISIBLE);
                }

            }
        });

        ivDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPhone.setText("");
            }
        });
    }

    public String getPhone() {
        return tvPhone.getText().toString().trim();
    }

    public interface OnTextChangeListener {
        void onTextChange(Editable s);
    }

    public OnTextChangeListener listener;

    public void setListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public void setTextSize(int value) {
        tvPhone.setTextSize(UiUtils.pt(value));
    }


    public void setIsChinese(boolean isChinese) {
        this.isChinese = isChinese;

        if (isChinese) {
            tvPhone.setHint("请输入您的真实姓名");
            tvPhone.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public void setHintText(String hint) {
        tvPhone.setHint(hint);
    }


}
