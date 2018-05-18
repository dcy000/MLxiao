package com.example.han.referralproject.questionair.constant;

/**
 * Created by lenovo on 2018/5/9.
 */

public enum ConstitutionJudgmentEnum {

    A(ConstitutionItemInfo.A_DESC, ConstitutionItemInfo.A, ConstitutionItemInfo.A_FEATURE, ConstitutionItemInfo.A_MANTENANCE),
    B(ConstitutionItemInfo.B_DESC, ConstitutionItemInfo.B, ConstitutionItemInfo.B_FEATURE, ConstitutionItemInfo.B_MANTENANCE),
    C(ConstitutionItemInfo.C_DESC, ConstitutionItemInfo.C, ConstitutionItemInfo.C_FEATURE, ConstitutionItemInfo.C_MANTENANCE),
    D(ConstitutionItemInfo.D_DESC, ConstitutionItemInfo.D, ConstitutionItemInfo.D_FEATURE, ConstitutionItemInfo.D_MANTENANCE),
    E(ConstitutionItemInfo.E_DESC, ConstitutionItemInfo.E, ConstitutionItemInfo.E_FEATURE, ConstitutionItemInfo.F_MANTENANCE),
    F(ConstitutionItemInfo.F_DESC, ConstitutionItemInfo.F, ConstitutionItemInfo.F_FEATURE, ConstitutionItemInfo.E_MANTENANCE),
    G(ConstitutionItemInfo.G_DESC, ConstitutionItemInfo.G, ConstitutionItemInfo.G_FEATURE, ConstitutionItemInfo.G_MANTENANCE),
    H(ConstitutionItemInfo.H_DESC, ConstitutionItemInfo.H, ConstitutionItemInfo.H_FEATURE, ConstitutionItemInfo.H_MANTENANCE),
    I(ConstitutionItemInfo.I_DESC, ConstitutionItemInfo.I, ConstitutionItemInfo.I_FEATURE, ConstitutionItemInfo.I_MANTENANCE);

    String maintenance;
    String featrue;
    String description;
    int itemCount;

    ConstitutionJudgmentEnum(String description, int itemCount, String featrue, String maintenance) {
        this.description = description;
        this.itemCount = itemCount;
        this.featrue = featrue;
        this.maintenance = maintenance;
    }

    public String getDescription() {
        return description;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getFeatrue() {
        return featrue;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public static ConstitutionJudgmentEnum getElement(String description) {
        for (ConstitutionJudgmentEnum c : ConstitutionJudgmentEnum.values()) {
            if (c.getDescription().equals(description)) {
                return c;
            }
        }
        return A;
    }

}
