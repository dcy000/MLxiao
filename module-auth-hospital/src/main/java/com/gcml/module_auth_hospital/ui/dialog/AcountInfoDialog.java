package com.gcml.module_auth_hospital.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.module_auth_hospital.R;

import org.w3c.dom.Text;


public class AcountInfoDialog extends DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "noticeInfo";
    private static final String ARG_PARAM2 = "organizationName";
    TextView tvCancle;
    TextView tvConfirm;
    TextView tvNoticeInfo;

    private String noticeInfo;
    private String organizationName;

    public void setListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    private OnFragmentInteractionListener mListener;

    public AcountInfoDialog() {
    }

    public static AcountInfoDialog newInstance(String operator, String organizationName) {
        AcountInfoDialog fragment = new AcountInfoDialog();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(operator)) {
            args.putString(ARG_PARAM1, operator);
        }
        if (!TextUtils.isEmpty(organizationName)) {
            args.putString(ARG_PARAM2, organizationName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        if (getArguments() != null) {
            noticeInfo = getArguments().getString(ARG_PARAM1);
            organizationName = getArguments().getString(ARG_PARAM2);
        }
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_acount_info_dialog, container, false);
        initView(inflate);
        return inflate;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_cancle) {
            if (mListener != null) {
                mListener.onCancle();
            }
            dismiss();
        } else if (id == R.id.tv_confirm) {
            if (mListener != null) {
                mListener.onConfirm();
            }
            dismiss();
        }
    }

    private void initView(View inflate) {
        tvCancle = (TextView) inflate.findViewById(R.id.tv_cancle);
        tvCancle.setOnClickListener(this);
        tvConfirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(this);

        tvNoticeInfo = (TextView) inflate.findViewById(R.id.auth_dialog_notice_info);
        if (!TextUtils.isEmpty(noticeInfo)) {
            tvNoticeInfo.setText(noticeInfo);
        }

    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }

    public interface OnFragmentInteractionListener {
        void onCancle();

        void onConfirm();
    }


}
