package com.dou.accounting.models;

public class OutNetReportModel {
    String agreementId;
    String agreementNo;
    String customerName;
    String collectionFee;
    String emiAtm;
    String lastEmi;
    String lastDuedate;
    String receiptAtm;
    String receiptDate;
    String netReceiveableAtReceiptDate;
    String netReceiveableAtReportDate;
    String forceClosureChargeAmt;
    String reportDate;
    String receiptFrom;
    String receiptTo;

    public String getReceiptTo() {
        return receiptTo;
    }

    public void setReceiptTo(String receiptTo) {
        this.receiptTo = receiptTo;
    }

    public String getReceiptFrom() {
        return receiptFrom;
    }

    public void setReceiptFrom(String receiptFrom) {
        this.receiptFrom = receiptFrom;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCollectionFee() {
        if(this.collectionFee == null){
            return "";
        }
        return collectionFee;
    }

    public void setCollectionFee(String collectionFee) {
        this.collectionFee = collectionFee;
    }

    public String getEmiAtm() {
        if(this.emiAtm == null){
            return "";
        }
        return emiAtm;
    }

    public void setEmiAtm(String emiAtm) {
        this.emiAtm = emiAtm;
    }

    public String getLastEmi() {
        if(this.lastEmi == null){
            return "";
        }
        return lastEmi;
    }

    public void setLastEmi(String lastEmi) {
        this.lastEmi = lastEmi;
    }

    public String getLastDuedate() {
        return lastDuedate;
    }

    public void setLastDuedate(String lastDuedate) {
        this.lastDuedate = lastDuedate;
    }

    public String getReceiptAtm() {
        if(this.receiptAtm == null){
            return "";
        }
        return receiptAtm;
    }

    public void setReceiptAtm(String receiptAtm) {
        this.receiptAtm = receiptAtm;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getNetReceiveableAtReceiptDate() {
        if(this.netReceiveableAtReceiptDate == null){
            return "";
        }
        return netReceiveableAtReceiptDate;
    }

    public void setNetReceiveableAtReceiptDate(String netReceiveableAtReceiptDate) {
        this.netReceiveableAtReceiptDate = netReceiveableAtReceiptDate;
    }

    public String getNetReceiveableAtReportDate() {
        return netReceiveableAtReportDate;
    }

    public void setNetReceiveableAtReportDate(String netReceiveableAtReportDate) {
        this.netReceiveableAtReportDate = netReceiveableAtReportDate;
    }

    public String getForceClosureChargeAmt() {
        return forceClosureChargeAmt;
    }

    public void setForceClosureChargeAmt(String forceClosureChargeAmt) {
        this.forceClosureChargeAmt = forceClosureChargeAmt;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}
