package com.gcml.module_edu_child.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.CenterScrollListener;
import com.gcml.common.widget.OverFlyingLayoutManager;
import com.gcml.module_edu_child.R;
import com.gcml.module_edu_child.bean.SheetModel;
import com.gcml.module_edu_child.net.EduChildRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class ChildEduSheetListActivity extends ToolbarBaseActivity {

    public static final String SHEET_CATEGORY_CHILD = "儿童歌曲";
    public static final String SHEET_CATEGORY_LULLABY = "摇篮曲";
    public static final String SHEET_CATEGORY_BABY = "胎教音乐";

    public static final String EXTRA_SHEET_CATEGORY = "sheetCategory";

    private String sheetCategory;

    private RecyclerView rvSheets;
    private Adapter mAdapter;
    private List<SheetModel> mModels;
    private ImageView ivTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_sheet_list);
        mToolbar.setVisibility(View.GONE);
        final Intent intent = getIntent();
        if (intent != null) {
            sheetCategory = intent.getStringExtra(EXTRA_SHEET_CATEGORY);
        } else {
            sheetCategory = SHEET_CATEGORY_CHILD;
        }
        ivTitle = findViewById(R.id.ce_common_iv_title);
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
            default:
                sheetCategory = SHEET_CATEGORY_CHILD;
                ivTitle.setImageResource(R.drawable.ce_entertianment_ic_title_child_sheets);
                break;
        }
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvSheets = findViewById(R.id.ce_entertainment_rv_sheets);
        rvSheets.addOnScrollListener(new CenterScrollListener());
        OverFlyingLayoutManager lm = new OverFlyingLayoutManager(this);
        lm.setMinScale(0.6f);
        lm.setItemSpace(0);
        lm.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        lm.setOnPageChangeListener(onPageChangeListener);
        mModels = new ArrayList<>();
        mAdapter = new Adapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvSheets.setLayoutManager(lm);
        rvSheets.setAdapter(mAdapter);
        if (mAdapter.getItemCount() > 1) {
            rvSheets.scrollToPosition(1);
        }
        showLoading("加载中...");
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
                        mModels.clear();
                        mModels.addAll(sheetModels);
                        Iterator<SheetModel> iterator = mModels.iterator();
                        while (iterator.hasNext()) {
                            SheetModel model = iterator.next();
                            if (!sheetCategory.equals(model.getFlag())) {
                                iterator.remove();
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

    private OverFlyingLayoutManager.OnPageChangeListener onPageChangeListener =
            new OverFlyingLayoutManager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    rvSheets.scrollToPosition(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

    private OnItemClickListener onItemClickListener =
            new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent();
                    intent.setClass(
                            ChildEduSheetListActivity.this,
                            ChildEduSheetDetailsActivity.class
                    ).putExtra("sheet", mModels.get(position));
                    startActivity(intent);
                }
            };

    private interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class Adapter extends RecyclerView.Adapter<VH> {
        private List<SheetModel> mModels;

        public Adapter(List<SheetModel> models) {
            mModels = models;
        }

        public void setModels(List<SheetModel> models) {
            mModels = models;
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
            Context context = vh.itemView.getContext();
            Glide.with(context)
                    .load(mModels.get(position).getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(vh.ivIndicator);
            vh.tvIndicator.setText(mModels.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mModels == null ? 0 : mModels.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView ivIndicator;
        TextView tvIndicator;
        private OnItemClickListener onItemClickListener;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            ivIndicator = itemView.findViewById(R.id.ce_home_iv_item_indicator);
            tvIndicator = itemView.findViewById(R.id.ce_home_tv_item_indicator);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
