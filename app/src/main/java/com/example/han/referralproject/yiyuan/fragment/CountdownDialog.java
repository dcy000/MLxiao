package com.example.han.referralproject.yiyuan.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;


/**
 * Created by lenovo on 2018/5/22.
 */

public class CountdownDialog extends DialogFragment implements View.OnClickListener {
    Handler handler = new Handler();
    private TextView count;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_count_down, container, false);
        initDialogView();
        count = view.findViewById(R.id.count);
        view.setOnClickListener(this);
        return view;
    }


    private void initDialogView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String trim = count.getText().toString().trim();
                final int[] i = {Integer.parseInt(trim)};
                if (i[0] <= 1) {
                    handler.removeCallbacksAndMessages(null);
                    CountdownDialog.this.dismiss();
                    return;
                }

                MyApplication.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count.setText("" + --i[0]);
                    }
                });
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private Ontouch ontouch;

    @Override
    public void onClick(View view) {
        if (ontouch == null) {
            return;
        }

        ontouch.OnTouch();
    }

    public interface Ontouch {
        void OnTouch();
    }

    public void setOntouch(Ontouch ontouch) {
        this.ontouch = ontouch;
    }
}
