package com.dou.accounting.models;

public class WaveOffAmountModel {
    String agreementId;
    String dateLastmaint;
    String waveOffAmount;

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getDateLastmaint() {
        return dateLastmaint;
    }

    public void setDateLastmaint(String dateLastmaint) {
        this.dateLastmaint = dateLastmaint;
    }

    public String getWaveOffAmount() {
        return waveOffAmount;
    }

    public void setWaveOffAmount(String waveOffAmount) {
        this.waveOffAmount = waveOffAmount;
    }
}
