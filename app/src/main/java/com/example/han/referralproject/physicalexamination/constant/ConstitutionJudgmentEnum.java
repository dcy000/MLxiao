package com.example.han.referralproject.physicalexamination.constant;

/**
 * Created by lenovo on 2018/5/9.
 */

public enum ConstitutionJudgmentEnum {

    A(ConstitutionItemCount.A_DESC, ConstitutionItemCount.A, ConstitutionItemCount.A_FEATURE, ConstitutionItemCount.A_MANTENANCE),
    B(ConstitutionItemCount.B_DESC, ConstitutionItemCount.B, ConstitutionItemCount.B_FEATURE, ConstitutionItemCount.B_MANTENANCE),
    C(ConstitutionItemCount.C_DESC, ConstitutionItemCount.C, ConstitutionItemCount.C_FEATURE, ConstitutionItemCount.C_MANTENANCE),
    D(ConstitutionItemCount.D_DESC, ConstitutionItemCount.D, ConstitutionItemCount.D_FEATURE, ConstitutionItemCount.D_MANTENANCE),
    E(ConstitutionItemCount.E_DESC, ConstitutionItemCount.E, ConstitutionItemCount.E_FEATURE, ConstitutionItemCount.F_MANTENANCE),
    F(ConstitutionItemCount.F_DESC, ConstitutionItemCount.F, ConstitutionItemCount.F_FEATURE, ConstitutionItemCount.E_MANTENANCE),
    G(ConstitutionItemCount.G_DESC, ConstitutionItemCount.G, ConstitutionItemCount.G_FEATURE, ConstitutionItemCount.G_MANTENANCE),
    H(ConstitutionItemCount.H_DESC, ConstitutionItemCount.H, ConstitutionItemCount.H_FEATURE, ConstitutionItemCount.H_MANTENANCE),
    I(ConstitutionItemCount.I_DESC, ConstitutionItemCount.I, ConstitutionItemCount.I_FEATURE, ConstitutionItemCount.I_MANTENANCE);

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
