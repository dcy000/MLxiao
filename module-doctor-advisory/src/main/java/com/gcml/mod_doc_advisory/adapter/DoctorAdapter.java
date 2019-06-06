package com.gcml.mod_doc_advisory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.common.imageloader.ImageLoader;
import com.gcml.mod_doc_advisory.R;
import com.gcml.mod_doc_advisory.bean.Docter;

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

        ImageLoader.with(context)
                .load(mListPat.get(position).getDocter_photo())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.mImagine);
        holder.mNameview.setText(mListPat.get(position).getDoctername());
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
