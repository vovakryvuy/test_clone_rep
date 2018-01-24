package com.example.volodymyr.sunrisesunset.model_response.city_information;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volodymyr on 1/23/2018.
 */

public class Viewport {
    @SerializedName("northeast")
    @Expose
    private Northeast_ northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest_ southwest;

    public Northeast_ getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast_ northeast) {
        this.northeast = northeast;
    }

    public Southwest_ getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest_ southwest) {
        this.southwest = southwest;
    }
}
