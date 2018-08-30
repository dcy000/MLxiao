package com.ml.edu.old;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ml.edu.OldRouter;
import com.ml.edu.R;
import com.ml.edu.common.widget.recycleyview.CenterScrollListener;
import com.ml.edu.common.widget.recycleyview.OverFlyingLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class TheOldHomeActivity extends AppCompatActivity {

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, TheOldHomeActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private RecyclerView rvItems;
    private TextView tvIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_old_home);
        rvItems = findViewById(R.id.old_rv_items);
        tvIndicator = findViewById(R.id.old_tv_home_indicator);
        rvItems.addOnScrollListener(new CenterScrollListener());
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(0.6f, 0, OverFlyingLayoutManager.HORIZONTAL);
        lm.setAngle(0);
        lm.setOnPageChangeListener(new OverFlyingLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tvIndicator.setText(indicatorTexts[position % 3]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rvItems.setLayoutManager(lm);
        rvItems.setAdapter(new Adapter(onItemClickListener));
        findViewById(R.id.old_iv_home_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lm.scrollToPosition(1);
        tvIndicator.setText(indicatorTexts[1]);
    }

    private final String[] indicatorTexts = new String[]{"收\n音\n机", "视\n频\n播\n放", "音\n乐\n播\n放"};

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (position == 2) {
                OldRouter.routeToOldMusicActivity(TheOldHomeActivity.this);
                return;
            }

            if (position == 1) {
                try {
                    Class<?> aClass = Class.forName("com.example.han.referralproject.video.TheOldVideoActivity");
                    Intent intent = new Intent();
                    intent.setClass(TheOldHomeActivity.this, aClass);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (position == 0) {
                try {
                    Class<?> aClass = Class.forName("com.example.han.referralproject.radio.RadioActivity");
                    Intent intent = new Intent();
                    intent.setClass(TheOldHomeActivity.this, aClass);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static class Adapter extends RecyclerView.Adapter<Holder> {
        private final OnItemClickListener onItemClickListener;
        private LayoutInflater inflater;

        private List<Integer> imageResources;

        public Adapter(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            imageResources = new ArrayList<>();
            imageResources.add(R.drawable.old_ic_home_radio);
            imageResources.add(R.drawable.old_ic_home_video);
            imageResources.add(R.drawable.old_ic_home_music);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.item_media, parent, false);
            return new Holder(view, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.ivMedia.setImageResource(imageResources.get(position));
        }

        @Override
        public int getItemCount() {
            return imageResources == null ? 0 : imageResources.size();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private final OnItemClickListener onItemClickListener;

        private ImageView ivMedia;

        public Holder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            ivMedia = itemView.findViewById(R.id.old_iv_media);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Holder.this.onItemClickListener != null) {
                        Holder.this.onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
