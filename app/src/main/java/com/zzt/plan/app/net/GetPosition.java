package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.LocationEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by snow on 15-6-23.
 */
public class GetPosition {
    public GetPosition(String account, String token, int planId, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_PLAN_ID, String.valueOf(planId));

        String actionURL = Config.SERVER_URL + Config.ACTION_GET_POSITION + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:

                            if (successCallback != null) {
                                /*********do!******************/

                                JSONArray jsonArray = new JSONArray(obj.getString("people").toString());
                                List<LocationEntity> position = new ArrayList<>();
                                double[] lat = new double[jsonArray.length()];
                                double[] lon = new double[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    lat[i] = jsonObject.getDouble(Config.KEY_LATITUDE);
                                    lon[i] = jsonObject.getDouble(Config.KEY_LONGITUDE);
                                }
                                /*********do!******************/
                                successCallback.onSuccess(lat, lon);
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

            }
        }, params);
    }

    public interface SuccessCallback {
        void onSuccess(double[] lat, double[] lon);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}