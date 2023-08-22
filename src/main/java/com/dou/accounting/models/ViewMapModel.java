package com.dou.accounting.models;

public class ViewMapModel {

    private String batchId;
    private String transType;
    private String transDesc;
    private String agreementId;
    private String debitAmt;
    private String creditAmt;
    private String transDt;
    private String sapGl;
    private String remarks;
    private String leaVoucherId;
    private String updateDt;
    private String sapYn;
    private String channelCd;
    private String channelDesc;

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public String getChannelCd() {
        return channelCd;
    }

    public void setChannelCd(String channelCd) {
        this.channelCd = channelCd;
    }

    public String getSapYn() {
        return sapYn;
    }

    public void setSapYn(String sapYn) {
        this.sapYn = sapYn;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(String debitAmt) {
        this.debitAmt = debitAmt;
    }

    public String getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(String creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getTransDt() {
        return transDt;
    }

    public void setTransDt(String transDt) {
        this.transDt = transDt;
    }

    public String getSapGl() {
        return sapGl;
    }

    public void setSapGl(String sapGl) {
        this.sapGl = sapGl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeaVoucherId() {
        return leaVoucherId;
    }

    public void setLeaVoucherId(String leaVoucherId) {
        this.leaVoucherId = leaVoucherId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }
}
