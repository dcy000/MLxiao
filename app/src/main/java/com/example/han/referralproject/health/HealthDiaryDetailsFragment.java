package com.example.han.referralproject.health;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.health.model.DetailsModel;
import com.ml.edu.common.widget.recycleyview.CenterScrollListener;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;
import com.ml.rulerview.RulerView;

public class HealthDiaryDetailsFragment extends Fragment {

    public static final String TAG = "HealthDiaryDetailsFragment";

    private DetailsModel mModel;

    private View mView;
    private TextView tvTitle;
    private TextView tvCount;
    private RulerView rvRuler;
    private RecyclerView rvUnits;
    private TextView tvAction;

    private float mSelectedValue;

    private UnitsAdapter mUnitsAdapter;
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
        mView = inflater.inflate(R.layout.health_fragment_diary_details, container, false);
        tvTitle = (TextView) findViewById(R.id.health_diary_tv_title);
        tvCount = (TextView) findViewById(R.id.health_diary_tv_count);
        rvRuler = (RulerView) findViewById(R.id.health_diary_rv_ruler);
        rvUnits = (RecyclerView) findViewById(R.id.health_diary_rv_units);
        tvAction = (TextView) findViewById(R.id.health_diary_tv_action);
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).speak("主人，请" + mModel.getTitle());
        }
        tvTitle.setText(mModel.getTitle());
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
        rvRuler.setOnValueChangeListener(onValueChangeListener);
        mUnitsAdapter = new UnitsAdapter();
        rvUnits.setAdapter(mUnitsAdapter);
        rvUnits.addOnScrollListener(new CenterScrollListener());
        mLayoutManager = new OverFlyingLayoutManager(getContext());
        mLayoutManager.setMaxVisibleItemCount(3);
        mLayoutManager.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        mLayoutManager.setMinScale(1.0f);
        mLayoutManager.setItemSpace(0);
        mLayoutManager.setOnPageChangeListener(onPageChangeListener);
        rvUnits.setLayoutManager(mLayoutManager);
        tvAction.setText(mModel.getAction());
        tvAction.setOnClickListener(actionOnClickListener);
        return mView;
    }

    public void setValue(float value) {
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

    private OverFlyingLayoutManager.OnPageChangeListener onPageChangeListener =
            new OverFlyingLayoutManager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    position %= mModel.getUnits().length;
                    int lastPosition = mModel.getUnitPosition();
                    if (lastPosition == position) {
                        return;
                    }
                    mModel.setUnitPosition(position);
                    View lastView = mLayoutManager.getChildAt(lastPosition);
                    View newView = mLayoutManager.getChildAt(position);
                    UnitHolder lastUnitHolder = (UnitHolder) rvUnits.getChildViewHolder(lastView);
                    UnitHolder newUnitHolder = (UnitHolder) rvUnits.getChildViewHolder(newView);
                    lastUnitHolder.tvUnit.setSelected(false);
                    newUnitHolder.tvUnit.setSelected(true);
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
                    mSelectedValue = 0;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

    public <V extends View> V findViewById(int id) {
        if (mView == null) {
            return null;
        }
        return (V) mView.findViewById(id);
    }

    private class UnitHolder extends RecyclerView.ViewHolder {

        private TextView tvUnit;

        public UnitHolder(View itemView) {
            super(itemView);
            tvUnit = (TextView) itemView.findViewById(R.id.health_diary_tv_item_unit);
        }

        public void onBind(int position) {
            tvUnit.setText(mModel.getUnits()[position]);
        }
    }

    private class UnitsAdapter extends RecyclerView.Adapter<UnitHolder> {
        @Override
        public UnitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.health_item_unit2, viewGroup, false);
            return new UnitHolder(view);
        }

        @Override
        public void onBindViewHolder(UnitHolder unitHolder, int position) {
            unitHolder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mModel.getUnits() == null ? 0 : mModel.getUnits().length;
        }
    }
}
