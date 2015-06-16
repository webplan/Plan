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
 * Created by zzt on 15-6-16.
 */
public class UploadContacts {
    public UploadContacts(String account, String token, JSONArray phoneList, final SuccessCallback successCallback, final FailCallback failCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_TOKEN, token);
        params.put(Config.KEY_PHONE_LIST, phoneList.toString());
        String actionURL = Config.SERVER_URL + Config.ACTION_UPLOAD_FRIEND + Config.SERVER_ACTION_SUFFIX;

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

            }
        }, params);
    }

    public interface SuccessCallback {
        void onSuccess(List<UserEntity> friend);
    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
