package com.gcml.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spanned;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.business.R;

/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：仿IOS提示弹出框（底部只有一个按钮，提示文本左对其）
 */

public class SingleDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout bg_content;
    private TextView txt_msg;
    private Button btn_pos;
    private Display display;
    private boolean showMsg = false;
    private boolean showPosBtn = false;

    public SingleDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public SingleDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_single_dialog, null);

        // 获取自定义Dialog布局中的控件
        bg_content = (LinearLayout) view.findViewById(R.id.ll_content);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.HealthPopupAnimaFade);
        dialog.setContentView(view);

        // 调整dialog背景大小
        bg_content.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.60), (int) (display.getHeight() * 0.55)));

        return this;
    }

    public SingleDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }
    public SingleDialog setMsg(Spanned spanned){
        showMsg=true;
        if (spanned==null){
            txt_msg.setText("内容");
        }else{
            txt_msg.setText(spanned);
        }
        return this;
    }
    public SingleDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public SingleDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_single_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_right_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}
