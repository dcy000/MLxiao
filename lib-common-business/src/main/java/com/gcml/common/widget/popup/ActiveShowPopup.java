package com.gcml.common.widget.popup;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.widget.popup.base.BasePopupWindow;


/**
 * 作者：wecent
 * 时间：2017/10/24
 * 描述：商品下拉列表弹出框
 */

public class ActiveShowPopup extends BasePopupWindow {

    private Activity mActivity;
    private RelativeLayout popupShadow;
    private TextView popupAction;

    public ActiveShowPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mActivity = context;
    }

    @Override
    public View getPopupView() {
        View view = getPopupViewById(R.layout.popup_health_show);
        popupShadow = view.findViewById(R.id.popup_shadow);
        popupShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popupAction = view.findViewById(R.id.tv_popup_action);
        popupAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public View getAnimaView() {
        return null;
    }

    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return null;
    }

//    public void setGoodsEvent(final ArrayList<GoodsActiveBean> myList, ArrayList<GoodsBoxBean> boxList, int goods_id) {
//        if (myList.isEmpty()) {
//            return;
//        }
//        ActiveShowAdapter activeShowAdapter = new ActiveShowAdapter(mActivity, myList, boxList, goods_id);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mRecyclerView.setAdapter(activeShowAdapter);
//    }
}

