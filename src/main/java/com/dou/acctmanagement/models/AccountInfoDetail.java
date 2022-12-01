package com.dou.acctmanagement.models;

import java.util.Date;
import java.util.List;

public class AccountInfoDetail {
    private String id;
    private String requestId;
    private String accountId;
    private String password;
    private String fullName;
    private String mafcCode;
    private String email;
    private String departmentId;
    private String personalId;

    private String mobile;
    private String deptPhone;
    private String supervisorCode;
    private String staffType;
    private String position;
    private String group;
    private String team;
    private String status;
    private String area;
    private Integer level;
    private String branchId;
    private String branchName;
    private Date joinDate;
    private String joinDateString;
    private String endDateString;
    private String note;

    private List<String> listPermission;
    private String isBlocked;
    private String isDeleted;
    private String isStatus;
    private String isAdmin;
    private String accessToken;
    private String accessTokenExpired;
    private String generateKey;

    private String groupUserId;
    private String departmentName;
    private Integer isChangeDept;
    private String newDept;
    private String tableDepartment;
    private String verifyStatus;
    private String verifyNote;
    private String userVerify;
    private String timeVerify;
    private String requestTypeName;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenExpired() {
        return accessTokenExpired;
    }

    public void setAccessTokenExpired(String accessTokenExpired) {
        this.accessTokenExpired = accessTokenExpired;
    }

    public String getGenerateKey() {
        return generateKey;
    }

    public void setGenerateKey(String generateKey) {
        this.generateKey = generateKey;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMafcCode() {
        return mafcCode;
    }

    public void setMafcCode(String mafcCode) {
        this.mafcCode = mafcCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeptPhone() {
        return deptPhone;
    }

    public void setDeptPhone(String deptPhone) {
        this.deptPhone = deptPhone;
    }

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getListPermission() {
        return listPermission;
    }

    public void setListPermission(List<String> listPermission) {
        this.listPermission = listPermission;
    }

    public Integer getIsChangeDept() {
        return isChangeDept;
    }

    public void setIsChangeDept(Integer isChangeDept) {
        this.isChangeDept = isChangeDept;
    }

    public String getNewDept() {
        return newDept;
    }

    public void setNewDept(String newDept) {
        this.newDept = newDept;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getTableDepartment() {
        return tableDepartment;
    }

    public void setTableDepartment(String tableDepartment) {
        this.tableDepartment = tableDepartment;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyNote() {
        return verifyNote;
    }

    public void setVerifyNote(String verifyNote) {
        this.verifyNote = verifyNote;
    }

    public String getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(String userVerify) {
        this.userVerify = userVerify;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getJoinDateString() {
        return joinDateString;
    }

    public void setJoinDateString(String joinDateString) {
        this.joinDateString = joinDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public String getTimeVerify() {
        return timeVerify;
    }

    public void setTimeVerify(String timeVerify) {
        this.timeVerify = timeVerify;
    }
}
