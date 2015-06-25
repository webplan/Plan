package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.LocationEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-6-23.
 */
public class ConfirmPlan {
    public ConfirmPlan(String account, String token, int planID, long time, LocationEntity location, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_PLAN_ID, String.valueOf(planID));
        params.put(Config.KEY_TIME, String.valueOf(time));
        JSONObject JSONLocation = new JSONObject();
        try {
            JSONLocation.put(Config.KEY_LOCATION, location.getName());
            JSONLocation.put(Config.KEY_LATITUDE, location.getLatitude());
            JSONLocation.put(Config.KEY_LONGITUDE, location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(Config.KEY_LOCATION, JSONLocation.toString());

        String actionURL = Config.SERVER_URL + Config.ACTION_CONFIRM_PLAN + Config.SERVER_ACTION_SUFFIX;

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
