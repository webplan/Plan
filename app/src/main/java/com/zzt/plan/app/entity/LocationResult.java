package com.zzt.plan.app.entity;

/**
 * Created by zzt on 15-6-23.
 */
public class LocationResult {
    private LocationEntity location;
    private int num;

    public LocationResult(LocationEntity location, int num) {
        this.location = location;
        this.num = num;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public int getNum() {
        return num;
    }
}
