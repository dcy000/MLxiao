package com.gcml.health.measure.first_diagnosis.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.first_diagnosis.bean.ApiResponse;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.manifest.HealthMeasureSPManifest;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.UiUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthDetectionIntelligentReportFragment extends BluetoothBaseFragment {

    private RecyclerView rvReport;
    private Adapter mAdapter;
    private List<DetectionData> cacheDatas;

    public HealthDetectionIntelligentReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheDatas = ((HealthIntelligentDetectionActivity) mActivity).getCacheDatas();
        if (cacheDatas.isEmpty()) {
            ToastUtils.showShortOnTop("您还没有测量哦，快去测量哦！");
            return;
        }
        String json = new Gson().toJson(cacheDatas);
        cacheDatas.clear();
        OkGo.<String>post(HealthMeasureApi.DETECTION_RESULT + HealthMeasureSPManifest.getUserId() + "/")
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.isSuccessful()) {
                            ToastUtils.showLong("服务器繁忙");
                            return;
                        }
                        String body = response.body();
                        try {
                            ApiResponse<List<DetectionResult>> apiResponse = new Gson().fromJson(body,
                                    new TypeToken<ApiResponse<List<DetectionResult>>>() {
                                    }.getType());
                            if (apiResponse.isSuccessful()) {
                                onApiResult(apiResponse.getData());
                            } else {
                                ToastUtils.showLong(apiResponse.getMessage());
                            }
                            return;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        ToastUtils.showLong("服务器繁忙");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showLong("服务器繁忙");
                    }
                });
    }

    private void onApiResult(List<DetectionResult> data) {
        mResults.clear();
        mResults.addAll(data);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_detection_intelligent_report;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rvReport = view.findViewById(R.id.rv_report);
        rvReport.setLayoutManager(new LinearLayoutManager(
                mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new Adapter();
        rvReport.setAdapter(mAdapter);
    }

    private final ArrayList<DetectionResult> mResults = new ArrayList<>();

    private final SparseIntArray layoutIds = new SparseIntArray();

    {
        layoutIds.put(0, R.layout.health_measure_report_item_blood_presssure);
        layoutIds.put(1, R.layout.health_measure_report_item_blood_oxygen);
        layoutIds.put(2, R.layout.health_measure_report_item_blood_oxygen);
        layoutIds.put(3, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_report_item_weight
        layoutIds.put(4, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_report_item_tem);
        layoutIds.put(6, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_measure_report_item_blood_oxygen
        layoutIds.put(7, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_report_item_cholesterin
        layoutIds.put(8, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_report_item_lithic_acid
        layoutIds.put(9, R.layout.health_measure_report_item_blood_oxygen); //R.layout.health_report_item_pulse
    }

    //    0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    private final SparseArray<Class<?>> vhs = new SparseArray<>();

    {
        vhs.put(0, BloodPressureVH.class);
        vhs.put(1, BloodSugarVH.class);
        vhs.put(2, EegVH.class);
        vhs.put(3, WeightVH.class);
        vhs.put(4, TemVH.class);
        vhs.put(6, BloodOxygenVH.class);
        vhs.put(7, CholesterinVH.class);
        vhs.put(8, LithicAcidVH.class);
        vhs.put(9, PulseVH.class);
    }

    private static void applyAnimation(
            ConstraintLayout clContainer,
            ConstraintSet constraintSet,
            int targetId,
            int descId) {
        constraintSet.clone(clContainer);
        constraintSet.connect(targetId, ConstraintSet.START, descId, ConstraintSet.START);
        constraintSet.connect(targetId, ConstraintSet.END, descId, ConstraintSet.END);
        constraintSet.connect(targetId, ConstraintSet.BOTTOM, descId, ConstraintSet.TOP, UiUtils.pt(20));
        constraintSet.applyTo(clContainer);
    }

    private class BloodPressureVH extends VH {

        private final TextView tvTitle;
        private final TextView tvLeftPressure;
        private final TextView tvRightPressure;
        private final TextView tvLeftPulse;
        private final TextView tvRightPulse;
        private final TextView tvResult;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String leftPressureFormat = "左手测量平均值：%s mmHg";
        private String rightPressureFormat = "右手测量平均值：%s mmHg";
        private String leftPulseFormat = "脉搏：%s 次/分";
        private String rightPulseFormat = "脉搏：%s 次/分";

        public BloodPressureVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLeftPressure = itemView.findViewById(R.id.tv_left_pressure);
            tvRightPressure = itemView.findViewById(R.id.tv_right_pressure);
            tvLeftPulse = itemView.findViewById(R.id.tv_left_pulse);
            tvRightPulse = itemView.findViewById(R.id.tv_right_pulse);
            tvResult = itemView.findViewById(R.id.tv_result);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            HealthBloodDetectionUiFragment.Data pressure = ((HealthIntelligentDetectionActivity) mActivity).getBloodpressureCacheData();
            String leftPressure = pressure.leftHighPressure + "/" + pressure.leftLowPressure;
            String rightPressure = pressure.rightHighPressure + "/" + pressure.rightLowPressure;
            String leftPulse = String.valueOf(pressure.leftPulse);
            String rightPulse = String.valueOf(pressure.rightPulse);
            String leftPressureText = String.format(leftPressureFormat, leftPressure);
            String rightPressureText = String.format(rightPressureFormat, rightPressure);
            String leftPulseText = String.format(leftPulseFormat, leftPulse);
            String rightPulseText = String.format(rightPulseFormat, rightPulse);
            tvLeftPressure.setText(leftPressureText);
            tvRightPressure.setText(rightPressureText);
            tvLeftPulse.setText(leftPulseText);
            tvRightPulse.setText(rightPulseText);
            DetectionResult detectionResult = mResults.get(position);
            tvTitle.setText("血压");
            tvResult.setText(detectionResult.getResult());
            String diagnose = detectionResult.getDiagnose();
            View decs = blockMiddle;
            if (("偏低").equals(diagnose)) {
                decs = blockLow;
            } else if (("增高").equals(diagnose)
                    || ("正常高值").equals(diagnose)
                    || ("异常增高").equals(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    private class BloodSugarVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s mmol/L";

        public BloodSugarVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            DetectionResult detectionResult = mResults.get(position);
            tvTitle.setText("血糖");
            String diagnose = detectionResult.getDiagnose();
            tvValue.setText(String.format(sugarFormt, detectionResult.getData().getBloodSugar()));
            View decs = blockMiddle;
            if (("低血糖").equals(diagnose)) {
                decs = blockLow;
            } else if (("糖调节受损").equals(diagnose)
                    || "血糖增高".equals(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    private class EegVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s";

        public EegVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("心电");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            tvValue.setText(String.format(sugarFormt, diagnose));
            View decs = blockMiddle;
            if (("异常").contains(diagnose)) {
                decs = blockLow;
            } else if (("异常").contains(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    private class WeightVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s Kg";

        public WeightVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("体重");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            View decs = blockMiddle;
            if (("偏瘦").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏胖").contains(diagnose)) {
                decs = blockHigh;
            }
            tvValue.setText(String.format(sugarFormt, mResults.get(position).getData().getWeight()));
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    /**
     * 温度
     */
    private class TemVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatordiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();


        public TemVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatordiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("体温");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            View decs = blockMiddle;
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatordiagnose.getId(),
                    decs.getId());
        }
    }

    private class BloodOxygenVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s mmol/L";

        public BloodOxygenVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("血氧");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            View decs = blockMiddle;
            if (!TextUtils.isEmpty(diagnose) && ("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (!TextUtils.isEmpty(diagnose) && ("偏高").contains(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    /**
     * 胆固醇
     */
    private class CholesterinVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s mmol/L";

        public CholesterinVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("胆固醇");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            tvValue.setText(String.format(sugarFormt, detectionResult.getData().getCholesterol()));
            View decs = blockMiddle;
            if (("偏低").equals(diagnose)) {
                decs = blockLow;
            } else if (("偏高").equals(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    /**
     * 血尿酸
     */
    private class LithicAcidVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatordiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String sugarFormt = "测量结果：%s mmol/L";

        public LithicAcidVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatordiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("血尿酸");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            tvValue.setText(String.format(sugarFormt, detectionResult.getData().getUricAcid()));
            View decs = blockMiddle;
            if (("增高").equals(diagnose)) {
                decs = blockHigh;
            } else if (("增高").contains(diagnose)) {
                decs = blockLow;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatordiagnose.getId(),
                    decs.getId());
        }
    }

    private class PulseVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        private String pulseFormat = "脉搏：%s 次/分";

        public PulseVH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            tvTitle.setText("脉搏");
            DetectionResult detectionResult = mResults.get(position);
            String diagnose = detectionResult.getDiagnose();
            tvValue.setText(String.format(pulseFormat, detectionResult.getData().getPulse()));
            View decs = blockMiddle;
            if (!TextUtils.isEmpty(diagnose) && ("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (!TextUtils.isEmpty(diagnose) && ("偏高").contains(diagnose)) {
                decs = blockHigh;
            }
            applyAnimation(
                    (ConstraintLayout) itemView,
                    constraintSet,
                    indicatorDiagnose.getId(),
                    decs.getId());
        }
    }

    private abstract class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }

        public abstract void onBind(int position);
    }

    private class Adapter extends RecyclerView.Adapter<VH> {

        @Override
        public int getItemViewType(int position) {
            return Integer.parseInt(mResults.get(position).getData().getDetectionType());
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layoutId = layoutIds.get(viewType);
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            Class<?> vhClass = vhs.get(viewType);
            try {
                Constructor<?> constructor = vhClass.getConstructor(HealthDetectionIntelligentReportFragment.class, View.class);
                return (VH) constructor.newInstance(HealthDetectionIntelligentReportFragment.this, view);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            }
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }
}
