package com.zzt.plan.app.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-16.
 */
public class EventEntity {
    private String title;
    private String info;
    private long ddl;
    private List<Long> timeList;
    private List<String> locationList;
    private List<UserEntity> invitePeopleList;

    public EventEntity() {
        timeList = new ArrayList<>();
        locationList = new ArrayList<>();
        invitePeopleList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getDdl() {
        return ddl;
    }

    public void setDdl(long ddl) {
        this.ddl = ddl;
    }

    public List<Long> getTimeList() {
        return timeList;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public List<UserEntity> getInvitePeopleList() {
        return invitePeopleList;
    }
}
