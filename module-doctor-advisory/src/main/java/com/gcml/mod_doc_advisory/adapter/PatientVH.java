package com.gcml.mod_doc_advisory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.mod_doc_advisory.R;


/**
 * Created by han on 2016/12/27.
 */
public class PatientVH extends RecyclerView.ViewHolder {

    ImageView mImagine;
    public TextView mNameview;
    TextView mRoomId;
    public int mPosition;

    public Context mContext;


    public PatientVH(View itemView, final DoctorAdapter.OnItemClickListener onItemClistListener) {
        super(itemView);
        mImagine = itemView.findViewById(R.id.item_img);
        mNameview = itemView.findViewById(R.id.item_title);
        mRoomId = itemView.findViewById(R.id.item_desc);
    }
}
