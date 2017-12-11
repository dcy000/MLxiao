package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.SymptomBean;

import java.util.ArrayList;

public class SymptomRecyclerAdapter extends RecyclerView.Adapter<SymptomRecyclerAdapter.SymptomHolder>{
    private LayoutInflater mInflater;
    private ArrayList<SymptomBean> mDataList;
    private ArrayList<Integer> mSelectList = new ArrayList<>();
    private int select_index=-1;//记录选中的位置

    public SymptomRecyclerAdapter(Context context, ArrayList<SymptomBean> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    public String getResult(){
//        if (mSelectList.size() == 0){
//            return null;
//        }
//        StringBuilder mBuilder = new StringBuilder();
//        for (Integer i : mSelectList){
//            mBuilder.append(i + 1).append(",");
//        }
//        return mBuilder.substring(0, mBuilder.length() - 1).toString();
        return mDataList.get(select_index).id;
    }

    @Override
    public SymptomRecyclerAdapter.SymptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SymptomHolder(mInflater.inflate(R.layout.item_symptom_layout, null));
    }

    @Override
    public void onBindViewHolder(SymptomRecyclerAdapter.SymptomHolder holder,final int position) {
        holder.symptomTv.setText(mDataList.get(position).name);
//        if(mDataList.get(position).isSelected){
//            holder.symptomTv.setSelected(true);
//        }else{
//            holder.symptomTv.setSelected(false);
//        }
        holder.symptomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v.isSelected()){
//                    mSelectList.remove(Integer.valueOf(position));
//                    v.setSelected(false);
//                } else {
//                    mSelectList.add(Integer.valueOf(position));
//                    v.setSelected(true);
//                }
                selectTag.getTag(mDataList.get(position),position);
//                for(int i=0;i<mDataList.size();i++){
//                    if(i==position){
//                        mDataList.get(i).isSelected=true;
//                        select_index=position;
//                    }else{
//                        mDataList.get(i).isSelected=false;
//                    }
//                }
//                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class SymptomHolder extends RecyclerView.ViewHolder {
        public TextView symptomTv;

        public SymptomHolder(View itemView){
            super(itemView);
            symptomTv = (TextView) itemView.findViewById(R.id.tv_item_symptom);
        }
    }

    private SelectTag selectTag;

    public void setOnSelectTagListenter(SelectTag selectTag) {
        this.selectTag = selectTag;
    }

    public interface SelectTag{
        void getTag(SymptomBean symptomBean,int position);
    }
}