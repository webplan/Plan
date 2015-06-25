package com.zzt.plan.app.entity;

/**
 * Created by zzt on 15-6-23.
 */
public class TimeResult {
    private long time;
    private int num;

    public TimeResult(long time, int num) {
        this.time = time;
        this.num = num;
    }

    public long getTime() {
        return time;
    }

    public int getNum() {
        return num;
    }
}
