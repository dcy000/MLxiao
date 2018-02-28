package com.example.han.referralproject.tool.xfparsebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */

public class BaiKeBean implements Serializable {

    /**
     * summary : 浙江大学（Zhejiang University），简称“浙大”，坐落于“人间天堂”杭州。前身是1897年创建的求是书院，是中国人自己最早创办的现代高等学府之一。1928年更名为国立浙江大学。中华民国时期，浙江大学在竺可桢老校长的带领下，崛起为民国最高学府之一，被英国学者李约瑟誉为“东方剑桥”，迎来了浙大百年历史上最辉煌的时期。竺可桢老校长也因其历史贡献，成为了浙大校史上最伟大的人，并为浙大确立了“求是”校训和文言文《浙江大学校歌》。浙江大学直属于中华人民共和国教育部，是中国首批7所“211工程”、首批9所“985工程”重点建设的全国重点大学之一，是九校联盟、世界大学联盟、环太平洋大学联盟的成员，是教育部“卓越医生教育培养计划”、“卓越农林人才教育培养计划”改革试点高校，是中国著名顶尖学府之一，也是中国学科最齐全的大学。截至2015年9月，浙江大学共拥有紫金港、玉泉、西溪、华家池、之江、舟山6个校区，占地总面积4.50平方公里，校舍总建筑面积2072303平方米，图书馆藏书671万余册，并有7家附属医院。
     * img : http://a2.att.hudong.com/11/51/19300001090235133595515161206_140.jpg
     * category : ["中国大学","中国学校","中国知名法学院","中国高校","各国教育","教育","暑期游","浙江大学","浙江省高等院校"]
     * sectionList : [{"sectionTitle":"办学历史","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#1"},{"sectionTitle":"办学条件","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#11"},{"sectionTitle":"学术研究","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#25"},{"sectionTitle":"文化传统","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#35"},{"sectionTitle":"校园环境","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#43"},{"sectionTitle":"知名校友","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#45"},{"sectionTitle":"学校领导","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#47"},{"sectionTitle":"教学力量","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#53"},{"sectionTitle":"办学理念","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#55"},{"sectionTitle":"学科设定","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#57"},{"sectionTitle":"硬件条件","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#59"},{"sectionTitle":"学校前景","sectionUrl":"http://www.baike.com/gwiki/浙大&fr=xunfei#61"}]
     * title : 浙大
     * url : http://www.baike.com/gwiki/浙大
     */

    public String summary;
    public String img;
    public String title;
    public String url;
    public List<String> category;
    public List<SectionListBean> sectionList;

    public static class SectionListBean {
        /**
         * sectionTitle : 办学历史
         * sectionUrl : http://www.baike.com/gwiki/浙大&fr=xunfei#1
         */

        public String sectionTitle;
        public String sectionUrl;
    }
}
