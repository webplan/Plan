package com.zzt.plan.app.net;

import android.graphics.Bitmap;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.tools.ImageUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zzt on 15-5-30.
 */
public class Register {

    public Register(String account, String password_md5, String nickname, String phone, Bitmap avatar, final SuccessCallback successCallback, final FailCallback failCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.KEY_ACCOUNT, account);
        params.put(Config.KEY_PASSWORD_MD5, password_md5);
        params.put(Config.KEY_NICKNAME, nickname);
        params.put(Config.KEY_PHONE, phone);
        params.put(Config.KEY_AVATAG, ImageUtil.Bitmap2StrByBase64(avatar));
        String actionURL = Config.SERVER_URL + Config.ACTION_REGISTER + Config.SERVER_ACTION_SUFFIX;
        new NetConnection(actionURL, HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null)
                                successCallback.onSuccess();
                            break;
                        default:
                            if (failCallback != null)
                                failCallback.onFail();
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
    }

}
