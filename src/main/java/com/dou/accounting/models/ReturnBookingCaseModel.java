package com.dou.accounting.models;

public class ReturnBookingCaseModel {
    String appId;
    String amtReturn;
    String batchId;
    String uploadDt;
    String type;
    String fromDt;
    String toDt;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAmtReturn() {
        return amtReturn;
    }

    public void setAmtReturn(String amtReturn) {
        this.amtReturn = amtReturn;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getUploadDt() {
        return uploadDt;
    }

    public void setUploadDt(String uploadDt) {
        this.uploadDt = uploadDt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
