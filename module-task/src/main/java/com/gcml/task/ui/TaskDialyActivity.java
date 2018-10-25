package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.billy.cc.core.component.CC;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.DetailsModel;
import com.gcml.task.bean.ItemsModel;
import com.gcml.task.bean.Post.TaskWheelBean;
import com.gcml.task.network.TaskRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 每日任务页面，包括食盐、运动和饮酒设置（单页面单独设置） .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskDialyActivity extends FragmentActivity implements TaskDialyDetailsFragment.OnActionListener {

    Handler mHandler = new Handler();
    TranslucentToolBar mToolBar;
    private Fragment[] mFragments;
    private TaskRepository mTaskRepository = new TaskRepository();
    DetailsModel saltDetails = new DetailsModel();
    DetailsModel sportDetails = new DetailsModel();
    DetailsModel wineDetails = new DetailsModel();
    int what = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        what = getIntent().getExtras().getInt("what");
        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task);
    }

    private void bindData() {
        mToolBar.setData("摄 盐 控 制", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });
        if (what == 0) {
            mToolBar.setTitle("摄 盐 控 制");
        } else if (what == 1) {
            mToolBar.setTitle("运 动 控 制");
        } else {
            mToolBar.setTitle("饮 酒 控 制");
        }

        saltDetails.setWhat(0);
        saltDetails.setAction("完成");
        saltDetails.setTitle("选择盐摄入量");
        saltDetails.setUnitPosition(0);
        saltDetails.setUnits(new String[]{"勺", "克"});
        saltDetails.setUnitSum(new String[]{"勺(1勺约等于2克)", "克"});
        saltDetails.setSelectedValues(new float[]{0f, 0f});
        saltDetails.setMinValues(new float[]{0f, 0f});
        saltDetails.setMaxValues(new float[]{99f, 199f});
        saltDetails.setPerValues(new float[]{1f, 1f});

        sportDetails.setWhat(1);
        sportDetails.setAction("完成");
        sportDetails.setTitle("选择运动时间");
        sportDetails.setUnitPosition(0);
        sportDetails.setUnits(new String[]{"分钟", "小时"});
        sportDetails.setUnitSum(new String[]{"分钟", "小时"});
        sportDetails.setSelectedValues(new float[]{0f, 0f});
        sportDetails.setMinValues(new float[]{0f, 0f});
        sportDetails.setMaxValues(new float[]{1440f, 24f});
        sportDetails.setPerValues(new float[]{1f, 1f});

        wineDetails.setWhat(2);
        wineDetails.setAction("完成");
        wineDetails.setTitle("选择饮酒量");
        wineDetails.setUnitPosition(0);
        wineDetails.setUnits(new String[]{"杯", "瓶", "毫升"});
        wineDetails.setUnitSum(new String[]{"杯(一杯约等于100毫升)", "瓶(一瓶约等于500毫升)", "毫升"});
        wineDetails.setSelectedValues(new float[]{0f, 0f, 0f});
        wineDetails.setMinValues(new float[]{0f, 0f, 0f});
        wineDetails.setMaxValues(new float[]{99f, 499f, 9999f});
        wineDetails.setPerValues(new float[]{1f, 1f, 100f});

        ItemsModel sportItems = new ItemsModel();
        sportItems.setTitle("选择运动项目");
        ArrayList<String> sportList = new ArrayList<>();
        sportList.add("足球");
        sportList.add("羽毛球");
        sportList.add("网球");
        sportList.add("打篮球");
        sportList.add("乒乓球");
        sportList.add("桌球");
        sportList.add("高尔夫球");
        sportList.add("仰卧起坐");
        sportList.add("俯卧撑");
        sportList.add("太极");
        sportList.add("跑步");
        sportList.add("游泳");
        sportList.add("瑜伽");
        sportList.add("跳舞");
        sportList.add("体操");
        sportItems.setItems(sportList);

        ItemsModel wineItems = new ItemsModel();
        wineItems.setTitle("选择饮酒类型");
        ArrayList<String> wineList = new ArrayList<>();
        wineList.add("白酒");
        wineList.add("啤酒");
        wineList.add("红酒");
        wineList.add("黄酒");
        wineList.add("威士忌");
        wineItems.setItems(wineList);

        mFragments = new Fragment[]{
                TaskDialyDetailsFragment.newInstance(saltDetails),
                TaskDialyGroupFragment.newInstance(sportItems, sportDetails),
                TaskDialyGroupFragment.newInstance(wineItems, wineDetails),
        };
        switchFragment(what, what);
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        TaskWheelBean wheel = new TaskWheelBean();
        if (what == 0) {
            int saltMulriple;
            if (unitPosition == 0) {
                saltMulriple = 2;
            } else {
                saltMulriple = 1;
            }
            wheel.na = (int) (selectedValue * saltMulriple);
        } else if (what == 1) {
            int sportMulriple;
            if (unitPosition == 0) {
                sportMulriple = 1;
            } else {
                sportMulriple = 60;
            }
            wheel.sportType = item;
            wheel.sports = (int) (selectedValue * sportMulriple);
        } else if (what == 2) {
            int sportMulriple;
            if (unitPosition == 0) {
                sportMulriple = 100;
            } else if (unitPosition == 1) {
                sportMulriple = 500;
            } else {
                sportMulriple = 1;
            }
            wheel.wineType = item;
            wheel.drink = (int) (selectedValue * sportMulriple);
        }
        wheel.userid = Integer.parseInt((String) SPUtil.get("user_id",""));
        postWheelData(wheel);
    }

    @SuppressLint("CheckResult")
    private void postWheelData(TaskWheelBean wheel) {
        LoadingDialog upDialog = new LoadingDialog.Builder(TaskDialyActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();
        mTaskRepository.taskWheelListForApi(wheel, (String) SPUtil.get("user_id",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        upDialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        upDialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object body) {
                        super.onNext(body);
                        LoadingDialog successDialog = new LoadingDialog.Builder(TaskDialyActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("上传成功")
                                .create();
                        successDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successDialog.dismiss();
                                finish();
                            }
                        }, 500);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(TaskDialyActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("上传失败")
                                .create();
                        errorDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog.dismiss();
                            }
                        }, 500);
                    }
                });
    }

    private void switchFragment(int theWhat, int oldWhat) {
        Fragment theFragment = mFragments[theWhat];
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment oldFragment = mFragments[oldWhat];
        if (oldFragment.isAdded()) {
            transaction.hide(oldFragment);
        }
        if (theFragment.isAdded()) {
            transaction.show(theFragment);
        } else {
            transaction.add(R.id.fl_task, theFragment, theFragment.getClass().getName() + theWhat);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
