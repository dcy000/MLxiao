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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.referralproject.R;
import com.example.han.referralproject.intelligent_diagnosis.LastWeekAllReport;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.utils.display.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.megvii.faceppidcardui.imageview.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/5/15.
 */

public class LastWeekHealthReportFragment extends Fragment {
    private View view;
    private String TAG = "LastWeekHealthReportFragment";
    private CircleImageView head;
    private TextView name;
    private TextView age;
    private TextView sex;
    private ImageView qrCode;
    private TextView weight;
    private TextView tvXueyaTitle;
    private TextView tvGaoya;
    private TextView tvDiya;
    private TextView tvXuetangTitle;
    private TextView tvXuetangEmpty;
    private TextView tvXuetangOne;
    private TextView tvXuetangTwo;
    private IChangToolbar iChangToolbar;
    private LastWeekAllReport data;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.activity_life_treatment_program, container, false);
            getData();
            initView(view);
        }

        return view;
    }

    private void getData() {
        Log.e(TAG, "getDataCache: ");
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.WEEK_OF_YEAR, curr.get(Calendar.WEEK_OF_YEAR) - 1);
        long weekAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.LastWeekAllReport)
                .params("userId", UserSpHelper.getUserId())
                .params("timeStamp", weekAgoTime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                LastWeekAllReport data = new Gson().fromJson(object.optJSONObject("data").toString(), LastWeekAllReport.class);
                                dealData(data);
                            } else if (object.optInt("code") == 500) {
                                ToastUtils.showShort("暂无上周健康数据");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e(TAG, "onError: " + response.message());
                    }
                });
    }

    private void dealData(LastWeekAllReport data) {
        if (data == null) {
            ((TreatmentPlanActivity) getActivity()).speak("主人，暂无上周健康数据");
            return;
        }
        this.data = data;
        LastWeekAllReport.UserBean user = data.getUser();
        if (user != null) {
            String user_photo = user.getUser_photo();
            if (!TextUtils.isEmpty(user_photo) && isAdded()) {
                Glide.with(getContext().getApplicationContext())
                        .load(user_photo)
                        .into(head);
            }
            String bname = user.getBname();
            if (!TextUtils.isEmpty(bname)) {
                name.setText(bname);
            }
            int int_age = user.getAge();
            age.setText("年龄：" + int_age + "岁");
            String string_sex = user.getSex();
            if (!TextUtils.isEmpty(string_sex)) {
                sex.setText("性别：" + string_sex);
            }

            double dou_weight = data.getWeight();
            weight.setText(String.format("%.2f", dou_weight));

            int highPressure = data.getHighPressure();
            tvGaoya.setText(highPressure + "");
            int lowPressure = data.getLowPressure();
            tvDiya.setText(lowPressure + "");

            double bloodSugar = data.getBloodSugar();
            tvXuetangEmpty.setText(String.format("%.2f", bloodSugar));
            double bloodSugarOne = data.getBloodSugarOne();
            tvXuetangOne.setText(String.format("%.2f", bloodSugarOne));
            double bloodSugarTwo = data.getBloodSugarTwo();
            tvXuetangTwo.setText(String.format("%.2f", bloodSugarTwo));
            ((TreatmentPlanActivity) getActivity()).speak("主人，您上周的平均体重" + String.format("%.2f", dou_weight)
                    + "千克，平均收缩压" + highPressure + ",平均舒张压" + lowPressure + ",空腹平均血糖"
                    + String.format("%.2f", bloodSugar) + ",饭后一小时平均血糖" + String.format("%.2f", bloodSugarOne) +
                    "饭后两小时平均血糖" + String.format("%.2f", bloodSugarTwo));
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: ");
            if (data != null) {
                ((TreatmentPlanActivity) getActivity()).speak("主人，您上周的平均体重" + String.format("%.2f", data.getUser().getWeight())
                        + "千克，平均收缩压" + data.getHighPressure() + ",平均舒张压" + data.getLowPressure() + ",空腹平均血糖"
                        + String.format("%.2f", data.getBloodSugar()) + ",饭后一小时平均血糖" + String.format("%.2f", data.getBloodSugarOne()) +
                        "饭后两小时平均血糖" + String.format("%.2f", data.getBloodSugarTwo()));
            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {

        head = view.findViewById(R.id.head);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        sex = view.findViewById(R.id.sex);
        qrCode = view.findViewById(R.id.qr_code);
        weight = view.findViewById(R.id.weight);
        tvXueyaTitle = view.findViewById(R.id.tv_xueya_title);
        tvGaoya = view.findViewById(R.id.tv_gaoya);
        tvDiya = view.findViewById(R.id.tv_diya);
        tvXuetangTitle = view.findViewById(R.id.tv_xuetang_title);
        tvXuetangEmpty = view.findViewById(R.id.tv_xuetang_empty);
        tvXuetangOne = view.findViewById(R.id.tv_xuetang_one);
        tvXuetangTwo = view.findViewById(R.id.tv_xuetang_two);

        weight.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvXuetangEmpty.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvXuetangOne.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        tvXuetangTwo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
