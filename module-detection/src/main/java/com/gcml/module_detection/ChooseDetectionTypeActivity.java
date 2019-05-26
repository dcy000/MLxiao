package com.gcml.module_detection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.bean.ChooseDetectionTypeBean;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import timber.log.Timber;

@Route(path = "/module/detection/choose/dection/type")
public class ChooseDetectionTypeActivity extends ToolbarBaseActivity {
    private RecyclerView mRv;
    private BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder> adapter;
    ArrayList<ChooseDetectionTypeBean> types = new ArrayList<>();

    {
        types.add(new ChooseDetectionTypeBean(R.drawable.type_bloodpressure, "血压", "", "", "(mmHg)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_bloodsugar, "血糖", "", "", "(mmol/L)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_temper, "体温", "", "", "(℃)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_weight, "体重", "", "", "(kg)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_ecg, "心电", "", "", ""));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_bloodoxygen, "血氧", "", "", "(%)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_chrol, "胆固醇", "", "", "(mmol/L)"));
        types.add(new ChooseDetectionTypeBean(R.drawable.type_uac, "血尿酸", "", "", "(mmol/L)"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_detection_type);
        mRv = findViewById(R.id.rv);

        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(adapter = new BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder>(R.layout.layout_item_detection_type, types) {
            @Override
            protected void convert(BaseViewHolder helper, ChooseDetectionTypeBean item) {
//                ((TextView) helper.getView(R.id.tv_last_data)).setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
                ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(item.getIcon());
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_unit, item.getUnit());
                if (TextUtils.isEmpty(item.getDate())) helper.setText(R.id.tv_date, item.getDate());
                if (TextUtils.isEmpty(item.getResult())) helper.setText(R.id.tv_last_data, item.getResult());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_PRESSURE);
                        break;
                    case 1:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_SUGAR);
                        break;
                    case 2:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_TEMPERATURE);
                        break;
                    case 3:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_WEIGHT);
                        break;
                    case 4:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_OXYGEN);
                        break;
                    case 5:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_CHOLESTEROL);
                        break;
                    case 6:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_URIC_ACID);
                        break;
                    case 7:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_ECG);
                        break;
                    case 8:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.SCAN_ID_CARD, new ActivityCallback() {
                            @Override
                            public void onActivityResult(int result, Object data) {
                                Timber.i(">>>" + data);
                            }
                        });
                        break;
                }
            }
        });
    }
}
