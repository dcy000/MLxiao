package com.example.han.referralproject.children.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.PoemModel;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.gzq.lib_core.utils.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildEduPoemListActivity extends BaseActivity {

    private RecyclerView rvPoems;
    private ArrayList<PoemModel> mModels;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_poem_list);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvPoems = (RecyclerView) findViewById(R.id.ce_poems_rv_poems);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mModels = new ArrayList<>();
        mAdapter = new Adapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvPoems.setLayoutManager(lm);
        rvPoems.setAdapter(mAdapter);

        fetchPoems();
    }

    private void fetchPoems() {
        Handlers.bg().removeCallbacks(fetchPoemsRunnable);
        Handlers.bg().post(fetchPoemsRunnable);
    }

    private Runnable fetchPoemsRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFinishing()) {
                return;
            }
            HashMap<String, String> results = QaApi.getQaFromXf("唐诗宋词");
            if (results == null) {
                return;
            }
            String poemsJson = results.get("resultJson");
            if (TextUtils.isEmpty(poemsJson)) {
                return;
            }
            List<PoemModel> models = PoemModel.parsePoems(poemsJson);
            if (models == null || models.isEmpty()) {
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

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            PoemModel model = mModels.get(position);
            Intent intent = new Intent();
            intent.setClass(
                    ChildEduPoemListActivity.this,
                    ChildEduPoemDetailsActivity.class
            ).putExtra("poems", mModels).putExtra("position", position);
            startActivity(intent);
        }
    };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<PoemModel> mModels;

        public Adapter(List<PoemModel> models) {
            mModels = models;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_poem, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            PoemModel model = mModels.get(position);
            vh.tvNumber.setText(String.valueOf(position + 1));
            vh.tvPoemAuthor.setText(model.getAuthor());
            vh.tvPoemTitle.setText(model.getTitle());
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvNumber;
        TextView tvPoemTitle;
        TextView tvPoemAuthor;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvNumber = (TextView) itemView.findViewById(R.id.ce_poems_tv_item_number);
            tvPoemTitle = (TextView) itemView.findViewById(R.id.ce_poems_tv_item_poem_title);
            tvPoemAuthor = (TextView) itemView.findViewById(R.id.ce_poems_tv_item_author);
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

    @Override
    protected void onDestroy() {
        Handlers.bg().removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
