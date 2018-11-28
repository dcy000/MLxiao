package com.gcml.lib_widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.lib_widget.R;
import com.gzq.lib_core.base.Box;


/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：仿IOS提示弹出框
 */

public class IconDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private ImageView img_icon;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showIcon = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public IconDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IconDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_icon_dialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        img_icon = view.findViewById(R.id.img_icon);
        img_icon.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialog);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.60), (int) (display.getHeight() * 0.60)));
        return this;
    }

    public IconDialog setIcon(int icon) {
        showIcon = true;
        if (icon == 0) {
            Glide.with(Box.getApp())
                    .applyDefaultRequestOptions(RequestOptions
                            .circleCropTransform()
                            .override(480, 480))
                    .load(R.drawable.common_ic_robot)
                    .into(img_icon);

        } else {
            Glide.with(Box.getApp())
                    .applyDefaultRequestOptions(RequestOptions
                            .circleCropTransform()
                            .override(480, 480))
                    .load(icon)
                    .into(img_icon);
        }
        return this;
    }

    public IconDialog setIcon(Bitmap bitmap) {
        showIcon = true;
        if (bitmap == null) {
            Glide.with(Box.getApp())
                    .applyDefaultRequestOptions(RequestOptions
                            .circleCropTransform()
                            .override(480, 480))
                    .load(R.drawable.common_ic_robot)
                    .into(img_icon);
        } else {
            Glide.with(Box.getApp())
                    .applyDefaultRequestOptions(RequestOptions
                            .circleCropTransform()
                            .override(480, 480))
                    .load(bitmap)
                    .into(img_icon);
        }
        return this;
    }

    public IconDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public IconDialog setPositiveButton(String text, final View.OnClickListener listener) {
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

    public IconDialog setNegativeButton(String text,
                                        final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (showIcon) {
            img_icon.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
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

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
