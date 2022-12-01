package com.dou.acctmanagement.models;

public class AccountInfoSearch {
    private String id;
    private String accountId;
    private String departmentId;
    private String departmentName;
    private String tableDepartment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTableDepartment() {
        return tableDepartment;
    }

    public void setTableDepartment(String tableDepartment) {
        this.tableDepartment = tableDepartment;
    }
}
