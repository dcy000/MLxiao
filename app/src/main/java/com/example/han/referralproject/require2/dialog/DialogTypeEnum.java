package com.example.han.referralproject.require2.dialog;

/**
 * 常用dialog的文案
 * Created by lenovo on 2018/7/11.
 */

public enum DialogTypeEnum {
    /**
     * 身份证尚未注册
     */
    idCardUnregistered("您的身份证尚未注册", "去注册", "下次再说"),
    /**
     * 未建档,提醒建档
     */

    noDocument("您还未建立档案，完成档案后您可享受每年一次免费体检服务，请先完善个人档案", "立即建档", "下次再说"),
    noDocumentForPresure("您还未建立档案，完成档案后您可享受高血压/糖尿病免费随访服务，请先完善个人档案", "立即建档", "下次再说"),
    /**
     * 免费随访用完
     */

    noSuiFangTime("您本年度的免费随访次数已用完，若想继续体检，请前往收费项目", "立即前往", "下次再说"),
    /**
     * 免费体检用完
     */

    noHealtCheckTime("您已享受本年度的免费健康体检服务，若想继续体检，请前往收费项目，是否前往?", "立即前往", "下次再说"),
    /**
     * 收费项目返回提醒
     */

    paid("您已付费，离开后再次检测需重新付款，是否离开?", "确认离开", "取消"),
    /**
     * 余额不足提醒--充值
     */

    notSufficientFunds("您的账户余额不足，请先进行充值", "去充值", "取消");

    DialogTypeEnum(String description, String leftText, String rightText) {
        this.description = description;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    String description;
    String leftText;
    String rightText;

    public String getValue() {
        return description;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getRifhtText() {
        return rightText;
    }
}
