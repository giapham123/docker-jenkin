package com.dou.accounting.services.impl;

public class LoadingUploadSingle {

    private static LoadingUploadSingle single_instance = null;

    public boolean isUploadFinish = true;

    public LoadingUploadSingle(){}
    public static LoadingUploadSingle getInstance()
    {
        if (single_instance == null)
            single_instance = new LoadingUploadSingle();

        return single_instance;
    }

    public void setUploadFinish(boolean param){
        this.isUploadFinish = param;
    }

    public boolean getUploadFinish(){
        return isUploadFinish;
    }

}
