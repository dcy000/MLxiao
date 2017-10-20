package com.example.han.referralproject.bean;

/**
 * Created by han on 2017/10/20.
 */

public class Doctors {


    public String doctername;
    public String amount;
    public String documents;
    public String count;
    public String pro;
    public String hosname;
    public String docterid;
    public String evaluation;
    public String service_amount;
    public String zige;
    public String rankid;
    public String rankname;
    public String duty;
    public String tel;
    public String gat;
    public String department;
    public String card;
    public String adds;
    public String apply_amount;
    public String pend;


    public Doctors() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Doctors(String doctername, String amount, String documents, String count, String pro, String hosname,
                   String docterid, String evaluation, String service_amount, String zige, String rankid, String rankname,
                   String duty, String tel, String gat, String department, String card, String adds, String apply_amount,
                   String pend) {
        super();
        this.doctername = doctername;
        this.amount = amount;
        this.documents = documents;
        this.count = count;
        this.pro = pro;
        this.hosname = hosname;
        this.docterid = docterid;
        this.evaluation = evaluation;
        this.service_amount = service_amount;
        this.zige = zige;
        this.rankid = rankid;
        this.rankname = rankname;
        this.duty = duty;
        this.tel = tel;
        this.gat = gat;
        this.department = department;
        this.card = card;
        this.adds = adds;
        this.apply_amount = apply_amount;
        this.pend = pend;
    }


    public String getDoctername() {
        return doctername;
    }


    public void setDoctername(String doctername) {
        this.doctername = doctername;
    }


    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getDocuments() {
        return documents;
    }


    public void setDocuments(String documents) {
        this.documents = documents;
    }


    public String getCount() {
        return count;
    }


    public void setCount(String count) {
        this.count = count;
    }


    public String getPro() {
        return pro;
    }


    public void setPro(String pro) {
        this.pro = pro;
    }


    public String getHosname() {
        return hosname;
    }


    public void setHosname(String hosname) {
        this.hosname = hosname;
    }


    public String getDocterid() {
        return docterid;
    }


    public void setDocterid(String docterid) {
        this.docterid = docterid;
    }


    public String getEvaluation() {
        return evaluation;
    }


    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }


    public String getService_amount() {
        return service_amount;
    }


    public void setService_amount(String service_amount) {
        this.service_amount = service_amount;
    }


    public String getZige() {
        return zige;
    }


    public void setZige(String zige) {
        this.zige = zige;
    }


    public String getRankid() {
        return rankid;
    }


    public void setRankid(String rankid) {
        this.rankid = rankid;
    }


    public String getRankname() {
        return rankname;
    }


    public void setRankname(String rankname) {
        this.rankname = rankname;
    }


    public String getDuty() {
        return duty;
    }


    public void setDuty(String duty) {
        this.duty = duty;
    }


    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getGat() {
        return gat;
    }


    public void setGat(String gat) {
        this.gat = gat;
    }


    public String getDepartment() {
        return department;
    }


    public void setDepartment(String department) {
        this.department = department;
    }


    public String getCard() {
        return card;
    }


    public void setCard(String card) {
        this.card = card;
    }


    public String getAdds() {
        return adds;
    }


    public void setAdds(String adds) {
        this.adds = adds;
    }


    public String getApply_amount() {
        return apply_amount;
    }


    public void setApply_amount(String apply_amount) {
        this.apply_amount = apply_amount;
    }


    public String getPend() {
        return pend;
    }


    public void setPend(String pend) {
        this.pend = pend;
    }


    @Override
    public String toString() {
        return "Doctor [doctername=" + doctername + ", amount=" + amount + ", documents=" + documents + ", count="
                + count + ", pro=" + pro + ", hosname=" + hosname + ", docterid=" + docterid + ", evaluation="
                + evaluation + ", service_amount=" + service_amount + ", zige=" + zige + ", rankid=" + rankid
                + ", rankname=" + rankname + ", duty=" + duty + ", tel=" + tel + ", gat=" + gat + ", department="
                + department + ", card=" + card + ", adds=" + adds + ", apply_amount=" + apply_amount + ", pend="
                + pend + "]";
    }


}
