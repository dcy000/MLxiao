package com.gcml.module_detection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;

import timber.log.Timber;

@Route(path = "/module/detection/choose/dection/type")
public class ChooseDetectionTypeActivity extends ToolbarBaseActivity {
    String[] projects = {"血压", "血糖", "耳温", "体重", "血氧", "胆固醇", "血尿酸", "心电", "身份证阅读"};
    private RecyclerView mRv;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_detection_type);
        mRv = findViewById(R.id.rv);

        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_detection_type, Arrays.asList(projects)) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.item, item);
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
