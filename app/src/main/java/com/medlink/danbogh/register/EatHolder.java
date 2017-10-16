package com.medlink.danbogh.register;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/10/13.
 */

public class EatHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_sign_up_eat)
    ImageView ivEat;
    @BindView(R.id.tv_sign_up_eat)
    TextView tvEat;
    @BindView(R.id.ll_sign_up_eat_item)
    LinearLayout llEatItem;

    public EatModel mEatModel;

    public EatHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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

    private void select(TextView tvEat, EatModel eatModel) {

    }

    @OnClick(R.id.tv_sign_up_eat)
    public void onTvEatClicked() {
        boolean selected = !mEatModel.isSelected();
        tvEat.setSelected(selected);
        mEatModel.setSelected(selected);
        tvEat.setBackgroundResource(selected
                ? mEatModel.getBgSelected()
                : mEatModel.getBg());
        tvEat.setTextColor(selected
                ? tvEat.getResources().getColor(R.color.white)
                : mEatModel.getSelectedColor());
    }

    @OnClick(R.id.ll_sign_up_eat_item)
    public void onLlEatItemClicked() {

    }
}
