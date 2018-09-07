package com.gcml.common.widget.popup;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.utils.DisplayHelper;
import com.gcml.common.widget.popup.base.BasePopupWindow;

/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：商品下拉列表弹出框
 */

public class FriendInvitePopup extends BasePopupWindow {

    private Activity mActivity;
    private RelativeLayout popupShadow;
    private LinearLayout popupHint, popupContent;
    private EditText popupPhone, popupLable;
    private TextView popupTitle, popupHintText, popupHintMsg, popupAction;
    private ImageView popupHintIcon;
    private int actionNum = 0;

    public FriendInvitePopup(Activity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mActivity = context;
    }

    @Override
    public View getPopupView() {
        View view = getPopupViewById(R.layout.layout_popup_friend);
        popupContent = view.findViewById(R.id.popup_content);
        popupContent.setClickable(true);
        popupShadow = view.findViewById(R.id.popup_shadow);
        popupShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popupPhone = view.findViewById(R.id.et_popup_friend);
        popupPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    popupAction.setSelected(true);
                    popupAction.setOnClickListener(null);
                    hidePopupHint();
                } else {
                    popupAction.setSelected(false);
                    popupAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (actionNum == 0) {
                                actionNum = 1;
                                showPopupHintText();
                            } else if (actionNum == 1) {
                                actionNum = 2;
                                showPopupHintFriend();
                            } else if (actionNum == 2) {
                                actionNum = 3;
                                showPopupHintLable();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                            if (actionNum == 3) {
                                actionNum = 0;
                                dismiss();
                            }
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
        popupHint = view.findViewById(R.id.ll_popup_hint);
        popupTitle = view.findViewById(R.id.tv_popup_friend);
        popupHintText = view.findViewById(R.id.tv_popup_hint);
        popupHintIcon = view.findViewById(R.id.tv_popup_hint_icon);
        popupHintMsg = view.findViewById(R.id.tv_popup_hint_msg);
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

    public void hidePopupHint() {
        popupHintText.setVisibility(View.INVISIBLE);
        popupHint.setVisibility(View.GONE);
    }

    public void showPopupHintText() {
        popupPhone.setVisibility(View.VISIBLE);
        popupLable.setVisibility(View.GONE);
        popupHintText.setVisibility(View.VISIBLE);
        popupTitle.setText("添加联系人");
        popupHintText.setText("该用户不存在，请确认后重新输入");
        popupHint.setVisibility(View.GONE);
        popupAction.setText("下一步");
    }

    public void showPopupHintFriend() {
        popupPhone.setVisibility(View.VISIBLE);
        popupLable.setVisibility(View.GONE);
        popupHintText.setVisibility(View.GONE);
        popupHint.setVisibility(View.VISIBLE);
        popupTitle.setText("添加联系人");
        popupHintMsg.setText("机器人");
        ImageLoader.Options options = ImageLoader.newOptionsBuilder(popupHintIcon, R.drawable.common_ic_robot)
                .resize(DisplayHelper.dp2px(mContext,80), DisplayHelper.dp2px(mContext,80))
                .circle()
                .build();
        ImageLoader.instance().load(options);
        popupAction.setText("下一步");
    }

    public void showPopupHintLable() {
        popupPhone.setVisibility(View.GONE);
        popupLable.setVisibility(View.VISIBLE);
        popupHintText.setVisibility(View.VISIBLE);
        popupTitle.setText("添加标签");
        popupHintText.setText("不超过3个字");
        popupHint.setVisibility(View.GONE);
        popupAction.setText("确认");
    }
}

