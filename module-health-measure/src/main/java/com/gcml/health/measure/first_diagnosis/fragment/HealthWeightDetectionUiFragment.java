package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetailsModel;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.weight.WeightFragment;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthWeightDetectionUiFragment extends WeightFragment
        implements HealthDiaryDetailsFragment.OnActionListener {

    private DetailsModel mUiModel;
    private static final int WHAT_WEIGHT_DETECTION = 0;
    private boolean isJump2Next = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiModel = new DetailsModel();
        mUiModel.setWhat(WHAT_WEIGHT_DETECTION);
        mUiModel.setTitle(getResources().getString(R.string.health_measure_detection_weight_title));
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"kg"});
        mUiModel.setUnitSum(new String[]{"kg"});
        mUiModel.setSelectedValues(new float[]{60f});
        mUiModel.setMinValues(new float[]{0f});
        mUiModel.setMaxValues(new float[]{200f});
        mUiModel.setPerValues(new float[]{0.2f});
        mUiModel.setAction("下一步");
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        showUi();
    }

    private HealthDiaryDetailsFragment mUiFragment;

    private void showUi() {
        String tag = HealthDiaryDetailsFragment.class.getName();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null && fragment.isHidden()) {
            transaction.show(fragment);
        } else {
            fragment = HealthDiaryDetailsFragment.newInstance(mUiModel);
            transaction.add(R.id.fl_container, fragment, tag);
        }
        mUiFragment = (HealthDiaryDetailsFragment) fragment;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        if (WHAT_WEIGHT_DETECTION == what) {
            uploadData(selectedValue);
        }
    }

    @SuppressLint("CheckResult")
    private void uploadData(float weight) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        final DetectionData weightData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        weightData.setDetectionType("3");
        weightData.setWeight(weight);
        datas.add(weightData);

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        move2Next();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showUploadDataFailedDialog(weight);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        mUiFragment.setValue(detectionData.getWeight());
    }

    private void move2Next() {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(
                    HealthWeightDetectionUiFragment.this, null);
        }
    }

    private void showUploadDataFailedDialog(float weight) {
        NiceDialog.init()
                .setLayoutId(com.gcml.module_blutooth_devices.R.layout.dialog_first_diagnosis_upload_failed)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.getView(com.gcml.module_blutooth_devices.R.id.btn_neg).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                move2Next();
                                dialog.dismiss();
                            }
                        });
                        holder.getView(com.gcml.module_blutooth_devices.R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadData(weight);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getFragmentManager());
    }
}
