package com.zzt.plan.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzt on 15-6-20.
 */
public class EventEntity implements Serializable {
    private int eventID;
    private String title;
    private String info;
    private long time;
    private LocationEntity location;
    private List<UserEntity> memberList;

    public EventEntity(int eventID, String title, String info, long time, LocationEntity location, List<UserEntity> memberList) {
        this.eventID = eventID;
        this.title = title;
        this.info = info;
        this.time = time;
        this.location = location;
        this.memberList = memberList;
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

    public long getTime() {
        return time;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public List<UserEntity> getMemberList() {
        return memberList;
    }
}
