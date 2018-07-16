package com.qdhualing.qrcodetracker.bean;

/**
 * Created by Administrator on 2018/1/27.
 */
public class LoginResult {
    private String userId;

    private String userName ;

    private String pwd;

    private String trueName;

    private String checkQXGroup;//审核权限组，有19的话就是领导（负责人），反之是质检员

    private String portraitUrl ;

    public String getCheckQXGroup() {
        return checkQXGroup;
    }

    public void setCheckQXGroup(String checkQXGroup) {
        this.checkQXGroup = checkQXGroup;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
