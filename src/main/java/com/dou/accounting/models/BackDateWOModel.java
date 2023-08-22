package com.dou.accounting.models;

public class BackDateWOModel {

  private String agreementId;
  private String runDt;
  private String uptDt;
    private String backDt;
  private String runYn;
  private String errorMsg;
    private String userLogin;

    public String getBackDt() {
        return backDt;
    }

    public void setBackDt(String backDt) {
        this.backDt = backDt;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getRunDt() {
        return runDt;
    }

    public void setRunDt(String runDt) {
        this.runDt = runDt;
    }

    public String getUptDt() {
        return uptDt;
    }

    public void setUptDt(String uptDt) {
        this.uptDt = uptDt;
    }

    public String getRunYn() {
        return runYn;
    }

    public void setRunYn(String runYn) {
        this.runYn = runYn;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
