package com.example.module_child_edu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.module_child_edu.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.recycleview.CenterScrollListener;
import com.gzq.lib_core.recycleview.OverFlyingLayoutManager;
import com.iflytek.wake.MLVoiceWake;

import java.util.ArrayList;
import java.util.List;

public class ChildEduHomeActivity extends ToolbarBaseActivity {

    private RecyclerView rvItems;
    private Adapter mAdapter;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.ce_activity_home;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        rvItems = (RecyclerView) findViewById(R.id.ce_home_rv_items);
        rvItems.addOnScrollListener(new CenterScrollListener());
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(this);
        lm.setMinScale(0.6f);
        lm.setItemSpace(0);
        lm.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        lm.setOnPageChangeListener(onPageChangeListener);
        mAdapter = new Adapter();
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvItems.setLayoutManager(lm);
        rvItems.setAdapter(mAdapter);
        rvItems.scrollToPosition(1);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private OverFlyingLayoutManager.OnPageChangeListener onPageChangeListener =
            new OverFlyingLayoutManager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    rvItems.scrollToPosition(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

    private List<Class<? extends Activity>> mActivityClasses = new ArrayList<>();

    {
        mActivityClasses.add(ChildEduCartoonActivity.class);
        mActivityClasses.add(ChildEduStudyActivity.class);
        mActivityClasses.add(ChildEduEntertainmentActivity.class);
    }

    private OnItemClickListener onItemClickListener =
            new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent();
                    intent.setClass(
                            ChildEduHomeActivity.this,
                            mActivityClasses.get(position % 3)
                    );
                    startActivity(intent);
                }
            };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {

        private List<Integer> imageReses;
        private List<String> texts;

        public Adapter() {
            imageReses = new ArrayList<Integer>();
            texts = new ArrayList<>();
            imageReses.add(R.drawable.ce_home_ic_video);
            imageReses.add(R.drawable.ce_home_ic_edu);
            imageReses.add(R.drawable.ce_home_ic_music);
            texts.add("动画片");
            texts.add("儿童幼教");
            texts.add("儿童娱乐");
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_child, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            vh.ivIndicator.setImageResource(imageReses.get(position));
            vh.tvIndicator.setText(texts.get(position));
        }

        @Override
        public int getItemCount() {
            return texts == null ? 0 : texts.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView ivIndicator;
        TextView tvIndicator;
        private OnItemClickListener onItemClickListener;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ivIndicator = (ImageView) itemView.findViewById(R.id.ce_home_iv_item_indicator);
            tvIndicator = (TextView) itemView.findViewById(R.id.ce_home_tv_item_indicator);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
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
        MLVoiceWake.startWakeUp(null);
        super.onResume();
    }
}
