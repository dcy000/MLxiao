package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonTipsDialogFragment extends DialogFragment {
    private static final String ARG_TIPS = "tips";
    private static final String ARG_COLOR_TEXT = "colorText";
    private static final String ARG_COLOR = "color";

    private String tips;
    private String colorText;
    private int color;

    private TextView mTvTips;
    private TextView mTvAction;


    public CommonTipsDialogFragment() {

    }

    public static CommonTipsDialogFragment newInstance(String tips, String colorText, int color) {
        CommonTipsDialogFragment fragment = new CommonTipsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TIPS, tips);
        args.putString(ARG_COLOR_TEXT, colorText);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        if (getArguments() != null) {
            tips = getArguments().getString(ARG_TIPS);
            colorText = getArguments().getString(ARG_COLOR_TEXT);
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_tips_dialog, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mTvAction = (TextView) view.findViewById(R.id.tv_action);
        if (!TextUtils.isEmpty(tips)
                && !TextUtils.isEmpty(colorText)
                && color > 0
                && tips.contains(colorText)) {
            SpannableStringBuilder text = new SpannableStringBuilder(tips);
            ForegroundColorSpan what = new ForegroundColorSpan(getResources().getColor(color));
            int start = tips.indexOf(colorText);
            text.setSpan(what, start, start + colorText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvTips.setText(text);
            return;
        }
        mTvTips.setText(tips);
        mTvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (actionOnClickListener != null) {
                    actionOnClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
    }

    private View.OnClickListener actionOnClickListener;

    public void setActionOnClickListener(View.OnClickListener actionOnClickListener) {
        this.actionOnClickListener = actionOnClickListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionOnClickListener = null;
    }
}
