package com.example.han.referralproject.intelligent_diagnosis;

import android.support.annotation.NonNull;

/**
 * Created by gzq on 2018/3/19.
 */

public class HealthCompare implements Comparable<HealthCompare>{
    public float fenshu;
    public String tips;

    @Override
    public int compareTo(@NonNull HealthCompare o) {
        return (int) (o.fenshu-fenshu);
    }

    public HealthCompare(float fenshu, String tips) {
        this.fenshu = fenshu;
        this.tips = tips;
    }
}
