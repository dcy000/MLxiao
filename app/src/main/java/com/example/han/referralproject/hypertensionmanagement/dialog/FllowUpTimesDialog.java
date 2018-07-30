package com.example.han.referralproject.hypertensionmanagement.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
 * 收费项目的dialog
 * Created by lenovo on 2018/7/16.
 */

@SuppressLint("ValidFragment")
public class FllowUpTimesDialog extends DialogFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;
    Unbinder unbinder;
    String notice;

    @SuppressLint("ValidFragment")
    public FllowUpTimesDialog(String notice) {
        this.notice = notice;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fllow_up_times_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        String source = "您当前测量次数未满足非同日3次测量,血压诊断条件不足,再测"+notice+"日即可为您开启方案";
        SpannableString colorText = new SpannableString(source);
        ForegroundColorSpan what = new ForegroundColorSpan(Color.parseColor("#F56C6C"));
        colorText.setSpan(what, source.indexOf("再测2日"), source.indexOf("即可为您"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            dismiss();
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

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    private OnDialogClickListener listener;
}
