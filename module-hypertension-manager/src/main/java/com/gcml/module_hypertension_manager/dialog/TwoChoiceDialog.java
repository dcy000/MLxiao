package com.gcml.module_hypertension_manager.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.module_hypertension_manager.R;

/**
 * Created by lenovo on 2018/3/9.
 */

@SuppressLint("ValidFragment")
public class TwoChoiceDialog extends DialogFragment implements View.OnClickListener {

    private String rithtText;
    private String leftText;
    TextView tvTitle;
    TextView confirm;
    TextView cancel;
    private OnDialogClickListener listener;
    String content;


    public interface OnDialogClickListener {
        void onClickConfirm(String content);

        void onClickCancel();
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
        View view = inflater.inflate(R.layout.dialog_two_choice, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        tvTitle.setText(this.content);
        confirm.setText(this.leftText);
        cancel.setText(this.rithtText);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            clickConfirm();
        } else if (v.getId() == R.id.cancel) {
            clickCancel();
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
        if (listener == null) {
            return;
        }
        listener.onClickCancel();

        dismiss();
    }

    public void reverseButtonTextColor(boolean isRight) {
        if (isRight) {
//            confirm.setTextColor(Color.parseColor("#BBBBBB"));
//            cancel.setTextColor(Color.parseColor("#3F86FC"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
    }
}
