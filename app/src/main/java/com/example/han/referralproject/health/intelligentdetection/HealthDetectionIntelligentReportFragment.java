package com.example.han.referralproject.health.intelligentdetection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionResult;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.UiUtils;
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
public class HealthDetectionIntelligentReportFragment extends Fragment {

    private ImageView ivRight;
    private RecyclerView rvReport;
    private Adapter mAdapter;

    public HealthDetectionIntelligentReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkGo.<String>post(NetworkApi.DETECTION_RESULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.isSuccessful()) {
                            ToastUtils.showLong("服务器繁忙");
                            return;
                        }
                        String body = response.body();
                        try {
                            ApiResponse<List<DetectionResult>> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<List<DetectionResult>>>() {
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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private int layoutId() {
        return R.layout.health_fragment_detection_intelligent_report;
    }

    private void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.healthy_report);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        rvReport = ((RecyclerView) view.findViewById(R.id.rv_report));
        rvReport.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new Adapter();
        rvReport.setAdapter(mAdapter);
    }

    private final ArrayList<DetectionResult> mResults = new ArrayList<>();

    private final SparseIntArray layoutIds = new SparseIntArray();

    {
        layoutIds.put(0, R.layout.health_report_item_blood_presssure);
        layoutIds.put(1, R.layout.health_report_item_blood_oxygen);
        layoutIds.put(2, R.layout.health_report_item_ecg);
        layoutIds.put(3, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_weight
        layoutIds.put(4, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_tem);
        layoutIds.put(6, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_blood_oxygen
        layoutIds.put(7, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_cholesterin
        layoutIds.put(8, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_lithic_acid
        layoutIds.put(9, R.layout.health_report_item_blood_oxygen); //R.layout.health_report_item_pulse
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

        public BloodPressureVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLeftPressure = (TextView) itemView.findViewById(R.id.tv_left_pressure);
            tvRightPressure = (TextView) itemView.findViewById(R.id.tv_right_pressure);
            tvLeftPulse = (TextView) itemView.findViewById(R.id.tv_left_pulse);
            tvRightPulse = (TextView) itemView.findViewById(R.id.tv_right_pulse);
            tvResult = (TextView) itemView.findViewById(R.id.tv_result);
            blockLow = itemView.findViewById(R.id.block_low);
            blockHigh = itemView.findViewById(R.id.block_high);
            blockMiddle = itemView.findViewById(R.id.block_middle);
            indicatorDiagnose = itemView.findViewById(R.id.indicator_diagnose);
        }

        @Override
        public void onBind(int position) {
            DetectionResult detectionResult = mResults.get(position);
            tvTitle.setText("血压");
            tvResult.setText(detectionResult.getResult());
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

        public BloodSugarVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            View decs = blockMiddle;
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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

        public EegVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            View decs = blockMiddle;
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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

        public WeightVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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

        public BloodOxygenVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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

        public CholesterinVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            View decs = blockMiddle;
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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

        public LithicAcidVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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

    private class PulseVH extends VH {

        private final TextView tvTitle;
        private final TextView tvValue;
        private final View blockLow;
        private final View blockHigh;
        private final View blockMiddle;
        private final View indicatorDiagnose;
        private ConstraintSet constraintSet = new ConstraintSet();

        public PulseVH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
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
            View decs = blockMiddle;
            if (("偏低").contains(diagnose)) {
                decs = blockLow;
            } else if (("偏高").contains(diagnose)) {
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
                Constructor<?> constructor = vhClass.getConstructor(View.class);
                return (VH) constructor.newInstance(view);
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
