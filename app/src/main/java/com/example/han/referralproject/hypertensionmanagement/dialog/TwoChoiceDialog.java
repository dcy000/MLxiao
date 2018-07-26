package com.example.han.referralproject.hypertensionmanagement.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.EventType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/9.
 */

@SuppressLint("ValidFragment")
public class TwoChoiceDialog extends DialogFragment {

    private String rithtText;
    private String leftText;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;
    Unbinder unbinder;
    private OnDialogClickListener listener;
    String content;


    public interface OnDialogClickListener {
        void onClickConfirm(String content);

//        void onClickCancel();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ValidFragment")
    public TwoChoiceDialog(String content, String leftText, String rithtText) {
        this.content = content;
        this.leftText = leftText;
        this.rithtText = rithtText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clearcache_reset, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(this.content);
        confirm.setText(this.leftText);
        cancel.setText(this.rithtText);
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
        listener.onClickConfirm(this.content);
        dismiss();
    }

    private void clickCancel() {
//        if (listener==null){
//            return;
//        }
//        listener.onClickCancel();

        dismiss();
    }
}
