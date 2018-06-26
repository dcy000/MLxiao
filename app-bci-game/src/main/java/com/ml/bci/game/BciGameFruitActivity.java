package com.ml.bci.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ml.bci.game.common.widget.recyclerview.AutoScrollHelper;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import java.util.ArrayList;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class BciGameFruitActivity extends AppCompatActivity {

    private ConstraintLayout clRoot;
    private TextView tvFruitIndicator;
    private ProgressBar pbAttention;
    private RecyclerView rvFruits;
    private ImageView ivFruitBasketBack;
    private ImageView ivFruitChoose;
    private Adapter mAdapter;
    private AutoScrollHelper mAutoScrollHelper;
    private GalleryLayoutManager mLayoutManager;
    private AttentionObservable mAttentionObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bci_activity_game_fruit);
        initView();
        initAttention();
        initAttentionDevice();
    }


    private void initView() {
        clRoot = (ConstraintLayout) findViewById(R.id.bci_cl_root);
        tvFruitIndicator = (TextView) findViewById(R.id.bci_tv_fruit_indicator);
        pbAttention = (ProgressBar) findViewById(R.id.bci_pb_attention);
        ivFruitBasketBack = (ImageView) findViewById(R.id.bci_iv_fruit_basket_back);
        ivFruitChoose = (ImageView) findViewById(R.id.bci_iv_choose_fruit);
        rvFruits = (RecyclerView) findViewById(R.id.bci_rv_fruits);

//        mLayoutManager = new OverFlyingLayoutManager(this);
//        mLayoutManager.setMinScale(1.0f);
//        mLayoutManager.setItemSpace(0);
//        mLayoutManager.setMaxVisibleItemCount(fruitResources.size());
//        mLayoutManager.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        mLayoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        mLayoutManager.attach(rvFruits, fruitResources.size() - 1);
        mLayoutManager.setCallbackInFling(true);
        mLayoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                int curSelectedPosition = mLayoutManager.getCurSelectedPosition();
                tvFruitIndicator.setText(getFruit(curSelectedPosition));
            }
        });
        rvFruits.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter();
        rvFruits.setAdapter(mAdapter);
        rvFruits.setEnabled(false);
        rvFruits.addOnItemTouchListener(onItemTouchListener);
        mAutoScrollHelper = new AutoScrollHelper();
        mAutoScrollHelper.attach(rvFruits);

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFruitChoose.setVisibility(View.GONE);
                closeDevice();
                initFruits();
                mAdapter.notifyDataSetChanged();
                rvFruits.smoothScrollToPosition(fruitResources.size() - 1);
                connectDevice();
            }
        });
    }

    private void initFruits() {
        fruitResources.clear();
        fruitResources.add(R.drawable.bci_ic_fruit_apple);
        fruitResources.add(R.drawable.bci_ic_fruit_peach);
        fruitResources.add(R.drawable.bci_ic_fruit_pear);
        fruitResources.add(R.drawable.bci_ic_fruit_watermelon);
    }

    private void initAttention() {
        mAttentionObservable = new AttentionObservable();
        mAttentionObservable.registerObserver(new AttentionObservable.Observer() {
            @Override
            public void onAttentionChanged(int intensity) {
                pbAttention.setProgress(intensity);
                if (!mAutoScrollHelper.isStarted()) {
                    mAutoScrollHelper.start();
                }
                mAutoScrollHelper.setOffsetX((int) (mAutoScrollHelper.baseOffset() * intensity / 100f));
            }

            @Override
            public void onBlinkChanged(int intensity) {
                if (intensity > 88) {
                    mockAnimate();
                }
            }
        });

//        mockAnimate();
    }

    private void mockAnimate() {
        rvFruits.post(new Runnable() {
            @Override
            public void run() {
                int currentPosition = fruitResources.size() == 0 ? 0 : mLayoutManager.getCurSelectedPosition();
                Log.d("fruit", "currentPosition: " + currentPosition);
                Log.d("fruit", getFruit(currentPosition));
                if (fruitResources.size() != 0) {
                    final int position;
                    position = currentPosition % fruitResources.size();
                    final Integer removed;
                    synchronized (fruitResources) {
                        removed = fruitResources.remove(position);
                    }
                    final int[] startLocation = new int[2];
                    View view = mLayoutManager.getChildAt(position);
                    view.getLocationInWindow(startLocation);
                    mAdapter.notifyItemRemoved(position);
                    rvFruits.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(startLocation, position, removed);
                        }
                    }, 16);
                }

//                if (fruitResources.size() > 0) {
//                    rvFruits.postDelayed(this, 600);
//                }
            }
        });
    }

    private void animate(int[] startLocation, int position, final int removed) {
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(removed);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        clRoot.addView(imageView, layoutParams);

        int[] parentLocation = new int[2];
        clRoot.getLocationInWindow(parentLocation);

        int[] endLocation = new int[2];
        ivFruitChoose.getLocationInWindow(endLocation);

        int startX = startLocation[0] - parentLocation[0] + ivFruitChoose.getWidth() / 2;
        int startY = startLocation[1] - parentLocation[1] + ivFruitChoose.getHeight() / 2;
        int endX = endLocation[0] - parentLocation[0] + ivFruitChoose.getWidth() / 2;
        int endY = endLocation[1] - parentLocation[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);

        ValueAnimator animator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float[] pos = new float[2];
                pathMeasure.getPosTan(value, pos, null);
                imageView.setTranslationX(pos[0]);
                imageView.setTranslationY(pos[1]);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clRoot.removeView(imageView);
                ivFruitChoose.setImageResource(removed);
                ivFruitChoose.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    private String getFruit(int currentPosition) {
        int res;
        synchronized (fruitResources) {
            int position = currentPosition % fruitResources.size();
            res = fruitResources.get(position);
        }
        String fruit = "苹果";
        switch (res) {
            case R.drawable.bci_ic_fruit_apple:
                fruit = "苹果";
                break;
            case R.drawable.bci_ic_fruit_peach:
                fruit = "桃子";
                break;
            case R.drawable.bci_ic_fruit_pear:
                fruit = "梨子";
                break;
            case R.drawable.bci_ic_fruit_watermelon:
                fruit = "西瓜";
                break;
            default:
                break;
        }
        return fruit;
    }

    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    private final ArrayList<Integer> fruitResources = new ArrayList<>();

    private class VH extends RecyclerView.ViewHolder {

        private ImageView ivFruit;

        public VH(View itemView) {
            super(itemView);
            ivFruit = (ImageView) itemView.findViewById(R.id.bci_iv_item_fruit);
        }

        public void onBind(int position) {
            synchronized (fruitResources) {
                int fruitRes = fruitResources.get(getAdapterPosition());
                ivFruit.setImageResource(fruitRes);
            }
        }

        public void onRecycled() {

        }
    }

    private class Adapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bci_item_fruit, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public void onViewRecycled(VH holder) {
            super.onViewRecycled(holder);
            holder.onRecycled();
        }

        @Override
        public int getItemCount() {
            return fruitResources.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDevice();
    }

    public static final String TAG_TG_DEVICE = "TGDevice";

    private int subjectContactQuality_last = -1; /* start with impossible value */
    private int subjectContactQuality_cnt = 200; /* start over the limit, so it gets reported the 1st time */

    private double task_famil_baseline, task_famil_cur, task_famil_change;
    private boolean task_famil_first = true;
    private double task_diff_baseline, task_diff_cur, task_diff_change;
    private boolean task_diff_first = true;

    private BluetoothAdapter adapter;
    private TGDevice tgDevice;

    public void connectDevice() {
        if (tgDevice != null
                && tgDevice.getState() != TGDevice.STATE_CONNECTING
                && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
            tgDevice.connect(true);
        }

    }

    public void closeDevice() {
        if (tgDevice != null) {
            tgDevice.close();
        }
    }

    private void initAttentionDevice() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "无法获取蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        tgDevice = new TGDevice(adapter, callbackHandler);
    }

    private void onBlinkChanged(int value) {
        mAttentionObservable.notifyBlinkChanged(value);
    }

    private void onAttentionChanged(int value) {
        mAttentionObservable.notifyAttentionChanged(value);
    }

    private Handler callbackHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            int arg1 = msg.arg1;
            switch (what) {
                case TGDevice.MSG_MODEL_IDENTIFIED:
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_MODEL_IDENTIFIED");
                    tgDevice.setBlinkDetectionEnabled(true);
                    tgDevice.setTaskDifficultyRunContinuous(true);
                    tgDevice.setTaskDifficultyEnable(true);
                    tgDevice.setTaskFamiliarityRunContinuous(true);
                    tgDevice.setTaskFamiliarityEnable(true);
                    tgDevice.setRespirationRateEnable(true); /// not allowed on EEG hardware, here to show the override message
                    break;
                case TGDevice.MSG_STATE_CHANGE:
                    String state;
                    switch (arg1) {
                        case TGDevice.STATE_IDLE:
                            state = "STATE_IDLE";
                            break;
                        case TGDevice.STATE_CONNECTING:
                            state = "STATE_CONNECTING";
                            break;
                        case TGDevice.STATE_CONNECTED:
                            state = "STATE_CONNECTING";
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            state = "STATE_NOT_FOUND. Check bluetooth!!!";
                            break;
                        case TGDevice.STATE_ERR_NO_DEVICE:
                            state = "STATE_ERR_NO_DEVICE. Check bluetooth!!!";
                            break;
                        case TGDevice.STATE_ERR_BT_OFF:
                            state = "STATE_ERR_BT_OFF. Turn on Bluetooth and try again!!!";
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            state = "STATE_DISCONNECTED.";
                            break;
                        default:
                            state = "" + arg1;
                            break;
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_STATE_CHANGE " + state);
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    if (subjectContactQuality_cnt >= 30 || arg1 != subjectContactQuality_last) {
                        String poor = arg1 == 0 ? " Good" : " Poor";
                        Log.d(TAG_TG_DEVICE, "handleMessage: MSG_POOR_SIGNAL " + arg1 + poor);
                        subjectContactQuality_cnt = 0;
                        subjectContactQuality_last = arg1;
                    } else {
                        subjectContactQuality_cnt++;
                    }
                    break;
                case TGDevice.MSG_RAW_DATA:
//                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_RAW_DATA ");
                    break;
                case TGDevice.MSG_ATTENTION:
                    onAttentionChanged(arg1);
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_ATTENTION " + arg1);
                    break;
                case TGDevice.MSG_MEDITATION:
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_MEDITATION " + arg1);
                    break;
                case TGDevice.MSG_EEG_POWER:
                    TGEegPower e = (TGEegPower) msg.obj;
                    if (e != null) {
                        Log.d(TAG_TG_DEVICE, "handleMessage: MSG_MEDITATION MSG_EEG_POWER: " + e.delta + " theta: " + e.theta + " alpha1: " + e.lowAlpha + " alpha2: " + e.highAlpha + "\n");
                    }
                    break;
                case TGDevice.MSG_FAMILIARITY:
                    task_famil_cur = (Double) msg.obj;
                    String familiarity;
                    if (task_famil_first) {
                        task_famil_first = false;
                        familiarity = "start";
                    } else {
                        /*
                         * calculate the percentage change from the previous sample
                		 */
                        task_famil_change = calcPercentChange(task_famil_baseline, task_famil_cur);
                        if (task_famil_change > 500.0 || task_famil_change < -500.0) {
                            familiarity = "excessive range";
                        } else {
                            familiarity = String.valueOf(task_famil_change) + "%";
                        }
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_FAMILIARITY " + familiarity);
                    task_famil_baseline = task_famil_cur;
                    break;
                case TGDevice.MSG_DIFFICULTY:
                    task_diff_cur = (Double) msg.obj;
                    String difficulty;
                    if (task_diff_first) {
                        task_diff_first = false;
                        difficulty = "start";
                    } else {
                        /*
                         * calculate the percentage change from the previous sample
                		 */
                        task_diff_change = calcPercentChange(task_diff_baseline, task_diff_cur);
                        if (task_diff_change > 500.0 || task_diff_change < -500.0) {
                            difficulty = "excessive range";
                        } else {
                            difficulty = task_diff_change + "%";
                        }
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_DIFFICULTY " + difficulty);
                    task_diff_baseline = task_diff_cur;
                    break;
                case TGDevice.MSG_ZONE:
                    String zone;
                    switch (arg1) {
                        case 3:
                            zone = " Elite";
                            break;
                        case 2:
                            zone = " Intermediate";
                            break;
                        case 1:
                            zone = " Beginner";
                            break;
                        case 0:
                        default:
                            zone = " relax and try to focus";
                            break;
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_ZONE " + zone);
                    break;
                case TGDevice.MSG_BLINK:
                    onBlinkChanged(arg1);
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_BLINK " + arg1);
                    break;
                case TGDevice.MSG_ERR_CFG_OVERRIDE:
                    String errorMsg;
                    switch (arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            errorMsg = "blinkDetect";
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            errorMsg = "Familiarity";
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            errorMsg = "Difficulty";
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            errorMsg = "Positivity";
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            errorMsg = "RESPIRATIONRATE";
                            break;
                        default:
                            errorMsg = arg1 + "";
                            break;
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_ERR_CFG_OVERRIDE " + errorMsg);
                    break;
                case TGDevice.MSG_ERR_NOT_PROVISIONED:
                    String notProvided;
                    switch (arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            notProvided = "blinkDetect";
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            notProvided = "Familiarity";
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            notProvided = "Difficulty";
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            notProvided = "Positivity";
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            notProvided = "RESPIRATIONRATE";
                            break;
                        default:
                            notProvided = arg1 + "";
                            break;
                    }
                    Log.d(TAG_TG_DEVICE, "handleMessage: MSG_ERR_NOT_PROVISIONED " + notProvided);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private double calcPercentChange(double baseline, double current) {
        double change;

        if (baseline == 0.0) baseline = 1.0; //don't allow divide by zero
        /*
         * calculate the percentage change
		 */
        change = current - baseline;
        change = (change / baseline) * 1000.0 + 0.5;
        change = Math.floor(change) / 10.0;
        return change;
    }

}
