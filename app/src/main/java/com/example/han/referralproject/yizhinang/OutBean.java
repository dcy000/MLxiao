package com.example.han.referralproject.yizhinang;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2019/5/6.
 */

public class OutBean implements Serializable {

    /**
     * type : links
     * links : [{"name":"资讯","type":"text","data":[{"title":"常见的6类食物对2型糖尿病的影响(上) | 每日涨营养姿势1006","link":"http://www.medppp.com/news/r/AT0004019859","type":"at"},{"title":"糖尿病有7站旅程，别等走到最后一步才悔悟！它的克星关键在这里","link":"http://www.medppp.com/news/r/AT0004037174","type":"at"},{"title":"西瓜含糖量如何？糖尿病人能吃吗？","link":"http://www.medppp.com/news/r/AT0004036424","type":"at"}]},{"name":"精选合集","type":"text","data":[{"title":"糖尿病饮食秘籍","link":"http://www.medppp.com/news/CollVP/465","type":"bl"},{"title":"糖尿病饮食误区与要点","link":"http://www.medppp.com/news/CollVP/464","type":"bl"}]},{"name":"百科","type":"text","data":[{"title":"糖尿病","link":"https://baike.baidu.com/item/%e7%b3%96%e5%b0%bf%e7%97%85","type":"bk"},{"title":"糖尿病足","link":"https://baike.baidu.com/item/%e7%b3%96%e5%b0%bf%e7%97%85%e8%b6%b3","type":"bk"},{"title":"糖尿病眼","link":"http://www.medppp.com/news/r/BK0000004196","type":"bk"},{"title":"糖尿病足","link":"http://www.medppp.com/news/r/BK0000011178","type":"bk"},{"title":"2型糖尿病","link":"http://www.medppp.com/news/r/BK0000005927","type":"bk"}]},{"name":"更多信息","type":"link","link":"http://www.medppp.com/news/sch/2?&k=%e7%b3%96%e5%b0%bf%e7%97%85"}]
     */

    public String type;
    public List<LinksBean> links;
    public List<LinksBean> maps;

    public static class LinksBean implements Serializable {
        /**
         * name : 资讯
         * type : text
         * data : [{"title":"常见的6类食物对2型糖尿病的影响(上) | 每日涨营养姿势1006","link":"http://www.medppp.com/news/r/AT0004019859","type":"at"},{"title":"糖尿病有7站旅程，别等走到最后一步才悔悟！它的克星关键在这里","link":"http://www.medppp.com/news/r/AT0004037174","type":"at"},{"title":"西瓜含糖量如何？糖尿病人能吃吗？","link":"http://www.medppp.com/news/r/AT0004036424","type":"at"}]
         * link : http://www.medppp.com/news/sch/2?&k=%e7%b3%96%e5%b0%bf%e7%97%85
         */

        public String name;
        public String type;
        public String link;
        public List<DataBean> data;

        public static class DataBean implements Serializable {
            /**
             * title : 常见的6类食物对2型糖尿病的影响(上) | 每日涨营养姿势1006
             * link : http://www.medppp.com/news/r/AT0004019859
             * type : at
             */

            public String title;
            public String link;
            public String type;
        }
    }
}
