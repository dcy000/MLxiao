package com.gcml.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.mall.R;

import java.util.ArrayList;

public class MallMenuAdapter extends RecyclerView.Adapter<MallMenuAdapter.MallMenuHolder> {

    private Context mContext;
    private int selectItem = 0;
    private ArrayList<String> menuList;
    private OnMenuClickListener onMenuClickListener;

    public MallMenuAdapter(Context context, ArrayList<String> list){
        menuList = list;
        mContext = context;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.onMenuClickListener = listener;
    }

    @Override
    public MallMenuAdapter.MallMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MallMenuHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mall_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(MallMenuAdapter.MallMenuHolder holder, int position) {
        String menu = menuList.get(position);
        holder.menuText.setText(menu);
        if (position == selectItem) {
            holder.menuMark.setVisibility(View.VISIBLE);
            holder.menuText.setBackgroundColor(Color.WHITE);
            holder.menuText.setTextColor(mContext.getResources().getColor(R.color.config_color_primary));
        } else {
            holder.menuMark.setVisibility(View.GONE);
            holder.menuText.setBackgroundColor(mContext.getResources().getColor(R.color.color_background));
            holder.menuText.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem = position;
                notifyDataSetChanged();
                onMenuClickListener.onMenuClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList == null ? 0 : menuList.size();
    }

    class MallMenuHolder extends RecyclerView.ViewHolder {
        TextView menuMark;
        TextView menuText;

        public MallMenuHolder(View view){
            super(view);
            menuMark = view.findViewById(R.id.tv_menu_mark);
            menuText = view.findViewById(R.id.tv_menu_name);
        }
    }

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }
}
