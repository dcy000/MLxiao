package com.example.han.referralproject.settting.dialog;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.bean.VoicerBean;
import com.example.han.referralproject.speech.setting.IatSettings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lenovo on 2018/3/23.
 */

public class VoicerSetDialog extends DialogFragment {
    @BindView(R.id.rv_voices)
    RecyclerView rvVoices;
    Unbinder unbinder;
    private List<VoicerBean> data;
    private SharedPreferences mIatPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mIatPreferences = getActivity().getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE);
        View view = inflater.inflate(R.layout.dialog_voicer_set, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRV();
        return view;
    }

    private void initRV() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVoices.setLayoutManager(lm);
        rvVoices.setAdapter(new VoicerSetRVAdapter(initData()));
    }

    private List<VoicerBean> initData() {
//          <item>xiaoyu</item>
//        <item>nannan</item>
//        <item>xiaoyan</item>
//        <item>vixy</item>
//        <item>xiaoqi</item>
//        <item>vils</item>
        data = new ArrayList<>();
        data.add(new VoicerBean().setName("小燕").setVoicerName("xiaoyan").setCheck(true));
        data.add(new VoicerBean().setName("小宇").setVoicerName("xiaoyu"));
        data.add(new VoicerBean().setName("楠楠").setVoicerName("nannan"));
        data.add(new VoicerBean().setName("威雅").setVoicerName("vixy"));
        data.add(new VoicerBean().setName("小齐").setVoicerName("xiaoqi"));
        data.add(new VoicerBean().setName("万斯").setVoicerName("vils"));
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class VoicerSetRVAdapter extends RecyclerView.Adapter<VoicerSetRVAdapter.ViewHolder> {
        List<VoicerBean> data;

        public VoicerSetRVAdapter(List<VoicerBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View view = View.inflate(viewGroup.getContext(), R.layout.dialog_item_voicer_set, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final VoicerBean bean = data.get(position);

            int index = mIatPreferences.getInt("language_index", 0);
            holder.ivCheck.setImageResource(index == position ? R.drawable.ic_check : R.drawable.ic_no_check);
            holder.tvVoicer.setText(bean.name);

            holder.rvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setCheck(false);
                    }
                    data.get(position).setCheck(true);
                    notifyDataSetChanged();
                    saveData(position);
                }
            });
        }

        private void saveData(int position) {
            mIatPreferences.edit()
                    .putString("iat_language_preference", data.get(position).voicerName)
                    .putInt("language_index", position)
                    .commit();
            dismiss();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @OnClick(R.id.rv_item)
        public void onViewClicked() {
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_check)
            ImageView ivCheck;
            @BindView(R.id.tv_voicer)
            TextView tvVoicer;
            @BindView(R.id.rv_item)
            RelativeLayout rvItem;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }


    }
}
