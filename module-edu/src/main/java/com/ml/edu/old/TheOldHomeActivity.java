package com.ml.edu.old;

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

import com.ml.edu.OldRouter;
import com.ml.edu.R;
import com.ml.edu.old.music.SheetListFragment;

import java.util.ArrayList;
import java.util.List;

public class TheOldHomeActivity extends AppCompatActivity {

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, TheOldHomeActivity.class);
        return intent;
    }

    private RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_old_home);
        rvItems = (RecyclerView) findViewById(R.id.old_rv_items);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvItems.setLayoutManager(lm);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvItems);
        rvItems.setAdapter(new Adapter(onItemClickListener));
        findViewById(R.id.old_tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (position == 2) {
                OldRouter.routeToOldMusicActivity(TheOldHomeActivity.this);
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
            if (position != 1) {
                holder.ivMedia.setScaleX(0.6f);
                holder.ivMedia.setScaleY(0.6f);
            }
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

        public Holder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            ivMedia = (ImageView) itemView.findViewById(R.id.old_iv_media);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
