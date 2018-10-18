package com.gcml.common.data;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.2.5
 * created on 2018/10/18 15:40
 * created by: gzq
 * description: TODO
 */
public class MessageBean {
    private String title;
    private String message;

    public MessageBean() {
    }

    public MessageBean(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
