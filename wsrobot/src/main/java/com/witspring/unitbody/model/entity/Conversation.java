package com.witspring.unitbody.model.entity;

import java.io.Serializable;

/**
 * @author Created by Goven on 17/5/25 上午10:11
 * @email gxl3999@gmail.com
 */
public class Conversation<T> implements Serializable {

    private static final long serialVersionUID = -218894316400749696L;
    private String message;// 消息文本
    private int msgId;// 消息id
    private T content;// 对话内容，根据msgType解析
    private boolean isDoctor = true;
    private String voice;

    public Conversation() {}

    public Conversation(String voice) {
        this.message = voice;
    }

    public Conversation(boolean isDoctor, String msg) {
        this.isDoctor = isDoctor;
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVoice() {
        return voice == null ? message : voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public T getValue() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

}
