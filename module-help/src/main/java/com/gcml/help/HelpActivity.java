package com.gcml.help;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.ui.UiUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "help/help/activity")
public class HelpActivity extends ToolbarBaseActivity {

    private RecyclerView rvHelp;

    private RecyclerView.Adapter outerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mTitleText.setText("帮  助  中  心");

        rvHelp = findViewById(R.id.rvHelp);

        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2, GridLayoutManager.VERTICAL, false);
        rvHelp.setLayoutManager(layoutManager);
        rvHelp.setAdapter(outerAdapter = new HelpListAdapter());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = outerAdapter.getItemViewType(position);
                if (viewType == TYPE_HEADER) {
                    return 2;
                }

                if (viewType == TYPE_CONTENT_VIDEO) {
                    return 2;
                }

                return 1;
            }
        });

    }


    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT_VIDEO = 1;
    public static final int TYPE_CONTENT_TEXT = 2;

    private ArrayList<Object> items = new ArrayList<>();

    {
        items.add(new HeaderEntity("视频教程"));
        VideoListEntity videoListEntity = new VideoListEntity();
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_pressure, "血压检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_sugar, "血糖检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_temperate, "体温检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_weight, "体重检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_oxygen, "血氧检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_pulse, "心电检测教程"));
        videoListEntity.entities.add(new VideoEntity("", R.drawable.help_ic_three_in_one, "三合一检测教程"));
        items.add(videoListEntity);

        items.add(new HeaderEntity("常见问题"));
        items.add(new TextEntity("关于修改头像。", "点击“我的”类目、进入“我的资料”，点击头像。接收短信验证码，输入正确的验证码，录入人脸，人脸保存。"));
        items.add(new TextEntity("关于更换手机号。", ""));
        items.add(new TextEntity("如何更改密码？", ""));
        items.add(new TextEntity("关于修改头像。", ""));
        items.add(new TextEntity("关于修改头像。", ""));
        items.add(new TextEntity("关于修改头像。", ""));

    }

    public class HeaderEntity {
        private String label;

        public HeaderEntity(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public class VideoListEntity {
        private int position;
        private ArrayList<VideoEntity> entities = new ArrayList<>();

        public VideoListEntity() {
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ArrayList<VideoEntity> getEntities() {
            return entities;
        }

        public void setEntities(ArrayList<VideoEntity> entities) {
            this.entities = entities;
        }
    }

    public class VideoEntity {
        private String url;
        private int imgRes;
        private String label;

        public VideoEntity(String url, int imgRes, String label) {
            this.url = url;
            this.imgRes = imgRes;
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public class TextEntity {
        private String helpText;
        private String helpDetailText;

        public TextEntity(String helpText, String helpDetailText) {
            this.helpText = helpText;
            this.helpDetailText = helpDetailText;
        }

        public String getHelpText() {
            return helpText;
        }

        public void setHelpText(String helpText) {
            this.helpText = helpText;
        }

        public String getHelpDetailText() {
            return helpDetailText;
        }

        public void setHelpDetailText(String helpDetailText) {
            this.helpDetailText = helpDetailText;
        }
    }

    public class MultiTypeVH extends RecyclerView.ViewHolder {

        public MultiTypeVH(View itemView) {
            super(itemView);
        }

        public void onBind(int position) {

        }
    }

    private class HeaderVH extends MultiTypeVH {

        private final TextView tvLabel;

        public HeaderVH(View itemView) {
            super(itemView);
            tvLabel = ((TextView) itemView.findViewById(R.id.tvLabel));
        }

        @Override
        public void onBind(int position) {
            HeaderEntity entity = (HeaderEntity) items.get(position);
            tvLabel.setText(entity.label);
        }
    }

    private VideoListEntity videos;

    private class VideoListVH extends MultiTypeVH {

        private final RecyclerView rvVideos;
        private RecyclerView.Adapter adapter;

        public VideoListVH(View itemView) {
            super(itemView);
            rvVideos = (RecyclerView) itemView.findViewById(R.id.rvVideos);
            rvVideos.setLayoutManager(new LinearLayoutManager(
                    itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvVideos.setAdapter(adapter = new RecyclerView.Adapter<VideoVH>() {
                @NonNull
                @Override
                public VideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    View view = inflater.inflate(R.layout.item_help_content_video, parent, false);
                    return new VideoVH(view);
                }

                @Override
                public void onBindViewHolder(@NonNull VideoVH holder, int position) {
                    holder.onBind(position);
                }

                @Override
                public int getItemCount() {
                    return videos == null || videos.entities == null ? 0 : videos.entities.size();
                }
            });
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            videos = (VideoListEntity) items.get(position);
            adapter.notifyDataSetChanged();
        }

        private class VideoVH extends RecyclerView.ViewHolder {

            private final ImageView ivMask;
            private final TextView tvLabel;


            public VideoVH(View itemView) {
                super(itemView);
                ivMask = itemView.findViewById(R.id.ivMask);
                tvLabel = itemView.findViewById(R.id.tvLabel);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            public void onBind(int position) {
                setMarginIfNeed(position);
                VideoEntity entity = videos.entities.get(position);
                ivMask.setImageResource(entity.getImgRes());
                tvLabel.setText(entity.getLabel());
            }

            private int smallMargin = UiUtils.pt(10);
            private int largeMargin = UiUtils.pt(50);

            private void setMarginIfNeed(int position) {
                RecyclerView.LayoutParams layoutParams =
                        (RecyclerView.LayoutParams) itemView.getLayoutParams();
                int leftMargin = position == 0 ? largeMargin : smallMargin;
                int rightMargin = position == videos.entities.size() - 1 ? largeMargin : smallMargin;
                boolean changed = false;
                if (layoutParams.leftMargin != leftMargin) {
                    layoutParams.leftMargin = leftMargin;
                    changed = true;
                }
                if (layoutParams.rightMargin != rightMargin) {
                    layoutParams.rightMargin = rightMargin;
                    changed = true;
                }
                if (changed) {
                    itemView.setLayoutParams(layoutParams);
                }
            }
        }
    }

    private class TextVH extends MultiTypeVH {

        private final TextView tvHelp;

        public TextVH(View itemView) {
            super(itemView);
            tvHelp = ((TextView) itemView.findViewById(R.id.tvHelp));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextEntity textEntity = (TextEntity) items.get(getAdapterPosition());
                    Routerfit.register(AppRouter.class)
                            .skipHelpDetailActivity(textEntity.helpText, textEntity.helpDetailText);
                }
            });
        }

        @Override
        public void onBind(int position) {
            setPaddingIfNeed(position);

            TextEntity textEntity = (TextEntity) items.get(position);
            tvHelp.setText(textEntity.getHelpText());
        }

        int padding = UiUtils.pt(40);

        public void setPaddingIfNeed(int position) {
            int paddingLeft = 0;
            int paddingRight = 0;
            if (position % 2 == 1) {
                paddingLeft = padding;
            } else {
                paddingRight = padding;
            }
            itemView.setPadding(paddingLeft, 0, paddingRight, 0);
        }
    }

    private class HelpListAdapter extends RecyclerView.Adapter<MultiTypeVH> {

        @NonNull
        @Override
        public MultiTypeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_HEADER) {
                View view = inflater.inflate(R.layout.item_help_header, parent, false);
                return new HeaderVH(view);
            }
            if (viewType == TYPE_CONTENT_VIDEO) {
                View view = inflater.inflate(R.layout.item_help_content_video_list, parent, false);
                return new VideoListVH(view);
            }
            if (viewType == TYPE_CONTENT_TEXT) {
                View view = inflater.inflate(R.layout.item_help_content_text, parent, false);
                return new TextVH(view);
            }
            return new MultiTypeVH(new View(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull MultiTypeVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            Object obj = items.get(position);
            if (obj instanceof HeaderEntity) {
                return TYPE_HEADER;
            }
            if (obj instanceof VideoListEntity) {
                return TYPE_CONTENT_VIDEO;
            }
            if (obj instanceof TextEntity) {
                return TYPE_CONTENT_TEXT;
            }
            return -1;
        }
    }
}
