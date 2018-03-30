package com.example.han.referralproject.children.study;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.BrainTeaserModel;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.HashMap;

public class ChildEduBrainTeaserActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_brain_teaser);

        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvContent = (TextView) findViewById(R.id.ce_brain_teaser_tv_content);

        fetchBrainTeaser();
    }

    private void confirm(String answer) {
        if (TextUtils.isEmpty(answer)) {
//            T.show("请输入答案");
            return;
        }
        BrainTeaserModel model = ChildEduBrainTeaserActivity.this.model;
        if (model == null) {
            return;
        }
        String realAnswer = model.getAnswer();
        boolean right = answer.contains(realAnswer) || realAnswer.contains(answer);
        MyDialogFragment.newInstance(right, realAnswer)
                .show(getSupportFragmentManager(), "MyDialogFragment");

    }

    private BrainTeaserModel model;

    private void fetchBrainTeaser() {
        Handlers.bg().removeCallbacks(fetchBrainTeaserRunnable);
        Handlers.bg().post(fetchBrainTeaserRunnable);
    }

    private Runnable fetchBrainTeaserRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf("脑筋急转弯");
            if (isFinishing()) {
                return;
            }
            String brainTeaser = results.get("text");
            model = BrainTeaserModel.parseBrainTeaser(brainTeaser);
            if (model == null
                    || TextUtils.isEmpty(model.getQuestion())
                    || TextUtils.isEmpty(model.getAnswer())
                    ) {
                ChildEduBrainTeaserActivity.this.model = null;
                T.show("服务器繁忙");
            }
            showQuestion(model);
        }
    };

    private void showQuestion(BrainTeaserModel model) {
        String question;
        if (model == null
                || TextUtils.isEmpty(model.getQuestion())
                || TextUtils.isEmpty(model.getAnswer())) {
            question = "";
        } else {
            question = model.getQuestion();
        }
        final String finalQuestion = question;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvContent.setText(finalQuestion);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Handlers.bg().removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        super.onSpeakListenerResult(result);
        confirm(result);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        fetchBrainTeaser();
    }

    public static class MyDialogFragment extends DialogFragment {

        private View mView;
        private TextView tvAnswer;
        private TextView tvRight;
        private TextView tvContinue;

        private boolean right;
        private String answer;

        private float dimAmount;
        private boolean showBottom;
        private boolean cancelable;

        public static MyDialogFragment newInstance(boolean right, String answer) {
            return newInstance(right, answer, 0f, false, true);
        }

        public static MyDialogFragment newInstance(
                boolean right,
                String answer,
                float dimAmount,
                boolean showBottom,
                boolean cancelable) {
            Bundle args = new Bundle();
            args.putBoolean("right", right);
            args.putString("answer", answer);
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
                right = arguments.getBoolean("right", false);
                answer = arguments.getString("answer");
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
            mView = inflater.inflate(R.layout.ce_dialog_brain_teaser, container, false);
            tvAnswer = (TextView) findViewById(R.id.ce_brain_teaser_tv_answer);
            tvRight = (TextView) findViewById(R.id.ce_brain_teaser_tv_right);
            tvContinue = (TextView) findViewById(R.id.ce_brain_teaser_tv_continue);
            tvAnswer.setText(answer);
            tvRight.setText(right ? "恭喜你回答正确" : "继续努力，你会更强的");
            tvContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return mView;
        }

        @Override
        public void onStart() {
            // init WindowStyle
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
//                android:layout_width="635dp"
//                android:layout_height="335dp"

                lp.width = dp(635);
                lp.height = dp(335);

                window.setAttributes(lp);
            }
            setCancelable(cancelable);
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
}
