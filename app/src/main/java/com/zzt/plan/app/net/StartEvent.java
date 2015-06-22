package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.EventPlanEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-6-16.
 */
public class StartEvent {
    public StartEvent(String account, String token, EventPlanEntity plan, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_TITLE, plan.getTitle());
        params.put(Config.KEY_INFO, plan.getInfo());
        params.put(Config.KEY_DDL, String.valueOf(plan.getDdl()));
        JSONArray JSONTimeList = new JSONArray();
        for (Calendar time : plan.getTimeList()) {
            JSONTimeList.put(time.getTimeInMillis());
        }
        params.put(Config.KEY_TIME_LIST, JSONTimeList.toString());
        JSONArray JSONLocationList = new JSONArray();
        for (LocationEntity location : plan.getLocationList()) {
            try {
                JSONObject object = new JSONObject();
                object.put(Config.KEY_LOCATION, location.getName());
                object.put(Config.KEY_LATITUDE, location.getLatitude());
                object.put(Config.KEY_LONGITUDE, location.getLongitude());
                JSONLocationList.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put(Config.KEY_LOCATION_LIST, JSONLocationList.toString());

        JSONArray JSONAccountList = new JSONArray();
        for (UserEntity user : plan.getInvitePeopleList()) {
            JSONAccountList.put(user.getAccount());
        }
        params.put(Config.KEY_PEOPLE, JSONAccountList.toString());

        String actionURL = Config.SERVER_URL + Config.ACTION_START_EVENT + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt(Config.KEY_STATUS);
                    switch (status) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null)
                                successCallback.onSuccess();
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
        void onSuccess();
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
