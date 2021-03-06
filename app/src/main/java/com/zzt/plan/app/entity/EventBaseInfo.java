package com.zzt.plan.app.entity;

import java.io.Serializable;

/**
 * Created by zzt on 15-6-23.
 */
public class EventBaseInfo implements Serializable {
    private int eventID;
    private String title;
    private String info;

    public EventBaseInfo(int eventID, String title, String info) {
        this.eventID = eventID;
        this.title = title;
        this.info = info;
    }

    public int getEventID() {
        return eventID;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }
}
