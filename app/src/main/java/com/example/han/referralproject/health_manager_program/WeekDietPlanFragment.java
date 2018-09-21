package com.example.han.referralproject.health_manager_program;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.intelligent_diagnosis.WeekDietPlan;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/16.
 */

public class WeekDietPlanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RadioButton mMonday;
    private RadioButton mTuesday;
    private RadioButton mWednesday;
    private RadioButton mThursday;
    private RadioButton mFriday;
    private RadioButton mSaturday;
    private RadioButton mSunday;
    private RadioGroup mRg;
    private TextView mTvZaocan;
    private ImageView mIv1;
    private TextView mTvBreakfast;
    private TextView mTvWucan;
    private ImageView mIv2;
    private TextView mTvLunch;
    private TextView mTvWancan;
    private ImageView mIv3;
    private TextView mTvDinner;
    private WeekDietPlan cacheWeekDietPlan;
    private IChangToolbar iChangToolbar;
    private String TAG = "WeekDietPlanFragment";

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_week_diet_plan, container, false);
            getData();
            initView(view);
        }
        return view;
    }

    private void getData() {
        Log.e(TAG, "getDataCache: ");
        OkGo.<String>get(NetworkApi.WeekHealthDietPlan)
                .params("userId", UserSpHelper.getUserId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                WeekDietPlan data = new Gson().fromJson(object.optJSONObject("data")
                                        .toString(), WeekDietPlan.class);
                                dealData(data);
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

    private void dealData(WeekDietPlan data) {
        if (data == null) {
            return;
        }
        cacheWeekDietPlan = data;
        WeekDietPlan.MondayCookbookBean mondayCookbook = data.getMondayCookbook();
        if (mondayCookbook != null) {
            String breakfast = mondayCookbook.getBreakfast();
            String lunch = mondayCookbook.getLunch();
            String dinner = mondayCookbook.getDinner();
            if (!TextUtils.isEmpty(breakfast)) {
                mTvBreakfast.setText(ToDBC(breakfast));
            }
            if (!TextUtils.isEmpty(ToDBC(lunch))) {
                mTvLunch.setText(lunch);
            }
            if (!TextUtils.isEmpty(ToDBC(dinner))) {
                mTvDinner.setText(dinner);
            }
        }
    }
    private   String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: ");
//            if (cacheWeekDietPlan != null) {
//                ((TreatmentPlanActivity) getActivity()).speak(
//                        "主人，为了您的健康，我们精心为您准备了每天的健康食谱，请点击上方的按钮查看详情");
//            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {

        mMonday = view.findViewById(R.id.monday);
        mMonday.setChecked(true);
        mMonday.setOnClickListener(this);
        mTuesday = view.findViewById(R.id.tuesday);
        mTuesday.setOnClickListener(this);
        mWednesday = view.findViewById(R.id.wednesday);
        mWednesday.setOnClickListener(this);
        mThursday = view.findViewById(R.id.thursday);
        mThursday.setOnClickListener(this);
        mFriday = view.findViewById(R.id.friday);
        mFriday.setOnClickListener(this);
        mSaturday = view.findViewById(R.id.saturday);
        mSaturday.setOnClickListener(this);
        mSunday = view.findViewById(R.id.sunday);
        mSunday.setOnClickListener(this);
        mRg = view.findViewById(R.id.rg);
        mTvZaocan = view.findViewById(R.id.tv_zaocan);
        mIv1 = view.findViewById(R.id.iv_1);
        mTvBreakfast = view.findViewById(R.id.tv_breakfast);
        mTvWucan = view.findViewById(R.id.tv_wucan);
        mIv2 = view.findViewById(R.id.iv_2);
        mTvLunch = view.findViewById(R.id.tv_lunch);
        mTvWancan = view.findViewById(R.id.tv_wancan);
        mIv3 = view.findViewById(R.id.iv_3);
        mTvDinner = view.findViewById(R.id.tv_dinner);
    }

    @Override
    public void onClick(View v) {
        if (cacheWeekDietPlan == null) {
            return;
        }
        switch (v.getId()) {
            default:
                break;
            case R.id.monday:
                WeekDietPlan.MondayCookbookBean mondayCookbook = cacheWeekDietPlan.getMondayCookbook();
                if (mondayCookbook != null) {
                    String breakfast = mondayCookbook.getBreakfast();
                    String lunch = mondayCookbook.getLunch();
                    String dinner = mondayCookbook.getDinner();
                    setContent(breakfast, lunch, dinner);
                }
                break;
            case R.id.tuesday:
                WeekDietPlan.TuesdayCookbookBean tuesdayCookbook = cacheWeekDietPlan.getTuesdayCookbook();
                if (tuesdayCookbook != null) {
                    String breakfast = tuesdayCookbook.getBreakfast();
                    String lunch = tuesdayCookbook.getLunch();
                    String dinner = tuesdayCookbook.getDinner();
                    setContent(breakfast, lunch, dinner);
                }
                break;
            case R.id.wednesday:
                WeekDietPlan.WednesdayCookbookBean wednesdayCookbook = cacheWeekDietPlan.getWednesdayCookbook();
                if (wednesdayCookbook != null) {
                    String breakfast = wednesdayCookbook.getBreakfast();
                    String lunch = wednesdayCookbook.getLunch();
                    String dinner = wednesdayCookbook.getDinner();
                    setContent(breakfast, lunch, dinner);
                }
                break;
            case R.id.thursday:
                WeekDietPlan.ThursdayCookbookBean thursdayCookbook = cacheWeekDietPlan.getThursdayCookbook();
                if (thursdayCookbook != null) {
                    String breakfast = thursdayCookbook.getBreakfast();
                    String lunch = thursdayCookbook.getLunch();
                    String dinner = thursdayCookbook.getDinner();
                    setContent(breakfast, lunch, dinner);
                }
                break;
            case R.id.friday:
                WeekDietPlan.FridayCookbookBean fridayCookbook = cacheWeekDietPlan.getFridayCookbook();
                if (fridayCookbook != null) {
                    setContent(fridayCookbook.getBreakfast(), fridayCookbook.getLunch(), fridayCookbook.getDinner());
                }
                break;
            case R.id.saturday:
                WeekDietPlan.SaturdayCookbookBean saturdayCookbook = cacheWeekDietPlan.getSaturdayCookbook();
                if (saturdayCookbook != null) {
                    setContent(saturdayCookbook.getBreakfast(), saturdayCookbook.getLunch(), saturdayCookbook.getDinner());
                }
                break;
            case R.id.sunday:
                WeekDietPlan.SundayCookbookBean sundayCookbook = cacheWeekDietPlan.getSundayCookbook();
                if (sundayCookbook != null) {
                    setContent(sundayCookbook.getBreakfast(), sundayCookbook.getLunch(), sundayCookbook.getDinner());
                }
                break;
        }
    }

    private void setContent(String breakfast, String lunch, String dinner) {
        if (!TextUtils.isEmpty(breakfast)) {
            mTvBreakfast.setText(breakfast);

        }
        if (!TextUtils.isEmpty(lunch)) {
            mTvLunch.setText(lunch);
        }
        if (!TextUtils.isEmpty(dinner)) {
            mTvDinner.setText(dinner);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
