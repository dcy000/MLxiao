package com.example.han.referralproject.children.study;

import android.content.Intent;
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
import com.example.han.referralproject.children.model.PoemModel;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChildEduPoemDetailsActivity extends BaseActivity {

    private TextView tvPoemTitle;
    private TextView tvAuthorAndDynasty;
    private RecyclerView rvPoemSentences;
    private ImageView ivReplay;
    private ImageView ivNext;

    private List<String> mSentences;
    private Adapter mAdapter;
    private LinearLayoutManager lm;
    private PoemModel mPoemModel;
    private ArrayList<PoemModel> mPoems;
    private volatile int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_poem_details);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPoemTitle = findViewById(R.id.ce_poem_details_tv_poem_title);
        tvAuthorAndDynasty = findViewById(R.id.ce_poem_details_tv_poem_author_and_dynasty);
        rvPoemSentences = findViewById(R.id.ce_poem_details_rv_poem_sentences);
        ivReplay = findViewById(R.id.ce_poem_details_iv_replay);
        ivNext = findViewById(R.id.ce_poem_details_iv_next_normal);

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
        Intent intent = getIntent();
        if (intent != null) {
            mPoems = intent.getParcelableArrayListExtra("poems");
            position = intent.getIntExtra("position", 0);
            if (mPoems != null
                    && mPoems.size() > 0
                    && position < mPoems.size()) {
                mPoemModel = mPoems.get(position);
            }
        }
        showPoem(mPoemModel);
        replay();
    }

    private void showPoem(PoemModel poemModel) {
        if (poemModel != null) {
            tvPoemTitle.setText(poemModel.getTitle());
            tvAuthorAndDynasty.setText(String.format("%s·%s", poemModel.getAuthor(), poemModel.getDynasty()));
            String content = poemModel.getContent().replaceAll(" ","");
            String[] sentences = content.split("[、，。；？！,.;?!]");
            mSentences.clear();
//            Collections.addAll(mSentences, sentences);
//            mAdapter.notifyDataSetChanged();
            mAdapter.setmModels(Arrays.asList(sentences));
            mSentences.addAll(Arrays.asList(sentences));
        }
    }

    @Override
    protected void onActivitySpeakFinish() {
        super.onActivitySpeakFinish();
        if (isPlaying) {
            nextSentence();
        }
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
        mAdapter.notifyItemChanged(positionSelected);
        rvPoemSentences.scrollToPosition(positionSelected);
        speak(mSentences.get(positionSelected));
    }

    private void lastSentence() {
        if (mSentences.isEmpty()) {
            isPlaying = false;
            return;
        }
        int positionSelected = mAdapter.getPositionSelected();
        if (positionSelected == 0) {
            //complete
            return;
        }
        positionSelected--;
        mAdapter.setPositionSelected(positionSelected);
        mAdapter.notifyDataSetChanged();
        rvPoemSentences.scrollToPosition(positionSelected);
        speak(mSentences.get(positionSelected));
    }

    private void replay() {

        if (mSentences.isEmpty()) {
            isPlaying = false;
            return;
        }
        isPlaying = true;
//        mAdapter.notifyDataSetChanged();
        mAdapter.setPositionSelected(0);
        rvPoemSentences.scrollToPosition(0);
        speak(mPoemModel.getTitle() + "," + mPoemModel.getAuthor() + "," + mPoemModel.getDynasty() + "," + mSentences.get(0));

    }

    private void next() {
        if (mPoems == null
                || mPoems.size() <= 1) {
            return;
        }
        int position = (this.position + 1) % mPoems.size();
        this.position = position;
        PoemModel model = mPoems.get(position);
        mPoemModel = model;
        showPoem(model);
        replay();
    }

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        public void setmModels(List<String> mModels) {
            this.mModels = mModels;
            notifyDataSetChanged();
        }

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
            tvSentence = itemView.findViewById(R.id.ce_poem_details_tv_item_sentence);
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
        super.onDestroy();
        stopListening();
        stopSpeaking();
    }
}
