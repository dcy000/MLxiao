package com.gcml.common.widget.base.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.business.R;

/**
 * @author vondear
 * @date 2016/7/19
 * 确认 弹出框
 */
public class DialogSure extends BaseDialog implements View.OnClickListener {

    private ImageView mIvLogo;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvSure;
    private DialogClickSureListener clickSureListener;

    public void setOnClickSureListener(DialogClickSureListener clickSureListener) {
        this.clickSureListener = clickSureListener;
    }

    public DialogSure(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogSure(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogSure(Context context) {
        super(context);
        initView();
    }

    public DialogSure(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public TextView getSureView() {
        return mTvSure;
    }

//    public void setSureListener(View.OnClickListener listener) {
//        mTvSure.setOnClickListener(listener);
//    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setLogo(int resId) {
        mIvLogo.setImageResource(resId);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setSure(String content) {
        mTvSure.setText(content);
    }

    public void setContent(String str) {
        mTvContent.setText(str);

    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.utils_dialog_sure, null);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mTvTitle.setTextIsSelectable(true);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        mIvLogo = dialogView.findViewById(R.id.iv_logo);
        setContentView(dialogView);

        mTvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_sure && clickSureListener != null) {
            clickSureListener.clickSure(this);
        }
    }
}
