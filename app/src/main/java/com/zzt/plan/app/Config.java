package com.zzt.plan.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zzt on 15-5-25.
 */
public class Config {
    public static final String CHARSET = "UTF-8";
    public static final String SERVER_URL = "http://192.168.1.107:8080/";
    public static final String SERVER_ACTION_SUFFIX = ".action";
    public static final String APP_ID = "com.zzt.plan";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD_MD5 = "password_md5";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_STATUS = "status";
    public static final String KEY_AVATAG = "avatag";
    public static final String KEY_FRIENDS = "people";
    public static final String KEY_AVATAR_URL = "avatar_url";
    public static final String KEY_PHONE_LIST = "phone_list";
    public static final String KEY_FRIEND_ACCOUNT = "friend_account";
    public static final String KEY_USERS = "users";
    public static final String KEY_EVENT_LIST = "planlist";
    public static final String KET_EVENT_ID = "plan_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_INFO = "info";
    public static final String KEY_TIME = "time";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PERSON = "person";
    public static final String KEY_TIME_LIST = "time_list";
    public static final String KEY_DDL = "ddl";
    public static final String KEY_LOCATION_LIST = "location_list";
    public static final String KEY_PEOPLE = "people";
    public static final String KEY_NUM = "num";
    public static final String KEY_UNSELECTED_FRIEND_ACCOUNT_LIST = "unselected_friend_account_list";
    public static final String KEY_SELECTED_FRIEND_ACCOUNT_LIST = "selected_friend_account_list";
    public static final String KEY_USER_ACCOUNT = "user_account";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "lon";
    public static final String KEY_EVENT_PLAN = "event_plan";
    public static final String KEY_PLAN_ID = "plan_id";

    public static final String ACTION_REGISTER = "register";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_GET_FRIENDS = "get_friendlist";
    public static final String ACTION_UPLOAD_FRIEND = "recommend";
    public static final String ACTION_ADD_FRIEND = "add_friend";
    public static final String ACTION_DELETE_FRIEND = "delete_friend";
    public static final String ACTION_SEARCH_USER = "search_user";
    public static final String ACTION_LOAD_EVENT = "view";
    public static final String ACTION_LOAD_INVATATION = "invite";
    public static final String ACTION_START_EVENT = "start";
    public static final String ACTION_USER_INFO = "user_info";
    public static final String ACTION_RETURN_PLAN = "return_plan";

    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;
    public static final int RESULT_STATUS_DUPLICATE_KEY = 3;

    public static String getCachedToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
    }

    public static void cacheToken(Context context, String token) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

    public static String getCachedAccount(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_ACCOUNT, null);
    }

    public static void cacheAccount(Context context, String account) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_ACCOUNT, account);
        e.commit();
    }
}
