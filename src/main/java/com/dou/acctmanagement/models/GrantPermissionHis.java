package com.dou.acctmanagement.models;

public class GrantPermissionHis {
    private String userCreate;
    private String accountId;
    private String featureCodeId;
    private String createdDateTime;
    private String action;
    private String groupId;

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFeatureCodeId() {
        return featureCodeId;
    }

    public void setFeatureCodeId(String featureCodeId) {
        this.featureCodeId = featureCodeId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
