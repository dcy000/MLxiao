package com.gcml.module_health_profile.bean;

public class HealthRecordBean {

    /**
     * seq : 0
     * createdOn : 1547729881000
     * createdTime : 2019-01-17 20:58:01
     * rdUserRecordId : a5c0654e2f774a049d2f047d0660beb0
     * rdRecordId : 76e9139bf448430bbcb98d5998db05c4
     * userId : 130396
     * followUpDoctor : 10001
     * responsibleDoctor : 10064
     */

    private int seq;
    private long createdOn;
    private String createdTime;
    private long modifiedOn;
    private String modifiedTime;
    private String rdUserRecordId;
    private String rdRecordId;
    private int userId;
    private int followUpDoctor;
    private int responsibleDoctor;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getRdUserRecordId() {
        return rdUserRecordId;
    }

    public void setRdUserRecordId(String rdUserRecordId) {
        this.rdUserRecordId = rdUserRecordId;
    }

    public String getRdRecordId() {
        return rdRecordId;
    }

    public void setRdRecordId(String rdRecordId) {
        this.rdRecordId = rdRecordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFollowUpDoctor() {
        return followUpDoctor;
    }

    public void setFollowUpDoctor(int followUpDoctor) {
        this.followUpDoctor = followUpDoctor;
    }

    public int getResponsibleDoctor() {
        return responsibleDoctor;
    }

    public void setResponsibleDoctor(int responsibleDoctor) {
        this.responsibleDoctor = responsibleDoctor;
    }

    public long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
