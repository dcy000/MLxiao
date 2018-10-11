package com.gcml.common.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.business.R;

import java.util.List;

/**
 * Created by lenovo on 2017/10/13.
 */

public class EatAdapter extends RecyclerView.Adapter<EatAdapter.EatHolder> {
    public List<EatModel> mEatModels;

    public EatAdapter(List<EatModel> eatModels) {
        mEatModels = eatModels;
    }

    @Override
    public EatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.common_item_eat, parent, false);
        return new EatHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(EatHolder holder, int position) {
        EatModel eatModel = mEatModels.get(position);
        holder.onBind(eatModel);
    }

    @Override
    public int getItemCount() {
        return mEatModels == null ? 0 : mEatModels.size();
    }

    private View.OnClickListener mListener;

    public void setOnItemClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    public class EatHolder extends RecyclerView.ViewHolder {
        ImageView ivEat;
        TextView tvEat;

        public EatModel mEatModel;

        public EatHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            ivEat = (ImageView) itemView.findViewById(R.id.iv_sign_up_eat);
            tvEat = (TextView) itemView.findViewById(R.id.tv_sign_up_eat);
            itemView.setOnClickListener(listener);
        }

        public void onBind(EatModel eatModel) {
            mEatModel = eatModel;
            ivEat.setImageResource(eatModel.getImgRes());
            tvEat.setBackgroundResource(eatModel.isSelected()
                    ? eatModel.getBgSelected()
                    : eatModel.getBg());
            tvEat.setTextColor(eatModel.isSelected()
                    ? tvEat.getResources().getColor(R.color.white)
                    : eatModel.getSelectedColor());
            tvEat.setText(eatModel.getEatType());
            tvEat.setSelected(eatModel.isSelected());
        }
    }
}
