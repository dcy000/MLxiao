package com.example.han.referralproject.homepage;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.imageloader.ImageLoader;

import java.util.List;

public class ChangeUserAdapter extends BaseQuickAdapter<UserEntity, BaseViewHolder> {

    public ChangeUserAdapter(int layoutResId, @Nullable List<UserEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserEntity item) {
        ImageLoader.with(helper.getView(R.id.iv_user_icon))
                .load(item.avatar)
                .circle()
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(helper.getView(R.id.iv_user_icon));
        helper.setText(R.id.tv_user_name, item.name);
    }
}
