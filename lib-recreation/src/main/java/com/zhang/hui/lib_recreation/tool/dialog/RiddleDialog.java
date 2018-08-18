package com.zhang.hui.lib_recreation.tool.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;


/**
 * Created by lenovo on 2018/3/6.
 */

public class RiddleDialog extends DialogFragment implements View.OnClickListener {


    private View view;
    private TextView mTvbutton;
    private TextView mTvAnwser;
    /**
     * 答案
     */
    private TextView mTvQuestion;
    /**
     * 下一题
     */
    private TextView mTvNext;
    private ConstraintLayout mConstraintLayout;
    private ShowNextListener listener;
    private String answer;

    public interface ShowNextListener {
        void onNext();
    }

    public void setListener(ShowNextListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.riddle_dialog, container, false);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            answer = bundle.getString("answer");
            mTvAnwser.setText(answer);
        }
        MLVoiceSynthetize.stop();
        MLVoiceSynthetize.startSynthesize(getContext().getApplicationContext(), "答案是" + answer, false);
        return view;
    }

    private void initView(View view) {
        mTvbutton = (TextView) view.findViewById(R.id.tvbutton);
        mTvbutton.setOnClickListener(this);
        mTvAnwser = (TextView) view.findViewById(R.id.tv_anwser);
        mTvQuestion = (TextView) view.findViewById(R.id.tv_question);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        mConstraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvbutton) {
            dismiss();
        } else if (i == R.id.tv_next) {
            dismiss();
            if (listener != null) {
                listener.onNext();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        MLVoiceSynthetize.stop();
    }
}

