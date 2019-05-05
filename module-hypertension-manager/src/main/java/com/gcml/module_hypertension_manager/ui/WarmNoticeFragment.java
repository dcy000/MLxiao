package com.gcml.module_hypertension_manager.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * Created by lenovo on 2018/7/23.
 */

public class WarmNoticeFragment extends Fragment implements View.OnClickListener {
    TextView tvTipContent;
    TextView tvButton;
    public static final String TIP_CONTENT = "tipContent";
    public static final String BUTTON_TEXT = "buttonText";
    private int COUNT_DOWN_TIME = 5;
    private Bundle arguments;
    private Runnable action;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.warm_notice_dialog, null);
        tvTipContent=view.findViewById(R.id.tv_tip_content);
        tvButton=view.findViewById(R.id.tv_button);
        tvButton.setOnClickListener(this);
        arguments = getArguments();
        tvTipContent.setText(arguments.getString(TIP_CONTENT));
        tvButton.setText(arguments.getString(BUTTON_TEXT) + "(关闭5S)");
        btnCountDown(tvButton);
        tvButton.setOnClickListener(this);
        return view;
    }

    private void btnCountDown(final TextView view) {
        //                    COUNT_DOWN_TIME = 5;
        action = new Runnable() {
            @Override
            public void run() {
                COUNT_DOWN_TIME--;
                if (COUNT_DOWN_TIME < 0) {
//                    COUNT_DOWN_TIME = 5;
                    listener.onFragmentBtnTimeOut();
                    return;
                }
                view.setText(arguments.getString(BUTTON_TEXT) + "(关闭" + COUNT_DOWN_TIME + "S)");
                view.postDelayed(this, 1000);
            }
        };
        view.postDelayed(action, 1000);
    }


    public static WarmNoticeFragment getInstance(String tipContent, String buttonText) {
        WarmNoticeFragment fragment = new WarmNoticeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TIP_CONTENT, tipContent);
        bundle.putString(BUTTON_TEXT, buttonText);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tvButton != null) {
            tvButton.removeCallbacks(action);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onFragmentBtnClick();
            tvButton.removeCallbacks(action);
        }

    }

    public interface OnButtonClickListener {
        void onFragmentBtnClick();

        void onFragmentBtnTimeOut();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tvButton != null) {
            tvButton.removeCallbacks(action);
            MLVoiceSynthetize.stop();
        }
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    private OnButtonClickListener listener;
}
