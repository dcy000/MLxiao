package com.ml.edu.old.music;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ml.edu.R;
import com.ml.edu.data.ApiObserver;
import com.ml.edu.data.entity.SheetEntity;
import com.ml.edu.domain.GetSheetListUseCase;

import java.util.ArrayList;
import java.util.List;

public class SheetListFragment extends Fragment {

    public static void addOrShow(FragmentManager fm, int id) {
        Fragment fragment = fm.findFragmentByTag(SheetListFragment.class.getSimpleName());
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment == null) {
            transaction.add(
                    id,
                    SheetListFragment.newInstance(),
                    SheetListFragment.class.getSimpleName()
            );
        } else {
            transaction.show(fragment);
        }
        transaction.commitNowAllowingStateLoss();
    }

    private static SheetListFragment newInstance() {
        return new SheetListFragment();
    }

    private RecyclerView rvSheets;

    private Adapter adapter;

    private ArrayList<SheetEntity> entities;


    public SheetListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sheet_list, container, false);
        rvSheets = (RecyclerView) view.findViewById(R.id.old_music_rv_sheets);
        GridLayoutManager lm = new GridLayoutManager(
                getContext(),
                4,
                LinearLayoutManager.VERTICAL,
                false
        );
        rvSheets.setLayoutManager(lm);
        rvSheets.setHasFixedSize(true);
        entities = new ArrayList<>();
        adapter = new Adapter(entities);
        adapter.setOnItemClickListener(onItemClickListener);
        rvSheets.setAdapter(adapter);
        getSheetListUseCase = new GetSheetListUseCase();
        getSheetList();
        return view;
    }

    private void getSheetList() {
        if (sheetListObserver == null) {
            sheetListObserver = new SheetListObserver();
        }
        getSheetListUseCase.execute(
                new GetSheetListUseCase.Params("", 1, 12),
                sheetListObserver
        );
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SheetEntity entity = entities.get(position);
            SheetDetailFragment.addOrShow(getActivity().getSupportFragmentManager(), android.R.id.content, entity.getId());
        }
    };

    private SheetListObserver sheetListObserver;

    private class SheetListObserver extends ApiObserver<List<SheetEntity>> {
        @Override
        public void onNext(List<SheetEntity> sheetEntities) {
            entities.addAll(sheetEntities);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private GetSheetListUseCase getSheetListUseCase;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int ITEM_TYPE_SHEETS_HEADER = 1;
        public static final int ITEM_TYPE_SHEET = 2;

        private List<SheetEntity> entities;

        public Adapter(List<SheetEntity> entities) {
            this.entities = entities;
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public int getItemViewType(int position) {
//            if (position == 0) {
//                return ITEM_TYPE_SHEETS_HEADER;
//            }
            return ITEM_TYPE_SHEET;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lm instanceof GridLayoutManager) {
                final GridLayoutManager glm = (GridLayoutManager) lm;
                glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int viewType = getItemViewType(position);
                        if (ITEM_TYPE_SHEETS_HEADER == viewType) {
                            return glm.getSpanCount();
                        }
                        if (ITEM_TYPE_SHEET == viewType) {
                            return 1;
                        }
                        return 1;
                    }
                });
            }
        }

        private LayoutInflater inflater;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            if (ITEM_TYPE_SHEETS_HEADER == viewType) {
                View view = inflater.inflate(R.layout.item_sheets_header, parent, false);
                return new SheetsHeaderHolder(view);
            }
            if (ITEM_TYPE_SHEET == viewType) {
                View view = inflater.inflate(R.layout.item_sheet, parent, false);
                return new SheetHolder(view, onItemClickListener);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SheetEntity entity = entities.get(position);
            if (holder instanceof SheetsHeaderHolder) {
                SheetsHeaderHolder sheetsHeaderHolder = (SheetsHeaderHolder) holder;
                sheetsHeaderHolder.tvSheetsCategory.setText(entity.getCategory());
                return;
            }
            if (holder instanceof SheetHolder) {
                SheetHolder sheetHolder = (SheetHolder) holder;
                Glide.with(sheetHolder.ivSheetCover.getContext())
                        .load(entity.getImageUrl())
                        .into(sheetHolder.ivSheetCover);
                sheetHolder.tvSheetName.setText(entity.getName());
                sheetHolder.tvSheetListenerCount.setText("9999人听过");
            }
        }

        @Override
        public int getItemCount() {
            return entities == null ? 0 : entities.size();
        }
    }

    public static class SheetsHeaderHolder extends RecyclerView.ViewHolder {

        private final TextView tvSheetsCategory;

        public SheetsHeaderHolder(View itemView) {
            super(itemView);
            tvSheetsCategory = (TextView) itemView.findViewById(R.id.old_music_sheets_header_catetogy);
        }

    }

    public static class SheetHolder extends RecyclerView.ViewHolder {

        private final ImageView ivSheetCover;
        private final TextView tvSheetName;
        private final TextView tvSheetListenerCount;

        private final OnItemClickListener onItemClickListener;

        public SheetHolder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            ivSheetCover = (ImageView) itemView.findViewById(R.id.old_music_iv_sheet_cover);
            tvSheetName = (TextView) itemView.findViewById(R.id.old_music_tv_sheet_name);
            tvSheetListenerCount = (TextView) itemView.findViewById(R.id.old_music_tv_sheet_listener_count);
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
