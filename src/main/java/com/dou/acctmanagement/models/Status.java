package com.dou.acctmanagement.models;

public class Status {
    private String statusCode;
    private String statusName;
    private String datelastmaint;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDatelastmaint() {
        return datelastmaint;
    }

    public void setDatelastmaint(String datelastmaint) {
        this.datelastmaint = datelastmaint;
    }
}
