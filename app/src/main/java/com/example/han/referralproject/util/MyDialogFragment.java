package com.example.han.referralproject.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.intelligent_diagnosis.MonthlyReportActivity;
import com.example.han.referralproject.intelligent_diagnosis.WeeklyReportActivity;
import com.gzq.lib_core.utils.QRCodeUtils;
import com.gzq.lib_core.utils.UiUtils;
import com.medlink.danbogh.utils.Handlers;

public class MyDialogFragment extends DialogFragment {

    private static final String TAG = "QrcodeDialog";

    private View mView;
    private ImageView ivQrcode;

    private String text;

    private float dimAmount;
    private boolean showBottom;
    private boolean cancelable;

    public static MyDialogFragment newInstance(String text) {
        return newInstance(text, 0f, false, true);
    }

    public static MyDialogFragment newInstance(
            String text,
            float dimAmount,
            boolean showBottom,
            boolean cancelable) {
        Bundle args = new Bundle();
        args.putString("text", text);
        args.putFloat("dimAmount", dimAmount);
        args.putBoolean("showBottom", showBottom);
        args.putBoolean("cancelable", cancelable);
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDismissListener = (DialogInterface.OnDismissListener) context;
        } catch (Throwable e) {
            e.printStackTrace();
            onDismissListener = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        Bundle arguments = getArguments();
        if (arguments != null) {
            text = arguments.getString("text");
            dimAmount = arguments.getFloat("dimAmount", 0f);
            showBottom = arguments.getBoolean("showBottom", false);
            cancelable = arguments.getBoolean("cancelable", true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.health_dialog_fragment_qrcode, container, false);
        ivQrcode = (ImageView) findViewById(R.id.health_record_iv_qrcode);
        findViewById(R.id.health_diary_tv_week_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(activity, WeeklyReportActivity.class);
                activity.startActivity(intent);
            }
        });
        findViewById(R.id.health_diary_tv_month_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(activity, MonthlyReportActivity.class);
                activity.startActivity(intent);
            }
        });
        if (TextUtils.isEmpty(text)) {
            return mView;
        }
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
//                    if (text.startsWith("http")){
//                        text = MyDialogFragment.this.text.replaceFirst("https://|http://", "");
//                    }
                final Bitmap bitmap = QRCodeUtils.creatQRCode(text, dp(260), dp(260));
                if (bitmap != null && ivQrcode != null) {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            ivQrcode.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        // initWindowStyle
        super.onStart();
        initWindowParams();
    }

    private void initWindowParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
            } else {
                lp.gravity = Gravity.CENTER;
            }

            lp.width = UiUtils.pt(1280);
            lp.height = UiUtils.pt(840);

            window.setAttributes(lp);
        }
        setCancelable(cancelable);
    }

    @Override
    public void onStop() {
        Handlers.ui().removeCallbacksAndMessages(null);
        Handlers.bg().removeCallbacksAndMessages(null);
        super.onStop();
    }

    public int dp(float value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * value + 0.5f);
    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (mView == null) {
            return null;
        }
        return (V) mView.findViewById(id);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onDetach() {
        onDismissListener = null;
        super.onDetach();
    }
}
