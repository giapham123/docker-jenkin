
package com.dou.accounting.models;

public class ViewSapEntriesModel {

  private String glAcct;
  private String acctNm;
  private String debit;
  private String credit;
  private String remarks;
  private String roDept;
  private String budgetCd;
  private String descVn;
  private String groupGl;
  private String txnDt;
  private String runDt;
  private String updateDt;
  private String transDt;
  private String sapGl;
  private String sapYn;

    private String usrLg;

    public String getUsrLg() {
        return usrLg;
    }

    public void setUsrLg(String usrLg) {
        this.usrLg = usrLg;
    }

    public String getSapYn() {
        return sapYn;
    }

    public void setSapYn(String sapYn) {
        this.sapYn = sapYn;
    }

    public String getSapGl() {
        return sapGl;
    }

    public void setSapGl(String sapGl) {
        this.sapGl = sapGl;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public String getTransDt() {
        return transDt;
    }

    public void setTransDt(String transDt) {
        this.transDt = transDt;
    }

    public String getGlAcct() {
        return glAcct;
    }

    public void setGlAcct(String glAcct) {
        this.glAcct = glAcct;
    }

    public String getAcctNm() {
        return acctNm;
    }

    public void setAcctNm(String acctNm) {
        this.acctNm = acctNm;
    }

    public String getDebit() {
        if( debit == null){
            return "";
        }
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        if(credit == null){
            return "";
        }
        return credit;
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

    public String getBudgetCd() {
        return budgetCd;
    }

    public void setBudgetCd(String budgetCd) {
        this.budgetCd = budgetCd;
    }

    public String getDescVn() {
        return descVn;
    }

    public void setDescVn(String descVn) {
        this.descVn = descVn;
    }

    public String getGroupGl() {
        return groupGl;
    }

    public void setGroupGl(String groupGl) {
        this.groupGl = groupGl;
    }

    public String getTxnDt() {
        return txnDt;
    }

    public void setTxnDt(String txnDt) {
        this.txnDt = txnDt;
    }

    public String getRunDt() {
        return runDt;
    }

    public void setRunDt(String runDt) {
        this.runDt = runDt;
    }
}
