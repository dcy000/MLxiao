package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
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
 * desc: 每日任务页面，包括食盐、运动和饮酒设置（多页面连续设置） .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskDialyContactActivity extends AppCompatActivity implements TaskDialyDetailsFragment.OnActionListener {

    private int what;
    private Fragment[] mFragments;
    private TranslucentToolBar mToolBar;
    private TaskRepository mTaskRepository = new TaskRepository();
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mToolBar = findViewById(R.id.tb_task);
        mToolBar.setData(UM.getString(R.string.Health_diary), R.drawable.common_btn_back, UM.getString(R.string.toolbar_back), R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                if (what <= 0) {
                    finish();
                    return;
                }
                switchFragment(what - 1, what);
                what = what - 1;
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });

        DetailsModel detailsModel0 = new DetailsModel();
        detailsModel0.setWhat(0);
        detailsModel0.setAction(UM.getString(R.string.next_step));
        detailsModel0.setTitle(UM.getString(R.string.Choose_salt_intake));
        detailsModel0.setUnitPosition(0);
        detailsModel0.setUnits(new String[]{UM.getString(R.string.spoon), UM.getString(R.string.gram)});
        detailsModel0.setUnitSum(new String[]{UM.getString(R.string.spoon_gram), UM.getString(R.string.gram)});
        detailsModel0.setSelectedValues(new float[]{0f, 0f});
        detailsModel0.setMinValues(new float[]{0f, 0f});
        detailsModel0.setMaxValues(new float[]{99f, 99f});
        detailsModel0.setPerValues(new float[]{1f, 1f});
        DetailsModel detailsModel1 = new DetailsModel();
        detailsModel1.setWhat(1);
        detailsModel1.setAction(UM.getString(R.string.next_step));
        detailsModel1.setTitle(UM.getString(R.string.Choose_exercise_time));
        detailsModel1.setUnitPosition(0);
        detailsModel1.setUnits(new String[]{UM.getString(R.string.minute), UM.getString(R.string.hour)});
        detailsModel1.setUnitSum(new String[]{UM.getString(R.string.minute), UM.getString(R.string.hour)});
        detailsModel1.setSelectedValues(new float[]{0f, 0f});
        detailsModel1.setMinValues(new float[]{0f, 0f});
        detailsModel1.setMaxValues(new float[]{999f, 99f});
        detailsModel1.setPerValues(new float[]{1f, 1f});
        DetailsModel detailsModel2 = new DetailsModel();
        detailsModel2.setWhat(2);
        detailsModel2.setAction(UM.getString(R.string.Submission));
        detailsModel2.setTitle(UM.getString(R.string.choose_drinking_amount));
        detailsModel2.setUnitPosition(0);
        detailsModel2.setUnits(new String[]{UM.getString(R.string.glass), UM.getString(R.string.bottle), UM.getString(R.string.minute)});
        detailsModel2.setUnitSum(new String[]{UM.getString(R.string.glass_milliliter), UM.getString(R.string.bottle_milliliter), UM.getString(R.string.minute)});
        detailsModel2.setSelectedValues(new float[]{0f, 0f, 0f});
        detailsModel2.setMinValues(new float[]{0f, 0f, 0f});
        detailsModel2.setMaxValues(new float[]{99f, 99f, 9999f});
        detailsModel2.setPerValues(new float[]{1f, 1f, 100f});

        ItemsModel itemsModel0 = new ItemsModel();
        itemsModel0.setTitle(UM.getString(R.string.Select_sports));
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
        itemsModel0.setItems(sportList);
        ItemsModel itemsModel1 = new ItemsModel();
        itemsModel1.setTitle(UM.getString(R.string.Alcohol_and_degree));
        ArrayList<String> wineList = new ArrayList<>();
        wineList.add(UM.getString(R.string.Liquor));
        wineList.add(UM.getString(R.string.beer));
        wineList.add(UM.getString(R.string.Red_wine));
        wineList.add(UM.getString(R.string.Yellow_wine));
        wineList.add(UM.getString(R.string.Whisky));
        itemsModel1.setItems(wineList);
        mFragments = new Fragment[]{
                TaskDialyDetailsFragment.newInstance(detailsModel0),
                TaskDialyGroupFragment.newInstance(itemsModel0, detailsModel1),
                TaskDialyGroupFragment.newInstance(itemsModel1, detailsModel2),
        };
        switchFragment(0, 0);
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
            this.what++;
            switchFragment(what + 1, what);
        } else if (what == 1) {
            int sportMulriple;
            if (unitPosition == 0) {
                sportMulriple = 1;
            } else {
                sportMulriple = 60;
            }
            wheel.sportType = item;
            wheel.sports = (int) (selectedValue * sportMulriple);
            this.what++;
            switchFragment(what + 1, what);
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
            postWheelData(wheel);
        }
    }

    @SuppressLint("CheckResult")
    private void postWheelData(TaskWheelBean wheel) {
        LoadingDialog upDialog = new LoadingDialog.Builder(TaskDialyContactActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(UM.getString(R.string.uploading))
                .create();
        mTaskRepository.taskWheelListForApi(wheel, UserSpHelper.getUserId())
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
                        LoadingDialog successDialog = new LoadingDialog.Builder(TaskDialyContactActivity.this)
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
                        showWeekTarget();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(TaskDialyContactActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord(UM.getString(R.string.upload_fail))
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

    private void showWeekTarget() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        String tag = TaskWeekReportFragment.class.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (mFragments != null) {
            for (Fragment fragment1 : mFragments) {
                if (fragment1 != null && fragment1.isAdded() && !fragment1.isHidden()) {
                    transaction.hide(fragment1);
                }
            }
        }
        if (fragment == null) {
            fragment = TaskWeekReportFragment.newInstance();
            transaction.add(android.R.id.content, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private SparseIntArray saltUnitValues;

    {
        saltUnitValues = new SparseIntArray();
        saltUnitValues.put(0, 2);
        saltUnitValues.put(1, 1);
    }

    private SparseIntArray sportsUnitValues;

    {
        sportsUnitValues = new SparseIntArray();
        sportsUnitValues.put(0, 1);
        sportsUnitValues.put(1, 60);
    }

    private SparseIntArray drinkUnitValues;

    {
        drinkUnitValues = new SparseIntArray();
        drinkUnitValues.put(0, 100);
        drinkUnitValues.put(1, 500);
        drinkUnitValues.put(2, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
