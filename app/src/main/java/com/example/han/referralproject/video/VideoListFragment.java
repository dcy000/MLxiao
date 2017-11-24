package com.example.han.referralproject.video;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoListFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    private int position;

    public VideoListFragment() {

    }

    public static VideoListFragment newInstance(int position) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_list, container, false);
    }

    private RecyclerView rvVideos;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvVideos = (RecyclerView) view.findViewById(R.id.rv_videos);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideos.setLayoutManager(layoutManager);
        rvVideos.setAdapter(adapter);
        fetchVideos(position);
    }

    private void fetchVideos(int position) {
        switch (position) {
            case 0:
                provideVideos("hypertension", "高血压");
                break;
            case 1:
                provideVideos(/*"stroke"*/"psychosis", "中风");
                break;
            case 2:
                provideVideos("psychosis", "精神病");
                break;
            case 3:
                provideVideos(/*"palsy"*/"hypertension", "脑瘫");
                break;
            default:
                break;
        }
    }

    public static final String BASE_URL = "http://oyptcv2pb.bkt.clouddn.com/";
    public static final String TITLE_FORMAT = "%s 第%d讲";

    private void provideVideos(String type, String extra) {
        List<VideoDetailEntity> entities = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String url = BASE_URL + type + i + ".mp4";
            String title = String.format(Locale.CHINA, TITLE_FORMAT, extra, i);
            entities.add(new VideoDetailEntity(url, title));
        }
        showVideos(entities);
    }

    private void showVideos(List<VideoDetailEntity> entities) {
        videos = entities;
        adapter.notifyDataSetChanged();
    }

    private List<VideoDetailEntity> videos;

    private Adapter adapter = new Adapter();

    private class Adapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_health_video, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            VideoDetailEntity entity = videos.get(position);
            holder.tvTitle.setText(entity.getTitle());
            holder.ivThumbnail.setImageResource(R.drawable.icon_test_01);
        }

        @Override
        public int getItemCount() {
            return videos == null ? 0 : videos.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        private TextView tvTitle;

        public Holder(final View itemView) {
            super(itemView);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_video_thumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_video_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    VideoDetailEntity entity = videos.get(position);
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("url", entity.getUrl());
                    context.startActivity(intent);
                }
            });
        }
    }
}