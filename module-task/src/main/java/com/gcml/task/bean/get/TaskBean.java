package com.gcml.task.bean.get;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wecent on 2018/8/15.
 * 商品信息
 */

public class TaskBean implements Serializable {

    /**
     * taskList : [{"complianceTaskId":"030032b8-7f44-447e-adbf-049292385659","userId":100206,"name":"运动控制","taskDate":1535040000000,"taskType":"33","illnessType":null,"remindStatus":"0","remindStart":1535040000000,"remindEnd":1535126340000,"mustStatus":"1","complitionStatus":"0","complitionTime":null},{"complianceTaskId":"23a4dfc2-2257-4f1a-9248-ef6c1118fd8d","userId":100206,"name":"酒精控制","taskDate":1535040000000,"taskType":"34","illnessType":null,"remindStatus":"0","remindStart":1535040000000,"remindEnd":1535126340000,"mustStatus":"1","complitionStatus":"0","complitionTime":null},{"complianceTaskId":"7bb26d3f-067c-4eb3-9c67-552756cd8c31","userId":100206,"name":"摄盐控制","taskDate":1535040000000,"taskType":"32","illnessType":null,"remindStatus":"0","remindStart":1535040000000,"remindEnd":1535126340000,"mustStatus":"1","complitionStatus":"0","complitionTime":null},{"complianceTaskId":"1467ad36-1be8-4a17-aae7-d08536dd928c","userId":100206,"name":"体重测量","taskDate":1535040000000,"taskType":"31","illnessType":null,"remindStatus":"0","remindStart":1535040000000,"remindEnd":1535126340000,"mustStatus":"0","complitionStatus":"0","complitionTime":null},{"complianceTaskId":"5681b8b4-7538-4abd-83e2-c751c1b0fbe2","userId":100206,"name":"血压测量","taskDate":1535040000000,"taskType":"11","illnessType":"0","remindStatus":"1","remindStart":1535061600000,"remindEnd":1535068800000,"mustStatus":"1","complitionStatus":"0","complitionTime":null}]
     * complitionStatus : false
     * surpassHuman : null
     */

    public boolean complitionStatus;
    public String surpassHuman;
    public List<TaskListBean> taskList;

    public static class TaskListBean implements Serializable {
        /**
         * complianceTaskId : 030032b8-7f44-447e-adbf-049292385659
         * userId : 100206
         * name : 运动控制
         * taskDate : 1535040000000
         * taskType : 33
         * illnessType : null
         * remindStatus : 0
         * remindStart : 1535040000000
         * remindEnd : 1535126340000
         * mustStatus : 1
         * complitionStatus : 0
         * complitionTime : null
         */

        public String complianceTaskId;
        public int userId;
        public String name;
        public long taskDate;
        public String taskType;
        public String illnessType;
        public String remindStatus;
        public long remindStart;
        public long remindEnd;
        public String mustStatus;
        public String complitionStatus;
        public String complitionTime;
    }
}
