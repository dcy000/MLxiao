package com.gcml.module_inquiry.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.module_inquiry.R;


/**
 * Created by lenovo on 2018/11/21.
 */

@SuppressLint("ValidFragment")
public class AffirmSignatureDialog extends DialogFragment implements View.OnClickListener {
    private byte[] imageData;
    ImageView ivHead;
    TextView confirm;
    TextView cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_affirm_signature, container, false);
        ivHead = view.findViewById(R.id.iv_head);
        confirm = view.findViewById(R.id.confirm);
        cancel = view.findViewById(R.id.cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            imageData = arguments.getByteArray("imageData");
        }
        updateHead();
        return view;
    }

    public void updateHead() {
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ivHead.setImageBitmap(bitmap);
        }
    }


    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.confirm) {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();
        } else if (id == R.id.cancel) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }

    public interface ClickListener {
        void onConfirm();

        void onCancel();
    }

    private AffirmSignatureDialog.ClickListener listener;

    public void setListener(AffirmSignatureDialog.ClickListener listener) {
        this.listener = listener;
    }
}
