package com.dou.accounting.models;

import java.util.Date;

public class UploadBankStatementModel {
    String appId;
    String detail;
    String txnNo;
    String descAcc;
    String debitAmt;
    String userLogin;
    String reason;
    Date statementDate;
    String statementDateExcel;
    String fromDt;
    String toDt;
    Integer page;

    public String getStatementDateExcel() {
        return statementDateExcel;
    }

    public void setStatementDateExcel(String statementDateExcel) {
        this.statementDateExcel = statementDateExcel;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getFromDt() {
        return fromDt;
    }

    public void setFromDt(String fromDt) {
        this.fromDt = fromDt;
    }

    public String getToDt() {
        return toDt;
    }

    public void setToDt(String toDt) {
        this.toDt = toDt;
    }

    public Date getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Date statementDate) {
        this.statementDate = statementDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTxnNo() {
        return txnNo;
    }

    public void setTxnNo(String txnNo) {
        this.txnNo = txnNo;
    }

    public String getDescAcc() {
        return descAcc;
    }

    public void setDescAcc(String descAcc) {
        this.descAcc = descAcc;
    }

    public String getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(String debitAmt) {
        this.debitAmt = debitAmt;
    }
}
