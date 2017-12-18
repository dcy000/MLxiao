package com.example.han.referralproject.video;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.han.referralproject.recyclerview.LinearLayoutDividerItemDecoration;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.UiUtils;
import com.medlink.danbogh.utils.Utils;

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
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideos.setLayoutManager(layoutManager);
        rvVideos.addItemDecoration(new GridViewDividerItemDecoration(30,52));
        rvVideos.setAdapter(adapter);
        fetchVideos(position);
    }

    private void fetchVideos(int position) {
        switch (position) {
            case 0:
                provideVideos("hypertension", "健康讲堂");
                break;
            case 1:
                provideVideos(/*"stroke"*/"opera", "曲艺天地");
                break;
            case 2:
                provideVideos("lifetip", "生活助手");
                break;
            case 3:
                provideVideos(/*"palsy"*/"cartoon", "动画片");
                break;
            default:
                break;
        }
    }

    public static final String BASE_URL = "http://oyptcv2pb.bkt.clouddn.com/";
    public static final String TITLE_FORMAT = "%s %d";

    private void provideVideos(String type, String extra) {
        List<VideoDetailEntity> entities = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i == 7 && type.equals("opera")) {
                break;
            }
            String url = BASE_URL + type + i  + ".mp4";
            String title = String.format(Locale.CHINA, TITLE_FORMAT, extra, i + 1);
            entities.add(new VideoDetailEntity(url, title));
        }
        showVideos(entities);
    }

    private void showVideos(List<VideoDetailEntity> entities) {
        videos = entities;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        public void onBindViewHolder(final Holder holder, int position) {
            final VideoDetailEntity entity = videos.get(position);
            holder.tvTitle.setText(entity.getTitle());
            holder.ivThumbnail.setImageResource(R.drawable.ic_thumbnail_placeholder);
            Handlers.bg().post(new Runnable() {
                @Override
                public void run() {
                    final Bitmap thumbnail = Utils.createVideoThumbnail(entity.getUrl(), UiUtils.pt(455), UiUtils.pt(255));
                    Handlers.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.ivThumbnail.setImageBitmap(thumbnail);
                        }
                    });
                }
            });
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