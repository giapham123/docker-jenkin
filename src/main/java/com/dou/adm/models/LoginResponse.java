package com.dou.adm.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LoginResponse {

    @JsonProperty("user_info")
    private User account;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expireHours;

    @JsonProperty("is_default_password")
    private boolean defaultPassword;

    @JsonProperty("user_permissions")
    private Map<String, Feature> permissions;

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpireHours() {
        return expireHours;
    }

    public void setExpireHours(int expireHours) {
        this.expireHours = expireHours;
    }

    public boolean isDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public Map<String, Feature> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Feature> permissions) {
        this.permissions = permissions;
    }
}