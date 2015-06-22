package com.zzt.plan.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zzt on 15-6-16.
 */
public class EventPlanEntity implements Serializable {
    private int eventPlanID;
    private String title;
    private String info;
    private long ddl;
    private List<Calendar> timeList;
    private List<LocationEntity> locationList;
    private List<UserEntity> invitePeopleList;

    public EventPlanEntity() {
        timeList = new ArrayList<>();
        locationList = new ArrayList<>();
        invitePeopleList = new ArrayList<>();
    }

    public int getEventPlanID() {
        return eventPlanID;
    }

    public void setEventPlanID(int eventPlanID) {
        this.eventPlanID = eventPlanID;
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

    public List<Calendar> getTimeList() {
        return timeList;
    }

    public List<LocationEntity> getLocationList() {
        return locationList;
    }

    public List<UserEntity> getInvitePeopleList() {
        return invitePeopleList;
    }

    public void addTime(Calendar time) {
        timeList.add(time);
    }

    public void addLocation(LocationEntity location) {
        locationList.add(location);
    }

    public void addInvitePeople(UserEntity invitePeople) {
        invitePeopleList.add(invitePeople);
    }
}
