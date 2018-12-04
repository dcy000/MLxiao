package com.example.han.referralproject.children.entertainment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.JokeModel;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.gzq.lib_core.utils.Handlers;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ChildEduJokesActivity extends BaseActivity {

    private TextView tvPoemTitle;
    private TextView tvAuthorAndDynasty;
    private RecyclerView rvPoemSentences;
    private ImageView ivReplay;
    private ImageView ivNext;

    private List<String> mSentences;
    private Adapter mAdapter;
    private LinearLayoutManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_jokes);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPoemTitle = (TextView) findViewById(R.id.ce_poem_details_tv_poem_title);
        tvAuthorAndDynasty = (TextView) findViewById(R.id.ce_poem_details_tv_poem_author_and_dynasty);
        rvPoemSentences = (RecyclerView) findViewById(R.id.ce_poem_details_rv_poem_sentences);
        ivReplay = (ImageView) findViewById(R.id.ce_poem_details_iv_replay);
        ivNext = (ImageView) findViewById(R.id.ce_poem_details_iv_next_normal);

        ivReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replay();
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        lm = new LinearLayoutManager(this);
        lm.setOrientation(OverFlyingLayoutManager.VERTICAL);
        mSentences = new ArrayList<>();
        mAdapter = new Adapter(mSentences);
        rvPoemSentences.setLayoutManager(lm);
        rvPoemSentences.setAdapter(mAdapter);
        fetchJokes();
    }

    private volatile boolean isPlaying;

    private void nextSentence() {
        if (mSentences.isEmpty()) {
            isPlaying = false;
            return;
        }
        int positionSelected = mAdapter.getPositionSelected();
        if (positionSelected == mSentences.size()) {
            isPlaying = false;
            //complete
            return;
        }
        isPlaying = true;
        positionSelected++;
        mAdapter.setPositionSelected(positionSelected);
        mAdapter.notifyDataSetChanged();
        rvPoemSentences.scrollToPosition(positionSelected);
        MLVoiceSynthetize.startSynthesize(mSentences.get(positionSelected),voiceListener);
    }

    private void replay() {
        if (mSentences.isEmpty()) {
            isPlaying = false;
            return;
        }
        isPlaying = true;
        mAdapter.setPositionSelected(0);
        mAdapter.notifyDataSetChanged();
        rvPoemSentences.scrollToPosition(0);
        MLVoiceSynthetize.startSynthesize(mSentences.get(0),voiceListener);
    }

    private void fetchJokes() {
        Handlers.bg().removeCallbacks(fetchJokesRunnable);
        Handlers.bg().post(fetchJokesRunnable);
    }

    private Runnable fetchJokesRunnable = new Runnable() {

        private Random random;

        @Override
        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf("讲个笑话");
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (results == null) {
                MLVoiceSynthetize.startSynthesize("没有笑话了， 让我再想想",voiceListener);
                return;
            }
            String jokesJson = results.get("resultJson");
            List<JokeModel> models = JokeModel.parseJokes(jokesJson);
            if (models == null || models.isEmpty()) {
                MLVoiceSynthetize.startSynthesize("没有笑话了， 让我再想想",voiceListener);
                return;
            }
            if (random == null) {
                random = new Random();
            }
            int size = models.size();
            int i = random.nextInt(size);
            mModel = models.get(i % size);
            Handlers.ui().post(showJokesRunnable);
        }
    };

    Runnable showJokesRunnable = new Runnable() {
        @Override
        public void run() {
            if (mModel == null) {
                return;
            }
            showJokes(mModel);
            replay();
        }
    };

    private void showJokes(JokeModel model) {
        if (model != null) {
            tvPoemTitle.setText("");
            tvAuthorAndDynasty.setText("");
            String content = model.getContent();
            String[] sentences = content.split("[、，。：:；？！,.;?!]");
            mSentences.clear();
            Collections.addAll(mSentences, sentences);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void next() {
        fetchJokes();
    }

    private JokeModel mModel;


    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<String> mModels;

        public Adapter(List<String> models) {
            mModels = models;
        }

        private volatile int positionSelected = 0;

        public int getPositionSelected() {
            return positionSelected;
        }

        public void setPositionSelected(int positionSelected) {
            this.positionSelected = positionSelected;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_sentence, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            String model = mModels.get(position);
            vh.tvSentence.setText(model);
            vh.tvSentence.setSelected(position == positionSelected);
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvSentence;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvSentence = (TextView) itemView.findViewById(R.id.ce_poem_details_tv_item_sentence);
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
        setDisableWakeup(false);
        super.onResume();
    }
    private MLSynthesizerListener voiceListener=new MLSynthesizerListener(){
        @Override
        public void onCompleted(SpeechError speechError) {
            super.onCompleted(speechError);
            if (isPlaying) {
                nextSentence();
            }
        }
    };
}
