package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.YuYueInfo;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by han on 2017/12/20.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {


    List<YuYueInfo> mList;
    private Context context;

    public HistoryAdapter(List<YuYueInfo> mListInfo, Context context) {
        this.mList = mListInfo;
        this.context = context;

    }

    @Override
    public HistoryVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.water_fall_history, null);


        return new HistoryVH(view, onItemClistListener);
    }

    @Override
    public void onBindViewHolder(HistoryVH holder, final int position) {

        holder.mContext = context;
        holder.mPosition = position;

        holder.mTextView.setText(StringUtils.substringBeforeLast(mList.get(position).getStart_time(), ":"));
        holder.mTextView1.setText(StringUtils.substringBeforeLast(mList.get(position).getEnd_time(), ":"));


    }

    @Override
    public int getItemCount() {

        if (null == mList) {
            return 0;

        } else {
            return mList.size();
        }
    }


    private OnItemClickListener onItemClistListener;

    public interface OnItemClickListener {
        void onItemClick(int postion);
    }

    public void setOnItemClistListener(OnItemClickListener itemClistListener) {
        this.onItemClistListener = itemClistListener;
    }


    public class HistoryVH extends RecyclerView.ViewHolder {


        public int mPosition;
        public Context mContext;
        public TextView mTextView;
        public TextView mTextView1;


        public HistoryVH(View itemView) {
            super(itemView);
        }

        public HistoryVH(View itemView, final HistoryAdapter.OnItemClickListener onItemClistListener) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.history_time);
            mTextView1 = itemView.findViewById(R.id.history_time1);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onItemClistListener) {
                        onItemClistListener.onItemClick(mPosition);
                    }
                }
            });

        }


    }


}
