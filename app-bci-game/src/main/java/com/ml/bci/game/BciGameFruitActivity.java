package com.ml.bci.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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

import com.ml.bci.game.common.utils.FragmentUtils;
import com.ml.bci.game.common.widget.recyclerview.AutoScrollHelper;
import com.ml.bci.game.common.widget.recyclerview.RandomLayoutManager;
import com.neurosky.thinkgear.TGDevice;

import java.util.ArrayList;

public class BciGameFruitActivity extends AppCompatActivity {

    private ConstraintLayout clRoot;
    private TextView tvFruitIndicator;
    private ProgressBar pbAttention;
    private TextView tvAttention;
    private RecyclerView rvFruits;

    private ImageView ivFruitChoose;
    private Adapter mAdapter;
    private AutoScrollHelper mAutoScrollHelper;
    private RandomLayoutManager mLayoutManager;

    private BciDeviceControllerFragment mBciDeviceControllerFragment;
    private TextView tvBlink;
    private TextView tvState;

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
    }

    private void initView() {
        clRoot = (ConstraintLayout) findViewById(R.id.bci_cl_root);
        tvFruitIndicator = (TextView) findViewById(R.id.bci_tv_fruit_indicator);
        pbAttention = (ProgressBar) findViewById(R.id.bci_pb_attention);
        tvAttention = (TextView) findViewById(R.id.bci_tv_attention);
        tvBlink = (TextView) findViewById(R.id.bci_tv_blink);
        tvState = (TextView) findViewById(R.id.bci_tv_state);
        ivFruitChoose = (ImageView) findViewById(R.id.bci_iv_choose_fruit);
        rvFruits = (RecyclerView) findViewById(R.id.bci_rv_fruits);


        mLayoutManager = new RandomLayoutManager();
        rvFruits.setLayoutManager(mLayoutManager);
        mLayoutManager.setOnSelectionListener(new RandomLayoutManager.OnSelectionListener() {
            @Override
            public void onSelect(View view, int position) {
                tvFruitIndicator.setText(getFruit(position));
            }

            @Override
            public void onUnselect() {
                tvFruitIndicator.setText("");
            }
        });
        rvFruits.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter();
        rvFruits.setAdapter(mAdapter);
        rvFruits.setEnabled(false);
        rvFruits.addOnItemTouchListener(onItemTouchListener);
        mAutoScrollHelper = new AutoScrollHelper();
        mAutoScrollHelper.attach(rvFruits);

        start();

    }

    private void start() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("开始游戏")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvFruitIndicator.setText("");
                        ivFruitChoose.setImageDrawable(null);
                        mBciDeviceControllerFragment.closeDevice();
                        initFruits();
                        mAdapter.notifyDataSetChanged();
//                rvFruits.smoothScrollToPosition(fruitResources.size() - 1);
                        mBciDeviceControllerFragment.connectDevice();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void initFruits() {
        fruitResources.clear();
        fruitResources.add(R.drawable.bci_ic_fruit_apple);
        fruitResources.add(R.drawable.bci_ic_fruit_peach);
        fruitResources.add(R.drawable.bci_ic_fruit_pear);
//        fruitResources.add(R.drawable.bci_ic_fruit_watermelon);
    }

    private void initAttention() {
        mBciDeviceControllerFragment = FragmentUtils.get(
                getSupportFragmentManager(),
                BciDeviceControllerFragment.class
        );
        mBciDeviceControllerFragment.register(new BciSignalObservable.Observer() {
            @Override
            public void onMessageChanged(Message message) {
                int what = message.what;
                int arg1 = message.arg1;
                String state = "";
                switch (what) {
                    case TGDevice.MSG_STATE_CHANGE:
                        switch (arg1) {
                            case TGDevice.STATE_IDLE:
                                state = "IDLE";
                                break;
                            case TGDevice.STATE_CONNECTING:
                                state = "CONNECTING";
                                break;
                            case TGDevice.STATE_CONNECTED:
                                state = "CONNECTING";
                                break;
                            case TGDevice.STATE_NOT_FOUND:
                                state = "NOT FOUND";
                                break;
                            case TGDevice.STATE_ERR_NO_DEVICE:
                                state = "NO DEVICE";
                                break;
                            case TGDevice.STATE_ERR_BT_OFF:
                                state = "BT OFF.";
                                break;
                            case TGDevice.STATE_DISCONNECTED:
                                state = "DISCONNECTED.";
                                break;
                            default:
                                state = "" + arg1;
                                break;
                        }
                        break;
                    case TGDevice.MSG_RAW_DATA:
                        state = String.valueOf(arg1);
                        break;
                }
                tvState.setText(state);
            }

            @Override
            public void onAttentionChanged(int intensity) {
                pbAttention.setProgress(intensity);
                tvAttention.setText(String.valueOf(intensity));
                if (!mAutoScrollHelper.isStarted()) {
                    mAutoScrollHelper.start();
                }
                mAutoScrollHelper.setOffsetX((int) (mAutoScrollHelper.baseOffset() * intensity / 100f));
            }

            @Override
            public void onBlinkChanged(int intensity) {
                tvBlink.setText("" + intensity);
                if (intensity > 50) {
                    animateDown();
                }
            }
        });
    }

    private void animateDown() {
        rvFruits.post(new Runnable() {
            @Override
            public void run() {
                final int selectedPosition = mLayoutManager.getSelectedPosition();
                if (selectedPosition < 0) {
                    return;
                }

                Log.d("fruit", "currentPosition: " + selectedPosition);
                Log.d("fruit", getFruit(selectedPosition));
                final int[] startLocation = new int[2];
                View view = mLayoutManager.getSelectedView();
                view.getLocationInWindow(startLocation);
                mLayoutManager.remove(selectedPosition);
                final Integer removed = fruitResources.remove(selectedPosition);
                mAdapter.notifyItemRemoved(removed);
                rvFruits.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animate(startLocation, selectedPosition, removed);
                    }
                }, 16);
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
                if (fruitResources.size() == 0) {
                    end();
                }
            }
        });
        animator.start();
    }

    private void end() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("再来一把!!!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvFruitIndicator.setText("");
                        ivFruitChoose.setImageDrawable(null);
//                        mBciDeviceControllerFragment.closeDevice();
                        initFruits();
                        mAdapter.notifyDataSetChanged();
//                        mBciDeviceControllerFragment.connectDevice();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
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

        public void onBind() {
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
            holder.onBind();
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
        if (mAutoScrollHelper != null) {
            mAutoScrollHelper.stop();
        }
        super.onDestroy();
    }
}
