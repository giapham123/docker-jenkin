package com.dou.accounting.models;

public class UploadFileModel {
  String agreementId;
  String agreementNo;
  String batchId;
  String receiptNo;
  String chequeNo;
  String bankAcnum;
  String drawNon;
  String toWards;
  String receiptAmt;
  String receiptDate;
  String userId;
  String uploadDate;
  String chequeId;
  Integer page;
  String fromDate;
  String toDate;
  Integer no;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getBankAcnum() {
        return bankAcnum;
    }

    public void setBankAcnum(String bankAcnum) {
        this.bankAcnum = bankAcnum;
    }

    public String getDrawNon() {
        return drawNon;
    }

    public void setDrawNon(String drawNon) {
        this.drawNon = drawNon;
    }

    public String getToWards() {
        return toWards;
    }

    public void setToWards(String toWards) {
        this.toWards = toWards;
    }

    public String getReceiptAmt() {
        return receiptAmt;
    }

    public void setReceiptAmt(String receiptAmt) {
        this.receiptAmt = receiptAmt;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getChequeId() {
        return chequeId;
    }

    public void setChequeId(String chequeId) {
        this.chequeId = chequeId;
    }
}
