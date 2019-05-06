package com.example.module_control_volume.dialog;

import android.app.DialogFragment;
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

import com.example.module_control_volume.R;
import com.example.module_control_volume.bean.TalkTypeBean;
import com.gcml.common.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/28.
 */

public class TalkTypeDialog extends DialogFragment {
    RecyclerView rvVoices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_voicer_set, container, false);
        rvVoices=view.findViewById(R.id.rv_voices);
        initRv();
        return view;

    }

    private void initRv() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVoices.setLayoutManager(lm);
        rvVoices.setAdapter(new TalkTypeRVAdpater(initData()));
    }

    private List<TalkTypeBean> initData() {
        List<TalkTypeBean> data = new ArrayList<>();
        Boolean xunhuan = (Boolean) SharedPreferencesUtils.getParam(getActivity(), "yuyin", true);
        TalkTypeBean bean = new TalkTypeBean();
        bean.check = !xunhuan;
        bean.name = "点击聊天";
        data.add(bean);

        TalkTypeBean bean2 = new TalkTypeBean();
        bean2.check = xunhuan;
        bean2.name = "实时聊天";
        data.add(bean2);


        return data;
    }

    class TalkTypeRVAdpater extends RecyclerView.Adapter<TalkTypeRVAdpater.ViewHolder> {

        List<TalkTypeBean> data;

        public TalkTypeRVAdpater(List<TalkTypeBean> data) {
            this.data = data;
        }

        @Override
        public TalkTypeRVAdpater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(viewGroup.getContext(), R.layout.dialog_item_voicer_set, null);
            return new TalkTypeRVAdpater.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TalkTypeRVAdpater.ViewHolder holder, final int position) {
            TalkTypeBean bean = data.get(position);
            holder.tvVoicer.setText(bean.name);

            holder.ivCheck.setImageResource(bean.check ? R.drawable.ic_check : R.drawable.ic_no_check);
            holder.rvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).check = false;
                    }
                    data.get(position).check = true;
                    notifyDataSetChanged();
                    saveData(position);
                    dismiss();
                }
            });


        }

        private void saveData(int position) {
            boolean object = position == 1;
            SharedPreferencesUtils.setParam(getActivity(), "yuyin", object);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCheck;
            TextView tvVoicer;
            RelativeLayout rvItem;

            ViewHolder(View view) {
                super(view);
                ivCheck=view.findViewById(R.id.iv_check);
                tvVoicer=view.findViewById(R.id.tv_voicer);
                rvItem=view.findViewById(R.id.rv_item);
            }
        }
    }

}
