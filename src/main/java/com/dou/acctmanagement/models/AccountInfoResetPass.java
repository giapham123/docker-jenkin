package com.dou.acctmanagement.models;

import java.util.Date;

public class AccountInfoResetPass {
    private String id;
    private String accountId;
    private String passwordHash;
    private String updateBy;
    private Date updateDate;
    private Integer isChangePass;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getIsChangePass() {
        return isChangePass;
    }

    public void setIsChangePass(Integer isChangePass) {
        this.isChangePass = isChangePass;
    }
}
