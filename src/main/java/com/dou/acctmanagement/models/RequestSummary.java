package com.dou.acctmanagement.models;

public class RequestSummary {
    private String requestId;
    private String accountId;
    private String requestTypeId;
    private String requestTypeName;
    private String requestNote;
    private String requestBy;
    private String requestTimeString;
    private String verifyNote;
    private String verifyStatus;
    private String verifyBy;
    private String verifyTimeString;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestTimeString() {
        return requestTimeString;
    }

    public void setRequestTimeString(String requestTimeString) {
        this.requestTimeString = requestTimeString;
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

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }
}
