package com.gcml.family.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.widget.popup.base.BasePopupWindow;
import com.gcml.family.R;

/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：商品下拉列表弹出框
 */

public class FamilyMenuPopup extends BasePopupWindow {

    private OnSelectListener onSelectListener;
    private Activity mActivity;
    private RelativeLayout popupShadow, popupAdd, popupNews;
    private LinearLayout popupContent;
    private TextView popupAddText, popupNewsText;


    public FamilyMenuPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mActivity = context;
    }

    @Override
    public View getPopupView() {
        View view = getPopupViewById(R.layout.layout_menu_popup);
        popupContent = view.findViewById(R.id.popup_content);
        popupContent.setClickable(true);
        popupShadow = view.findViewById(R.id.popup_shadow);
        popupShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popupAdd = view.findViewById(R.id.popup_add);
        popupAddText = view.findViewById(R.id.txt_msg_add);
        popupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelected(0);
                dismiss();
            }
        });
        popupNews = view.findViewById(R.id.popup_news);
        popupNewsText = view.findViewById(R.id.txt_msg_news);
        popupNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelected(1);
                dismiss();
            }
        });

        return view;
    }

    public void setPopupText(String add, String news) {
        popupAddText.setText(add);
        popupNewsText.setText(news);
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

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    public interface OnSelectListener {
        void onSelected(int position);
    }

}

