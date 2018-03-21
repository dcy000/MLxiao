package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.example.han.referralproject.R;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by han on 2017/8/29.
 */
public class DoctorAdapter extends RecyclerView.Adapter<PatientVH> {

    List<Docter> mListPat;
    private Context context;

    public DoctorAdapter(List<Docter> mListGood, Context context) {
        this.mListPat = mListGood;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        if (null == mListPat) {
            return 0;

        } else {
            return mListPat.size();
        }
    }

    @Override
    public PatientVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.water_fall_item, null);
        return new PatientVH(view, onItemClistListener);
    }

    @Override
    public void onBindViewHolder(PatientVH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClistListener) {
                    onItemClistListener.onItemClick(position);
                }
            }
        });
        holder.mContext = context;
        holder.mPosition = position;
        Picasso.with(context)
                .load(R.drawable.hq_ic_half_doctor)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(context)
                .fit()
                .into(holder.mImagine);
        //    holder.mImagine.setImageResource(R.drawable.avatar_placeholder);
        holder.mNameview.setText("半个医生客服");
        holder.mRoomId.setText(mListPat.get(position).getDepartment());


    }

    private OnItemClickListener onItemClistListener;

    public interface OnItemClickListener {
        void onItemClick(int postion);
    }

    public void setOnItemClistListener(OnItemClickListener itemClistListener) {
        this.onItemClistListener = itemClistListener;
    }

    public OnItemClickListener getOnItemClistListener() {
        return onItemClistListener;
    }
}
