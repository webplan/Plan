package com.zzt.plan.app.entity;

import com.zzt.plan.app.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONArray getMemberList() {
        JSONArray array = new JSONArray();
        for (UserEntity member : memberList) {
            try {
                JSONObject object = new JSONObject();
                object.put(Config.KEY_ACCOUNT, member.getAccount());
                object.put(Config.KEY_NICKNAME, member.getNickname());
                object.put(Config.KEY_AVATAG, member.getAvatarURL());
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
}
