package com.example.han.referralproject.children.study;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.BrainTeaserModel;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.Utils;

import java.util.HashMap;

public class ChildEduBrainTeaserActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    private TextView tvContent;

    private ImageView ivShowAnswer;
    private ImageView ivNext;
    private VoiceLineView vlvWave;
    private boolean isStart;
    private TextView tvVoice;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private ImageView ivVoice;

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

        tvContent = findViewById(R.id.ce_brain_teaser_tv_content);
        ivShowAnswer = findViewById(R.id.ce_brain_teaser_iv_show_answer);
        ivNext = findViewById(R.id.ce_brain_teaser_iv_next);
        vlvWave = findViewById(R.id.ce_brain_teaser_vlv_voice_wave);
        tvVoice = findViewById(R.id.ce_brain_teaser_tv_voice);
        ivVoice = findViewById(R.id.ce_brain_teaser_iv_voice);
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endOfSpeech();
                MLVoiceSynthetize.stop();
                startListener();
            }
        });
        ivShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model == null) {
                    return;
                }
                MyDialogFragment.newInstance(true, model.getAnswer())
                        .show(getSupportFragmentManager(), "MyDialogFragment");
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBrainTeaser();
            }
        });
        ivNext.performClick();
    }

    public int recordTotalTime;

    private void updateTimerUI(int recordTotalTime) {
        String string = String.format("%s", StringUtil.formatTime(recordTotalTime));
        vlvWave.setText(string);
    }

    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
        tvVoice.setVisibility(View.GONE);
        vlvWave.setVisibility(View.VISIBLE);
        vlvWave.setText("00:00");
        vlvWave.startRecord();
        mainHandler.removeCallbacksAndMessages(null);

        mainHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                recordTotalTime += 1000;
                updateTimerUI(recordTotalTime);
                mainHandler.postDelayed(this, 1000);
            }

        }, 1000);
    }

    private MLRecognizerListener recognizerListener = new MLRecognizerListener() {
        @Override
        public void onMLVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onMLBeginOfSpeech() {
            showWave();
        }

        @Override
        public void onMLEndOfSpeech() {
            endOfSpeech();
            tvVoice.setVisibility(View.VISIBLE);
        }

        @Override
        public void onMLResult(String result) {
            doData(result);
        }

        @Override
        public void onMLError(SpeechError error) {
            endOfSpeech();
            speak("主人,我没听清,您能再说一遍吗");
        }
    };

    private void startListener() {
        MLVoiceRecognize.startRecognize(this, recognizerListener);
    }

    private void doData(String result) {
        if (model == null || TextUtils.isEmpty(model.getAnswer())) {
            return;
        }

        if (TextUtils.isEmpty(result)) {
//            speak("主人,我没听清,您能再说一遍吗");
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(".*xiayiti.*")) {
            ivNext.performClick();
            return;
        }

        if (inSpell.matches(".*xianshidaan|kandaan.*")) {
            ivShowAnswer.performClick();
            return;
        }

        String answer = model.getAnswer();
        if (answer.equals(result)
                || answer.contains(result)
                || result.contains(answer)) {
            speak("恭喜主人答对了");
        } else {
            speak("主人,回答错误，您再猜一下!");
        }
    }

    private void endOfSpeech() {
        stopListening();
        vlvWave.setVisibility(View.GONE);
        vlvWave.stopRecord();
        tvVoice.setVisibility(View.VISIBLE);
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }

    private void confirm(String answer) {
        if (TextUtils.isEmpty(answer)) {
//            ToastUtils.showShort("请输入答案");
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
                ToastUtils.showShort("服务器繁忙");
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
                speak(finalQuestion);
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
        setDisableGlobalListen(false);
        setEnableListeningLoop(false);
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
//        fetchBrainTeaser();
    }

    public static class MyDialogFragment extends DialogFragment {

        private View mView;
        private TextView tvTitle;
        private TextView tvAnswer;
        private TextView tvConfirm;

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
            tvTitle = findViewById(R.id.ce_brain_teaser_tv_title);
            tvAnswer = findViewById(R.id.ce_brain_teaser_tv_answer);
            tvConfirm = findViewById(R.id.ce_brain_teaser_tv_confirm);
            tvAnswer.setText(answer);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
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
                lp.width = UiUtils.pt(1200);
                lp.height = UiUtils.pt(658);
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
