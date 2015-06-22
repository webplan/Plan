package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-17.
 */
public class SearchUser {
    public SearchUser(String account, String token, String nickname, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_NICKNAME, nickname);

        String actionURL = Config.SERVER_URL + Config.ACTION_SEARCH_USER + Config.SERVER_ACTION_SUFFIX;

        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                JSONArray array = object.getJSONArray(Config.KEY_USERS);
                                List<UserEntity> userList = new ArrayList<>();
                                JSONObject user;
                                for (int i = 0; i < array.length(); i++) {
                                    user = array.getJSONObject(i);
                                    userList.add(new UserEntity(user.getString(Config.KEY_ACCOUNT),
                                            user.getString(Config.KEY_NICKNAME),
                                            user.getString(Config.KEY_AVATAR_URL)));
                                }
                                successCallback.onSuccess(userList);
                            }
                            break;
                        default:
                            if (failCallback != null)
                                failCallback.onFail(object.getInt(Config.KEY_STATUS));
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
        void onSuccess(List<UserEntity> userList);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
