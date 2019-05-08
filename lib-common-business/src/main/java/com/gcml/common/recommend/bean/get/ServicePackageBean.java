package com.gcml.common.recommend.bean.get;

public class ServicePackageBean {

    /**
     * seq : 0
     * createdBy : admin
     * createdOn : 1548148125000
     * modifiedBy : admin
     * modifiedOn : 1548148125000
     * deletionState : 0
     * setmealId : 1811e350-6d1c-475f-bc88-fe3239a30b13
     * userid : 100001
     * type : 3
     * effectiveTime : 2019-01-22 17:08:45
     * effectiveSign : 1
     */

    private int seq;
    private String createdBy;
    private long createdOn;
    private String modifiedBy;
    private long modifiedOn;
    private String deletionState;
    private String setmealId;
    private int userid;
    public int orderid;
    private String type;
    private String effectiveTime;
    private String effectiveSign;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getDeletionState() {
        return deletionState;
    }

    public void setDeletionState(String deletionState) {
        this.deletionState = deletionState;
    }

    public String getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(String setmealId) {
        this.setmealId = setmealId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getEffectiveSign() {
        return effectiveSign;
    }

    public void setEffectiveSign(String effectiveSign) {
        this.effectiveSign = effectiveSign;
    }
}
