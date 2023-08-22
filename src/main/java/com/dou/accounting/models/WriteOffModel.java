package com.dou.accounting.models;

public class WriteOffModel {
    String agreementId;
    String agreementNo;
    String outDate;
    String amtFin;
    String principleBf;
    String interestBf;
    String lppBf;
    String principleAt;
    String interestAt;
    String lppAt;
    String totalPrinciple;
    String totalInterest;
    String totalLpp;
    String totalNetReceivable;
    String insertDt;
    Integer page;
    Integer no;
    String paid;
    String waiveoffAmt;
    String totalExcess;

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getWaiveoffAmt() {
        return waiveoffAmt;
    }

    public void setWaiveoffAmt(String waiveoffAmt) {
        this.waiveoffAmt = waiveoffAmt;
    }

    public String getTotalExcess() {
        return totalExcess;
    }

    public void setTotalExcess(String totalExcess) {
        this.totalExcess = totalExcess;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
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

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getAmtFin() {
        return amtFin;
    }

    public void setAmtFin(String amtFin) {
        this.amtFin = amtFin;
    }

    public String getPrincipleBf() {
        return principleBf;
    }

    public void setPrincipleBf(String principleBf) {
        this.principleBf = principleBf;
    }

    public String getInterestBf() {
        return interestBf;
    }

    public void setInterestBf(String interestBf) {
        this.interestBf = interestBf;
    }

    public String getLppBf() {
        return lppBf;
    }

    public void setLppBf(String lppBf) {
        this.lppBf = lppBf;
    }

    public String getPrincipleAt() {
        return principleAt;
    }

    public void setPrincipleAt(String principleAt) {
        this.principleAt = principleAt;
    }

    public String getInterestAt() {
        return interestAt;
    }

    public void setInterestAt(String interestAt) {
        this.interestAt = interestAt;
    }

    public String getLppAt() {
        return lppAt;
    }

    public void setLppAt(String lppAt) {
        this.lppAt = lppAt;
    }

    public String getTotalPrinciple() {
        return totalPrinciple;
    }

    public void setTotalPrinciple(String totalPrinciple) {
        this.totalPrinciple = totalPrinciple;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalLpp() {
        return totalLpp;
    }

    public void setTotalLpp(String totalLpp) {
        this.totalLpp = totalLpp;
    }

    public String getTotalNetReceivable() {
        return totalNetReceivable;
    }

    public void setTotalNetReceivable(String totalNetReceivable) {
        this.totalNetReceivable = totalNetReceivable;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }
}
