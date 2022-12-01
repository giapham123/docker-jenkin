package com.dou.adm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class User {

    @JsonIgnore
    private Long id;

    @JsonProperty("account_id")
    private String accountId;

    @JsonIgnore
    private String password;

    private boolean isBlocked;

    private boolean isDeleted;

    @JsonProperty("status")
    private long isStatus;

    @JsonProperty("admin")
    private long isAdmin;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private Date accessTokenExpired;

    @JsonIgnore
    private char[] generateKey;

    @JsonIgnore
    private String resetToken;

    @JsonProperty("group")
    private String groupId;

    @JsonProperty("department")
    private String departmentId;

    @JsonIgnore
    private List<Role> roles;

    public User() {}

    public User(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(long isStatus) {
        this.isStatus = isStatus;
    }

    public long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(long isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getAccessTokenExpired() {
        return accessTokenExpired;
    }

    public void setAccessTokenExpired(Date accessTokenExpired) {
        this.accessTokenExpired = accessTokenExpired;
    }

    public char[] getGenerateKey() {
        return generateKey;
    }

    public void setGenerateKey(char[] generateKey) {
        this.generateKey = generateKey;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}