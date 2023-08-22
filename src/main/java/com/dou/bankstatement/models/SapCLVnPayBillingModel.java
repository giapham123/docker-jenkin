package com.dou.bankstatement.models;

public class SapCLVnPayBillingModel {
    private String stt;
    private String refnum;
    private String paiddate;
    private String contractno;
    private String customername;
    private String amount;
    private String file_name;

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getRefnum() {
        return refnum;
    }

    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    public String getPaiddate() {
        return paiddate;
    }

    public void setPaiddate(String paiddate) {
        this.paiddate = paiddate;
    }

    public String getContractno() {
        return contractno;
    }

    public void setContractno(String contractno) {
        this.contractno = contractno;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
