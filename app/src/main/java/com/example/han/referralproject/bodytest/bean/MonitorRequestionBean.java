package com.example.han.referralproject.bodytest.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/8.
 */

public class MonitorRequestionBean implements Serializable {


    /**
     * 体质类型
     */
    public String requestionType="";

    /**
     * 测试题
     */
    public String requesionTitle="";

    /**
     * 答案选项
     */

    public AnwserBean anwser;
    /**
     * 答案是否被选择
     */

    public boolean isSelected;

    /**
     * 条目数（每项问题数)
     */
    public int itemCount;

    /**
     * 答案得分
     */

    public int score;


    public static class AnwserBean implements Serializable {
        public String A;
        public String B;
        public String C;
        public String D;
        public String E;
    }

}
