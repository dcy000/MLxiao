package com.example.han.referralproject.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.filterClick.FilterClickListener;

/**
 * Created by lenovo on 2019/3/18.
 */

public class ConfirmDialogFramgment extends DialogFragment implements View.OnClickListener {

    private TextView confirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        confirm = view.findViewById(R.id.tv_dialog_confirm);
        confirm.setOnClickListener(new FilterClickListener(this));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setListener(onClickConfirmListener listener) {
        this.listener = listener;
    }

    private onClickConfirmListener listener;

    public interface onClickConfirmListener {
        void onclickConfirm();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onclickConfirm();
            dismiss();
        }
    }
}
