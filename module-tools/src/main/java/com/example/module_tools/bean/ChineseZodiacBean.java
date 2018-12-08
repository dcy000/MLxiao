package com.example.module_tools.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */

public class ChineseZodiacBean implements Serializable {

    /**
     * summary : 戌年出生人，其性刚直，重义理与信义励业，此人有胆力、奋斗、活动性、聪明、直感性、机敏、大望、热情、费金钱，有稍暴燥性，女子者，富有引人之魅力，易多变自己必理，嫌虚荣、短气、苦劳性、不坚实、忍耐性，对此矫正自然获得良运遁来。
     * detail : [{"content":"","description":"勇敢风趣，机智多谋，热情专注。","type":"优点"},{"content":"","description":"刻薄短视，心胸狭窄，傲慢自大。","type":"缺点"},{"content":"","description":"全力投入必会卓然增色。","type":"事业"},{"content":"","description":"易有口角，忽略联系和关心。","type":"爱情"},{"content":"","description":"收入有突破，应把握分寸。","type":"财运"},{"content":"","description":"小心交通意外，及水险、火灾。","type":"健康"}]
     * source : xingzuowu
     * numerology : [{"description":"戌","type":"天干"},{"type":"地支"},{"description":"戌属土","type":"五行分析"},{"description":"阿弥陀佛","type":"本命佛"},{"description":"绿、红、紫色","type":"吉祥颜色"},{"description":"蓝、白、金色","type":"大凶颜色"},{"description":"3、4、9","type":"幸运数字"},{"description":"1、6、7","type":"大凶数字"},{"description":"玫瑰花、文心兰、惠兰","type":"幸运花"},{"description":"东、东南及南方","type":"吉祥方位"}]
     * name : 狗
     * year : 2018
     * fortune : [{"description":2,"name":"运势指数"},{"description":"属狗的人来到本命年，为【值太岁】。流年又受【指背】【伏尸】【剑锋】凶星影响，今年必定浮沉不定，容易出现天灾人祸，使得狗人运势好比雪上加霜、热汤浇雪，无论做何事，都会诸多不顺；幸得吉星【禄动】【华盖】星临，能帮助缓解【犯太岁】的凶运，可谓凶中藏吉，属狗人今年若能慎言慎行，脚踏实地，少言多行，便能转危为安。【禄动】吉星入宫，今年工作上机会很多，切忌我行我素，否则易遭孤立影响事业。","name":"运势详情"}]
     * causes :
     * url : https://m.xzw.com/shengxiao/xugou/?_app=xf
     */

    public String summary;
    public String source;
    public String name;
    public String year;
    public String causes;
    public String url;
    public List<DetailBean> detail;
    public List<NumerologyBean> numerology;
    public List<FortuneBean> fortune;

    public static class DetailBean {
        /**
         * content :
         * description : 勇敢风趣，机智多谋，热情专注。
         * type : 优点
         */

        public String content;
        public String description;
        public String type;
    }

    public static class NumerologyBean {
        /**
         * description : 戌
         * type : 天干
         */

        public String description;
        public String type;
    }

    public static class FortuneBean {
        /**
         * description : 2
         * name : 运势指数
         */

        public String description;
        public String name;
    }
}
