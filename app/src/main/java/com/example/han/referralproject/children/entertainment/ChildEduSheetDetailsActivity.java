package com.example.han.referralproject.children.entertainment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.model.SheetModel;
import com.example.han.referralproject.children.model.SongModel;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.MusicUtils;
import com.medlink.danbogh.utils.T;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChildEduSheetDetailsActivity extends BaseActivity {

    private ImageView ivSheetCover;
    private TextView tvSheetName;
    private RecyclerView rvSongs;

    private SheetModel sheetModel;
    private List<SongModel> mModels;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_sheet_details);
        Intent intent = getIntent();
        if (intent != null) {
            sheetModel = intent.getParcelableExtra("sheet");
        }

        ImageView ivTitle = (ImageView) findViewById(R.id.ce_common_iv_title);
        ivTitle.setImageResource(R.drawable.ce_entertianment_ic_title_baby_sheets);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivSheetCover = (ImageView) findViewById(R.id.ce_sheet_details_iv_sheet_cover);
        tvSheetName = (TextView) findViewById(R.id.ce_sheet_details_tv_sheet_name);
        rvSongs = (RecyclerView) findViewById(R.id.ce_sheet_details_rv_songs);

        if (sheetModel != null) {
            Glide.with(this)
                    .load(sheetModel.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivSheetCover);
            tvSheetName.setText(sheetModel.getName());
        }

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mModels = new ArrayList<>();
        mAdapter = new Adapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvSongs.setLayoutManager(lm);
        rvSongs.setAdapter(mAdapter);

        if (sheetModel != null) {
            showLoadingDialog("加载中...");
            NetworkApi.getChildEduSongListBySheetId(
                    1,
                    12,
                    sheetModel.getId(),
                    3,
                    "",
                    new NetworkManager.SuccessCallback<List<SongModel>>() {
                        @Override
                        public void onSuccess(List<SongModel> response) {
                            if (isFinishing()) {
                                return;
                            }
                            hideLoadingDialog();
                            mModels.clear();
                            mModels.addAll(response);
                            mAdapter.notifyDataSetChanged();
                        }
                    },
                    new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            if (isFinishing()) {
                                return;
                            }
                            hideLoadingDialog();
                            T.show("服务器繁忙");
                        }
                    }
            );
        }
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SongModel model = mModels.get(position);
            MusicUtils.searchAndPlayMusic(ChildEduSheetDetailsActivity.this, model.getName());
        }
    };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<SongModel> mModels;

        public Adapter(List<SongModel> models) {
            mModels = models;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.ce_item_song, parent, false);
            return new VH(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int position) {
            SongModel model = mModels.get(position);
            vh.tvNumber.setText(String.valueOf(position + 1));
            vh.tvSinger.setText(model.getSinger());
            vh.tvSongName.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        TextView tvNumber;
        TextView tvSongName;
        TextView tvSinger;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvNumber = (TextView) itemView.findViewById(R.id.ce_sheet_details_tv_item_number);
            tvSongName = (TextView) itemView.findViewById(R.id.ce_sheet_details_iv_item_song_name);
            tvSinger = (TextView) itemView.findViewById(R.id.ce_sheet_details_tv_item_singer);
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
