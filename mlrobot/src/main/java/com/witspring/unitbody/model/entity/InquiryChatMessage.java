package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 症状
 * Created by chenqiang on 16/3/8.
 */
public class InquiryChatMessage implements Serializable {
    private static final long serialVersionUID = -2938532414448501616L;
    private int boxType;//item的布局类型，一共四种，1，用户文本。2单选对话。3.多选对话。4疾病列表
    private String textContent;//单文本回复信息
    private String boxTitle;//单选对话，多选对话的标题
    private String location;//当前对话块位置，location由服务端给出，用户回复没有该字段内容
    private List<ChooseItem> radioButtons;
    private List<ChooseItem> checkboxButtons;
    private List<DiseaseItem> diseaseList;

    public List<DiseaseItem> getDiseaseList() {
        return diseaseList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDiseaseList(List<DiseaseItem> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public List<ChooseItem> getRadioButtons() {
        return radioButtons;
    }

    public void setRadioButtons(List<ChooseItem> radioButtons) {
        this.radioButtons = radioButtons;
    }

    public List<ChooseItem> getCheckboxButtons() {
        return checkboxButtons;
    }

    public void setCheckboxButtons(List<ChooseItem> checkboxButtons) {
        this.checkboxButtons = checkboxButtons;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getBoxTitle() {
        return boxTitle;
    }

    public void setBoxTitle(String boxTitle) {
        this.boxTitle = boxTitle;
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }
    //    private List<>

}
