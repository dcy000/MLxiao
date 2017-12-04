package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;

/**
 * Created by gzq on 2017/11/27.
 */

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.SymViewHolder>{
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    public SymptomAdapter(Context context, ArrayList<String> mData){
        this.mData=mData;
        mInflater=LayoutInflater.from(context);

    }
    @Override
    public SymViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SymViewHolder(mInflater.inflate(R.layout.item_symptom_layout, null));
    }

    @Override
    public void onBindViewHolder(SymViewHolder holder, final int position) {
        holder.symptomTv.setText(mData.get(position));
        holder.symptomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SymViewHolder extends RecyclerView.ViewHolder{
        public TextView symptomTv;
        public SymViewHolder(View itemView) {
            super(itemView);
            symptomTv = (TextView) itemView.findViewById(R.id.tv_item_symptom);
        }
    }
    private OnClickListener listener;
    public void setOnItemClickListener(OnClickListener listener){
        this.listener=listener;
    }
    public interface OnClickListener{
        void onItemClick(View view,int position);
    }
}
