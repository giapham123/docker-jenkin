package com.dou.acctmanagement.models;

public class Group {
    private String codeId;
    private String name;
    private String application;
    private String effDate;
    private String inactiveDate;
    private String datelastmant;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(String inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public String getDatelastmant() {
        return datelastmant;
    }

    public void setDatelastmant(String datelastmant) {
        this.datelastmant = datelastmant;
    }
}
