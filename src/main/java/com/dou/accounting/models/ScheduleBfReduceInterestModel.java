package com.dou.accounting.models;

public class ScheduleBfReduceInterestModel {

    String appId;
    String startTenure;
    String endTenure;
    String oldEffrate;
    String newEffrate;
    String totalIntcomp;
    String newEffrate2;
    String addDonw;
    String totalIntcompBf1;
    String anr;

    String proposalId;
    String instlNum;
    String dueDate;
    String instlAmt;
    String printComp;
    String intComp;
    String effRate;
    String balPrin;
    String days;

    String fromDt;
    String toDt;
    String page;
    String agreementId;

    public String getTotalIntcompBf1() {
        totalIntcompBf1 = totalIntcompBf1 == null? "":totalIntcompBf1;
        return totalIntcompBf1;
    }

    public void setTotalIntcompBf1(String totalIntcompBf1) {
        this.totalIntcompBf1 = totalIntcompBf1;
    }

    public String getAnr() {
        return anr;
    }

    public void setAnr(String anr) {
        this.anr = anr;
    }

    public String getTotalIntcomp() {
        return totalIntcomp;
    }

    public void setTotalIntcomp(String totalIntcomp) {
        this.totalIntcomp = totalIntcomp;
    }

    public String getNewEffrate2() {
        return newEffrate2;
    }

    public void setNewEffrate2(String newEffrate2) {
        this.newEffrate2 = newEffrate2;
    }

    public String getAddDonw() {
        addDonw = addDonw == null? "":addDonw;
        return addDonw;
    }

    public void setAddDonw(String addDonw) {
        this.addDonw = addDonw;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStartTenure() {
        return startTenure;
    }

    public void setStartTenure(String startTenure) {
        this.startTenure = startTenure;
    }

    public String getEndTenure() {
        return endTenure;
    }

    public void setEndTenure(String endTenure) {
        this.endTenure = endTenure;
    }

    public String getOldEffrate() {
        return oldEffrate;
    }

    public void setOldEffrate(String oldEffrate) {
        this.oldEffrate = oldEffrate;
    }

    public String getNewEffrate() {
        return newEffrate;
    }

    public void setNewEffrate(String newEffrate) {
        this.newEffrate = newEffrate;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public String getInstlNum() {
        return instlNum;
    }

    public void setInstlNum(String instlNum) {
        this.instlNum = instlNum;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getInstlAmt() {
        return instlAmt;
    }

    public void setInstlAmt(String instlAmt) {
        this.instlAmt = instlAmt;
    }

    public String getPrintComp() {
        return printComp;
    }

    public void setPrintComp(String printComp) {
        this.printComp = printComp;
    }

    public String getIntComp() {
        return intComp;
    }

    public void setIntComp(String intComp) {
        this.intComp = intComp;
    }

    public String getEffRate() {
        return effRate;
    }

    public void setEffRate(String effRate) {
        this.effRate = effRate;
    }

    public String getBalPrin() {
        return balPrin;
    }

    public void setBalPrin(String balPrin) {
        this.balPrin = balPrin;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
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
