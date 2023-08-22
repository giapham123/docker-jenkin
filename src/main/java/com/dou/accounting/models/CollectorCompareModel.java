package com.dou.accounting.models;

public class CollectorCompareModel {
    private String agreementId;
    private String agreementNo;
    private String txnDt;
    private String txnAmt;
    private String collector;
    private String channel;
    private String resultMsg;
    private String processDt;

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

    public String getTxnDt() {
        return txnDt;
    }

    public void setTxnDt(String txnDt) {
        this.txnDt = txnDt;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getProcessDt() {
        return processDt;
    }

    public void setProcessDt(String processDt) {
        this.processDt = processDt;
    }
}
