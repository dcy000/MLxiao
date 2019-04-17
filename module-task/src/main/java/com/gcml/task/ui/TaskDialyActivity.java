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
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
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
        mToolBar.setData(UM.getString(R.string.title_salt_control), R.drawable.common_btn_back, UM.getString(R.string.toolbar_back), R.drawable.common_btn_home, null, new ToolBarClickListener() {
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
            mToolBar.setTitle(UM.getString(R.string.title_salt_control));
        } else if (what == 1) {
            mToolBar.setTitle(UM.getString(R.string.title_sport_control));
        } else {
            mToolBar.setTitle(UM.getString(R.string.title_drinking_control));
        }

        saltDetails.setWhat(0);
        saltDetails.setAction(UM.getString(R.string.finished));
        saltDetails.setTitle(UM.getString(R.string.choose_salt_intake));
        saltDetails.setUnitPosition(0);
        saltDetails.setUnits(new String[]{UM.getString(R.string.spoon), UM.getString(R.string.gram)});
        saltDetails.setUnitSum(new String[]{UM.getString(R.string.spoon_gram), UM.getString(R.string.gram)});
        saltDetails.setSelectedValues(new float[]{0f, 0f});
        saltDetails.setMinValues(new float[]{0f, 0f});
        saltDetails.setMaxValues(new float[]{99f, 199f});
        saltDetails.setPerValues(new float[]{1f, 1f});

        sportDetails.setWhat(1);
        sportDetails.setAction(UM.getString(R.string.finished));
        sportDetails.setTitle(UM.getString(R.string.Choose_exercise_time));
        sportDetails.setUnitPosition(0);
        sportDetails.setUnits(new String[]{UM.getString(R.string.minute), UM.getString(R.string.hour)});
        sportDetails.setUnitSum(new String[]{UM.getString(R.string.minute), UM.getString(R.string.hour)});
        sportDetails.setSelectedValues(new float[]{0f, 0f});
        sportDetails.setMinValues(new float[]{0f, 0f});
        sportDetails.setMaxValues(new float[]{1440f, 24f});
        sportDetails.setPerValues(new float[]{1f, 1f});

        wineDetails.setWhat(2);
        wineDetails.setAction(UM.getString(R.string.finished));
        wineDetails.setTitle(UM.getString(R.string.choose_drinking_amount));
        wineDetails.setUnitPosition(0);
        wineDetails.setUnits(new String[]{UM.getString(R.string.glass), UM.getString(R.string.bottle), UM.getString(R.string.minute)});
        wineDetails.setUnitSum(new String[]{UM.getString(R.string.glass_milliliter), UM.getString(R.string.bottle_milliliter), UM.getString(R.string.minute)});
        wineDetails.setSelectedValues(new float[]{0f, 0f, 0f});
        wineDetails.setMinValues(new float[]{0f, 0f, 0f});
        wineDetails.setMaxValues(new float[]{99f, 499f, 9999f});
        wineDetails.setPerValues(new float[]{1f, 1f, 100f});

        ItemsModel sportItems = new ItemsModel();
        sportItems.setTitle(UM.getString(R.string.Select_sports));
        ArrayList<String> sportList = new ArrayList<>();
        sportList.add(UM.getString(R.string.football));
        sportList.add(UM.getString(R.string.badminton));
        sportList.add(UM.getString(R.string.tennis));
        sportList.add(UM.getString(R.string.play_basketball));
        sportList.add(UM.getString(R.string.pingpong));
        sportList.add(UM.getString(R.string.golf));
        sportList.add(UM.getString(R.string.Sit_ups));
        sportList.add(UM.getString(R.string.push_ups));
        sportList.add(UM.getString(R.string.Tai_Chi));
        sportList.add(UM.getString(R.string.Run));
        sportList.add(UM.getString(R.string.Swim));
        sportList.add(UM.getString(R.string.Yoga));
        sportList.add(UM.getString(R.string.dancing));
        sportList.add(UM.getString(R.string.gymnastics));
        sportItems.setItems(sportList);

        ItemsModel wineItems = new ItemsModel();
        wineItems.setTitle(UM.getString(R.string.Choose_type_of_drinking));
        ArrayList<String> wineList = new ArrayList<>();
        wineList.add(UM.getString(R.string.Liquor));
        wineList.add(UM.getString(R.string.beer));
        wineList.add(UM.getString(R.string.Red_wine));
        wineList.add(UM.getString(R.string.Yellow_wine));
        wineList.add(UM.getString(R.string.Whisky));
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
        wheel.userid = Integer.parseInt((String) SPUtil.get("user_id", ""));
        postWheelData(wheel);
    }

    @SuppressLint("CheckResult")
    private void postWheelData(TaskWheelBean wheel) {
        LoadingDialog upDialog = new LoadingDialog.Builder(TaskDialyActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(UM.getString(R.string.uploading))
                .create();
        mTaskRepository.taskWheelListForApi(wheel, (String) SPUtil.get("user_id", ""))
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
                                .setTipWord(UM.getString(R.string.upload_success))
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
