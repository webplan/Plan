package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.EventEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-20.
 */
public class LoadEvents {

    public LoadEvents(String account, String token, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);

        String actionURL = Config.SERVER_URL + Config.ACTION_LOAD_EVENT + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt(Config.KEY_STATUS);
                    switch (status) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                JSONArray array = object.getJSONArray(Config.KEY_EVENT_LIST);
                                JSONObject JSONEvent;
                                List<EventEntity> eventList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {

                                    JSONEvent = array.getJSONObject(i);
                                    int eventID = JSONEvent.getInt(Config.KET_EVENT_ID);
                                    String title = JSONEvent.getString(Config.KEY_TITLE);
                                    String info = JSONEvent.getString(Config.KEY_INFO);
                                    Long time = JSONEvent.getLong(Config.KEY_TIME);
                                    LocationEntity location = new LocationEntity();
                                    location.setName(JSONEvent.getJSONObject(Config.KEY_LOCATION).getString(Config.KEY_LOCATION));
                                    location.setLatitude(JSONEvent.getJSONObject(Config.KEY_LOCATION).getDouble(Config.KEY_LATITUDE));
                                    location.setLongitude(JSONEvent.getJSONObject(Config.KEY_LOCATION).getDouble(Config.KEY_LONGITUDE));

                                    List<UserEntity> userList = new ArrayList<>();
                                    JSONArray JSONUsers = JSONEvent.getJSONArray(Config.KEY_PERSON);
                                    JSONObject JSONUser;
                                    for (int j = 0; j < JSONUsers.length(); j++) {
                                        JSONUser = JSONUsers.getJSONObject(j);
                                        userList.add(new UserEntity(JSONUser.getString(Config.KEY_ACCOUNT),
                                                JSONUser.getString(Config.KEY_NICKNAME),
                                                JSONUser.getString(Config.KEY_AVATAG)));
                                    }
                                    eventList.add(new EventEntity(eventID, title, info, time, location, userList));
                                }

                                successCallback.onSuccess(eventList);
                            }
                            break;
                        default:
                            if (failCallback != null)
                                failCallback.onFail(status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null)
                        failCallback.onFail();
                }
            }
        }, new NetConnection.FailCallBack() {
            @Override
            public void onFail() {
                if (failCallback != null)
                    failCallback.onFail();
            }
        }, params);
    }

    public interface SuccessCallback {
        void onSuccess(List<EventEntity> friends);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int failCode);
    }
}
