package com.gcml.module_hypertension_manager.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * 收费项目的dialog
 * Created by lenovo on 2018/7/16.
 */

@SuppressLint("ValidFragment")
public class FllowUpTimesDialog extends DialogFragment implements View.OnClickListener {
    TextView tvTitle;
    TextView confirm;
    TextView cancel;
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
        tvTitle= view.findViewById(R.id.tv_title);
        confirm= view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel= view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        String source = "您当前测量次数未满足非同日3次测量，血压诊断条件不足，再测" + notice + "日即可为您开启方案。";
        SpannableString colorText = new SpannableString(source);
        ForegroundColorSpan what = new ForegroundColorSpan(Color.parseColor("#F56C6C"));
        colorText.setSpan(what, source.indexOf("再测"), source.indexOf("即可为您"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTitle.setText(colorText);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getActivity().getApplicationContext(), "您当前测量次数未满足非同日3次测量，血压诊断条件不足，再测" + notice + "日即可为您开启方案。");
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
        getActivity().finish();
    }



    public interface OnDialogClickListener {
        void onClickConfirm();

//        void onClickCancel();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    private OnDialogClickListener listener;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        MLVoiceSynthetize.stop();
    }
}
