package com.dou.acctmanagement.models;

import java.util.List;

public class FilterSim_Application {
    private List<String> listAppCode;

    private List<String> listDepartment;
    private String accountId;

    public List<String> getListAppCode() {
        return listAppCode;
    }

    public void setListAppCode(List<String> listAppCode) {
        this.listAppCode = listAppCode;
    }

    public List<String> getListDepartment() {
        return listDepartment;
    }

    public void setListDepartment(List<String> listDepartment) {
        this.listDepartment = listDepartment;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
