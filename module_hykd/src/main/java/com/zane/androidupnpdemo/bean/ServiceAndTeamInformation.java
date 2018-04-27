package com.zane.androidupnpdemo.bean;

/**
 * Created by gzq on 2018/4/13.
 */

public class ServiceAndTeamInformation {

    /**
     * serviceCount : 1
     * hospitalTeam : {"doctorId":0,"doctorName":"付远鹏_执业医师,王先林_助理医师,李永科_乡村医生,陈福万_乡村医生,徐芬_主管护士","hospitalTeamId":109,"hospitalTeamName":"大安街道办事处铜鼓村家庭医生团队","id":109,"orgAddress":"重庆市永川区大安街道同安大道","orgId":21,"orgName":"重庆市永川区大安街道社区卫生服务中心","rangeId":0,"rangeType":0,"servicePerson":0,"signPerson":0}
     * signInfoFlow : {"areaId":20,"createManagerId":1740,"createName":"陈福万","createTime":"2017-07-14 18:29:30","doctorId":1740,"endTime":null,"familyId":1443563,"fileUrl":"","hospitalTeamId":109,"id":128497,"isPaper":true,"isPay":false,"orgId":21,"paymentPackage":"","residentId":3347597,"residentType":2,"signTime":"2018-02-01 00:00:00","startTime":null,"status":1}
     */

    private int serviceCount;
    private HospitalTeamBean hospitalTeam;
    private SignInfoFlowBean signInfoFlow;

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public HospitalTeamBean getHospitalTeam() {
        return hospitalTeam;
    }

    public void setHospitalTeam(HospitalTeamBean hospitalTeam) {
        this.hospitalTeam = hospitalTeam;
    }

    public SignInfoFlowBean getSignInfoFlow() {
        return signInfoFlow;
    }

    public void setSignInfoFlow(SignInfoFlowBean signInfoFlow) {
        this.signInfoFlow = signInfoFlow;
    }

    public static class HospitalTeamBean {
        /**
         * doctorId : 0
         * doctorName : 付远鹏_执业医师,王先林_助理医师,李永科_乡村医生,陈福万_乡村医生,徐芬_主管护士
         * hospitalTeamId : 109
         * hospitalTeamName : 大安街道办事处铜鼓村家庭医生团队
         * id : 109
         * orgAddress : 重庆市永川区大安街道同安大道
         * orgId : 21
         * orgName : 重庆市永川区大安街道社区卫生服务中心
         * rangeId : 0
         * rangeType : 0
         * servicePerson : 0
         * signPerson : 0
         */

        private int doctorId;
        private String doctorName;
        private int hospitalTeamId;
        private String hospitalTeamName;
        private int id;
        private String orgAddress;
        private int orgId;
        private String orgName;
        private int rangeId;
        private int rangeType;
        private int servicePerson;
        private int signPerson;

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public int getHospitalTeamId() {
            return hospitalTeamId;
        }

        public void setHospitalTeamId(int hospitalTeamId) {
            this.hospitalTeamId = hospitalTeamId;
        }

        public String getHospitalTeamName() {
            return hospitalTeamName;
        }

        public void setHospitalTeamName(String hospitalTeamName) {
            this.hospitalTeamName = hospitalTeamName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrgAddress() {
            return orgAddress;
        }

        public void setOrgAddress(String orgAddress) {
            this.orgAddress = orgAddress;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public int getRangeId() {
            return rangeId;
        }

        public void setRangeId(int rangeId) {
            this.rangeId = rangeId;
        }

        public int getRangeType() {
            return rangeType;
        }

        public void setRangeType(int rangeType) {
            this.rangeType = rangeType;
        }

        public int getServicePerson() {
            return servicePerson;
        }

        public void setServicePerson(int servicePerson) {
            this.servicePerson = servicePerson;
        }

        public int getSignPerson() {
            return signPerson;
        }

        public void setSignPerson(int signPerson) {
            this.signPerson = signPerson;
        }
    }

    public static class SignInfoFlowBean {
        /**
         * areaId : 20
         * createManagerId : 1740
         * createName : 陈福万
         * createTime : 2017-07-14 18:29:30
         * doctorId : 1740
         * endTime : null
         * familyId : 1443563
         * fileUrl :
         * hospitalTeamId : 109
         * id : 128497
         * isPaper : true
         * isPay : false
         * orgId : 21
         * paymentPackage :
         * residentId : 3347597
         * residentType : 2
         * signTime : 2018-02-01 00:00:00
         * startTime : null
         * status : 1
         */

        private int areaId;
        private int createManagerId;
        private String createName;
        private String createTime;
        private int doctorId;
        private Object endTime;
        private int familyId;
        private String fileUrl;
        private int hospitalTeamId;
        private int id;
        private boolean isPaper;
        private boolean isPay;
        private int orgId;
        private String paymentPackage;
        private int residentId;
        private int residentType;
        private String signTime;
        private Object startTime;
        private int status;

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public int getCreateManagerId() {
            return createManagerId;
        }

        public void setCreateManagerId(int createManagerId) {
            this.createManagerId = createManagerId;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public int getFamilyId() {
            return familyId;
        }

        public void setFamilyId(int familyId) {
            this.familyId = familyId;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getHospitalTeamId() {
            return hospitalTeamId;
        }

        public void setHospitalTeamId(int hospitalTeamId) {
            this.hospitalTeamId = hospitalTeamId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsPaper() {
            return isPaper;
        }

        public void setIsPaper(boolean isPaper) {
            this.isPaper = isPaper;
        }

        public boolean isIsPay() {
            return isPay;
        }

        public void setIsPay(boolean isPay) {
            this.isPay = isPay;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getPaymentPackage() {
            return paymentPackage;
        }

        public void setPaymentPackage(String paymentPackage) {
            this.paymentPackage = paymentPackage;
        }

        public int getResidentId() {
            return residentId;
        }

        public void setResidentId(int residentId) {
            this.residentId = residentId;
        }

        public int getResidentType() {
            return residentType;
        }

        public void setResidentType(int residentType) {
            this.residentType = residentType;
        }

        public String getSignTime() {
            return signTime;
        }

        public void setSignTime(String signTime) {
            this.signTime = signTime;
        }

        public Object getStartTime() {
            return startTime;
        }

        public void setStartTime(Object startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
