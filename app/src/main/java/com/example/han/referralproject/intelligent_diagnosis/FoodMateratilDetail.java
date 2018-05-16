package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/15.
 */

public class FoodMateratilDetail implements Parcelable{

    /**
     * hmFoodId : 0094ca99-5cd7-4102-80db-e287c3cea35e
     * name : 胡萝卜
     * alias : 胡萝菔、黄萝卜、金笋、丁香萝卜、红芦菔、甘笋、黄根、卜香菜、药萝卜、赤珊瑚
     * infoUrl : http://www.meishij.net/胡萝卜
     * imgUrl : http://images.meishij.net/p/20111130/629aedacc88d9607d1d1caba2ec0af59_60x60.jpg
     * foodIntake : 每餐一根
     * suitableHuman : 癌症、高血压、夜盲症、皮肤粗糙者适宜
     * tabooHuman : 一般人群均可食用
     * suitablePhysique : 气虚质,气郁质,阳虚质
     * tabooPhysique : 湿热质
     * introduce :       胡萝卜为伞形科，一年生或二年生的根菜。原产地中海沿岸，我国栽培甚为普遍，以山东、河南、浙江、云南等省种植最多，品质亦佳，秋冬季节上市。胡萝卜供食用的部分是肥嫩的肉质直根。胡萝卜的品种很多，按色泽可分为红、黄、白、紫等数种，我国栽培最多的是红、黄两种。 　按形状可分为圆锥形和圆柱形。胡萝卜肉质细密，质地脆嫩，有特殊的甜味，并含有丰富的胡萝卜素，维生素C和B族维生素。 胡萝卜的品质要求：以质细味甜，脆嫩多汁，表皮光滑，形状整齐，心柱小，肉厚，不糠，无裂口和病虫伤害的为佳。       胡萝卜原产于亚洲的西南部，阿富汗为最早演化中心，栽培历史在2000年以上。公元10世纪从伊朗引入欧洲大陆，15世纪见于英国 ，发展成欧洲生态型，尤以地中海沿岸最多种植。16世纪传入美国。 约在13世纪，胡萝卜从伊朗引入中国，发展成中国生态型，以山东、河南、浙江、云南等省种植最多。胡萝卜于16世纪从中国传入日本。
     * nutrition : 1.胡萝卜素转变成维生素A，在预防上皮细胞癌变的过程中具有重要作用，胡萝卜中的木质素也能提高。作为一种抗氧化，具有抑制氧化及保护机体正常细胞免受氧化损害的防癌作用。 2.胡萝卜素既有造血功能补充人体所需的血液，从而改善贫血或冷血症，同时含有丰富的钾 。 3.胡萝卜含有植物纤维 吸水性强 在肠道中体积容易膨胀 是肠道中的“充盈物质” 。 4.胡萝卜中的维生素A是骨骼正常发育的必需物质，有利于细胞的生殖与增长。
     * edibleEffect : 1.增强抵抗力 胡萝卜中的木质素也能提高，间接消灭癌细胞。 2.降糖降脂 降低血脂，促进肾上腺素的合成，还有降压，强心作用，是高血压、冠心病患者的食疗佳品。 3.明目 胡萝卜含有大量胡萝卜素，进入机体后，在肝脏及小肠粘膜内经过酶的作用，其中50%变成维生素A，有补肝明目的作用，可治疗夜盲症。 4.利膈宽肠 植物纤维增加胃肠蠕动，促进代谢，通便防癌。
     * edibleTaboo :
     */

    private String hmFoodId;
    private String name;
    private String alias;
    private String infoUrl;
    private String imgUrl;
    private String foodIntake;
    private String suitableHuman;
    private String tabooHuman;
    private String suitablePhysique;
    private String tabooPhysique;
    private String introduce;
    private String nutrition;
    private String edibleEffect;
    private String edibleTaboo;

    protected FoodMateratilDetail(Parcel in) {
        hmFoodId = in.readString();
        name = in.readString();
        alias = in.readString();
        infoUrl = in.readString();
        imgUrl = in.readString();
        foodIntake = in.readString();
        suitableHuman = in.readString();
        tabooHuman = in.readString();
        suitablePhysique = in.readString();
        tabooPhysique = in.readString();
        introduce = in.readString();
        nutrition = in.readString();
        edibleEffect = in.readString();
        edibleTaboo = in.readString();
    }

    public static final Creator<FoodMateratilDetail> CREATOR = new Creator<FoodMateratilDetail>() {
        @Override
        public FoodMateratilDetail createFromParcel(Parcel in) {
            return new FoodMateratilDetail(in);
        }

        @Override
        public FoodMateratilDetail[] newArray(int size) {
            return new FoodMateratilDetail[size];
        }
    };

    public String getHmFoodId() {
        return hmFoodId;
    }

    public void setHmFoodId(String hmFoodId) {
        this.hmFoodId = hmFoodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFoodIntake() {
        return foodIntake;
    }

    public void setFoodIntake(String foodIntake) {
        this.foodIntake = foodIntake;
    }

    public String getSuitableHuman() {
        return suitableHuman;
    }

    public void setSuitableHuman(String suitableHuman) {
        this.suitableHuman = suitableHuman;
    }

    public String getTabooHuman() {
        return tabooHuman;
    }

    public void setTabooHuman(String tabooHuman) {
        this.tabooHuman = tabooHuman;
    }

    public String getSuitablePhysique() {
        return suitablePhysique;
    }

    public void setSuitablePhysique(String suitablePhysique) {
        this.suitablePhysique = suitablePhysique;
    }

    public String getTabooPhysique() {
        return tabooPhysique;
    }

    public void setTabooPhysique(String tabooPhysique) {
        this.tabooPhysique = tabooPhysique;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getEdibleEffect() {
        return edibleEffect;
    }

    public void setEdibleEffect(String edibleEffect) {
        this.edibleEffect = edibleEffect;
    }

    public String getEdibleTaboo() {
        return edibleTaboo;
    }

    public void setEdibleTaboo(String edibleTaboo) {
        this.edibleTaboo = edibleTaboo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hmFoodId);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeString(infoUrl);
        dest.writeString(imgUrl);
        dest.writeString(foodIntake);
        dest.writeString(suitableHuman);
        dest.writeString(tabooHuman);
        dest.writeString(suitablePhysique);
        dest.writeString(tabooPhysique);
        dest.writeString(introduce);
        dest.writeString(nutrition);
        dest.writeString(edibleEffect);
        dest.writeString(edibleTaboo);
    }
}
