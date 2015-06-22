package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

        String actionURL = Config.SERVER_URL + Config.ACTION_START_EVENT + Config.SERVER_ACTION_SUFFIX;

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
                                JSONArray timeList = object.getJSONArray(Config.KEY_TIME_LIST);
                                JSONArray locationList = object.getJSONArray(Config.KEY_LOCATION_LIST);
                                Map<Long, Integer> timeNumMap = new TreeMap<>();
                                Map<String, Integer> locationNumMap = new TreeMap<>();
                                for (int i = 0; i < timeList.length(); i++) {
                                    timeNumMap.put(timeList.getJSONObject(i).getLong(Config.KEY_TIME), timeList.getJSONObject(i).getInt(Config.KEY_NUM));
                                }
                                for (int i = 0; i < locationList.length(); i++) {
                                    locationNumMap.put(locationList.getJSONObject(i).getString(Config.KEY_LOCATION), locationList.getJSONObject(i).getInt(Config.KEY_NUM));
                                }
                                successCallback.onSuccess(title, info, timeNumMap, locationNumMap);
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
        void onSuccess(String title, String info, Map<Long, Integer> timeNumMap, Map<String, Integer> locationNumMap);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
