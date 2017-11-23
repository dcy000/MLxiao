package com.example.han.referralproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.SecondSymptomAnalyseActivity;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.util.ArrayList;

public class SymptomRecyclerAdapter extends RecyclerView.Adapter<SymptomRecyclerAdapter.SymptomHolder>{
    private LayoutInflater mInflater;
    private ArrayList<SymptomBean> mDataList;
    private ArrayList<Integer> mSelectList = new ArrayList<>();
    private int select_index=-1;//记录选中的位置
    private Context context;
    public SymptomRecyclerAdapter(Context context, ArrayList<SymptomBean> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        this.context=context;
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
        if(mDataList.get(position).isSelected){
            holder.symptomTv.setSelected(true);
        }else{
            holder.symptomTv.setSelected(false);
        }
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
                for(int i=0;i<mDataList.size();i++){
                    if(i==position){
                        mDataList.get(i).isSelected=true;
                        select_index=position;
                    }else{
                        mDataList.get(i).isSelected=false;
                    }
                }
                notifyDataSetChanged();
                NetworkApi.analyseSym(mDataList.get(position).id, mAnalyseCallback);
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
    private NetworkManager.SuccessCallback mAnalyseCallback = new NetworkManager.SuccessCallback<SymptomResultBean>() {
        @Override
        public void onSuccess(SymptomResultBean response) {
            if (response == null) {
                return;
            }
//            Intent mResultIntent = new Intent(mContext, SymptomAnalyseResultActivity.class);
//            mResultIntent.putExtra("result", (Serializable) response.getBqss());
//            startActivity(new Intent(mResultIntent));
            Intent secondIntent=new Intent(context,SecondSymptomAnalyseActivity.class);
            secondIntent.putExtra("result",response);
            secondIntent.putExtra("choose_one",mDataList.get(select_index));
            context.startActivity(secondIntent);
        }
    };
}