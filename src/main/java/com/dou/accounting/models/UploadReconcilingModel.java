package com.dou.accounting.models;

import java.util.Date;

public class UploadReconcilingModel {

    private String transType;
    private String channelCd;
    private String agreementId;
    private String transDt;
    private String amount;
    private String userLogin;
    private String reason;
    private Integer batchId;
    private Date transDate;
    private String channelDesc;
    private String transDesc;
    private String uploadDt;
    private Integer txnId;
    private String uploadBy;
    private String genSapYN;
    private String genSapDt;
    private String genSapBy;
    private String errorDesc;
    private Integer agreementIdImport;
    private String leaVoucherId;
    private String caseId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getLeaVoucherId() {
        return leaVoucherId;
    }

    public void setLeaVoucherId(String leaVoucherId) {
        this.leaVoucherId = leaVoucherId;
    }

    public Integer getAgreementIdImport() {
        return agreementIdImport;
    }

    public void setAgreementIdImport(Integer agreementIdImport) {
        this.agreementIdImport = agreementIdImport;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public Integer getTxnId() {
        return txnId;
    }

    public void setTxnId(Integer txnId) {
        this.txnId = txnId;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public String getGenSapYN() {
        return genSapYN;
    }

    public void setGenSapYN(String genSapYN) {
        this.genSapYN = genSapYN;
    }

    public String getGenSapDt() {
        return genSapDt;
    }

    public void setGenSapDt(String genSapDt) {
        this.genSapDt = genSapDt;
    }

    public String getGenSapBy() {
        return genSapBy;
    }

    public void setGenSapBy(String genSapBy) {
        this.genSapBy = genSapBy;
    }

    public String getUploadDt() {
        return uploadDt;
    }

    public void setUploadDt(String uploadDt) {
        this.uploadDt = uploadDt;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getChannelCd() {
        return channelCd;
    }

    public void setChannelCd(String channelCd) {
        this.channelCd = channelCd;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getTransDt() {
        return transDt;
    }

    public void setTransDt(String transDt) {
        this.transDt = transDt;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
