package com.dou.accounting.models;
public class SigProcess {
    private static SigProcess instance = null;
    public Boolean isGetTrans = false;

    public Boolean isProcessPD = false;

    public Boolean isAuthGenSap = false;

    public Boolean isProcessCollCompare = false;

    public Boolean isPutSap = false;

    public SigProcess(){}
    public static SigProcess getInstance()
    {
        if (instance == null)
            instance = new SigProcess();
        return instance;
    }

    public void setIsGetTrans(Boolean isGetTrans){
        this.isGetTrans = isGetTrans;
    }

    public Boolean getIsGetTrans(){
            return this.isGetTrans;
        }

    public void setProcessCollCompare(Boolean isProcessCollCompare){
        this.isProcessCollCompare = isProcessCollCompare;
    }

    public Boolean getProcessCollCompare(){
        return this.isProcessCollCompare;
    }

    public void setProcessPD(Boolean isProcessPD){
        this.isProcessPD = isProcessPD;
    }

    public Boolean getProcessPD(){
        return this.isProcessPD;
    }

    public void setAuthGenSap(Boolean isAuthGenSap){
        this.isAuthGenSap = isAuthGenSap;
    }

    public Boolean getAuthGenSap(){
        return this.isAuthGenSap;
    }

    public void setPutSap(Boolean isPutSap){
        this.isPutSap = isPutSap;
    }

    public Boolean getPutSap(){
        return this.isPutSap;
    }
}
