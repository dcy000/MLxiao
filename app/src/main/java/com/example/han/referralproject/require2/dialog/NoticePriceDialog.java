package com.example.han.referralproject.require2.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/7/16.
 */

@SuppressLint("ValidFragment")
public class NoticePriceDialog extends DialogFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;
    Unbinder unbinder;
    String price;

    @SuppressLint("ValidFragment")
    public NoticePriceDialog(String price) {
        this.price = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_price_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        String source = "您将进行血糖检测,本次服务将收取费用" + price + "元,确认后收取费用,是否继续?";
        SpannableString colorText = new SpannableString(source);
        ForegroundColorSpan what = new ForegroundColorSpan(Color.parseColor("#ff0000"));
        colorText.setSpan(what, source.indexOf(price + "元"), source.indexOf("确认后收取费用"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTitle.setText(colorText);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.confirm, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                clickConfirm();
                break;
            case R.id.cancel:
                clickCancel();
                break;
        }
    }

    private void clickConfirm() {
        if (listener == null) {
            return;
        }
        listener.onClickConfirm();
        dismiss();
    }

    private void clickCancel() {
//        if (listener==null){
//            return;
//        }
//        listener.onClickCancel();

        dismiss();
    }

    public interface OnDialogClickListener {
        void onClickConfirm();

//        void onClickCancel();
    }

    public void setListener(NoticePriceDialog.OnDialogClickListener listener) {
        this.listener = listener;
    }

    private NoticePriceDialog.OnDialogClickListener listener;
}
