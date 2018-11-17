package com.example.han.referralproject.tool.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/6.
 */

public class CalculationDialog extends DialogFragment {
    @BindView(R.id.tvbutton)
    TextView tvbutton;
    @BindView(R.id.tv_anwser)
    TextView tvAnwser;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    Unbinder unbinder;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculation_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        bundle = getArguments();
        if (bundle != null) {
            tvQuestion.setText(bundle.getString("question"));
            tvAnwser.setText(bundle.getString("answer").substring(2));
        }
        MLVoiceSynthetize.stop();
        MLVoiceSynthetize.startSynthesize(bundle.getString("answer"));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tvbutton)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
