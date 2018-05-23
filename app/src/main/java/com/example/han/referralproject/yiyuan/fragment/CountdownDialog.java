package com.example.han.referralproject.yiyuan.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 119:
                    String trim = count.getText().toString().trim();
                    final int[] i = {Integer.parseInt(trim)};
                    if (i[0] <= 1) {
                        CountdownDialog.this.dismiss();
                        if (ontouch == null) {
                            return;
                        }

                        ontouch.OnTime();
                        handler.removeMessages(119);
                        return;
                    }

                    MyApplication.getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            count.setText("" + --i[0]);
                        }
                    });
                    handler.sendEmptyMessageDelayed(119, 1000);
                    break;
            }
        }
    };
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
        handler.removeMessages(119);
        handler.sendEmptyMessageDelayed(119, 1000);
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
        void OnTime();
    }

    public void setOntouch(Ontouch ontouch) {
        this.ontouch = ontouch;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        handler.removeMessages(119);
    }
}
