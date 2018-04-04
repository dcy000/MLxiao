package com.example.han.referralproject.children.entertainment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.JokeModel;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.medlink.danbogh.utils.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ChildEduEntertainmentActivity extends BaseActivity {

    private RecyclerView rvItems;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_entertainment);
        rvItems = (RecyclerView) findViewById(R.id.ce_entertainment_rv_items);
        mAdapter = new Adapter();
        mAdapter.setOnItemClickListener(onItemClickListener);
        GridLayoutManager lm = new GridLayoutManager(this, 2);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(lm);
        rvItems.setAdapter(mAdapter);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private OnItemClickListener onItemClickListener =
            new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 3) {
                        tellJokes();
                    } else {
                        routeToSheetListActivity(position);
                    }
                }
            };

    private void routeToSheetListActivity(int position) {
        String sheetCategory = null;
        switch (position) {
            case 0:
                sheetCategory = ChildEduSheetListActivity.SHEET_CATEGORY_CHILD;
                break;
            case 1:
                sheetCategory = ChildEduSheetListActivity.SHEET_CATEGORY_LULLABY;
                break;
            case 2:
                sheetCategory = ChildEduSheetListActivity.SHEET_CATEGORY_BABY;
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(sheetCategory)) {
            Intent intent = new Intent();
            intent.setClass(
                    ChildEduEntertainmentActivity.this,
                    ChildEduSheetDetailsActivity.class
            );
            intent.putExtra(
                    ChildEduSheetDetailsActivity.EXTRA_SHEET_CATEGORY,
                    sheetCategory
            );
            startActivity(intent);
        }
    }

    private void tellJokes() {
        Intent intent = new Intent(this, ChildEduJokesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
//        Handlers.bg().removeCallbacks(fetchJokesRunnable);
//        Handlers.bg().post(fetchJokesRunnable);
    }

    private Runnable fetchJokesRunnable = new Runnable() {
        private Random random;

        @Override
        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf("讲个笑话");
            if (isFinishing()) {
                return;
            }
            if (results == null) {
                speak("没有笑话了， 让我再想想");
                return;
            }
            String jokesJson = results.get("resultJson");
            List<JokeModel> models = JokeModel.parseJokes(jokesJson);
            if (models == null || models.isEmpty()) {
                speak("没有笑话了， 让我再想想");
                return;
            }

            int size = models.size();
            if (size == 1) {
                speak(models.get(0).getContent());
                return;
            }
            if (random == null) {
                random = new Random();
            }
            int i = random.nextInt(size);
            speak(models.get(i % size).getContent());
        }
    };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<Integer> texts;

        public Adapter() {
            texts = new ArrayList<>();
            texts.add(R.drawable.ce_entertainment_ic_child);
            texts.add(R.drawable.ce_entertainment_ic_lullaby);
            texts.add(R.drawable.ce_entertainment_ic_baby);
            texts.add(R.drawable.ce_entertainment_ic_jokes);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_entertainment, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            vh.ivIndicator.setImageResource(texts.get(position));
        }

        @Override
        public int getItemCount() {
            return texts == null ? 0 : texts.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivIndicator;
        private OnItemClickListener onItemClickListener;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ivIndicator = (ImageView) itemView.findViewById(R.id.ce_entertainment_iv_item);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
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
}
