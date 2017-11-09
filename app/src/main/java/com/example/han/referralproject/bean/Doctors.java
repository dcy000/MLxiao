package com.example.han.referralproject.bean;

import java.io.Serializable;

public class Doctors implements Serializable {

    public int docterid;
    public String doctername;
    public String tel;
    public String hosname;
    public String add;
    public String duty;
    public String department;
    public String documents;
    public String card;
    public int rankId;
    public int state;
    public String priority;
    public String amount;
    public String gat;
    public String pro;
    public String pend;
    public String evaluation;
    public String apply_amount;
    public String service_amount;
    public String docter_photo;
    public String hosnames;

    public Doctors() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Doctors(int docterid, String doctername, String tel, String hosname, String add, String duty, String department,
                   String documents, String card, int rankId, int state, String priority, String amount, String gat,
                   String pro, String pend, String evaluation, String apply_amount, String service_amount, String docter_photo, String hosnames) {
        super();
        this.docterid = docterid;
        this.doctername = doctername;
        this.tel = tel;
        this.hosname = hosname;
        this.add = add;
        this.duty = duty;
        this.department = department;
        this.documents = documents;
        this.card = card;
        this.rankId = rankId;
        this.state = state;
        this.priority = priority;
        this.amount = amount;
        this.gat = gat;
        this.pro = pro;
        this.pend = pend;
        this.evaluation = evaluation;
        this.apply_amount = apply_amount;
        this.service_amount = service_amount;
        this.docter_photo = docter_photo;
        this.hosnames = hosnames;
    }


    public int getDocterid() {
        return docterid;
    }


    public void setDocterid(int docterid) {
        this.docterid = docterid;
    }


    public String getDoctername() {
        return doctername;
    }


    public void setDoctername(String doctername) {
        this.doctername = doctername;
    }


    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getHosname() {
        return hosname;
    }


    public void setHosid(String hosname) {
        this.hosname = hosname;
    }


    public String getAdd() {
        return add;
    }


    public void setAdd(String add) {
        this.add = add;
    }


    public String getDuty() {
        return duty;
    }


    public void setDuty(String duty) {
        this.duty = duty;
    }


    public String getDepartment() {
        return department;
    }


    public void setDepartment(String department) {
        this.department = department;
    }


    public String getDocuments() {
        return documents;
    }


    public void setDocuments(String documents) {
        this.documents = documents;
    }


    public String getCard() {
        return card;
    }


    public void setCard(String card) {
        this.card = card;
    }


    public int getRankId() {
        return rankId;
    }


    public void setRankId(int rankId) {
        this.rankId = rankId;
    }


    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public String getPriority() {
        return priority;
    }


    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getGat() {
        return gat;
    }


    public void setGat(String gat) {
        this.gat = gat;
    }


    public String getPro() {
        return pro;
    }


    public void setPro(String pro) {
        this.pro = pro;
    }


    public String getPend() {
        return pend;
    }


    public void setPend(String pend) {
        this.pend = pend;
    }


    public String getEvaluation() {
        return evaluation;
    }


    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }


    public String getApply_amount() {
        return apply_amount;
    }


    public void setApply_amount(String apply_amount) {
        this.apply_amount = apply_amount;
    }


    public String getService_amount() {
        return service_amount;
    }


    public void setService_amount(String service_amount) {
        this.service_amount = service_amount;
    }

    public String getDocter_photo() {
        return docter_photo;
    }


    public void setDocter_photo(String docter_photo) {
        this.docter_photo = docter_photo;
    }



    public String getHosnames() {
        return hosnames;
    }


    public void setHosnames(String hosnames) {
        this.hosnames = hosnames;
    }




    @Override
    public String toString() {
        return "Doctor [docterid=" + docterid + ", doctername=" + doctername + ", tel=" + tel + ", hosname=" + hosname
                + ", add=" + add + ", duty=" + duty + ", department=" + department + ", documents=" + documents
                + ", card=" + card + ", rankId=" + rankId + ", state=" + state + ", priority=" + priority + ", amount="
                + amount + ", gat=" + gat + ", pro=" + pro + ", pend=" + pend + ", evaluation=" + evaluation
                + ", apply_amount=" + apply_amount + ", service_amount=" + service_amount
                + ", docter_photo=" + docter_photo + ", hosnames=" + hosnames + "]";
    }


}
