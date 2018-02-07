package com.example.han.referralproject.radio;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.Music;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RadioActivity extends AppCompatActivity {


    private RecyclerView rvRadios;
    private LinearLayout llBack;
    private List<RadioEntity> entities = new ArrayList<>();
    private Adapter adapter;
    private TextView tvSelectedFm;
    private TextView tvSelectedName;
    private ImageView ivPauseOrPlay;
    private ImageView ivPrev;
    private ImageView ivNext;

    private MusicService musicService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bound = true;
            musicService=((MusicService.MusicBind) service).getService();
            //绑定好以后把要播放的文件set到服务中去
            musicService.setOnMusicPreparedListener(onMusicPreparedListener);
            musicService.setMusicResourse(music);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    MusicService.MusicPreParedOk onMusicPreparedListener = new MusicService.MusicPreParedOk() {
        @Override
        public void prepared(Music music) {
            musicService.play();
            Handlers.ui().post(updateUIAction);
        }
    };

    private Runnable updateUIAction = new Runnable() {
        @Override
        public void run() {
            RadioEntity entity = entities.get(adapter.getSelectedPosition());
            tvSelectedFm.setText(entity.getFm());
            tvSelectedName.setText(entity.getName());
            ivPauseOrPlay.setSelected(entity.isSelected());
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (musicService!=null) {
            musicService.release();
        }
    }

    @Override
    protected void onDestroy() {

        //取消绑定的服务
        if (serviceConnection != null&&bound)
            unbindService(serviceConnection);
        Handlers.bg().removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private boolean bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        ivPauseOrPlay = (ImageView) findViewById(R.id.old_iv_pause_or_play);
        ivPrev = (ImageView) findViewById(R.id.old_iv_prev);
        ivNext = (ImageView) findViewById(R.id.old_iv_next);
        tvSelectedFm = (TextView) findViewById(R.id.old_tv_selected_fm);
        tvSelectedName = (TextView) findViewById(R.id.old_tv_selected_name);
        rvRadios = (RecyclerView) findViewById(R.id.old_rv_radios);
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition == 0) {
                    return;
                }
                int position = selectedPosition - 1;
                adapter.setSelectedPosition(position);
                listener.onItemClick(position);
            }
        });

        ivPauseOrPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivPauseOrPlay.isSelected()) {
                    if (musicService != null) {
                        musicService.pause();
                    }
                } else {
                    if (musicService != null) {
                        musicService.play();
                    }
                }
                ivPauseOrPlay.setSelected(!ivPauseOrPlay.isSelected());
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition == adapter.getItemCount() - 1) {
                    return;
                }
                int position = selectedPosition + 1;
                adapter.setSelectedPosition(position);
                listener.onItemClick(position);
            }
        });

        rvRadios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(entities);
        adapter.setOnItemClickListener(listener);
        rvRadios.setAdapter(adapter);

        NetworkApi.getFM("6", "1", "12", new NetworkManager.SuccessCallback<List<RadioEntity>>() {
            @Override
            public void onSuccess(List<RadioEntity> response) {
                entities.addAll(response);
                adapter.notifyDataSetChanged();
                fetchFm();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });


    }

    private void fetchFm() {
        Handlers.bg().removeCallbacks(fetchAction);
        Handlers.bg().post(fetchAction);

    }

    private Music music;

    private Runnable fetchAction = new Runnable() {
        @Override

        public void run() {
            HashMap<String, String> results = QaApi.getQaFromXf(entities.get(adapter.getSelectedPosition()).getName() + entities.get(adapter.getSelectedPosition()).getFm());
            String audiopath = results.get("audiopath");
            music = new Music(audiopath);
            if (audiopath != null) {
                if (!bound) {
                    bindService(new Intent(RadioActivity.this, MusicService.class), serviceConnection, BIND_AUTO_CREATE);
                } else {
                    musicService.setOnMusicPreparedListener(onMusicPreparedListener);
                    musicService.setMusicResourse(music);
                }
            } else {
                T.show("服务器繁忙");
            }

        }
    };

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            adapter.setSelectedPosition(position);
            if (musicService != null) {
                musicService.pause();
            }
            fetchFm();
            RadioEntity entity = entities.get(position);
            tvSelectedFm.setText(entity.getFm());
            tvSelectedName.setText(entity.getName());
        }
    } ;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class Adapter extends RecyclerView.Adapter<VH> {

        List<RadioEntity> entities;

        public Adapter(List<RadioEntity> entities) {
            this.entities = entities;
        }

        private int selectedPosition = 0;

        public void setSelectedPosition(int selectedPosition) {
            if (selectedPosition == this.selectedPosition) {
                return;
            }
            entities.get(this.selectedPosition).setSelected(false);
            entities.get(selectedPosition).setSelected(true);
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.old_item_radio, viewGroup, false);
            return new VH(view, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(VH vh, int i) {
            RadioEntity entity = entities.get(i);
            vh.indicator.setVisibility(entity.isSelected()? View.VISIBLE : View.GONE);
            vh.itemView.setBackgroundColor(i % 2 == 0
                    ? Color.parseColor("#495462")
                    : Color.parseColor("#3D4856"));
            vh.tvName.setText(entity.getName());
            vh.tvFm.setText(entity.getFm());
        }

        @Override
        public int getItemCount() {
            return entities == null ? 0 : entities.size();
        }
    }

    public static class VH extends RecyclerView.ViewHolder {

        private final View indicator;
        private final TextView tvName;
        private final TextView tvFm;
        private final OnItemClickListener onItemClickListener;

        public VH(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            indicator = itemView.findViewById(R.id.old_radio_indicator);
            tvName = (TextView) itemView.findViewById(R.id.old_radio_name);
            tvFm = (TextView) itemView.findViewById(R.id.old_tv_fm);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VH.this.onItemClickListener != null) {
                        VH.this.onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
