package com.gcml.module_edu_child.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.recommend.bean.get.Music;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_edu_child.R;
import com.gcml.module_edu_child.bean.StoryModel;
import com.iflytek.utils.QaApi;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildEduStories2Activity extends ToolbarBaseActivity {

    private RecyclerView rvStories;
    private ArrayList<StoryModel> mModels;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_ce_stories2);
        mToolbar.setVisibility(View.GONE);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvStories = findViewById(R.id.ce_stories_rv_stories);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mModels = new ArrayList<>();
        mAdapter = new Adapter();
        rvStories.setLayoutManager(lm);
        rvStories.setAdapter(mAdapter);
        fetchStories();
    }

    private View.OnClickListener storyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvStories.getChildAdapterPosition(v);
            StoryModel model = mModels.get(position);
            Routerfit.register(AppRouter.class).skipMusicPlayActivity( new Music(model.getPlayUrl()));
        }
    };

    private String[] keywords = new String[]{"经典童话", "女孩故事", "睡前故事"};

    private int position;

    private void fetchStories() {
        position += 1;
        position %= 3;
        if (TextUtils.isEmpty(keywords[position])) {
            return;
        }
        Handlers.bg().removeCallbacks(fetchStoriesRunnable);
        Handlers.bg().post(fetchStoriesRunnable);
    }

    private Runnable fetchStoriesRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf(keywords[position]);
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

    private class StoryHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvTitle;

        public StoryHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.ce_story_tv_item_number);
            tvTitle = itemView.findViewById(R.id.ce_story_tv_item_poem_title);
            itemView.setOnClickListener(storyOnClickListener);
        }

        public void onBind(int position) {
            StoryModel storyModel = mModels.get(position);
            tvNumber.setText(String.valueOf(position+1));
            tvTitle.setText(storyModel.getName());
        }
    }

    private class Adapter extends RecyclerView.Adapter<StoryHolder> {

        @Override
        public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_story2, parent, false);
            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(StoryHolder vh, int position) {
            vh.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    @Override
    protected void onDestroy() {
        Handlers.bg().removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
