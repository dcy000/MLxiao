package com.gzq.lib_core.bean;

public class AlreadyYuyue {

    public String start_time;

    public String rid;


    public AlreadyYuyue() {
        super();
    }

    public AlreadyYuyue(String start_time, String rid) {
        this.start_time = start_time;
        this.rid = rid;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "AlreadyYuyue{" +
                "start_time='" + start_time + '\'' +
                ", rid='" + rid + '\'' +
                '}';
    }
}
