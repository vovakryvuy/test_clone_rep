package com.example.volodymyr.sunrisesunset.model_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volodymyr on 1/22/2018.
 */

public class ResponseModel {
    @SerializedName("results")
    @Expose
    private Results results;
    @SerializedName("status")
    @Expose
    private String status;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
