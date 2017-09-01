package com.example.han.referralproject.recyclerview;

import java.io.Serializable;

public class Doctor implements Serializable {

    public int doctoerId;
    public String docoerName;
    public String tel;
    public int hosid;
    public String add;
    public String duty;
    public String department;
    public String documents;
    public String card;
    public int rankId;
    public int state;
    public String priority;
    public String amount;


    public Doctor() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Doctor(int doctoerId, String docoerName, String tel, int hosid, String add, String duty, String department,
                  String documents, String card, int rankId, int state, String priority, String amount) {
        super();
        this.doctoerId = doctoerId;
        this.docoerName = docoerName;
        this.tel = tel;
        this.hosid = hosid;
        this.add = add;
        this.duty = duty;
        this.department = department;
        this.documents = documents;
        this.card = card;
        this.rankId = rankId;
        this.state = state;
        this.priority = priority;
        this.amount = amount;
    }


    public int getDoctoerId() {
        return doctoerId;
    }


    public void setDoctoerId(int doctoerId) {
        this.doctoerId = doctoerId;
    }


    public String getDocoerName() {
        return docoerName;
    }


    public void setDocoerName(String docoerName) {
        this.docoerName = docoerName;
    }


    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }


    public int getHosid() {
        return hosid;
    }


    public void setHosid(int hosid) {
        this.hosid = hosid;
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


    @Override
    public String toString() {
        return "Doctor [doctoerId=" + doctoerId + ", docoerName=" + docoerName + ", tel=" + tel + ", hosid=" + hosid
                + ", add=" + add + ", duty=" + duty + ", department=" + department + ", documents=" + documents
                + ", card=" + card + ", rankId=" + rankId + ", state=" + state + ", priority=" + priority + ", amount="
                + amount + "]";
    }


}
