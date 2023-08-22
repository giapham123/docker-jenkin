package com.dou.accounting.models;

public class CollectionTransModel {

    private String channel;
    private String cusNm;
    private String transDt;
    private String agreementId;
    private String amount;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCusNm() {
        return cusNm;
    }

    public void setCusNm(String cusNm) {
        this.cusNm = cusNm;
    }

    public String getTransDt() {
        return transDt;
    }

    public void setTransDt(String transDt) {
        this.transDt = transDt;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
