package com.gcml.module_hypertension_manager.bean;

/**
 * Created by Administrator on 2018/5/16.
 */

public class WeekDietPlan {

    /**
     * hmCookbookWeekId : 7a6a0ce9-c3ac-439d-8aaa-c2e565e03632
     * illnessType : 1
     * mondayCookbook : {"hmCookbookDayId":null,"breakfast":"豆浆400克，麻将花卷75克（熟重），咸鸭蛋半个，拍黄瓜（黄瓜50克）。","lunch":"红豆饭（大米35克、红豆15克），白菜鸡肉丸（小白菜150克、鸡肉丸50克、植物油2克），清炒莴笋（莴笋150克、植物油3克），加餐：苹果200克。","dinner":"馒头35克（熟重），小米粥（大米15克、小米10克），西蓝花烧胡萝卜（西蓝花100克、胡萝卜20克、植物油5克），海带拌豆芽（水发海带50克、绿豆芽50克、香油2克），油菜烧肉（鲜蘑菇150克、牛肉25克、油菜50克、植物油5克），加餐：苏打饼干25克。"}
     * tuesdayCookbook : {"hmCookbookDayId":null,"breakfast":"烧饼50克，煮鸡蛋1个，豆浆300克，拌菠菜（菠菜100克、香油2克）。","lunch":"米饭50克，芹菜炒虾仁（芹菜200克、鲜虾仁50克、植物油6克）。","dinner":"馒头50克，芥蓝炒肉丝（芥蓝150克、猪瘦肉50克、植物油9克），番茄汤（番茄150克、香油3克）。"}
     * wednesdayCookbook : {"hmCookbookDayId":null,"breakfast":"馒头（面粉50克），鲜牛奶250克，韭菜炒豆腐丝（韭菜50克、豆腐丝25克、香油3克）。","lunch":"米饭（大米50克），圆白菜炒肉（圆白菜100克、瘦猪肉50克、植物油2克），香菇白菜（香菇15克、大白菜150克、植物油2克），虾皮紫菜汤（虾皮5克、紫菜2克、黄瓜25克、香油2克）。","dinner":"花卷（面粉50克），肉末雪里蕻豆腐（牛肉25克、雪里蕻50克、豆腐50克、植物油4克），清炒茼蒿（茼蒿150克、香油2克）。"}
     * thursdayCookbook : {"hmCookbookDayId":null,"breakfast":"葱花卷（面粉50克），鲜豆浆250克，拌茄泥（茄子100克、植物油2克）。","lunch":"米饭（大米50克），菠菜丸子汤（猪瘦肉100克、菠菜汤150克、植物油3克），拌海带（水发海带100克、香油2克），西葫芦炒蛋（西葫芦50克、鸡蛋1个、植物油3克）。","dinner":"烙饼（面粉50克），肉炒茴香（猪瘦肉50克、茴香150克、植物油2克），拌芹菜（芹菜50克、香油2克），丝瓜汤（丝瓜75克、紫菜2克、植物油2克）。加餐牛奶250克。"}
     * fridayCookbook : {"hmCookbookDayId":null,"breakfast":"烧饼（面粉50克）、鲜牛奶250克、拌紫甘蓝（紫甘蓝100克、香油2克）。","lunch":"米饭（大米50克），红烧鸡块（鸡肉100克、植物油3克），清炒空心菜（空心菜200克、植物油3克），黄瓜汤（黄瓜50克、紫菜2克、香油2克）。","dinner":"绿豆粥（大米40克、绿豆10克），茭白炒肉（茭白150克、牛肉50克、植物油3克），莴笋拌豆腐丝（莴笋100克、豆腐丝25克、香油2克）。"}
     * saturdayCookbook : {"hmCookbookDayId":null,"breakfast":"包子（面粉50克、鸡蛋30克、韭菜50克、植物油2克），豆腐脑200克，鹌鹑蛋3个（带壳30克）。","lunch":"米饭（大米50克），清蒸鲫鱼（鲫鱼40克、植物油3克），芹菜炒豆芽（芹菜50克、绿豆芽200克、植物油3克），白菜豆腐汤（小白菜100克、豆腐50克、植物油2克）。","dinner":"小窝头（玉米面20克、面粉20克），热汤面（挂面25克、番茄50克、香油2克），红烧鸡翅（鸡翅25克），烧菜花（菜花150克、胡萝卜20克、植物油4克），油菜烧海米（油菜100克、海米5克、植物油4克）。"}
     * sundayCookbook : {"hmCookbookDayId":null,"breakfast":"馒头75克（熟重），牛奶250克，荷包蛋1个（带壳60克），清炒苋菜（苋菜100克、植物油2克）。","lunch":"米饭（大米75克），炒苋菜（苋菜200克、植物油2克），葱烧海参（水发海参200克、植物油5克），萝卜丝虾皮汤（白萝卜100克、虾皮5克、香油2克）。","dinner":"发面饼75克（熟重），红烧鸡块（鸡肉25克、胡萝卜20克、植物油3克），香菇油菜（油菜150克、干香菇5克、植物油3克），西葫芦豆腐汤（西葫芦150克、豆腐25克、植物油3克）。"}
     */

