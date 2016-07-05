package com.example.aishwarya.samplewear.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NearByAtmResult {

    @SerializedName("html_attributions")
    @Expose
    public List<Object> mHtmlAttributions = new ArrayList<Object>();
    @SerializedName("results")
    @Expose
    private ArrayList<NearByAtm> mResults = new ArrayList<NearByAtm>();
    @SerializedName("status")
    @Expose
    private String mStatus;
    @SerializedName("error_message")
    @Expose
    private String mErrorMessage;

    public ArrayList<NearByAtm> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<NearByAtm> results) {
        mResults = results;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
