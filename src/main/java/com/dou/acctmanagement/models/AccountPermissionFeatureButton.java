package com.dou.acctmanagement.models;

public class AccountPermissionFeatureButton {
    private String id;
    private String accountId;
    private String featureCodeId;
    private String buttonCodeId;
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

    public String getFeatureCodeId() {
        return featureCodeId;
    }

    public void setFeatureCodeId(String featureCodeId) {
        this.featureCodeId = featureCodeId;
    }

    public String getButtonCodeId() {
        return buttonCodeId;
    }

    public void setButtonCodeId(String buttonCodeId) {
        this.buttonCodeId = buttonCodeId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
