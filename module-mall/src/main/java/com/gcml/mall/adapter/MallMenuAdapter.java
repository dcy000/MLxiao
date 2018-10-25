package com.gcml.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.mall.R;
import com.gcml.mall.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

public class MallMenuAdapter extends RecyclerView.Adapter<MallMenuAdapter.MallMenuHolder> {

    private Context mContext;
    private int selectItem = 0;
    private List<CategoryBean> menuList;
    private OnMenuClickListener onMenuClickListener;

    public MallMenuAdapter(Context context, List<CategoryBean> list){
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
        holder.menuText.setText(menuList.get(position).name);
        if (position == selectItem) {
            holder.menuMark.setVisibility(View.VISIBLE);
            holder.menuContent.setBackgroundColor(Color.WHITE);
            holder.menuText.setTextColor(mContext.getResources().getColor(R.color.config_color_primary));
            holder.menuText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            holder.menuText.setTextSize(28);
        } else {
            holder.menuMark.setVisibility(View.GONE);
            holder.menuContent.setBackgroundColor(mContext.getResources().getColor(R.color.color_background));
            holder.menuText.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
            holder.menuText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
            holder.menuText.setTextSize(24);
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
        RelativeLayout menuContent;
        TextView menuMark;
        TextView menuText;

        public MallMenuHolder(View view){
            super(view);
            menuContent = view.findViewById(R.id.ll_menu_content);
            menuMark = view.findViewById(R.id.tv_menu_mark);
            menuText = view.findViewById(R.id.tv_menu_name);
        }
    }

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }
}
