package com.dou.bankstatement.models;

public class BankStatementModel {
    private String bankName;
    private String fileName;
    private String acct;
    private String balance;
    private String sapAcct;
    private String fromDt;
    private String toDt;
    private String statmentDt;

    public String getStatmentDt() {
        return statmentDt;
    }

    public void setStatmentDt(String statmentDt) {
        this.statmentDt = statmentDt;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSapAcct() {
        return sapAcct;
    }

    public void setSapAcct(String sapAcct) {
        this.sapAcct = sapAcct;
    }
}
