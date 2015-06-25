package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.LocationResult;
import com.zzt.plan.app.entity.TimeResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-21.
 */
public class ViewStat {
    public ViewStat(String account, String token, int eventID, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_TIME, String.valueOf(System.currentTimeMillis()));
        params.put(Config.KET_EVENT_ID, String.valueOf(eventID));

        String actionURL = Config.SERVER_URL + Config.ACTION_VIEW_STAT + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt(Config.KEY_STATUS);
                    switch (status) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                String title = object.getString(Config.KEY_TITLE);
                                String info = object.getString(Config.KEY_INFO);
                                long ddl = object.getLong(Config.KEY_DDL);
                                JSONArray JSONTimeList = object.getJSONArray(Config.KEY_TIME_LIST);
                                JSONArray JSONLocationList = object.getJSONArray(Config.KEY_LOCATION_LIST);
                                List<TimeResult> timeList = new ArrayList<>();
                                List<LocationResult> locationList = new ArrayList<>();
                                for (int i = 0; i < JSONTimeList.length(); i++) {
                                    TimeResult tr = new TimeResult(JSONTimeList.getJSONObject(i).getLong(Config.KEY_TIME),
                                            JSONTimeList.getJSONObject(i).getInt(Config.KEY_NUM));
                                    timeList.add(tr);
                                }
                                for (int i = 0; i < JSONLocationList.length(); i++) {
                                    LocationEntity location = new LocationEntity();
                                    location.setName(JSONLocationList.getJSONObject(i).getString(Config.KEY_LOCATION));
                                    location.setLatitude(JSONLocationList.getJSONObject(i).getDouble(Config.KEY_LATITUDE));
                                    location.setLongitude(JSONLocationList.getJSONObject(i).getDouble(Config.KEY_LONGITUDE));
                                    LocationResult lr = new LocationResult(location, JSONLocationList.getJSONObject(i).getInt(Config.KEY_NUM));
                                    locationList.add(lr);
                                }
                                successCallback.onSuccess(title, info, ddl, timeList, locationList);
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
        void onSuccess(String title, String info, long ddl, List<TimeResult> timeList, List<LocationResult> locationList);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
