package com.dou.adm.models;

import java.util.Date;
import java.util.Map;

public class UserLogin {
    String accountId;
    String ipAddress;
    Date startTimeLogin;
    Date endTimeLogin;
    Integer timeKeepAccount = 0;
    Map<String, Feature> permissions;

    public Date getStartTimeLogin() {
        return startTimeLogin;
    }

    public void setStartTimeLogin(Date startTimeLogin) {
        this.startTimeLogin = startTimeLogin;
    }

    public Date getEndTimeLogin() {
        return endTimeLogin;
    }

    public void setEndTimeLogin(Date endTimeLogin) {
        this.endTimeLogin = endTimeLogin;
    }

    public Map<String, Feature> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Feature> permissions) {
        this.permissions = permissions;
    }

    public Integer getTimeKeepAccount() {
        return timeKeepAccount;
    }

    public void setTimeKeepAccount(Integer timeKeepAccount) {
        this.timeKeepAccount = timeKeepAccount;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAccountId() {
        return accountId.toUpperCase();
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
