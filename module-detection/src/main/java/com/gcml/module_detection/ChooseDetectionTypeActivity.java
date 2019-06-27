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
import com.gcml.common.constant.EUserInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.menu.EMenu;
import com.gcml.common.menu.MenuEntity;
import com.gcml.common.menu.MenuHelperProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.CheckUserInfoProviderImp;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.bean.ChooseDetectionTypeBean;
import com.gcml.module_detection.bean.LatestDetecBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/module/detection/choose/dection/type")
public class ChooseDetectionTypeActivity extends ToolbarBaseActivity {
    private RecyclerView mRv;
    private BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder> adapter;
    private ArrayList<ChooseDetectionTypeBean> types = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_detection_type);
        mTitleText.setText("健 康 检 测");
        mRv = findViewById(R.id.rv);
        setAdapter();

    }


    private void setAdapter() {
        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        //TODO:item里面的布局写的有点渣，后来需求变更，所以没有整体重写，直接在上面加的
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
                    if (!TextUtils.isEmpty(item.getResult2())) {
                        helper.getView(R.id.tv_slash).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_last_low_pressure).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_last_low_pressure, item.getResult2());
                        if (item.isNormal2()) {
                            helper.setTextColor(R.id.tv_last_low_pressure, Color.parseColor("#303133"));
                        } else {
                            helper.setTextColor(R.id.tv_last_low_pressure, Color.parseColor("#E53B3B"));
                        }
                        if (item.isNormal() && item.isNormal2()) {
                            helper.setTextColor(R.id.tv_slash, Color.parseColor("#303133"));
                        } else {
                            helper.setTextColor(R.id.tv_slash, Color.parseColor("#E53B3B"));
                        }
                    } else {
                        helper.getView(R.id.tv_slash).setVisibility(View.GONE);
                        helper.getView(R.id.tv_last_low_pressure).setVisibility(View.GONE);
                    }
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
                        checkGender(new Runnable() {
                            @Override
                            public void run() {
                                Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_PRESSURE, true);
                            }
                        });
                        break;
                    case 1:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_SUGAR, true);
                        break;
                    case 2:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_TEMPERATURE, true);
                        break;
                    case 3:
                        //测量体重需要完善身高信息，后台需要计算BMI
                        checkHeight();
                        break;
                    case 4:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_ECG, true);
                        break;
                    case 5:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_BLOOD_OXYGEN, true);

                        break;
                    case 6:
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_CHOLESTEROL, true);
                        break;
                    case 7:
                        checkGender(new Runnable() {
                            @Override
                            public void run() {
                                Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_URIC_ACID, true);
                            }
                        });
                        break;
                }
            }
        });
    }

    private void checkHeight() {
        Routerfit.register(AppRouter.class).getCheckUserInfoProvider()
                .check(new CheckUserInfoProviderImp.CheckUserInfo() {
                    @Override
                    public void complete(UserEntity userEntity) {
                        UserSpHelper.setUserHeight((int) Float.parseFloat(userEntity.height));
                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_WEIGHT, true);
                    }

                    @Override
                    public void incomplete(UserEntity entity, List<EUserInfo> args, String s) {

                        showNotMsgDiaglog(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }, EUserInfo.HEIGHT);
    }

    private void showNotMsgDiaglog(String msg) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_not_person_msg)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.txt_msg, "测量体重需要先完善" + msg + "信息，方便我们为您计算BMI。");
                        holder.setOnClickListener(R.id.btn_neg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.btn_pos, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Routerfit.register(AppRouter.class).skipPersonDetailActivity();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getSupportFragmentManager());
    }

    private void checkGender(Runnable action) {
        Routerfit.register(AppRouter.class).getCheckUserInfoProvider()
                .check(new CheckUserInfoProviderImp.CheckUserInfo() {
                    @Override
                    public void complete(UserEntity userEntity) {
                        if (action != null) {
                            action.run();
                        }
//                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_WEIGHT, true);
                    }

                    @Override
                    public void incomplete(UserEntity entity, List<EUserInfo> args, String s) {
                        showFinishGenderDialog("请先去个人中心完善性别");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }, EUserInfo.GENDER);
    }

    private void showFinishGenderDialog(String msg) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_not_person_msg)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.txt_msg, msg);
                        holder.setOnClickListener(R.id.btn_neg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.btn_pos, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (ChannelUtils.isXiongAn() || ChannelUtils.isBj()) {
                                    Routerfit.register(AppRouter.class).skipUserInfoActivity();
                                    return;
                                }
                                Routerfit.register(AppRouter.class).skipPersonDetailActivity();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getSupportFragmentManager());
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMenu();
    }

    private void getMenu() {
        Routerfit.register(AppRouter.class)
                .getMenuHelperProvider()
                .menu(ChannelUtils.isXiongAn() || ChannelUtils.isBj(), EMenu.DETECTION, new MenuHelperProviderImp.MenuResult() {
                    @Override
                    public void onSuccess(List<MenuEntity> menus) {
                        getData();
                        dealMenu(menus);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
    }

    private void dealMenu(List<MenuEntity> menus) {
        types.clear();
        for (MenuEntity entity : menus) {
            String name = entity.getMenuLabel();
            if (TextUtils.isEmpty(name)) continue;
            ChooseDetectionTypeBean bean = new ChooseDetectionTypeBean();
            bean.setTitle(name);
            switch (name) {
                case "血压":
                    bean.setIcon(R.drawable.type_bloodpressure);
                    bean.setUnit("(mmHg)");
                    break;
                case "血糖":
                    bean.setIcon(R.drawable.type_bloodsugar);
                    bean.setUnit("(mmol/L)");
                    break;
                case "体温":
                    bean.setIcon(R.drawable.type_temper);
                    bean.setUnit("(℃)");
                    break;
                case "BMI":
                    bean.setIcon(R.drawable.type_weight);
                    bean.setUnit("");
                    break;
                case "心电":
                    bean.setIcon(R.drawable.type_ecg);
                    bean.setUnit("");
                    break;
                case "血氧":
                    bean.setIcon(R.drawable.type_bloodoxygen);
                    bean.setUnit("(%)");
                    break;
                case "胆固醇":
                    bean.setIcon(R.drawable.type_chrol);
                    bean.setUnit("(mmol/L)");
                    break;
                case "血尿酸":
                    bean.setIcon(R.drawable.type_uac);
                    bean.setUnit("(μmol/L)");
                    break;
            }
            types.add(bean);
            adapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        DetectionRepository.getLatestDetectionData()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<List<LatestDetecBean>>() {
                    @Override
                    public void accept(List<LatestDetecBean> latestDetecBeans) throws Exception {
                        for (LatestDetecBean latest : latestDetecBeans) {
                            //检测数据类型 -1低血压 0高血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸
                            String type = latest.getType();
                            boolean status = TextUtils.equals(latest.getStatus(), "0");
//                            String friendlyTimeSpanByNow = Time2Utils.getFriendlyTimeSpanByNow(latest.getDate());
                            String friendlyTimeSpanByNow = latest.getDate();
                            switch (type) {
                                case "-1":
                                    types.get(0).setResult2(String.format("%.0f", latest.getValue()));
                                    types.get(0).setDate(friendlyTimeSpanByNow);
                                    types.get(0).setNormal2(status);
                                    break;
                                case "0":
                                    types.get(0).setResult(String.format("%.0f", latest.getValue()));
                                    types.get(0).setDate(friendlyTimeSpanByNow);
                                    types.get(0).setNormal(status);
                                    break;
                                case "1":
                                    types.get(1).setResult(latest.getValue() + "");
                                    types.get(1).setDate(friendlyTimeSpanByNow);
                                    types.get(1).setNormal(status);
                                    break;
                                case "2":
                                    //todo:后台逻辑应该写反了，临时前端解决一下
                                    types.get(4).setResult(status ? "正常" : "异常");
                                    types.get(4).setDate(friendlyTimeSpanByNow);
                                    types.get(4).setNormal(status);
                                    break;
                                case "3":
                                    types.get(3).setResult(latest.getValue() + "");
                                    types.get(3).setDate(friendlyTimeSpanByNow);
                                    types.get(3).setNormal(status);
                                    break;
                                case "4":
                                    types.get(2).setResult(latest.getValue() + "");
                                    types.get(2).setDate(friendlyTimeSpanByNow);
                                    types.get(2).setNormal(status);
                                    break;
                                case "6":
                                    types.get(5).setResult(String.format("%.0f", latest.getValue()));
                                    types.get(5).setDate(friendlyTimeSpanByNow);
                                    types.get(5).setNormal(status);
                                    break;
                                case "7":
                                    types.get(6).setResult(latest.getValue() + "");
                                    types.get(6).setDate(friendlyTimeSpanByNow);
                                    types.get(6).setNormal(status);
                                    break;
                                case "8":
                                    types.get(7).setResult(latest.getValue() + "");
                                    types.get(7).setDate(friendlyTimeSpanByNow);
                                    types.get(7).setNormal(status);
                                    break;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<LatestDetecBean>>() {
                    @Override
                    public void onNext(List<LatestDetecBean> latestDetecBeans) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
