package com.example.han.referralproject.children.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.PoemModel;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChildEduPoemDetailsActivity extends BaseActivity {

    private TextView tvPoemTitle;
    private TextView tvAuthorAndDynasty;
    private RecyclerView rvPoemSentences;
    private TextView tvReadAgain;
    private TextView tvLastSentence;
    private TextView tvNextSentence;
    private List<String> mSentences;
    private Adapter mAdapter;
    private PoemModel mPoemModel;
    private OverFlyingLayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_poem_details);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("唐  诗  宋  词");
        tvPoemTitle = (TextView) findViewById(R.id.ce_poem_details_tv_poem_title);
        tvAuthorAndDynasty = (TextView) findViewById(R.id.ce_poem_details_tv_poem_author_and_dynasty);
        rvPoemSentences = (RecyclerView) findViewById(R.id.ce_poem_details_rv_poem_sentences);
        tvReadAgain = (TextView) findViewById(R.id.ce_poem_details_tv_read_again);
        tvLastSentence = (TextView) findViewById(R.id.ce_poem_details_tv_last_sentence);
        tvNextSentence = (TextView) findViewById(R.id.ce_poem_details_tv_next_sentence);
        tvReadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAgain();
            }
        });
        tvLastSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSentence();
            }
        });
        tvNextSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSentence();
            }
        });

        lm = new OverFlyingLayoutManager(this);
        lm.setItemSpace(0);
        lm.setMinScale(1);
        lm.setMaxVisibleItemCount(2);
        lm.setOrientation(OverFlyingLayoutManager.VERTICAL);
        mSentences = new ArrayList<>();
        mAdapter = new Adapter(mSentences);
        rvPoemSentences.setLayoutManager(lm);
        rvPoemSentences.setAdapter(mAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            mPoemModel = intent.getParcelableExtra("poem");
        }
        if (mPoemModel != null) {
            tvPoemTitle.setText(mPoemModel.getTitle());
            tvAuthorAndDynasty.setText(mPoemModel.getAuthor() + "·" + mPoemModel.getDynasty());
            String content = mPoemModel.getContent();
            String[] sentences = content.split("。");
            mSentences.clear();
            Collections.addAll(mSentences, sentences);
            mAdapter.notifyDataSetChanged();
        }
        readAgain();
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
        mAdapter.notifyDataSetChanged();
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

    private void readAgain() {
        if (mSentences.isEmpty()) {
            isPlaying = false;
            return;
        }
        isPlaying = true;
        mAdapter.setPositionSelected(0);
        mAdapter.notifyDataSetChanged();
        rvPoemSentences.scrollToPosition(0);
        speak(mSentences.get(0));
    }

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
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
