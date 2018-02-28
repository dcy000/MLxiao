package com.example.han.referralproject.radio;


import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class RadioActivity extends BaseActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener {
    private static final String TAG = "radio";
    private RecyclerView rvRadios;
    private List<RadioEntity> entities = new ArrayList<>();
    private Adapter adapter;
    private TextView tvSelectedFm;
    private TextView tvSelectedName;
    private ImageView ivPauseOrPlay;
    private ImageView ivPrev;
    private ImageView ivNext;

    private Runnable updateUIAction = new Runnable() {
        @Override
        public void run() {
            if (isBuffering) {
                showLoadingDialog("缓冲中...");
            } else {
                hideLoadingDialog();
            }
            RadioEntity entity = entities.get(adapter.getSelectedPosition());
            tvSelectedFm.setText(entity.getFm());
            tvSelectedName.setText(entity.getName());
            ivPauseOrPlay.setSelected(entity.isSelected());
        }
    };

    @Override
    protected void onDestroy() {
        Handlers.bg().removeCallbacksAndMessages(null);
        Handlers.ui().removeCallbacksAndMessages(null);
        audioHandler.post(new Runnable() {
            @Override
            public void run() {
                audioHandler.removeCallbacksAndMessages(null);
                stopPlay();
            }
        });
        super.onDestroy();
    }

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

        findViewById(R.id.old_iv_radio_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivPauseOrPlay.setSelected(isPlaying);

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition == 0) {
                    return;
                }
                adapter.setSelectedPosition(selectedPosition - 1);
            }
        });

        ivPauseOrPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                ivPauseOrPlay.setSelected(isPlaying);
                if (isPlaying) {
                    fetchFm();
                } else {
                    Handlers.bg().removeCallbacks(fetchAction);
                    stopPlay();
                }
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition > adapter.getItemCount() - 1) {
                    return;
                }
                adapter.setSelectedPosition(selectedPosition + 1);
            }
        });

        rvRadios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(entities);
        adapter.setOnItemSelectionChangedListener(listener);
        rvRadios.setAdapter(adapter);

        NetworkApi.getFM("6", "1", "12", new NetworkManager.SuccessCallback<List<RadioEntity>>() {
            @Override
            public void onSuccess(List<RadioEntity> response) {
                entities.addAll(response);
                adapter.notifyDataSetChanged();
                adapter.setSelectedPosition(0);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    private OnItemSelectionChangedListener listener = new OnItemSelectionChangedListener() {
        @Override
        public void onItemSelectionChanged(int newPosition, int lastPosition) {
            if (newPosition < 0 || newPosition >= entities.size()) {
                ivPauseOrPlay.performClick();
                return;
            }
            fetchFm();
        }
    };

    private void fetchFm() {
        Handlers.bg().removeCallbacks(fetchAction);
        Handlers.bg().post(fetchAction);
    }


    private Runnable fetchAction = new Runnable() {
        @Override
        public void run() {
            onBuffering();
            RadioEntity entity = entities.get(adapter.getSelectedPosition());
            HashMap<String, String> results = QaApi.getQaFromXf(
                    entity.getName());
            final String audiopath = results.get("audiopath");
            Log.d("fm", "audiopath: " + audiopath);
            if (!TextUtils.isEmpty(audiopath)) {
                audioHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        startPlay(audiopath);
                    }
                });
            } else {
                speak("主人，未搜索到该频道，请更换频道再试");
            }

        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(int newPosition, int lastPosition);
    }

    public static class Adapter extends RecyclerView.Adapter<VH> implements OnItemClickListener {

        List<RadioEntity> entities;

        public Adapter(List<RadioEntity> entities) {
            this.entities = entities;
        }

        private int selectedPosition = -1;

        public void setSelectedPosition(int selectedPosition) {
            if (selectedPosition == this.selectedPosition
                    || selectedPosition < 0
                    || selectedPosition >= getItemCount()) {
                return;
            }
            int lastPosition = this.selectedPosition;
            this.selectedPosition = selectedPosition;
            if (lastPosition >= 0 && lastPosition < getItemCount()) {
                entities.get(lastPosition).setSelected(false);
            }
            entities.get(selectedPosition).setSelected(true);
            notifyDataSetChanged();
            if (mOnItemSelectionChangedListener != null) {
                mOnItemSelectionChangedListener.onItemSelectionChanged(selectedPosition, lastPosition);
            }
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        private OnItemSelectionChangedListener mOnItemSelectionChangedListener;

        @Override
        public void onItemClick(int position) {
            setSelectedPosition(position);
        }

        public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener onItemSelectionChangedListener) {
            this.mOnItemSelectionChangedListener = onItemSelectionChangedListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.old_item_radio, viewGroup, false);
            return new VH(view, this);
        }

        @Override
        public void onBindViewHolder(VH vh, int i) {
            RadioEntity entity = entities.get(i);
            vh.indicator.setVisibility(entity.isSelected() ? View.VISIBLE : View.GONE);
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

    private volatile IjkMediaPlayer mPlayer;

    private volatile boolean isPlaying;

    private Handler audioHandler;

    {
        HandlerThread handlerThread = new HandlerThread("audio", Process.THREAD_PRIORITY_AUDIO);
        handlerThread.start();
        audioHandler = new Handler(handlerThread.getLooper());
    }

    public void startPlay(String url) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        onPlaying();
        mPlayer = new IjkMediaPlayer();
        try {
            mPlayer.setDataSource(url);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnInfoListener(this);
            mPlayer.setOnSeekCompleteListener(this);
            mPlayer.prepareAsync();
        } catch (Throwable e) {
            e.printStackTrace();
            stopPlay();
            onStopped();
        }
    }

    private volatile boolean isBuffering;

    private void onBuffering() {
        isBuffering = true;
        isPlaying = true;
        Handlers.ui().post(updateUIAction);
    }

    private void onPlaying() {
        isBuffering = false;
        isPlaying = true;
        Handlers.ui().post(updateUIAction);
    }

    private void onStopped() {
        isBuffering = false;
        isPlaying = false;
        Handlers.ui().post(new Runnable() {
            @Override
            public void run() {
                if (isBuffering) {
                    showLoadingDialog("缓冲中...");
                } else {
                    hideLoadingDialog();
                }
                ivPauseOrPlay.setSelected(isPlaying);
            }
        });
    }

    private void stopPlay() {
        if (mPlayer != null) {
            audioHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                }
            });
        }
    }

    @Override
    public void onPrepared(IMediaPlayer player) {
        Log.d(TAG, "onPrepared: ");
        player.start();
    }

    @Override
    public boolean onError(IMediaPlayer player, int i, int i1) {
        Log.d(TAG, "onError: " + i + " " + i1);
        stopPlay();
        onStopped();
        return true;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer player, int i) {
        Log.d(TAG, "onBufferingUpdate: " + i);
    }

    @Override
    public void onCompletion(IMediaPlayer player) {
        Log.d(TAG, "onCompletion: ");
        stopPlay();
        onStopped();
    }

    @Override
    public boolean onInfo(IMediaPlayer player, int i, int i1) {
        Log.d(TAG, "onInfo: " + i + " " + i1);
        return true;
    }

    @Override
    public void onSeekComplete(IMediaPlayer player) {
        Log.d(TAG, "onSeekComplete: ");
    }
}
