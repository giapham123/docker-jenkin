package com.dou.acctmanagement.models;

import java.util.List;

public class GroupIdPermissionRequest {
    private List<String> listGroupPermission;
    private String note;
    private String accountId;
    private String departmentId;
    private String fullName;

    public List<String> getListGroupPermission() {
        return listGroupPermission;
    }

    public void setListGroupPermission(List<String> listGroupPermission) {
        this.listGroupPermission = listGroupPermission;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
