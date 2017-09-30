package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.bean.YzInfoBean;

import java.util.ArrayList;

public class SymptomResultAdapter extends RecyclerView.Adapter<SymptomResultAdapter.ResultHolder> {
    private LayoutInflater mInflater;
    private ArrayList<SymptomResultBean> mDataList;

    public SymptomResultAdapter(Context context, ArrayList<SymptomResultBean> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    @Override
    public SymptomResultAdapter.ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(mInflater.inflate(R.layout.item_analyse_layout, null));
    }

    @Override
    public void onBindViewHolder(SymptomResultAdapter.ResultHolder holder, int position) {
        SymptomResultBean itemBean = mDataList.get(position);
        holder.titleTv.setText(itemBean.con);
        holder.probabilityTv.setText(String.format("患病概率 %.2f",itemBean.probability * 100) + "%");
        holder.dealTv.setText(itemBean.deal);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        public TextView probabilityTv;
        public TextView dealTv;

        public ResultHolder(View itemView){
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_title);
            probabilityTv = (TextView) itemView.findViewById(R.id.tv_item_probability);
            dealTv = (TextView) itemView.findViewById(R.id.tv_item_deal);
        }
    }
}
