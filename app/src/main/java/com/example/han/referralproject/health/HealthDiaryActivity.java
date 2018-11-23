package com.example.han.referralproject.health;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.health.model.DetailsModel;
import com.example.han.referralproject.health.model.ItemsModel;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

public class HealthDiaryActivity extends BaseActivity
        implements HealthDiaryDetailsFragment.OnActionListener {

    private int what;

    private DetailsModel[] mDetailsModels;

    private Fragment[] mFragments;
    private ItemsModel[] mItemsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_diary);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健  康  日  记");

        DetailsModel detailsModel0 = new DetailsModel();
        detailsModel0.setWhat(0);
        detailsModel0.setAction("下一步");
        detailsModel0.setTitle("选择盐的摄入量");
        detailsModel0.setUnitPosition(0);
        detailsModel0.setUnits(new String[]{"勺", "克"});
        detailsModel0.setUnitSum(new String[]{"勺(1勺约等于2克)", "克"});
        detailsModel0.setSelectedValues(new float[]{0f, 0f});
        detailsModel0.setMinValues(new float[]{0f, 0f});
        detailsModel0.setMaxValues(new float[]{99f, 99f});
        detailsModel0.setPerValues(new float[]{1f, 1f});
        DetailsModel detailsModel1 = new DetailsModel();
        detailsModel1.setWhat(1);
        detailsModel1.setAction("下一步");
        detailsModel1.setTitle("选择运动时间");
        detailsModel1.setUnitPosition(0);
        detailsModel1.setUnits(new String[]{"分钟", "小时"});
        detailsModel1.setUnitSum(new String[]{"分钟", "小时"});
        detailsModel1.setSelectedValues(new float[]{0f, 0f});
        detailsModel1.setMinValues(new float[]{0f, 0f});
        detailsModel1.setMaxValues(new float[]{999f, 99f});
        detailsModel1.setPerValues(new float[]{1f, 1f});
        DetailsModel detailsModel2 = new DetailsModel();
        detailsModel2.setWhat(2);
        detailsModel2.setAction("提交");
        detailsModel2.setTitle("选择饮酒量");
        detailsModel2.setUnitPosition(0);
        detailsModel2.setUnits(new String[]{"杯", "瓶", "毫升"});
        detailsModel2.setUnitSum(new String[]{"杯(一杯约等于100毫升)", "瓶(一瓶约等于500毫升)", "毫升"});
        detailsModel2.setSelectedValues(new float[]{0f, 0f, 0f});
        detailsModel2.setMinValues(new float[]{0f, 0f, 0f});
        detailsModel2.setMaxValues(new float[]{99f, 99f, 9999f});
        detailsModel2.setPerValues(new float[]{1f, 1f, 100f});
//        DetailsModel model3 = new DetailsModel();
//        model3.setWhat(3);
//        model3.setAction("提交");
//        model3.setTitle("选择您的体重，如果您有体重秤也可以连接蓝牙直接测量");
//        model3.setUnitPosition(0);
//        model3.setUnits(new String[]{"kg"});
//        model3.setMinValues(new float[]{0f});
//        model3.setMaxValues(new float[]{200f});
//        model3.setPerValues(new float[]{1f});
        ItemsModel itemsModel0 = new ItemsModel();
        itemsModel0.setTitle("选择运动项目");
        ArrayList<String> items0 = new ArrayList<>();
        items0.add("足球");
        items0.add("羽毛球");
        items0.add("网球");
        items0.add("打篮球");
        items0.add("乒乓球");
        items0.add("桌球");
        items0.add("高尔夫球");
        items0.add("仰卧起坐");
        items0.add("俯卧撑");
        items0.add("太极");
        items0.add("跑步");
        items0.add("游泳");
        items0.add("瑜伽");
        items0.add("跳舞");
        items0.add("体操");
        itemsModel0.setItems(items0);
        ItemsModel itemsModel1 = new ItemsModel();
        itemsModel1.setTitle("选择酒类与度数");
        ArrayList<String> items1 = new ArrayList<>();
        items1.add("白酒");
        items1.add("啤酒");
        items1.add("红酒");
        items1.add("黄酒");
        items1.add("威士忌");
        itemsModel1.setItems(items1);
        mItemsModels = new ItemsModel[]{itemsModel0, itemsModel1};
        mDetailsModels = new DetailsModel[]{detailsModel0, detailsModel1, detailsModel2};
        mFragments = new Fragment[]{
                HealthDiaryDetailsFragment.newInstance(detailsModel0),
                HealthDiaryDetails2Fragment.newInstance(itemsModel0, detailsModel1),
                HealthDiaryDetails2Fragment.newInstance(itemsModel1, detailsModel2),
        };
        switchFragment(0, 0);
    }

    @Override
    protected void backLastActivity() {
        if (what <= 0) {
            finish();
            return;
        }
        switchFragment(what - 1, what);
        what = what - 1;
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
            transaction.add(R.id.health_fl_container, theFragment, theFragment.getClass().getName() + theWhat);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        ResultModel resultModel = new ResultModel();
        resultModel.what = what;
        resultModel.selectedValue = selectedValue;
        resultModel.unitPosition = unitPosition;
        resultModel.item = item;
        resultModels.put(what, resultModel);
        if (what == 2) {
            double salt = resultModels.get(0).selectedValue * saltUnitValues.get(resultModels.get(0).unitPosition);
            int sports = (int) (resultModels.get(1).selectedValue * sportsUnitValues.get(resultModels.get(1).unitPosition));
            int drink = (int) (resultModels.get(2).selectedValue * drinkUnitValues.get(resultModels.get(2).unitPosition));
            NetworkApi.postHealthDiary(
                    salt,
                    sports,
                    drink,
                    new NetworkManager.SuccessCallback<Object>() {
                        @Override
                        public void onSuccess(Object response) {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }
                            ToastUtils.showShort("提交成功");
                            showWeekTarget();
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }
                            ToastUtils.showShort(message);
                            MLVoiceSynthetize.startSynthesize(message);
                        }
                    }
            );
        } else {
            this.what++;
            switchFragment(what + 1, what);
        }
    }

    private void showWeekTarget() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        String tag = HealthReportFragment.class.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (mFragments != null) {
            for (Fragment fragment1 : mFragments) {
                if (fragment1 != null && fragment1.isAdded() && !fragment1.isHidden()) {
                    transaction.hide(fragment1);
                }
            }
        }
        if (fragment == null) {
            fragment = HealthReportFragment.newInstance();
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

    private SparseArray<ResultModel> resultModels = new SparseArray<>();

    public static class ResultModel {
        public int what;
        public float selectedValue;
        public int unitPosition;
        public String item;
    }

    @Override
    protected void onResume() {
        setDisableWakeup(true);
        super.onResume();
    }
}
