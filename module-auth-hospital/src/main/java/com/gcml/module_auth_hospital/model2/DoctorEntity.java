package com.gcml.module_auth_hospital.model2;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/2/28.
 */

public class DoctorEntity implements Serializable {
    public String adds;
    public Double amount;
    public Double apply_amount;
    public String card;
    public String department;
    public String docter_photo;
    public Integer docterid;
    public String doctername;
    public String documents;
    public String duty;
    public Integer evaluation;
    public String gat;
    public String hosname;
    public Integer number;
    public Integer online_status;
    public Integer pend;
    public Integer priority;
    public String pro;
    public RBean r;
    public Double service_amount;
    public Integer state;
    public String tel;
    public String tj;
    public String wyyxId;
    public String wyyxPwd;

    public static class RBean implements Serializable {
        /**
         * rankid : 0
         * rankname : string
         */

        public int rankid;
        public String rankname;
    }
}
