package com.witspring.model.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public class Medicine implements Serializable {

    private static final long serialVersionUID = -42307772032010902L;
    private long id;// 药品ID
    private int status;// 启停状态，0停1启
    private String name;// 药品名称
    private String price;// 价格
    private String specification;// 规格
    private String approvalDoc;// 批准文号
    private String component;// 药品成分
    private String thumbnailImgUrl;//药品缩略图的URL
    private String imgUrl;// 图片URL
    private String indications;// 主治功能
    private String drugInteraction;// 药物相互作用
    private String attention;// 注意事项
    private String taboo;// 禁忌
    private String usage;// 用法用量
    private String adverseEffect;// 不良反应
    private int isPrescription;// 是否处方药，0不是，1是
    private int supportInsurance;// 是否支持医保，0不支持，1支持
    private int buyFlag;//  1 可购买, 0 不能购买
    private long pharmacyId;// 第三方平台买药 ID

    public final static int PRESCRIPTION_YES = 1, PRESCRIPTION_NO = 0, PRESCRIPTION_NULL = -1;// 是否处方药标识
    public final static int SUPPORT_INSURANCE_YES = 1, SUPPORT_INSURANCE_NO = 0;// 是否支持医保标识

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getApprovalDoc() {
        return approvalDoc;
    }

    public void setApprovalDoc(String approvalDoc) {
        this.approvalDoc = approvalDoc;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public void setThumbnailImgUrl(String thumbnailImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getDrugInteraction() {
        return drugInteraction;
    }

    public void setDrugInteraction(String drugInteraction) {
        this.drugInteraction = drugInteraction;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getTaboo() {
        return taboo;
    }

    public void setTaboo(String taboo) {
        this.taboo = taboo;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAdverseEffect() {
        return adverseEffect;
    }

    public void setAdverseEffect(String adverseEffect) {
        this.adverseEffect = adverseEffect;
    }

    public int getIsPrescription() {
        return isPrescription;
    }

    public void setIsPrescription(int isPrescription) {
        this.isPrescription = isPrescription;
    }

    public int getSupportInsurance() {
        return supportInsurance;
    }

    public void setSupportInsurance(int supportInsurance) {
        this.supportInsurance = supportInsurance;
    }

    public int getBuyFlag() {
        return buyFlag;
    }

    public void setBuyFlag(int buyFlag) {
        this.buyFlag = buyFlag;
    }

    public long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }


    /**
     * 解析药品
     * @param jsonObj
     * @return
     */
    public static Medicine buildMedicine(JSONObject jsonObj) {
        try {
            Medicine medicine = new Medicine();
            medicine.setId(jsonObj.optLong("drugId"));
            medicine.setName(jsonObj.optString("drugName"));
            medicine.setStatus(jsonObj.optInt("status"));
            medicine.setAdverseEffect(jsonObj.optString("adverseEffect"));
            medicine.setApprovalDoc(jsonObj.optString("approvalDoc"));
            medicine.setAttention(jsonObj.optString("specialUse"));
            medicine.setComponent(jsonObj.optString("component"));
            medicine.setDrugInteraction(jsonObj.optString("drugInteraction"));
            medicine.setThumbnailImgUrl(jsonObj.optString("thumbnailImgUrl"));
            medicine.setImgUrl(jsonObj.optString("imgUrl"));
            medicine.setIndications(jsonObj.optString("indicateFun"));
            medicine.setIsPrescription(jsonObj.optInt("prescriptionFlag"));
            medicine.setPrice(jsonObj.optString("price"));
            medicine.setSpecification(jsonObj.optString("specification"));
            medicine.setSupportInsurance(jsonObj.optInt("supportInsurance"));
            medicine.setTaboo(jsonObj.optString("taboo"));
            medicine.setUsage(jsonObj.optString("usage"));
            if(jsonObj.has("buyFlag")){
                medicine.setBuyFlag(jsonObj.optInt("buyFlag"));
            }
            if(jsonObj.has("pharmacyId")){
                medicine.setPharmacyId(jsonObj.optLong("pharmacyId"));
            }
            return medicine;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析药物列表
     */
    public static ArrayList<Medicine> buildMedicines(JSONArray jsonArr) {
        try {
            ArrayList<Medicine> medicines = new ArrayList<Medicine>(jsonArr.length());
            for (int i = 0; i < jsonArr.length(); i++) {
                medicines.add(buildMedicine(jsonArr.optJSONObject(i)));
            }
            return medicines;
        } catch (Exception e) {
            return null;
        }
    }

}