    private String hmCookbookWeekId;
    private String illnessType;
    private MondayCookbookBean mondayCookbook;
    private TuesdayCookbookBean tuesdayCookbook;
    private WednesdayCookbookBean wednesdayCookbook;
    private ThursdayCookbookBean thursdayCookbook;
    private FridayCookbookBean fridayCookbook;
    private SaturdayCookbookBean saturdayCookbook;
    private SundayCookbookBean sundayCookbook;

    public String getHmCookbookWeekId() {
        return hmCookbookWeekId;
    }

    public void setHmCookbookWeekId(String hmCookbookWeekId) {
        this.hmCookbookWeekId = hmCookbookWeekId;
    }

    public String getIllnessType() {
        return illnessType;
    }

    public void setIllnessType(String illnessType) {
        this.illnessType = illnessType;
    }

    public MondayCookbookBean getMondayCookbook() {
        return mondayCookbook;
    }

    public void setMondayCookbook(MondayCookbookBean mondayCookbook) {
        this.mondayCookbook = mondayCookbook;
    }

    public TuesdayCookbookBean getTuesdayCookbook() {
        return tuesdayCookbook;
    }

    public void setTuesdayCookbook(TuesdayCookbookBean tuesdayCookbook) {
        this.tuesdayCookbook = tuesdayCookbook;
    }

    public WednesdayCookbookBean getWednesdayCookbook() {
        return wednesdayCookbook;
    }

    public void setWednesdayCookbook(WednesdayCookbookBean wednesdayCookbook) {
        this.wednesdayCookbook = wednesdayCookbook;
    }

    public ThursdayCookbookBean getThursdayCookbook() {
        return thursdayCookbook;
    }

    public void setThursdayCookbook(ThursdayCookbookBean thursdayCookbook) {
        this.thursdayCookbook = thursdayCookbook;
    }

    public FridayCookbookBean getFridayCookbook() {
        return fridayCookbook;
    }

    public void setFridayCookbook(FridayCookbookBean fridayCookbook) {
        this.fridayCookbook = fridayCookbook;
    }

    public SaturdayCookbookBean getSaturdayCookbook() {
        return saturdayCookbook;
    }

    public void setSaturdayCookbook(SaturdayCookbookBean saturdayCookbook) {
        this.saturdayCookbook = saturdayCookbook;
    }

    public SundayCookbookBean getSundayCookbook() {
        return sundayCookbook;
    }

    public void setSundayCookbook(SundayCookbookBean sundayCookbook) {
        this.sundayCookbook = sundayCookbook;
    }

