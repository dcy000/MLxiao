package com.zhang.hui.lib_recreation.tool.dialog;

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

public class CalculationDialog extends DialogFragment implements View.OnClickListener {

    private Bundle bundle;
    private View view;
    private TextView tvbutton;
    /**
     * 等于
     */
    private TextView textView5;
    /**
     * TextView
     */
    private TextView tvAnwser;
    /**
     * TextView
     */
    private TextView tvQuestion;
    private ConstraintLayout constraintLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculation_dialog, container, false);
        initView(view);
        bundle = getArguments();
        if (bundle != null) {
            tvQuestion.setText(bundle.getString("question"));
            tvAnwser.setText(bundle.getString("answer").substring(2));
        }
        MLVoiceSynthetize.stop();
        MLVoiceSynthetize.startSynthesize(getContext(), bundle.getString("answer"), false);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }

    private void initView(View view) {
        tvbutton = (TextView) view.findViewById(R.id.tvbutton);
        tvbutton.setOnClickListener(this);
        textView5 = (TextView) view.findViewById(R.id.textView5);
        tvAnwser = (TextView) view.findViewById(R.id.tv_anwser);
        tvQuestion = (TextView) view.findViewById(R.id.tv_question);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
