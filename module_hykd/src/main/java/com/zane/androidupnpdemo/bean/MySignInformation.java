package com.zane.androidupnpdemo.bean;

import java.util.List;

/**
 * Created by gzq on 2018/4/13.
 */

public class MySignInformation {
    public int doctorId;
    public String doctorName;
    public int orgId;
    public String orgName;
    public int residentId;
    public int signId;
    public int teamId;
    public String signTime;
    public String teamName;
    public List<TeamDoc> doctorTeamDtos;
    public static class TeamDoc{
        public String docTitle;
        public String doctorName;
        public boolean isLeader;
    }
}
