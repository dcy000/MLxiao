package com.gcml.common.widget.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.business.R;


/**
 * @author vondear
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
public class DialogSureCancel extends BaseDialog implements View.OnClickListener {

    private ImageView mIvLogo;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private DialogClickSureListener clickSureListener;
    private DialogClickCancelListener clickCancelListener;

    public void setOnClickSureListener(DialogClickSureListener clickSureListener) {
        this.clickSureListener = clickSureListener;
    }

    public void setOnClickCancelListener(DialogClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public DialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public DialogSureCancel(Activity context) {
        super(context);
        initView();
    }

    public DialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

//    public void setSureListener(View.OnClickListener sureListener) {
//        mTvSure.setOnClickListener(sureListener);
//    }
//
//    public void setCancelListener(View.OnClickListener cancelListener) {
//        mTvCancel.setOnClickListener(cancelListener);
//    }


    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.utils_dialog_sure_false, null);
        mIvLogo = dialogView.findViewById(R.id.iv_logo);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        setContentView(dialogView);
        mTvSure.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_sure && clickSureListener != null) {
            clickSureListener.clickSure(this);
        } else if (v.getId() == R.id.tv_cancel) {
            if (clickCancelListener == null) {
                dismiss();
                return;
            }
            clickCancelListener.clickCancel(this);
        }
    }
}
