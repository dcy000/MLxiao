package com.gcml.common.base;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.utils.display.ToastUtils;


/**
 * Created by lenovo on 2019/1/21.
 */

public class PermissionDialog extends DialogFragment implements View.OnClickListener {
    OnClickListener onClickListener;
    private EditText passWord;
    private TextView complete;
    private ImageView close;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    interface OnClickListener {
        void onClick(String passWord);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_permission_dialog, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        passWord = inflate.findViewById(R.id.et_password);
        complete = inflate.findViewById(R.id.tv_complete);
        close = inflate.findViewById(R.id.iv_close);

        complete.setOnClickListener(this);
        close.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_complete) {
            if (onClickListener != null) {
                String passWord = this.passWord.getText().toString().trim();
                if (TextUtils.isEmpty(passWord)){
                    ToastUtils.showShort(getString(R.string.business_perimission_dialog_title));
                    return;
                }else{
                    onClickListener.onClick(passWord);
//                    dismiss();
                }
            }

        } else if (id == R.id.iv_close) {
            dismiss();
        }
    }


}
