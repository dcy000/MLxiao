package com.zane.androidupnpdemo.bean;

import java.util.List;

/**
 * Created by gzq on 2018/4/13.
 */

public class MyBaseInformation {
    public String way;
    public String isSign;
    public ResidentRecord ResidentRecords;
    public List<Crowd> selectCrowd;
    public static class ResidentRecord{
        public String address;
        public String addressCurrent;
        public String bday;
        public String birthday;
        public String changeDate;
        public String checkDate;
        public String checkName;
        public String contacts;
        public String contactsPhone;
        public String crowdIds;
        public String crowdNames;
        public String doctorName;
        public String education;
        public String email;
        public String familyRelation;
        public String healthFileCode;
        public String identityCard;
        public String lastChangeDoctorName;
        public String maritalStatus;
        public String name;
        public String nation;
        public String phone;
        public String postcode;
        public String profession;
        public String recordDate;
        public String recordName;
        public String recordUnit;
        public String registerDate;
        public String sex;
        public String tcmDate;
        public String tcmName;
        public String townName;
        public String varOrgId;
        public String varid;
        public String villageName;
        public String workAddress;
        public int villageId;
        public int townId;
        public int status;
        public int sourceCode;
        public int residentType;
        public int residentId;
        public int orgId;
        public int migrant;
        public int lastChangeDoctorId;
        public int id;
        public int houseRalation;
        public int familyId;
        public int dsmId;
        public int attention;
        public int age;
        public int addressType;
    }
    public static class Crowd{
        public String createTime;
        public String crowdCode;
        public String crowdDesc;
        public String crowdName;
        public String crowdTemplate;
        public String updateTime;
        public String varid;
        public int createManagerId;
        public int id;
        public int updateManagerId;
    }
}
