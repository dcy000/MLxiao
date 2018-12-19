package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdLiveness {
    /**
     * livemapscore : 0.08331970848
     */

    @SerializedName("livemapscore")
    private double livemapscore;

    public double getLivemapscore() {
        return livemapscore;
    }

    public void setLivemapscore(double livemapscore) {
        this.livemapscore = livemapscore;
    }
}
