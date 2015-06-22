package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.EventPlanEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zzt on 15-6-20.
 */
public class LoadInvitations {
    public LoadInvitations(String account, String token, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);

        String actionURL = Config.SERVER_URL + Config.ACTION_LOAD_INVATATION + Config.SERVER_ACTION_SUFFIX;

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
                                JSONObject JSONPlan;
                                List<EventPlanEntity> planList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONPlan = array.getJSONObject(i);
                                    EventPlanEntity plan = new EventPlanEntity();
                                    plan.setEventPlanID(JSONPlan.getInt(Config.KET_EVENT_ID));
                                    plan.setTitle(JSONPlan.getString(Config.KEY_TITLE));
                                    plan.setInfo(JSONPlan.getString(Config.KEY_INFO));
                                    plan.setDdl(JSONPlan.getLong(Config.KEY_DDL));

                                    JSONArray JSONTimeList = JSONPlan.getJSONArray(Config.KEY_TIME_LIST);
                                    JSONArray JSONLocationList = JSONPlan.getJSONArray(Config.KEY_LOCATION_LIST);
                                    JSONArray JSONUsers = JSONPlan.getJSONArray(Config.KEY_PERSON);

                                    for (int j = 0; j < JSONTimeList.length(); j++) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(JSONTimeList.getLong(j));
                                        plan.addTime(calendar);
                                    }

                                    for (int j = 0; j < JSONLocationList.length(); j++) {
                                        LocationEntity location = new LocationEntity();
                                        location.setName(JSONLocationList.getJSONObject(j).getString(Config.KEY_LOCATION));
                                        location.setLatitude(JSONLocationList.getJSONObject(j).getDouble(Config.KEY_LATITUDE));
                                        location.setLongitude(JSONLocationList.getJSONObject(j).getDouble(Config.KEY_LONGITUDE));
                                        plan.addLocation(location);
                                    }

                                    for (int j = 0; j < JSONUsers.length(); j++) {
                                        JSONObject JSONUser = JSONUsers.getJSONObject(j);
                                        plan.addInvitePeople(new UserEntity(JSONUser.getString(Config.KEY_ACCOUNT),
                                                JSONUser.getString(Config.KEY_NICKNAME),
                                                JSONUser.getString(Config.KEY_AVATAG)));
                                    }

                                    planList.add(plan);
                                }

                                successCallback.onSuccess(planList);
                            }
                            break;
                        default:
                            if (failCallback != null)
                                failCallback.onFail(status);
                    }
                } catch (JSONException e) {
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
        void onSuccess(List<EventPlanEntity> invitations);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int failCode);
    }
}
