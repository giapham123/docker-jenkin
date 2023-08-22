package com.dou.accounting.models;

import java.util.Date;

public class DailyDisbursalReportModel {
    Date logDt;
    String product;
    Integer countApp;
    String includedInsAmt;
    String notInsAmt;
    String insAmt;
    String accumApp;
    String accumAmt;
    String disbursalDate;
    String productType;

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    String dateT1;
    String base64Img;



    public String getDateT1() {
        return dateT1;
    }

    public void setDateT1(String dateT1) {
        this.dateT1 = dateT1;
    }

    public String getDisbursalDate() {
        return disbursalDate;
    }

    public void setDisbursalDate(String disbursalDate) {
        this.disbursalDate = disbursalDate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIncludedInsAmt() {
        return includedInsAmt;
    }

    public void setIncludedInsAmt(String includedInsAmt) {
        this.includedInsAmt = includedInsAmt;
    }

    public String getNotInsAmt() {
        return notInsAmt;
    }

    public void setNotInsAmt(String notInsAmt) {
        this.notInsAmt = notInsAmt;
    }

    public String getInsAmt() {
        return insAmt;
    }

    public void setInsAmt(String insAmt) {
        this.insAmt = insAmt;
    }

    public String getAccumApp() {
        return accumApp;
    }

    public void setAccumApp(String accumApp) {
        this.accumApp = accumApp;
    }

    public String getAccumAmt() {
        return accumAmt;
    }

    public void setAccumAmt(String accumAmt) {
        this.accumAmt = accumAmt;
    }

    public Date getLogDt() {
        return logDt;
    }

    public void setLogDt(Date logDt) {
        this.logDt = logDt;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getCountApp() {
        return countApp;
    }

    public void setCountApp(Integer countApp) {
        this.countApp = countApp;
    }
}
