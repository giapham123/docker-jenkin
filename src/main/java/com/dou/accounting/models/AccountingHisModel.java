package com.dou.accounting.models;

public class AccountingHisModel {
    String product;
    String scheme;
    String agreementNo;
    String agreementId;
    String principalOs;
    String pendingInst;
    String currentMonthint;
    String currLpi;
    String overDuecharges;
    String forceClosureCharges;
    String excessAmount;
    String netReceiable;
    String npaStage;
    String dpd;
    String lastEmi;
    String lastDuedt;
    Integer from;
    Integer to;
    Integer page;
    Integer no;
    String fromDate;
    String toDate;
    String closureDate;
    boolean netReceipt;

    public boolean isNetReceipt() {
        return netReceipt;
    }

    public void setNetReceipt(boolean netReceipt) {
        this.netReceipt = netReceipt;
    }

    public String getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(String closureDate) {
        this.closureDate = closureDate;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getPrincipalOs() {
        return principalOs;
    }

    public void setPrincipalOs(String principalOs) {
        this.principalOs = principalOs;
    }

    public String getPendingInst() {
        return pendingInst;
    }

    public void setPendingInst(String pendingInst) {
        this.pendingInst = pendingInst;
    }

    public String getCurrentMonthint() {
        return currentMonthint;
    }

    public void setCurrentMonthint(String currentMonthint) {
        this.currentMonthint = currentMonthint;
    }

    public String getCurrLpi() {
        return currLpi;
    }

    public void setCurrLpi(String currLpi) {
        this.currLpi = currLpi;
    }

    public String getOverDuecharges() {
        return overDuecharges;
    }

    public void setOverDuecharges(String overDuecharges) {
        this.overDuecharges = overDuecharges;
    }

    public String getForceClosureCharges() {
        return forceClosureCharges;
    }

    public void setForceClosureCharges(String forceClosureCharges) {
        this.forceClosureCharges = forceClosureCharges;
    }

    public String getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(String excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getNetReceiable() {
        return netReceiable;
    }

    public void setNetReceiable(String netReceiable) {
        this.netReceiable = netReceiable;
    }

    public String getNpaStage() {
        return npaStage;
    }

    public void setNpaStage(String npaStage) {
        this.npaStage = npaStage;
    }

    public String getDpd() {
        return dpd;
    }

    public void setDpd(String dpd) {
        this.dpd = dpd;
    }

    public String getLastEmi() {
        return lastEmi;
    }

    public void setLastEmi(String lastEmi) {
        this.lastEmi = lastEmi;
    }

    public String getLastDuedt() {
        return lastDuedt;
    }

    public void setLastDuedt(String lastDuedt) {
        this.lastDuedt = lastDuedt;
    }
}
