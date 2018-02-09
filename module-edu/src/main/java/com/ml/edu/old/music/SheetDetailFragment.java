package com.ml.edu.old.music;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.han.referralproject.new_music.MusicUtils;
import com.ml.edu.R;
import com.ml.edu.data.ApiObserver;
import com.ml.edu.data.entity.SongEntity;
import com.ml.edu.domain.GetSongListUseCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SheetDetailFragment extends Fragment {

    public static void remove(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(SheetDetailFragment.class.getSimpleName());
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commitNowAllowingStateLoss();
        }
    }

    public static void addOrShow(FragmentManager fm, int id, int sheetId) {
        Fragment fragment = fm.findFragmentByTag(SheetDetailFragment.class.getSimpleName());
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment == null) {
            transaction.add(
                    id,
                    SheetDetailFragment.newInstance(sheetId),
                    SheetDetailFragment.class.getSimpleName()
            );
        } else {
            transaction.show(fragment);
        }
        transaction.commitNowAllowingStateLoss();
    }

    public static SheetDetailFragment newInstance(int sheetId) {
        SheetDetailFragment fragment = new SheetDetailFragment();
        Bundle args = new Bundle();
        args.putInt("sheetId", sheetId);
        fragment.setArguments(args);
        return fragment;
    }

    public SheetDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            sheetId = arguments.getInt("sheetId");
        }
    }

    private int sheetId;

    private RecyclerView rvSongs;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sheet_detail, container, false);

        view.findViewById(R.id.old_iv_home_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm != null) {
                    fm.beginTransaction()
                            .remove(SheetDetailFragment.this)
                            .commitNowAllowingStateLoss();
                }
            }
        });
        rvSongs = (RecyclerView) view.findViewById(R.id.old_rv_songs);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSongs.setHasFixedSize(true);
        adapter = new Adapter(entities);
        adapter.setOnItemClickListener(onItemClickListener);
        rvSongs.setAdapter(adapter);
        getSongListUseCase = new GetSongListUseCase();
        return view;
    }

    private GetSongListUseCase getSongListUseCase;

    @Override
    public void onStart() {
        super.onStart();
        getSheetList();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getSongListUseCase != null) {
            getSongListUseCase.dispose();
        }
    }

    private void getSheetList() {
        if (songListObserver == null) {
            songListObserver = new SongListObserver();
        }
        getSongListUseCase.execute(
                new GetSongListUseCase.Params(0, 1, 12),
                songListObserver
        );
    }

    private SongListObserver songListObserver;

    private class SongListObserver extends ApiObserver<List<SongEntity>> {
        @Override
        public void onNext(List<SongEntity> songEntities) {
            Iterator<SongEntity> iterator = songEntities.iterator();
            while (iterator.hasNext()) {
                SongEntity next = iterator.next();
                if (!String.valueOf(sheetId).equals(next.getSheetId())) {
                    iterator.remove();
                }
            }
            entities.addAll(songEntities);
            adapter.notifyItemRangeInserted(entities.size() - songEntities.size(), songEntities.size());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SongEntity entity = entities.get(position);
            Context context = getContext();
            if (context != null) {
                MusicUtils.searchAndPlayMusic(getContext(), entity.getName());
            }
        }
    };

    private List<SongEntity> entities = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class Adapter extends RecyclerView.Adapter<Holder> {

        private List<SongEntity> entities;

        public Adapter(List<SongEntity> entities) {
            this.entities = entities;
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        private LayoutInflater inflater;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.item_song, parent, false);
            return new Holder(view, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            SongEntity entity = entities.get(position);
            holder.tvSinger.setText(entity.getSinger());
            holder.tvSongName.setText(entity.getName());
            String number = String.valueOf(position + 1);
            holder.tvSongNumber.setText(number);
        }

        @Override
        public int getItemCount() {
            return entities == null ? 0 : entities.size();
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private final TextView tvSongNumber;
        private final TextView tvSongName;
        private final TextView tvSinger;
        private final OnItemClickListener onItemClickListener;

        public Holder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            tvSongNumber = (TextView) itemView.findViewById(R.id.entertainment_old_music_tv_song_number);
            tvSongName = (TextView) itemView.findViewById(R.id.entertainment_old_music_tv_song_name);
            tvSinger = (TextView) itemView.findViewById(R.id.entertainment_old_music_tv_singer);
            this.onItemClickListener = onItemClickListener;
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
