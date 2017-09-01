package com.example.han.referralproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;

public class DiseaseShowAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private String[] diseaseArray;
    private ArrayList<Integer> mSelectList = new ArrayList<>();

    public DiseaseShowAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        diseaseArray = context.getResources().getStringArray(R.array.disease_type);
    }

    public String getMh(){
        if (mSelectList.size() == 0){
            return null;
        }
        StringBuilder mBuilder = new StringBuilder();
        for (Integer i : mSelectList){
            mBuilder.append(i + 1).append(",");
        }
        return mBuilder.substring(0, mBuilder.length() - 1).toString();
    }

    @Override
    public int getCount() {
        return diseaseArray.length;
    }

    @Override
    public Object getItem(int position) {
        return diseaseArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView mItemTv;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_disease_layout, null);
            mItemTv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(mItemTv);
        } else {
            mItemTv = (TextView) convertView.getTag();
        }
        mItemTv.setText(diseaseArray[position]);
        mItemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()){
                    mSelectList.remove(Integer.valueOf(position));
                    v.setSelected(false);
                } else {
                    mSelectList.add(Integer.valueOf(position));
                    v.setSelected(true);
                }
            }
        });
        return convertView;
    }
}
