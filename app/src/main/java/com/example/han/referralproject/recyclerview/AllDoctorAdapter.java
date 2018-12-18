package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.AllDoctor;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by han on 2017/8/29.
 */
public class AllDoctorAdapter extends RecyclerView.Adapter<AllDoctorAdapter.Holder> {

    List<AllDoctor> mListPat;
    private Context context;

    public AllDoctorAdapter(List<AllDoctor> mListGood, Context context) {
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
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.water_fall_item, null);
        return new Holder(view, onItemClistListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
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
                .load(mListPat.get(position).docter_photo)
                .placeholder(R.drawable.common_ic_avatar_placeholder)
                .error(R.drawable.common_ic_avatar_placeholder)
                .tag(context)
                .fit()
                .into(holder.mImagine);
        //    holder.mImagine.setImageResource(R.drawable.common_ic_avatar_placeholder);
        holder.mNameview.setText(mListPat.get(position).doctername);
        holder.mRoomId.setText(mListPat.get(position).department);


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

    public class Holder extends RecyclerView.ViewHolder {

        ImageView mImagine;
        public TextView mNameview;
        TextView mRoomId;
        public int mPosition;

        public Context mContext;


        public Holder(View itemView, final AllDoctorAdapter.OnItemClickListener onItemClistListener) {
            super(itemView);
            mImagine = itemView.findViewById(R.id.item_img);
            mNameview = itemView.findViewById(R.id.item_title);
            mRoomId = itemView.findViewById(R.id.item_desc);
        }
    }
}
