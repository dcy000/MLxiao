package com.example.han.referralproject.require2.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.util.LocalShared;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/7/14.
 */

@SuppressLint("ValidFragment")
public class AffirmHeadDialog extends DialogFragment {
    private final byte[] imageData;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;
    Unbinder unbinder;

    @SuppressLint("ValidFragment")
    public AffirmHeadDialog(byte[] imageData) {
        this.imageData = imageData;
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
        View view = inflater.inflate(R.layout.dialog_affirm_head, container, false);
        unbinder = ButterKnife.bind(this, view);
        updateHead();
        return view;
    }

    public void updateHead() {

        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ivHead.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.confirm, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (listener != null) {
                    listener.onConfirm();
                }
                dismiss();
                break;
            case R.id.cancel:

                if (listener != null) {
                    listener.onCancel();
                }
                dismiss();
                break;
        }
    }

    public interface ClickListener {
        void onConfirm();

        void onCancel();
    }

    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
