package com.example.han.referralproject.require2.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
 * 常用提示的dialog
 * Created by lenovo on 2018/7/11.
 */

@SuppressLint("ValidFragment")
public class SomeCommonDialog extends DialogFragment {

    private final DialogTypeEnum type;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;
    Unbinder unbinder;
    private OnDialogClickListener listener;



    public interface OnDialogClickListener {
        void onClickConfirm(DialogTypeEnum type);

//        void onClickCancel();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ValidFragment")
    public SomeCommonDialog(DialogTypeEnum type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.some_common_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(type.getValue());
        confirm.setText(type.getLeftText());
        cancel.setText(type.getRifhtText());
        return view;
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
        listener.onClickConfirm(type);
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
