package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.LocationEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-22.
 */
public class VotePlan {
    public VotePlan(String account, String token, int eventID, boolean accept, List<LocationEntity> locationList, List<Long> timeList, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_PLAN_ID, String.valueOf(eventID));
        if (accept)
            params.put(Config.KEY_TIME, String.valueOf(System.currentTimeMillis()));
        else
            params.put(Config.KEY_TIME, String.valueOf(-1));
        JSONArray JSONLocationList = new JSONArray();
        for (LocationEntity location : locationList) {
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
        JSONArray JSONTimeList = new JSONArray();
        for (long time : timeList) {
            JSONTimeList.put(time);
        }
        params.put(Config.KEY_TIME_LIST, JSONTimeList.toString());

        String actionURL = Config.SERVER_URL + Config.ACTION_RETURN_PLAN + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                successCallback.onSuccess();
                            }
                            break;
                        default:
                            if (failCallback != null)
                                failCallback.onFail(obj.getInt(Config.KEY_STATUS));

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
