package com.example.han.referralproject.settting.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/3/23.
 */

public class VoicerBean implements Serializable{
    public boolean check;
    public String name;
    public String voicerName;
    public String speed;
    public String pitch;
    public String rate;

    public VoicerBean setCheck(boolean check) {
        this.check = check;
        return this;
    }

    public VoicerBean setName(String name) {
        this.name = name;
        return this;
    }

    public VoicerBean setSpeed(String speed) {
        this.speed = speed;
        return this;
    }

    public VoicerBean setPitch(String pitch) {
        this.pitch = pitch;
        return this;
    }

    public VoicerBean setVoicerName(String voicerName) {
        this.voicerName = voicerName;
        return this;
    }

    public VoicerBean setRate(String rate) {
        this.rate = rate;
        return this;
    }
}
