package com.gcml.health.measure.demo;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/4 16:58
 * created by:gzq
 * description:TODO
 */
public class DemoGoodTipsFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private ConstraintLayout mCl1;
    private ConstraintLayout mCl2;

    @Override
    protected int initLayout() {
        return R.layout.demo_fragment_good_tip;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mCl1 = (ConstraintLayout) view.findViewById(R.id.cl_1);
        mCl1.setOnClickListener(this);
        mCl2 = (ConstraintLayout) view.findViewById(R.id.cl_2);
        mCl2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cl_1) {
            DemoGoodDetail demoGoodDetail = new DemoGoodDetail(R.drawable.demo_img4,
                    "海参肽酵素", "全新的独家配方、独特的产品概念、强大的保健功效，是您驰骋酵素市场的一把利剑。", "999.00");
            DemoGoodDetailActivity.startActivity(mContext, demoGoodDetail);

        } else if (i == R.id.cl_2) {
            DemoGoodDetail demoGoodDetail2 = new DemoGoodDetail(R.drawable.demo_img3,
                    "膳力达营养强化复合蛋白粉固体饮品", "改善睡眠，缓解失眠多梦，并能缓解焦虑消除烦躁、易怒情绪", "999.00");
            DemoGoodDetailActivity.startActivity(mContext, demoGoodDetail2);

        } else {

        }
    }
}
