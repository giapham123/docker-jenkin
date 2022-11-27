package com.dou.accounting.models;

public class CloseSoldoutModel {
    String agreementId;
    String agreementNo;
    String cusName;
    String status;
    String npaStage;
    String product;
    String closureDt;
    String error;
    String userLogin;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNpaStage() {
        return npaStage;
    }

    public void setNpaStage(String npaStage) {
        this.npaStage = npaStage;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getClosureDt() {
        return closureDt;
    }

    public void setClosureDt(String closureDt) {
        this.closureDt = closureDt;
    }
}
