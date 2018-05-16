package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BloodsugarWeeklyReport3Fragment extends Fragment{
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bloodsugar_weekly_report_fragment, container, false);
        return view;
    }

    public void notifyData(WeeklyOrMonthlyBloodsugarReport report) {
        if (report==null){
            return;
        }

    }
}
