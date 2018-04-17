package com.zane.androidupnpdemo.bean;

import java.util.List;

/**
 * Created by gzq on 2018/4/17.
 */

public class ServiceHistoryInformation {

    /**
     * agreement : 1
     * bloodPressure : 126/84mmHg
     * bloodSugar : mmol/L
     * contactCard : 1
     * crowd : 2
     * crowdSpecial : ["高血压患者","65岁以上老年人"]
     * healthy : 高血压.
     * interview : 1
     * interviewPhone :
     * interviewSign :
     * otherServices :
     * residentAge : 83
     * residentId : 3347597
     * residentName : 罗正碧
     * residentPhone : 15223034192
     * residentSex : 女
     * serviceContent : [{"list":["建立居民健康档案","测血压","评估是否存在危急情况","指导合理饮食、健康活动、规范服药","发放健康教育宣传资料","健康体检"],"title":"高血压患者"},{"list":["建立居民健康档案","测血压","测空腹血糖","生活方式和健康状况评估","指导合理饮食、健康活动、规范服药","发放健康教育宣传资料","健康体检","中医体质辨识","中医药保健指导"],"title":"65岁以上老年人"},{"list":[],"title":"重大公共卫生服务"}]
     * serviceTime : 2017-07-14 00:00:00
     */

    private int agreement;
    private String bloodPressure;
    private String bloodSugar;
    private int contactCard;
    private int crowd;
    private String healthy;
    private int interview;
    private String interviewPhone;
    private String interviewSign;
    private String otherServices;
    private String residentAge;
    private int residentId;
    private String residentName;
    private String residentPhone;
    private String residentSex;
    private String serviceTime;
    private List<String> crowdSpecial;
    private List<ServiceContentBean> serviceContent;

    public int getAgreement() {
        return agreement;
    }

    public void setAgreement(int agreement) {
        this.agreement = agreement;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(String bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public int getContactCard() {
        return contactCard;
    }

    public void setContactCard(int contactCard) {
        this.contactCard = contactCard;
    }

    public int getCrowd() {
        return crowd;
    }

    public void setCrowd(int crowd) {
        this.crowd = crowd;
    }

    public String getHealthy() {
        return healthy;
    }

    public void setHealthy(String healthy) {
        this.healthy = healthy;
    }

    public int getInterview() {
        return interview;
    }

    public void setInterview(int interview) {
        this.interview = interview;
    }

    public String getInterviewPhone() {
        return interviewPhone;
    }

    public void setInterviewPhone(String interviewPhone) {
        this.interviewPhone = interviewPhone;
    }

    public String getInterviewSign() {
        return interviewSign;
    }

    public void setInterviewSign(String interviewSign) {
        this.interviewSign = interviewSign;
    }

    public String getOtherServices() {
        return otherServices;
    }

    public void setOtherServices(String otherServices) {
        this.otherServices = otherServices;
    }

    public String getResidentAge() {
        return residentAge;
    }

    public void setResidentAge(String residentAge) {
        this.residentAge = residentAge;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
    }

    public String getResidentSex() {
        return residentSex;
    }

    public void setResidentSex(String residentSex) {
        this.residentSex = residentSex;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public List<String> getCrowdSpecial() {
        return crowdSpecial;
    }

    public void setCrowdSpecial(List<String> crowdSpecial) {
        this.crowdSpecial = crowdSpecial;
    }

    public List<ServiceContentBean> getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(List<ServiceContentBean> serviceContent) {
        this.serviceContent = serviceContent;
    }

    public static class ServiceContentBean {
        /**
         * list : ["建立居民健康档案","测血压","评估是否存在危急情况","指导合理饮食、健康活动、规范服药","发放健康教育宣传资料","健康体检"]
         * title : 高血压患者
         */

        private String title;
        private List<String> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}
