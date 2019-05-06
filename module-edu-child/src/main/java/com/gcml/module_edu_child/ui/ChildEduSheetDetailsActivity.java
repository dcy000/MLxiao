package com.gcml.module_edu_child.ui;

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
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_edu_child.R;
import com.gcml.module_edu_child.bean.SheetModel;
import com.gcml.module_edu_child.bean.SongModel;
import com.gcml.module_edu_child.help.AutoLoadMoreHelper;
import com.gcml.module_edu_child.net.EduChildRepository;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/edu/child/sheet/details/activity")
public class ChildEduSheetDetailsActivity extends ToolbarBaseActivity {

    public static final String SHEET_CATEGORY_CHILD = "儿童歌曲";
    public static final String SHEET_CATEGORY_LULLABY = "摇篮曲";
    public static final String SHEET_CATEGORY_BABY = "胎教音乐";

    public static final String EXTRA_SHEET_CATEGORY = "sheetCategory";
    private String sheetCategory;

    private ImageView ivSheetCover;
    private TextView tvSheetName;
    private RecyclerView rvSongs;

    private SheetModel sheetModel;
    private List<SongModel> mModels;
    private Adapter mAdapter;
    private AutoLoadMoreHelper mAutoLoadMoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_sheet_details);
        mToolbar.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            sheetModel = intent.getParcelableExtra("sheet");
        }

        if (intent != null) {
            sheetCategory = intent.getStringExtra(EXTRA_SHEET_CATEGORY);
        } else {
            sheetCategory = SHEET_CATEGORY_CHILD;
        }

        ImageView ivTitle = findViewById(R.id.ce_common_iv_title);
        switch (sheetCategory) {
            case SHEET_CATEGORY_CHILD:
                ivTitle.setImageResource(R.drawable.ce_entertianment_ic_title_child_sheets);
                break;
            case SHEET_CATEGORY_LULLABY:
                ivTitle.setImageResource(R.drawable.ce_entertianment_ic_title_lullaby_sheets);
                break;
            case SHEET_CATEGORY_BABY:
                ivTitle.setImageResource(R.drawable.ce_entertianment_ic_title_baby_sheets);
                break;
        }

        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivSheetCover = findViewById(R.id.ce_sheet_details_iv_sheet_cover);
        tvSheetName = findViewById(R.id.ce_sheet_details_tv_sheet_name);
        rvSongs = findViewById(R.id.ce_sheet_details_rv_songs);

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
        mAutoLoadMoreHelper = new AutoLoadMoreHelper();
        mAutoLoadMoreHelper.attachToRecyclerView(rvSongs);
        mAutoLoadMoreHelper.setOnAutoLoadMoreListener(onAutoLoadMoreListener);
        if (sheetModel != null) {
            showLoading("加载中...");
            loadSheet(page, limit);
        }
        new EduChildRepository()
                .getChildEduSheetList(1, 12)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ArrayList<SheetModel>>() {
                    @Override
                    public void onNext(ArrayList<SheetModel> sheetModels) {
                        if (isFinishing()) {
                            return;
                        }
                        if (sheetModels == null || sheetModels.isEmpty()) {
                            ToastUtils.showShort("服务器繁忙");
                            return;
                        }
                        Iterator<SheetModel> iterator = sheetModels.iterator();
                        while (iterator.hasNext()) {
                            SheetModel model = iterator.next();
                            if (sheetCategory.equals(model.getFlag())) {
                                sheetModel = model;
                                onSheetModel(sheetModel);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private void onSheetModel(SheetModel sheetModel) {
        if (sheetModel != null) {
            Glide.with(this)
                    .load(sheetModel.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivSheetCover);
            tvSheetName.setText(sheetModel.getName());
            showLoading("加载中...");
            loadSheet(page, limit);
        }
    }

    private int page = 1;
    private int limit = 12;

    private AutoLoadMoreHelper.OnAutoLoadMoreListener onAutoLoadMoreListener = new AutoLoadMoreHelper.OnAutoLoadMoreListener() {
        @Override
        public void onAutoLoadMore(AutoLoadMoreHelper autoLoadMoreHelper) {
            if (autoLoadMoreHelper.isLoading()) {
                return;
            }
            autoLoadMoreHelper.setLoading(true);
            page++;
            loadSheet(page, limit);
        }
    };

    private void loadSheet(int page, int limit) {
        new EduChildRepository()
                .getChildEduSongListBySheetId(page, limit, sheetModel.getId(), 3, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<SongModel>>() {
                    @Override
                    public void onNext(List<SongModel> songModels) {
                        if (isFinishing()) {
                            return;
                        }
                        if (mAutoLoadMoreHelper != null) {
                            mAutoLoadMoreHelper.setLoading(false);
                        }
//                        mModels.clear();
                        mModels.addAll(songModels);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        if (isFinishing()) {
                            return;
                        }
                        if (mAutoLoadMoreHelper != null) {
                            mAutoLoadMoreHelper.setLoading(false);
                        }
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SongModel model = mModels.get(position);
            Routerfit.register(AppRouter.class).getMusicPlayProvider().searchAndPlayMusic(ChildEduSheetDetailsActivity.this,model.getName());
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
            tvNumber = itemView.findViewById(R.id.ce_sheet_details_tv_item_number);
            tvSongName = itemView.findViewById(R.id.ce_sheet_details_iv_item_song_name);
            tvSinger = itemView.findViewById(R.id.ce_sheet_details_tv_item_singer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
