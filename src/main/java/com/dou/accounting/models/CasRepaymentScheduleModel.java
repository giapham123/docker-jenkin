package com.dou.accounting.models;

public class CasRepaymentScheduleModel {
    private String agreementId;
    private String instlNum;
    private String dueDt;
    private String instlAmt;
    private String prinComp;
    private String intComp;
    private String effRate;
    private String balPrin;
    private String days;

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getInstlNum() {
        return instlNum;
    }

    public void setInstlNum(String instlNum) {
        this.instlNum = instlNum;
    }

    public String getDueDt() {
        return dueDt;
    }

    public void setDueDt(String dueDt) {
        this.dueDt = dueDt;
    }

    public String getInstlAmt() {
        return instlAmt;
    }

    public void setInstlAmt(String instlAmt) {
        this.instlAmt = instlAmt;
    }

    public String getPrinComp() {
        return prinComp;
    }

    public void setPrinComp(String prinComp) {
        this.prinComp = prinComp;
    }

    public String getIntComp() {
        return intComp;
    }

    public void setIntComp(String intComp) {
        this.intComp = intComp;
    }

    public String getEffRate() {
        return effRate;
    }

    public void setEffRate(String effRate) {
        this.effRate = effRate;
    }

    public String getBalPrin() {
        return balPrin;
    }

    public void setBalPrin(String balPrin) {
        this.balPrin = balPrin;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
