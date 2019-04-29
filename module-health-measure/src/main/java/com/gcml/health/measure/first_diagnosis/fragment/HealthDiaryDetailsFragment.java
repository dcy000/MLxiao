package com.gcml.health.measure.first_diagnosis.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.utils.UM;
import com.gcml.common.widget.picker.RulerView;
import com.gcml.common.widget.picker.SelecterView;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetailsModel;
import com.gcml.health.measure.widget.OverFlyingLayoutManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Arrays;

public class HealthDiaryDetailsFragment extends Fragment {

    public static final String TAG = "HealthDiaryDetailsFragment";

    private DetailsModel mModel;

    private View mView;
    private TextView tvTitle;
    private TextView tvCount;
    private RulerView rvRuler;
    private SelecterView svUnits;
    private TextView tvAction;
    private float mSelectedValue;
    private OverFlyingLayoutManager mLayoutManager;
    private OnActionListener mOnActionListener;

    public HealthDiaryDetailsFragment() {

    }

    public static Fragment newInstance(DetailsModel model) {
        Bundle args = new Bundle();
        args.putParcelable("detailsModel", model);
        HealthDiaryDetailsFragment fragment = new HealthDiaryDetailsFragment();
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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.health_measure_fragment_diary_details, container, false);
        tvTitle = findViewById(R.id.health_diary_tv_title);
        tvCount = findViewById(R.id.health_diary_tv_count);
        rvRuler = findViewById(R.id.health_diary_rv_ruler);
        svUnits = findViewById(R.id.health_diary_sv_selecter);
        tvAction = findViewById(R.id.health_diary_tv_action);
        FragmentActivity activity = getActivity();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请" + mModel.getTitle(), false);
        tvTitle.setText(mModel.getTitle());
        tvCount.setText(getCount(
                mModel.getSelectedValues()[mModel.getUnitPosition()],
                mModel.getUnitSum()[mModel.getUnitPosition()]
        ));
        mSelectedValue = mModel.getSelectedValues()[mModel.getUnitPosition()];
        rvRuler.setValue(
                mModel.getSelectedValues()[mModel.getUnitPosition()],
                mModel.getMinValues()[mModel.getUnitPosition()],
                mModel.getMaxValues()[mModel.getUnitPosition()],
                mModel.getPerValues()[mModel.getUnitPosition()]
        );
        rvRuler.setOnValueChangeListener(onValueChangeListener);
        svUnits.setOnValueChangeListener(svOnValueChangeListener);
        svUnits.setData(Arrays.asList(mModel.getUnits()));
        tvAction.setText(mModel.getAction());
        tvAction.setOnClickListener(actionOnClickListener);
        return mView;
    }
    public void setValue(final float value) {
        if (rvRuler != null) {
            rvRuler.post(new Runnable() {
                @Override
                public void run() {
                    mSelectedValue = value;
                    tvCount.setText(getCount(
                            mSelectedValue,
                            mModel.getUnitSum()[mModel.getUnitPosition()]
                    ));
                    rvRuler.setValue(mSelectedValue,
                            mModel.getMinValues()[mModel.getUnitPosition()],
                            mModel.getMaxValues()[mModel.getUnitPosition()],
                            mModel.getPerValues()[mModel.getUnitPosition()]
                    );
                }
            });
        }
    }

    private View.OnClickListener actionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnActionListener != null) {
                mOnActionListener.onAction(
                        mModel.getWhat(),
                        mSelectedValue,
                        mModel.getUnitPosition(),
                        null
                );
            }
        }
    };

    private RulerView.OnValueChangeListener onValueChangeListener = new RulerView.OnValueChangeListener() {
        @Override
        public void onValueChange(float value) {
            mSelectedValue = value;
            tvCount.setText(getCount(
                    mSelectedValue,
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
        return (V) mView.findViewById(id);
    }
}
