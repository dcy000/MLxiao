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


/**
 * Created by lenovo on 2018/5/22.
 */

public class CountdownDialog extends DialogFragment {
    Handler handler = new Handler();
    private TextView count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_count_down, container, false);
        initDialogView();
        count = view.findViewById(R.id.count);
        return view;
    }


    private void initDialogView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String trim = count.getText().toString().trim();
                int i = Integer.parseInt(trim);

                if (i <= 1) {
                    handler.removeCallbacksAndMessages(null);
                    CountdownDialog.this.dismiss();
                    return;
                }

                count.setText("" + --i);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private Ontouch ontouch;

    public interface Ontouch {
        void OnTouch();
    }

    public void setOntouch(Ontouch ontouch) {
        this.ontouch = ontouch;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (ontouch == null) {
            return;
        }

        ontouch.OnTouch();

    }
}
