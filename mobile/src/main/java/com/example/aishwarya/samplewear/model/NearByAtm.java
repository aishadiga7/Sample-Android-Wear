package com.example.aishwarya.samplewear.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NearByAtm {

    @SerializedName("name")
    @Expose
    public String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
