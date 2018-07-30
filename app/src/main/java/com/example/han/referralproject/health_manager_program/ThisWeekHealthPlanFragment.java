package com.example.han.referralproject.health_manager_program;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.intelligent_diagnosis.IChangToolbar;
import com.example.han.referralproject.intelligent_diagnosis.ThisWeekHealthPlan;
import com.example.han.referralproject.network.NetworkApi;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.alarm.AlarmDetail2Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ThisWeekHealthPlanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView mTvDetectionFrequency;
    private TextView mTvXueyaDetectionFrequency;
    private TextView mTvXuetangDetectionFrequency;
    private TextView mWeight;
    private TextView mTvXueyaTitle;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvXuetangTitle;
    private TextView mTvXuetangEmpty;
    private TextView mTvXuetangOne;
    private TextView mTvXuetangTwo;
    private IChangToolbar iChangToolbar;
    private String TAG = "ThisWeekHealthPlanFragment";
    private TextView mTvSetAlarm;
    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    private ThisWeekHealthPlan data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_detection_plan, container, false);
            getData();
            initView(view);
        }
        return view;
    }

    private void getData() {
        Log.e(TAG, "getData: ");
        OkGo.<String>get(NetworkApi.ThisWeekPlan)
                .params("userId", MyApplication.getInstance().userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                ThisWeekHealthPlan data = new Gson().fromJson(object.optJSONObject("data").toString(), ThisWeekHealthPlan.class);
                                detalData(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    private void detalData(ThisWeekHealthPlan data) {
        if (data == null) {
            return;
        }
        this.data = data;
        String hypertensionFrequency = data.getHypertensionFrequency();
        if (!TextUtils.isEmpty(hypertensionFrequency)) {
            mTvXueyaDetectionFrequency.setText("血压：" + hypertensionFrequency);
        }
        String diabetesFrequency = data.getDiabetesFrequency();
        if (!TextUtils.isEmpty(diabetesFrequency)) {
            mTvXuetangDetectionFrequency.setText("血糖：" + diabetesFrequency);
        }
        double weightTarget = data.getWeightTarget();
        mWeight.setText(String.format("%.2f", weightTarget));
        int highPressureTarget = data.getHighPressureTarget();
        mTvGaoya.setText(highPressureTarget + "");
        int lowPressureTarget = data.getLowPressureTarget();
        mTvDiya.setText(lowPressureTarget + "");
        double bloodSugarTarget = data.getBloodSugarTarget();
        mTvXuetangEmpty.setText(String.format("%.2f", bloodSugarTarget));
        double bloodSugarOneTarget = data.getBloodSugarOneTarget();
        mTvXuetangOne.setText(String.format("%.2f", bloodSugarOneTarget));
        double bloodSugarTwoTarget = data.getBloodSugarTwoTarget();
        mTvXuetangTwo.setText(String.format("%.2f", bloodSugarTwoTarget));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: ");
            if (data != null) {
                ((TreatmentPlanActivity) getActivity()).speak("主人，小易为您制定了本周的健康计划。血压" + data.getHypertensionFrequency() + ",血糖" +
                        data.getDiabetesFrequency() + ",本周体重目标" + String.format("%.2f", data.getWeightTarget()) + "千克，高压目标"
                        + data.getHighPressureTarget() + ",低压" + data.getLowPressureTarget() + ",血糖目标" + String.format("%.2f", data.getBloodSugarTarget()));
            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {
        mTvDetectionFrequency = (TextView) view.findViewById(R.id.tv_detection_frequency);
        mTvXueyaDetectionFrequency = (TextView) view.findViewById(R.id.tv_xueya_detection_frequency);
        mTvXuetangDetectionFrequency = (TextView) view.findViewById(R.id.tv_xuetang_detection_frequency);
        mWeight = (TextView) view.findViewById(R.id.weight);
        mTvXueyaTitle = (TextView) view.findViewById(R.id.tv_xueya_title);
        mTvGaoya = (TextView) view.findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) view.findViewById(R.id.tv_diya);
        mTvXuetangTitle = (TextView) view.findViewById(R.id.tv_xuetang_title);
        mTvXuetangEmpty = (TextView) view.findViewById(R.id.tv_xuetang_empty);
        mTvXuetangOne = (TextView) view.findViewById(R.id.tv_xuetang_one);
        mTvXuetangTwo = (TextView) view.findViewById(R.id.tv_xuetang_two);
        mTvSetAlarm=(TextView)view.findViewById(R.id.tv_set_alarm);
        mTvSetAlarm.setOnClickListener(this);

        mWeight.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangEmpty.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangOne.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangTwo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.tv_set_alarm){
            AlarmDetail2Activity.newLaunchIntent(getContext(),-1);
        }
    }
}
