package com.zzt.plan.app.entity;

/**
 * Created by zzt on 15-6-16.
 */
public class UserEntity {
    private String account;
    private String nickname;
    private String avatarURL;

    public UserEntity(String account, String nickname, String avatarURL) {
        this.account = account;
        this.nickname = nickname;
        this.avatarURL = avatarURL;
    }

    public String getAccount() {
        return account;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarURL() {
        return avatarURL;
    }
}