    public static class MondayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 豆浆400克，麻将花卷75克（熟重），咸鸭蛋半个，拍黄瓜（黄瓜50克）。
         * lunch : 红豆饭（大米35克、红豆15克），白菜鸡肉丸（小白菜150克、鸡肉丸50克、植物油2克），清炒莴笋（莴笋150克、植物油3克），加餐：苹果200克。
         * dinner : 馒头35克（熟重），小米粥（大米15克、小米10克），西蓝花烧胡萝卜（西蓝花100克、胡萝卜20克、植物油5克），海带拌豆芽（水发海带50克、绿豆芽50克、香油2克），油菜烧肉（鲜蘑菇150克、牛肉25克、油菜50克、植物油5克），加餐：苏打饼干25克。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class TuesdayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 烧饼50克，煮鸡蛋1个，豆浆300克，拌菠菜（菠菜100克、香油2克）。
         * lunch : 米饭50克，芹菜炒虾仁（芹菜200克、鲜虾仁50克、植物油6克）。
         * dinner : 馒头50克，芥蓝炒肉丝（芥蓝150克、猪瘦肉50克、植物油9克），番茄汤（番茄150克、香油3克）。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class WednesdayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 馒头（面粉50克），鲜牛奶250克，韭菜炒豆腐丝（韭菜50克、豆腐丝25克、香油3克）。
         * lunch : 米饭（大米50克），圆白菜炒肉（圆白菜100克、瘦猪肉50克、植物油2克），香菇白菜（香菇15克、大白菜150克、植物油2克），虾皮紫菜汤（虾皮5克、紫菜2克、黄瓜25克、香油2克）。
         * dinner : 花卷（面粉50克），肉末雪里蕻豆腐（牛肉25克、雪里蕻50克、豆腐50克、植物油4克），清炒茼蒿（茼蒿150克、香油2克）。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class ThursdayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 葱花卷（面粉50克），鲜豆浆250克，拌茄泥（茄子100克、植物油2克）。
         * lunch : 米饭（大米50克），菠菜丸子汤（猪瘦肉100克、菠菜汤150克、植物油3克），拌海带（水发海带100克、香油2克），西葫芦炒蛋（西葫芦50克、鸡蛋1个、植物油3克）。
         * dinner : 烙饼（面粉50克），肉炒茴香（猪瘦肉50克、茴香150克、植物油2克），拌芹菜（芹菜50克、香油2克），丝瓜汤（丝瓜75克、紫菜2克、植物油2克）。加餐牛奶250克。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class FridayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 烧饼（面粉50克）、鲜牛奶250克、拌紫甘蓝（紫甘蓝100克、香油2克）。
         * lunch : 米饭（大米50克），红烧鸡块（鸡肉100克、植物油3克），清炒空心菜（空心菜200克、植物油3克），黄瓜汤（黄瓜50克、紫菜2克、香油2克）。
         * dinner : 绿豆粥（大米40克、绿豆10克），茭白炒肉（茭白150克、牛肉50克、植物油3克），莴笋拌豆腐丝（莴笋100克、豆腐丝25克、香油2克）。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class SaturdayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 包子（面粉50克、鸡蛋30克、韭菜50克、植物油2克），豆腐脑200克，鹌鹑蛋3个（带壳30克）。
         * lunch : 米饭（大米50克），清蒸鲫鱼（鲫鱼40克、植物油3克），芹菜炒豆芽（芹菜50克、绿豆芽200克、植物油3克），白菜豆腐汤（小白菜100克、豆腐50克、植物油2克）。
         * dinner : 小窝头（玉米面20克、面粉20克），热汤面（挂面25克、番茄50克、香油2克），红烧鸡翅（鸡翅25克），烧菜花（菜花150克、胡萝卜20克、植物油4克），油菜烧海米（油菜100克、海米5克、植物油4克）。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }

    public static class SundayCookbookBean {
        /**
         * hmCookbookDayId : null
         * breakfast : 馒头75克（熟重），牛奶250克，荷包蛋1个（带壳60克），清炒苋菜（苋菜100克、植物油2克）。
         * lunch : 米饭（大米75克），炒苋菜（苋菜200克、植物油2克），葱烧海参（水发海参200克、植物油5克），萝卜丝虾皮汤（白萝卜100克、虾皮5克、香油2克）。
         * dinner : 发面饼75克（熟重），红烧鸡块（鸡肉25克、胡萝卜20克、植物油3克），香菇油菜（油菜150克、干香菇5克、植物油3克），西葫芦豆腐汤（西葫芦150克、豆腐25克、植物油3克）。
         */

        private Object hmCookbookDayId;
        private String breakfast;
        private String lunch;
        private String dinner;

        public Object getHmCookbookDayId() {
            return hmCookbookDayId;
        }

        public void setHmCookbookDayId(Object hmCookbookDayId) {
            this.hmCookbookDayId = hmCookbookDayId;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }
    }
}
