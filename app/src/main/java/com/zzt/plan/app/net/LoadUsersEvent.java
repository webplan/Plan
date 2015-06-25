package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.EventBaseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-23.
 */
public class LoadUsersEvent {
    public LoadUsersEvent(String account, String token, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);

        String actionURL = Config.SERVER_URL + Config.ACTION_GET_USERS_EVENT + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt(Config.KEY_STATUS);
                    switch (status) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                JSONArray JSONPlanList = object.getJSONArray(Config.KEY_PLAN_LIST);
                                List<EventBaseInfo> eventList = new ArrayList<>();
                                for (int i = 0; i < JSONPlanList.length(); i++) {
                                    EventBaseInfo event = new EventBaseInfo(JSONPlanList.getJSONObject(i).getInt(Config.KEY_PLAN_ID),
                                            JSONPlanList.getJSONObject(i).getString(Config.KEY_TITLE),
                                            JSONPlanList.getJSONObject(i).getString(Config.KEY_INFO));
                                    eventList.add(event);
                                }
                                successCallback.onSuccess(eventList);
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
        void onSuccess(List<EventBaseInfo> eventList);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
