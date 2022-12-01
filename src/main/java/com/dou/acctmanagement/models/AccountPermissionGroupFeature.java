package com.dou.acctmanagement.models;

public class AccountPermissionGroupFeature {
    private String id;
    private String accountId;
    private String groupCodeId;
    private String featureCodeId;
    private String featureName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getGroupCodeId() {
        return groupCodeId;
    }

    public void setGroupCodeId(String groupCodeId) {
        this.groupCodeId = groupCodeId;
    }

    public String getFeatureCodeId() {
        return featureCodeId;
    }

    public void setFeatureCodeId(String featureCodeId) {
        this.featureCodeId = featureCodeId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
