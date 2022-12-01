package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AccountInfoSimMapper {
    List<AccountInfo> getAccountInfoSearch(@Param("accountId") String accountId, @Param("departmentId") String departmentId);
    AccountInfoDetail getAccountInfoByAcctIdDepartment (AccountInfoSearch accountInfoSearch);

    List<AccountInfo> getAccountInfo(@Param("accountId") String accountId);

    AccountInfo getAccountInfoDepartmentByAcctId (AccountInfoSearch accountInfoSearch);

    List<String> getListApplicationByDepartment(String departmentId);

    List<HashMap> getGroupFeaturePermision(@Param("listAppCode") List<String> listAppCode);

    int insertAccountInfo(AccountInfo accountInfo);

    int updateAccountInfo(AccountInfo accountInfo);

    int insertRequestSummary(RequestSummary requestSummary);

    int insertRequestDetailByReqId(AccountInfoDetail accountInfoDetail);

    int insertRequestPermissionByReqId(@Param("accountId") String accountId, @Param("requestId") String requestId, @Param("permission") String permission, @Param("action") String action);

    List<AccountInfo> getAccountInfoByAcctId (AccountInfo accountInfo);

    List<AccountInfo> getAccountExistRequestDetail(@Param("accountId") String accountId, @Param("typeVerifyStatus") String typeVerifyStatus);

    List<AccountInfo> getAccountExistRequestPermission(@Param("accountId") String accountId, @Param("typeVerifyStatus") String typeVerifyStatus);

    List<AccountPermissionGroupSimFeature> getGroupFeaturePermissionCurrent(AccountInfo accountInfo);

    List<String> getGroupPermissionCurrent(@Param("accountId") String accountId);

    //int insertAccountPermissionRequest(@Param("requestId") String requestId, @Param("accountId") String accountId, @Param("groupId") String groupId, @Param("action") String action);

    List<RequestSummary> getRequestSummaryInVerifyTicket(FilterSim_VerifyTicket accountInfo);

    List<AccountInfoDetail> getRequestDetailByRequestIdVerifyTicket(FilterSim_VerifyTicket accountInfo);

    List<AccountPermissionGroupSimFeature> getGroupFeatureRequestPermission(FilterSim_VerifyTicket accountInfo);

    int updateStatusVerifyRequestSummary(@Param("status") String status, @Param("requestId") String requestId);

    int insertAccountInfoDepartment(AccountInfoDetail accountInfoDetail);

    int insertAccountPermissionGroupFeature(AccountPermissionGroupSimFeature accountPermissionGroupSimFeature);

    int updateVerifyRequestDetail(DecisionVerifyAccount accountInfoDetail);

    List<AccountInfoDetail> getListRequestDetailProcessing(@Param("requestId") String requestId);

    List<AccountInfo> getAccountInfoRequestDetailDepartment(@Param("requestId") String requestId);

    List<AccountInfo> getAccountInfoRequestDetailChangeDepartment(@Param("requestId") String requestId);

    List<AccountInfoDetail> getListResetPassRequest(@Param("requestId") String requestId);

    int updateResetPasswordAccountInfo(AccountInfoResetPass accountInfoResetPass);

    List<AccountInfoDetail> getListActiveInactiveRequest(@Param("requestId") String requestId);

    List<AccountInfoDetail> getListRemoveAccountRequest(@Param("requestId") String requestId);

    int updateActiveInactiveAccountInfo(@Param("accountId") String accountId, @Param("isStatus") String isStatus, @Param("updateBy") String updateBy, @Param("isDelete") Integer isDelete);

    int removeAccountInfo(@Param("accountId") String accountId);

    int updateAccountInfoDepartment(AccountInfoDetail accountInfoDetail);

    int updateStatusAccountInfoDepartment(@Param("tableDepartment") String tableDepartment ,@Param("accountId") String accountId, @Param("status") String status, @Param("updateBy") String updateBy, @Param("isDelete") Integer isDelete);

    int updateVerifyRequestPermission(AccountPermissionGroupSimFeature accountPermissionGroupSimFeature);

    int updateVerifyRequestPermissionAllGroup(AccountPermissionGroupSimFeature accountPermissionGroupSimFeature);

    int insertAccountInfoPermissionGroupFeature(AccountPermissionGroupSimFeature accountPermissionGroupSimFeature);

    int deleteAccountInfoPermissionGroupFeature(AccountPermissionGroupSimFeature accountPermissionGroupSimFeature);

    int deleteAccountInfoPermissionAllGroupFeature(@Param("accountId") String accountId);

    List<AccountInfo> getListAccountInfoReview(FilterSim_Application filter);

}
