package com.gcml.module_health_profile.checklist.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2019/4/23.
 */

public class CheckListInfoBean implements Serializable {
    /*  answerList (Array[TRdUserAnswer], optional): 答案列表 ,
      answerObj (object, optional): 档案详情 ,
      questionList (Array[TRdQuestion], optional): 问题列表 ,
      rdRecordId (string, optional): 档案ID ,
      recordName (string, optional): 档案名称*/
    public String recordName;
    public String rdRecordId;
    public Object answerObj;
    public List<TRdUserAnswer> answerList;
    public List<TRdQuestion> questionList;

    public static class TRdUserAnswer implements Serializable {
//        createdOn (string, optional): 创建时间 ,
//        createdTime (string, optional): 创建时间 ,
//        factorCode (string, optional): 因素code ,
//        fatherQuestionId (string, optional): 父问题ID ,
//        groupId (string, optional): 组合ID 通过father_question_id组合而成 ,
//        modifiedOn (string, optional): 修改时间 ,
//        modifiedTime (string, optional): 修改时间 ,
//        optionCode (string, optional): 选项CODE ,
//        optionId (string, optional): 答案ID ,
//        optionNum (string, optional): 选项num ,
//        questionCode (string, optional): 问题code ,
//        questionContent (string, optional): 答案内容 ,
//        questionId (string, optional): 问题ID ,
//        rdRecordId (string, optional): 档案ID ,
//        rdUserAnswerId (string, optional): 用户答案ID ,
//        rdUserRecordId (string, optional): 用户档案ID ,
//        recordName (string, optional): 档案名称 ,
//        seq (integer, optional): 排序 ,
//        userId (integer, optional): 用户ID

        public String createdOn;
        public String createdTime;
        public String factorCode;
        public String fatherQuestionId;
        public String groupId;
        public String modifiedOn;
        public String modifiedTime;
        public String optionCode;
        public String optionId;
        public String optionNum;
        public String questionCode;
        public String questionContent;
        public String questionId;
        public String rdRecordId;
        public String rdUserAnswerId;
        public String rdUserRecordId;
        public String recordName;
        public Integer seq;
        public Integer userId;

    }

    public static class TRdQuestion implements Serializable {
      /*  answerList (Array[TRdUserAnswer], optional): 答案列表 ,
        checkedList (Array[string], optional): 多选选中列表 ,
        dataType (string, optional): 数据类型 1文字 2时间 3数字 4地址 ,
        dataUnit (string, optional): 数据单位 ,
        detectionType (string, optional),
        factorCode (string, optional): 因素code ,
        fatherQuestionId (string, optional): 父问题ID ,
        fatherQuestionName (string, optional): 父问题名称 ,
        optionCode (string, optional): 选项CODE ,
        optionId (string, optional): 选项id ,
        optionList (Array[TRdOption], optional): 选项列表 ,
        optionStr (string, optional): 选项 ,
        questionCode (string, optional): 问题code ,
        questionContent (string, optional): 填空答案 ,
        questionId (string, optional): 问题ID ,
        questionList (Array[TRdQuestion], optional): 问题列表 ,
        questionName (string, optional): 名称 ,
        questionType (string, optional): 问题类型 01标题 11填空 21单选 22多选 90其他 ,
        radioOption (string, optional): 单选选中ID ,
        rdRecordId (string, optional): 档案ID ,
        repetitionStatus (string, optional): 是否重复 0否 1是 ,
        seq (integer, optional): 排序*/

        public List<TRdUserAnswer> answerList;
        public List<String> checkedList;
        public List<TRdOption> optionList;
        public List<TRdQuestion> questionList;
        public String dataType;
        public String dataUnit;
        public String detectionType;
        public String factorCode;
        public String fatherQuestionId;
        public String fatherQuestionName;
        public String optionCode;
        public String optionId;
        public String optionStr;
        public String questionCode;
        public String questionContent;
        public String questionId;
        public String questionName;
        public String questionType;
        public String radioOption;
        public String rdRecordId;
        public String repetitionStatus;
        public Integer seq;


        public static class TRdOption implements Serializable {
            /*checkedStatus (string, optional): 选中状态 0未选中 1已选中 ,
                   exclusiveStatus (string, optional): 排他标识（多选） 0：不排他 1：排他 ,
                   factorCode (string, optional): 因素code ,
                   optionCode (string, optional): 选项CODE ,
                   optionId (string, optional): 选项ID ,
                   optionName (string, optional): 选项名称 ,
                   optionNum (string, optional): 选项序号 ,
                   questionCode (string, optional): 问题code ,
                   questionId (string, optional): 问题ID ,
                   questionList (Array[TRdQuestion], optional): 问题列表 ,
                   questionType (string, optional): 问题类型 01标题 11填空 21单选 22多选 90其他 ,
                   rdRecordId (string, optional): 档案ID ,
                   repetitionStatus (string, optional): 是否重复 0否 1是 ,
                   seq (integer, optional): 排序*/
            public String checkedStatus;
            public String exclusiveStatus;
            public String factorCode;
            public String optionCode;
            public String optionId;
            public String optionName;
            public String optionNum;
            public String questionCode;
            public String questionId;
            public List<TRdQuestion> questionList;
            public String questionType;
            public String rdRecordId;
            public String repetitionStatus;
            public Integer seq;

        }

    }
}
