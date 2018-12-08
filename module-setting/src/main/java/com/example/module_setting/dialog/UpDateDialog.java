package com.example.module_setting.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.module_setting.R;
import com.example.module_setting.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/9.
 */

@SuppressLint("ValidFragment")
public class UpDateDialog extends DialogFragment {

    @BindView(R2.id.tv_update)
    TextView tvUpdate;
    Unbinder unbinder;

    private UpDateDialog.OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onUpdateClick(UpDateDialog upDateDialog);
    }

    public void setListener(UpDateDialog.OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R2.id.tv_update)
    public void onViewClicked() {
        if (listener != null) {
            listener.onUpdateClick(this);
        }
    }
}
