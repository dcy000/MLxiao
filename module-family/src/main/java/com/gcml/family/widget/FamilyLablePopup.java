package com.gcml.family.widget;

import android.app.Activity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.widget.popup.base.BasePopupWindow;
import com.gcml.family.R;

/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：添加标签弹框
 */

public class FamilyLablePopup extends BasePopupWindow {

    private OnInputListener onInputListener;
    private Activity mActivity;
    private RelativeLayout popupShadow;
    private LinearLayout popupContent;
    private EditText popupLable;
    private TextView popupHintText, popupAction;

    public FamilyLablePopup(Activity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mActivity = context;
    }

    @Override
    public View getPopupView() {
        View view = getPopupViewById(R.layout.layout_lable_popup);
        popupContent = view.findViewById(R.id.popup_content);
        popupContent.setClickable(true);
        popupShadow = view.findViewById(R.id.popup_shadow);
        popupShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popupLable = view.findViewById(R.id.et_popup_lable);
        popupLable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    popupAction.setSelected(true);
                    popupAction.setOnClickListener(null);
                } else {
                    popupAction.setSelected(false);
                    popupAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onInputListener.onInputed(popupLable.getText().toString());
                            dismiss();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        popupAction = view.findViewById(R.id.tv_popup_action);
        popupAction.setSelected(true);
        popupHintText = view.findViewById(R.id.tv_popup_hint);
        Spanned str = Html.fromHtml("不超过<font color=#3F86FC>" + "5" + "</font>个字");
        popupHintText.setText(str);
        return view;
    }

    @Override
    public View getAnimaView() {
        return null;
    }

    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return null;
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    public interface OnInputListener {
        void onInputed(String lable);
    }

}

