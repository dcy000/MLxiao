package com.gcml.task.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.widget.picker.SelecterView;
import com.gcml.common.widget.picker.RulerView;
import com.gcml.task.bean.DetailsModel;

import com.gcml.task.R;

import java.util.Arrays;

public class TaskDialyDetailsFragment extends Fragment {

    private DetailsModel mModel;
    private View mView;
    private TextView tvTitle;
    private TextView tvCount;
    private RulerView rvRuler;
    private SelecterView svSelecter;
    private TextView tvAction;
    private float rvSelectedValue;
    private OnActionListener mOnActionListener;

    public static Fragment newInstance(DetailsModel model) {
        Bundle args = new Bundle();
        args.putParcelable("detailsModel", model);
        TaskDialyDetailsFragment fragment = new TaskDialyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnActionListener {
        void onAction(int what, float selectedValue, int unitPosition, String item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Fragment parentFragment = getParentFragment();
            if (parentFragment != null) {
                mOnActionListener = ((OnActionListener) parentFragment);
                return;
            }
            mOnActionListener = (OnActionListener) context;
        } catch (Throwable e) {
            e.printStackTrace();
            mOnActionListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActionListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mModel = arguments.getParcelable("detailsModel");
            if (mModel == null) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        }
    }

    private SpannableString getCount(float value, String unitSum) {
        String value1 = String.valueOf(value);
        String text = value1 + unitSum;
        SpannableString spannableString = new SpannableString(text);
        RelativeSizeSpan span = new RelativeSizeSpan(0.4f);
        spannableString.setSpan(span, text.length() - unitSum.length(), text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_dialy_details, container, false);
        tvTitle = findViewById(R.id.tv_task_diary_title);
        tvCount = findViewById(R.id.tv_task_diary_count);
        rvRuler = findViewById(R.id.rv_task_diary_ruler);
        svSelecter = findViewById(R.id.sv_task_diary_selecter);
        tvAction = findViewById(R.id.tv_task_diary_action);

        tvTitle.setText(mModel.getTitle());
        tvCount.setText(getCount(
                mModel.getSelectedValues()[mModel.getUnitPosition()],
                mModel.getUnitSum()[mModel.getUnitPosition()]
        ));
        rvSelectedValue = mModel.getSelectedValues()[mModel.getUnitPosition()];
        rvRuler.setValue(
                mModel.getSelectedValues()[mModel.getUnitPosition()],
                mModel.getMinValues()[mModel.getUnitPosition()],
                mModel.getMaxValues()[mModel.getUnitPosition()],
                mModel.getPerValues()[mModel.getUnitPosition()]
        );
        rvRuler.setOnValueChangeListener(rvOnValueChangeListener);
        svSelecter.setOnValueChangeListener(svOnValueChangeListener);
        svSelecter.setData(Arrays.asList(mModel.getUnits()));
        tvAction.setText(mModel.getAction());
        tvAction.setOnClickListener(actionOnClickListener);
        return mView;
    }

    private View.OnClickListener actionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnActionListener != null) {
                mOnActionListener.onAction(
                        mModel.getWhat(),
                        rvSelectedValue,
                        mModel.getUnitPosition(),
                        null
                );
            }
        }
    };

    private RulerView.OnValueChangeListener rvOnValueChangeListener = new RulerView.OnValueChangeListener() {
        @Override
        public void onValueChange(float value) {
            rvSelectedValue = value;
            tvCount.setText(getCount(
                    rvSelectedValue,
                    mModel.getUnitSum()[mModel.getUnitPosition()]
            ));
        }
    };

    private SelecterView.OnValueChangeListener svOnValueChangeListener = new SelecterView.OnValueChangeListener() {
        @Override
        public void onValueChange(int position) {
            mModel.setUnitPosition(position);
            tvCount.setText(getCount(
                    mModel.getSelectedValues()[mModel.getUnitPosition()],
                    mModel.getUnitSum()[mModel.getUnitPosition()]
            ));
            rvRuler.setValue(
                    mModel.getSelectedValues()[mModel.getUnitPosition()],
                    mModel.getMinValues()[mModel.getUnitPosition()],
                    mModel.getMaxValues()[mModel.getUnitPosition()],
                    mModel.getPerValues()[mModel.getUnitPosition()]
            );
        }
    };

    public <V extends View> V findViewById(int id) {
        if (mView == null) {
            return null;
        }
        return mView.findViewById(id);
    }
}
