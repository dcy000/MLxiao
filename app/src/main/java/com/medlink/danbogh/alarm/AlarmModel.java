package com.medlink.danbogh.alarm;

import android.net.Uri;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2017/9/21.
 */

public class AlarmModel extends DataSupport {

    public static final int INTERVAL_NONE = 0;
    public static final int INTERVAL_DAY = 1;
    public static final int INTERVAL_ONE_DAY = 2;
    public static final int INTERVAL_WEEK = 3;

    private long id = -1;

    private String content;

    private int hourOfDay;

    private int minute;

    private long timestamp = -1;

    private String tag;

    private int interval;

    private int flags;

    private boolean enabled;

    //1 - enabled;
    private int disabled;

    private Uri tone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void addDayOfWeek(int dayOfWeek) {
        this.flags |= (1 << dayOfWeek);
    }

    public boolean hasDayOfWeek(int dayOfWeek) {
        int flag = (1 << dayOfWeek);
        return (this.flags & flag) == flag;
    }

    public void clearDayOfWeek(int dayOfWeek) {
        int flag = 1 << dayOfWeek;
        this.flags |= (~flag);
    }

    public boolean isEnabled() {
        return disabled == 1;
    }

    public void setEnabled(boolean enabled) {
        disabled = enabled ? 1 : 2;
    }

    public Uri getTone() {
        return tone;
    }

    public void setTone(Uri tone) {
        this.tone = tone;
    }

    @Override
    public String toString() {
        return "AlarmModel{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", hourOfDay=" + hourOfDay +
                ", minute=" + minute +
                ", timestamp=" + timestamp +
                ", tag='" + tag + '\'' +
                ", interval=" + interval +
                ", flags=" + flags +
                ", enabled=" + enabled +
                ", tone=" + tone +
                '}';
    }
}
