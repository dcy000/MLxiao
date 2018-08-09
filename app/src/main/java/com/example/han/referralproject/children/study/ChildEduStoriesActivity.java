package com.example.han.referralproject.children.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.StoryModel;
import com.example.han.referralproject.new_music.Music;
import com.example.han.referralproject.new_music.MusicPlayActivity;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.medlink.danbogh.utils.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildEduStoriesActivity extends BaseActivity {

    private RadioGroup rgTabs;
    private RecyclerView rvStories;
    private List<StoryModel> mModels;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_stories);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("故  事  会");
        rgTabs = findViewById(R.id.ce_stories_rg_tabs);
        rvStories = findViewById(R.id.ce_stories_rv_stories);
        rgTabs.setOnCheckedChangeListener(onCheckedChangeListener);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mModels = new ArrayList<>();
        mAdapter = new Adapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvStories.setLayoutManager(lm);
        rvStories.setAdapter(mAdapter);
        rgTabs.check(R.id.ce_stories_rb_tab_classical_stories);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            StoryModel model = mModels.get(position);
            Intent intent = new Intent().setClass(
                    ChildEduStoriesActivity.this,
                    MusicPlayActivity.class
            ).putExtra("music", new Music(model.getPlayUrl()));
            startActivity(intent);
        }
    };

    private String keywords = null;

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.ce_stories_rb_tab_classical_stories:
                    keywords = "经典童话";
                    break;
                case R.id.ce_stories_rb_tab_girl_story:
                    keywords = "女孩故事";
                    break;
                case R.id.ce_stories_rb_tab_sleeping_stories:
                    keywords = "睡前故事";
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(keywords)) {
                fetchStories();
            }
        }
    };

    private void fetchStories() {
        if (TextUtils.isEmpty(keywords)) {
            return;
        }
        Handlers.bg().removeCallbacks(fetchStoriesRunnable);
        Handlers.bg().post(fetchStoriesRunnable);
    }

    private Runnable fetchStoriesRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf(keywords);
            if (isFinishing()) {
                return;
            }
            String storiesJson = results.get("resultJson");
            List<StoryModel> models = StoryModel.parseStories(storiesJson);
            if (models == null || models.isEmpty()) {
                ToastUtils.showShort("服务器繁忙");
                return;
            }
            mModels.clear();
            mModels.addAll(models);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<StoryModel> mModels;

        public Adapter(List<StoryModel> models) {
            mModels = models;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_story, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            StoryModel model = mModels.get(position);
            vh.tvNumber.setText(String.valueOf(position + 1));
            vh.tvStoryName.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvNumber;
        TextView tvStoryName;


        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvNumber = itemView.findViewById(R.id.ce_stories_tv_item_number);
            tvStoryName = itemView.findViewById(R.id.ce_stories_tv_item_story_name);
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
