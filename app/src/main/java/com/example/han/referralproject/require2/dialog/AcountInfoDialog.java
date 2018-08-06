package com.example.han.referralproject.require2.dialog;

import android.content.Context;
import android.os.Bundle;
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

public class AcountInfoDialog extends DialogFragment {

    private static final String ARG_PARAM1 = "operator";
    private static final String ARG_PARAM2 = "organizationName";
    @BindView(R.id.tv_operator_info)
    TextView tvOperatorInfo;
    @BindView(R.id.tv_orgnization_info)
    TextView tvOrgnizationInfo;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    Unbinder unbinder;

    private String operator;
    private String organizationName;

    private OnFragmentInteractionListener mListener;

    public AcountInfoDialog() {
    }

    public static AcountInfoDialog newInstance(String operator, String organizationName) {
        AcountInfoDialog fragment = new AcountInfoDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, operator);
        args.putString(ARG_PARAM2, organizationName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operator = getArguments().getString(ARG_PARAM1);
            organizationName = getArguments().getString(ARG_PARAM2);
        }
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_acount_info_dialog, container, false);
        intView();
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    private void intView() {
        tvOperatorInfo.setText(operator);
        tvOrgnizationInfo.setText(organizationName);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cancle, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                if (mListener != null) {
                    mListener.onCancle();
                }
                dismiss();
                break;
            case R.id.tv_confirm:
                if (mListener != null) {
                    mListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onCancle();

        void onConfirm();
    }
}
