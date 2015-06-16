package com.zzt.plan.app.net;

import com.zzt.plan.app.Config;
import com.zzt.plan.app.entity.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zzt on 15-6-6.
 */
public class LoadFriends {
    public LoadFriends(String account, String token, final SuccessCallback successCallback, final FailCallback failCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        String actionURL = Config.SERVER_URL + Config.ACTION_GET_FRIENDS + Config.SERVER_ACTION_SUFFIX;
        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                JSONArray array = obj.getJSONArray(Config.KEY_FRIENDS);
                                List<UserEntity> friends = new ArrayList<UserEntity>();
                                JSONObject friend;
                                for (int i = 0; i < array.length(); i++) {
                                    friend = array.getJSONObject(i);
                                    friends.add(new UserEntity(friend.getString(Config.KEY_ACCOUNT),
                                            friend.getString(Config.KEY_NICKNAME),
                                            friend.getString(Config.KEY_AVATAR_URL)));
                                }
                                successCallback.onSuccess(friends);
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
        void onSuccess(List<UserEntity> friends);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int failCode);
    }
}
