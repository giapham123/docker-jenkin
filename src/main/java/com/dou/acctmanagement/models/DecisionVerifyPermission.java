package com.dou.acctmanagement.models;

import java.util.List;

public class DecisionVerifyPermission {
    private String decision;
    private String requestId;
    private String accountId;
    private String noteDecision;
    private String userVerify;
    private List<AccountPermissionGroupSimFeature> listAccountInfoDetails;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

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

    public String getNoteDecision() {
        return noteDecision;
    }

    public void setNoteDecision(String noteDecision) {
        this.noteDecision = noteDecision;
    }

    public String getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(String userVerify) {
        this.userVerify = userVerify;
    }

    public List<AccountPermissionGroupSimFeature> getListAccountInfoDetails() {
        return listAccountInfoDetails;
    }

    public void setListAccountInfoDetails(List<AccountPermissionGroupSimFeature> listAccountInfoDetails) {
        this.listAccountInfoDetails = listAccountInfoDetails;
    }
}
