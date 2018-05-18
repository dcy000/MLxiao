package com.example.han.referralproject.yiyuan.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.yiyuan.bean.MainTiZHiDialogBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/5/18.
 */

public class TiZhiJianCeDialog extends DialogFragment {
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    Unbinder unbinder;
    List<MainTiZHiDialogBean> data;

    interface DialogItemClickListener {
        void onItemClick(String name);
    }

    DialogItemClickListener listener;

    public void setListener(DialogItemClickListener listener, List<MainTiZHiDialogBean> data) {
        this.listener = listener;
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_jiance_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRV();
        return view;
    }

    private void initRV() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvType.setLayoutManager(lm);
        rvType.setAdapter(new MainTiZhiSetRVAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class MainTiZhiSetRVAdapter extends RecyclerView.Adapter<MainTiZhiSetRVAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View view = View.inflate(viewGroup.getContext(), R.layout.dialog_main_tizhi, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final MainTiZHiDialogBean bean = data.get(position);
            holder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(bean.name);
                    }
                    dismiss();
                }
            });
            holder.tvItem.setText(bean.name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_item)
            TextView tvItem;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
