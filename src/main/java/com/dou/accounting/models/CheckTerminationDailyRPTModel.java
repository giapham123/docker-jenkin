package com.dou.accounting.models;

public class CheckTerminationDailyRPTModel {
    String TerDate;
    String agreementId;
    String userId;
    String maker;
    String checker;


    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getTerDate() {
        return TerDate;
    }

    public void setTerDate(String terDate) {
        TerDate = terDate;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
