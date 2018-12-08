package com.example.module_setting.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.module_setting.EventType;
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
public class ClearCacheOrResetDialog extends DialogFragment {

    private final EventType type;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.confirm)
    TextView confirm;
    @BindView(R2.id.cancel)
    TextView cancel;
    Unbinder unbinder;
    private OnDialogClickListener listener;


    public interface OnDialogClickListener {
        void onClickConfirm(EventType type);

//        void onClickCancel();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ValidFragment")
    public ClearCacheOrResetDialog(EventType type) {
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
        View view = inflater.inflate(R.layout.clearcache_reset, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(type.getValue());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.confirm, R2.id.cancel})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.confirm) {
            clickConfirm();

        } else if (i == R.id.cancel) {
            clickCancel();

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
