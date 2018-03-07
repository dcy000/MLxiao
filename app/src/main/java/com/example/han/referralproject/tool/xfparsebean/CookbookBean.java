package com.example.han.referralproject.tool.xfparsebean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/2/27.
 */

public class CookbookBean implements Serializable {

    /**
     * imgUrl : http://i1.douguo.net//upload/caiku/a/a/a/300_aa9203a0e0940198a6bc84561bfea2fa.jpg
     * ingredient : 龙口粉丝,1把;青瓜,小半锻;陈醋,2勺;香油,1勺;乐事薯片,1～5片;松仁,1小把
     * intro :
     * id :
     * tag : 煮,一人食,无水印,拌,家常菜,中国菜,家常味,咸,凉菜,晚餐,朋友聚餐,一家三口,午餐,煮锅,锅具
     * title : 凉拌小食#洁柔食刻，纸为爱下厨#
     * accessory :
     * steps : 1把粉丝放入清水里泡发。小半锻青瓜洗干净用刀切成细条，备用。泡发的龙口粉丝放入锅里煮7分钟。（煮熟）拿个大碗洗干净，放入3张洁柔面纸垫在碗里。这个洁柔面纸，吸水好，也不容易破。把煮熟的龙口粉丝用筛子装着，放在准备好的洁柔面纸上面，吸一吸水，吸水能力很强，非常好用。准备另外一个碗。加入陈醋2勺，香油1勺，搅拌一起。把没有水份的龙口粉丝加入调料中。在放入切好的青瓜条。最后加入1～5片乐事薯片和1小把松仁。完成。
     我为你下厨做的美食，吃的时候可以拌在一起吃，幸福味道
     * status :
     */

    public String imgUrl;
    public String ingredient;
    public String intro;
    public String id;
    public String tag;
    public String title;
    public String accessory;
    public String steps;
    public String status;
    public boolean flag;
}
