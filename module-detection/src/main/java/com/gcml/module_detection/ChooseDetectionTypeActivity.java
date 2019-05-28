package com.gcml.module_detection;

import android.graphics.Color;
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
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.bean.ChooseDetectionTypeBean;
import com.gcml.module_detection.bean.LatestDetecBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.gcml.module_detection.utils.Time2Utils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/module/detection/choose/dection/type")
public class ChooseDetectionTypeActivity extends ToolbarBaseActivity {
    private RecyclerView mRv;
    private BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder> adapter;
    private ArrayList<ChooseDetectionTypeBean> types = new ArrayList<>();

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
        mTitleText.setText("健 康 测 量");
        mRv = findViewById(R.id.rv);

        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(adapter = new BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder>(R.layout.layout_item_detection_type, types) {
            @Override
            protected void convert(BaseViewHolder helper, ChooseDetectionTypeBean item) {
                ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(item.getIcon());
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_unit, item.getUnit());
                if (!TextUtils.isEmpty(item.getDate()))
                    helper.setText(R.id.tv_date, item.getDate());

                if (!TextUtils.isEmpty(item.getResult())) {
                    helper.getView(R.id.ll_1).setVisibility(View.VISIBLE);
                    helper.getView(R.id.ll_2).setVisibility(View.GONE);
                    helper.setText(R.id.tv_last_data, item.getResult());
                    if (item.isNormal()) {
                        helper.setTextColor(R.id.tv_last_data, Color.parseColor("#303133"));
                    } else {
                        helper.setTextColor(R.id.tv_last_data, Color.parseColor("#E53B3B"));
                    }
                } else {
                    helper.getView(R.id.ll_1).setVisibility(View.GONE);
                    helper.getView(R.id.ll_2).setVisibility(View.VISIBLE);
                }
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
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_ECG);
                        break;
                    case 5:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_OXYGEN);

                        break;
                    case 6:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_CHOLESTEROL);
                        break;
                    case 7:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_URIC_ACID);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        DetectionRepository.getLatestDetectionData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new DefaultObserver<List<LatestDetecBean>>() {
                    @Override
                    public void onNext(List<LatestDetecBean> latestDetecBeans) {
                        for (LatestDetecBean latest : latestDetecBeans) {
                            //检测数据类型 -1低血压 0高血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸
                            String type = latest.getType();
                            switch (type) {
                                case "-1":
                                    types.get(0).setResult("/" + String.format("%.0f", latest.getValue()));
                                    types.get(0).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    break;
                                case "0":
                                    types.get(0).setResult(new StringBuffer(types.get(0).getResult()).insert(0, String.format("%.0f", latest.getValue())).toString());
                                    types.get(0).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "1":
                                    types.get(1).setResult(latest.getValue() + "");
                                    types.get(1).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(1).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "2":
                                    types.get(4).setResult(TextUtils.equals(latest.getStatus(), "0") ? "正常" : "异常");
                                    types.get(4).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(4).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "3":
                                    types.get(3).setResult(latest.getValue() + "");
                                    types.get(3).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(3).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "4":
                                    types.get(2).setResult(latest.getValue() + "");
                                    types.get(2).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(2).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "6":
                                    types.get(5).setResult(String.format("%.0f", latest.getValue()));
                                    types.get(5).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(5).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "7":
                                    types.get(6).setResult(latest.getValue() + "");
                                    types.get(6).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(6).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "8":
                                    types.get(7).setResult(latest.getValue() + "");
                                    types.get(7).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(7).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                            }
                        }
                        Handlers.ui().post(() -> {
                            Timber.i(">>>>数据变动");
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
