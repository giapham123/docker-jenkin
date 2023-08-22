package com.dou.accounting.models;

import java.util.Date;

public class MonthlyDisbursalReportModel {
    Date logDt;
    String product;
    int countApp;
    String includedInsAmt;
    String notInsAmt;
    String insAmt;
    String disbursalDate;
    String noAppActual;
    String actualCashDis;
    String actualCashDisInIns;
    String pendingDisAmount;
    String AccumDisAmountInIns;
    String productType;
    String accumApp;
    String dateT1;
    int monthRpt;
    String yearRpt;
    String disAmout;
    String base64Img;
    String base64Img1;

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    public String getBase64Img1() {
        return base64Img1;
    }

    public void setBase64Img1(String base64Img1) {
        this.base64Img1 = base64Img1;
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

    public int getCountApp() {
        return countApp;
    }

    public void setCountApp(int countApp) {
        this.countApp = countApp;
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

    public String getDisbursalDate() {
        return disbursalDate;
    }

    public void setDisbursalDate(String disbursalDate) {
        this.disbursalDate = disbursalDate;
    }

    public String getNoAppActual() {
        return noAppActual;
    }

    public void setNoAppActual(String noAppActual) {
        this.noAppActual = noAppActual;
    }

    public String getActualCashDis() {
        return actualCashDis;
    }

    public void setActualCashDis(String actualCashDis) {
        this.actualCashDis = actualCashDis;
    }

    public String getActualCashDisInIns() {
        return actualCashDisInIns;
    }

    public void setActualCashDisInIns(String actualCashDisInIns) {
        this.actualCashDisInIns = actualCashDisInIns;
    }

    public String getPendingDisAmount() {
        return pendingDisAmount;
    }

    public void setPendingDisAmount(String pendingDisAmount) {
        this.pendingDisAmount = pendingDisAmount;
    }

    public String getAccumDisAmountInIns() {
        return AccumDisAmountInIns;
    }

    public void setAccumDisAmountInIns(String accumDisAmountInIns) {
        AccumDisAmountInIns = accumDisAmountInIns;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAccumApp() {
        return accumApp;
    }

    public void setAccumApp(String accumApp) {
        this.accumApp = accumApp;
    }

    public String getDateT1() {
        return dateT1;
    }

    public void setDateT1(String dateT1) {
        this.dateT1 = dateT1;
    }

    public int getMonthRpt() {
        return monthRpt;
    }

    public void setMonthRpt(int monthRpt) {
        this.monthRpt = monthRpt;
    }

    public String getYearRpt() {
        return yearRpt;
    }

    public void setYearRpt(String yearRpt) {
        this.yearRpt = yearRpt;
    }

    public String getDisAmout() {
        return disAmout;
    }

    public void setDisAmout(String disAmout) {
        this.disAmout = disAmout;
    }
}
