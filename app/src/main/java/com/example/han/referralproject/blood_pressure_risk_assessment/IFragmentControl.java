package com.example.han.referralproject.blood_pressure_risk_assessment;

import android.support.v4.app.Fragment;

import com.example.han.referralproject.bean.QuestionChoosed;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public interface IFragmentControl {
    void stepNext(Fragment fragment, List<QuestionChoosed> lists);
}
