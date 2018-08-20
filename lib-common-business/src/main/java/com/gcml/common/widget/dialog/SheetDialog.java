package com.gcml.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.repository.imageloader.ImageLoader;

/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：自定义表格提示弹出框
 */

public class SheetDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private LinearLayout content;
    private LinearLayout contentLift, contentMiddle, contentRight;
    private TextView textLeft, textMiddle, textRight;
    private ImageView imgLeft, imgMiddle, imgRight;
    private boolean showLift = false;
    private boolean showMiddle = false;
    private boolean showRight = false;

    public SheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public SheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_sheet_dialog, null);

        // 获取自定义Dialog布局中的控件
        content = (LinearLayout) view.findViewById(R.id.ll_content);
        contentLift = (LinearLayout) view.findViewById(R.id.ll_content_left);
        contentLift.setVisibility(View.GONE);
        contentMiddle = (LinearLayout) view.findViewById(R.id.ll_content_middle);
        contentMiddle.setVisibility(View.GONE);
        contentRight = (LinearLayout) view.findViewById(R.id.ll_content_right);
        contentRight.setVisibility(View.GONE);

        textLeft = (TextView) view.findViewById(R.id.tv_msg_left);
        textMiddle = (TextView) view.findViewById(R.id.tv_msg_middle);
        textRight = (TextView) view.findViewById(R.id.tv_msg_right);

        imgLeft = (ImageView) view.findViewById(R.id.iv_icon_left);
        imgMiddle = (ImageView) view.findViewById(R.id.iv_icon_middle);
        imgRight = (ImageView) view.findViewById(R.id.iv_icon_right);


        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialog);
        dialog.setContentView(view);

        // 调整dialog背景大小
        content.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.70), (int) (display.getHeight() * 0.70)));

        return this;
    }

    public SheetDialog setLeftData(int resID, String msg, final View.OnClickListener listener) {
        showLift = true;
        ImageLoader.Options optionsLeft = ImageLoader.newOptionsBuilder(imgLeft, resID).build();
        ImageLoader.instance().load(optionsLeft);
        if ("".equals(msg)) {
            textLeft.setText("标签1");
        } else {
            textLeft.setText(msg);
        }
        contentLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public SheetDialog setMiddleData(int resID, String msg, final View.OnClickListener listener) {
        showMiddle = true;
        ImageLoader.Options optionsMiddle = ImageLoader.newOptionsBuilder(imgMiddle, resID).build();
        ImageLoader.instance().load(optionsMiddle);
        if ("".equals(msg)) {
            textMiddle.setText("标签2");
        } else {
            textMiddle.setText(msg);
        }
        contentMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public SheetDialog setRightData(int resID, String msg, final View.OnClickListener listener) {
        showRight = true;
        ImageLoader.Options optionsRight = ImageLoader.newOptionsBuilder(imgRight, resID).build();
        ImageLoader.instance().load(optionsRight);
        if ("".equals(msg)) {
            textRight.setText("标签3");
        } else {
            textRight.setText(msg);
        }
        contentRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public SheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    private void setLayout() {

        if (showLift) {
            contentLift.setVisibility(View.VISIBLE);
            contentLift.setBackgroundResource(R.drawable.alert_single_selector);
        }

        if (showMiddle) {
            contentMiddle.setVisibility(View.VISIBLE);
            contentMiddle.setBackgroundResource(R.drawable.alert_single_selector);
        }

        if (showRight) {
            contentRight.setVisibility(View.VISIBLE);
            contentRight.setBackgroundResource(R.drawable.alert_single_selector);
        }

        if (!showLift && showMiddle && showRight) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(180, 0, 0, 0);
            contentRight.setLayoutParams(layoutParams);
        }

        if (showLift && !showMiddle && showRight) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(180, 0, 0, 0);
            contentRight.setLayoutParams(layoutParams);
        }

        if (showLift && showMiddle && !showRight) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(180, 0, 0, 0);
            contentMiddle.setLayoutParams(layoutParams);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}
