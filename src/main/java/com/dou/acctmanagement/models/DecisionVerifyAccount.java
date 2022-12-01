package com.dou.acctmanagement.models;

import java.util.List;

public class DecisionVerifyAccount {
    private List<String> listAccountId;
    private String decision;
    private String requestId;
    private String accountId;
    private String noteDecision;
    private String userVerify;
    private String passwordHash;
    private List<AccountInfoDetail> listAccountInfoDetails;

    public List<String> getListAccountId() {
        return listAccountId;
    }

    public void setListAccountId(List<String> listAccountId) {
        this.listAccountId = listAccountId;
    }

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<AccountInfoDetail> getListAccountInfoDetails() {
        return listAccountInfoDetails;
    }

    public void setListAccountInfoDetails(List<AccountInfoDetail> listAccountInfoDetails) {
        this.listAccountInfoDetails = listAccountInfoDetails;
    }
}
