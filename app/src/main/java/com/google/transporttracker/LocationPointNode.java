package com.google.transporttracker;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by admin on 10/6/2017.
 */
@IgnoreExtraProperties

public class LocationPointNode {

    public long time;
    public Integer power;
    public Double lat;
    public Double lng;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


}
