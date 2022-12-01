package com.dou.acctmanagement.models;

public class AccountPermissionGroupSimFeature {
    private String id;
    private String accountId;
    private String groupId;
    private String requestId;
    private String requestBy;
    private String requestTimeString;
    private String verifyBy;
    private String verifyTimeString;
    private String listFeture;
    private String groupName;
    private String application;
    private String action;
    private String verifyNote;
    private String verifyStatus;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTimeString() {
        return requestTimeString;
    }

    public void setRequestTimeString(String requestTimeString) {
        this.requestTimeString = requestTimeString;
    }

    public String getVerifyBy() {
        return verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }

    public String getVerifyTimeString() {
        return verifyTimeString;
    }

    public void setVerifyTimeString(String verifyTimeString) {
        this.verifyTimeString = verifyTimeString;
    }

    public String getListFeture() {
        return listFeture;
    }

    public void setListFeture(String listFeture) {
        this.listFeture = listFeture;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVerifyNote() {
        return verifyNote;
    }

    public void setVerifyNote(String verifyNote) {
        this.verifyNote = verifyNote;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}
