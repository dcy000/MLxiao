package com.example.han.referralproject.intelligent_system.blood_pressure_risk_assessment;

import android.support.v4.app.Fragment;


import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public interface IFragmentControl {
    void stepNext(Fragment fragment, List<QuestionChoosed> lists);
}
