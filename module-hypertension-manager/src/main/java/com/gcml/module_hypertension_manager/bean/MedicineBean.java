package com.gcml.module_hypertension_manager.bean;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/28 19:18
 * created by:gzq
 * description:TODO
 */
public class MedicineBean {

    /**
     * level : 33
     * drugs : ["阿替洛尔","尼群地平","卡托普利","氢氯噻嗪等"]
     * advice : 通常2-3种降压药联用。具体用药方案请咨询健康顾问。
     */

    private String level;
    private String advice;
    private List<String> drugs;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public List<String> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<String> drugs) {
        this.drugs = drugs;
    }
}
