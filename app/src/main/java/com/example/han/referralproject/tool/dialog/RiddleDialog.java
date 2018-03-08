package com.example.han.referralproject.tool.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
 * Created by lenovo on 2018/3/6.
 */

public class RiddleDialog extends DialogFragment {


    @BindView(R.id.tvbutton)
    TextView tvbutton;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_next)
    TextView tvNext;
    Unbinder unbinder;
    @BindView(R.id.tv_anwser)
    TextView tvAnwser;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    private ShowNextListener listener;

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
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String answer = bundle.getString("answer");
            tvAnwser.setText(answer);
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvbutton, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvbutton:
                dismiss();
                break;
            case R.id.tv_next:
                dismiss();
                if (listener != null) {
                    listener.onNext();
                }
                break;
        }
    }
}

