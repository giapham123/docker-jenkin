package com.dou.accounting.models;

public class RejectUploadFileGLSAPModel {
      String agreementId;
      String glAcct;
      String debit;
      String credit;
      String remarks;
      String roDept;
      String descriptionvietnames;
      String uploadDate;
      String acctName;

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getGlAcct() {
        return glAcct;
    }

    public void setGlAcct(String glAcct) {
        this.glAcct = glAcct;
    }

    public String getDebit() {
        if(debit== null){
            return "";
        }
        double  rs = Double.parseDouble(debit)/1000;
        return String.valueOf(rs);
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        if(credit== null){
            return "";
        }
        double rs = Double.parseDouble(credit)/1000;
        return String.valueOf(rs);
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRoDept() {
        return roDept;
    }

    public void setRoDept(String roDept) {
        this.roDept = roDept;
    }

    public String getDescriptionvietnames() {
        return descriptionvietnames;
    }

    public void setDescriptionvietnames(String descriptionvietnames) {
        this.descriptionvietnames = descriptionvietnames;
    }
}
