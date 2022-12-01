package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.*;
import com.dou.adm.shared.ResponseObject;

import java.util.List;

public interface AccountInfoServiceSimInterface {
    ResponseObject getAccountInfoSearch(AccountInfo accountInfo);

    ResponseObject getAccountRequestExist(String accountId);

    ResponseObject getAccountInfoDetail(AccountInfoSearch accountInfoSearch);

    ResponseObject getListApplicationByDepartment(String departmentId);

    ResponseObject getGroupFeaturePermision(FilterSim_Application listAppCode);

    ResponseObject createAccountInfoDetail(AccountInfoDetail accountInfoDetail, String userId);

    ResponseObject createRequestUpdateInfoAccount(AccountInfoDetail accountInfoDetail, String userId);

    ResponseObject getGroupIdPermissionCurrentByAcct(AccountInfo accountInfo);

    ResponseObject createRequestUpdatePermissionAccount(GroupIdPermissionRequest groupIdPermissionRequest, String userId);

    ResponseObject createRequestResetPassword(List<AccountInfoDetail> accountInfo, String userId);

    ResponseObject createRequestActiveInactive(List<AccountInfoDetail> accountInfo, String userId);

    ResponseObject createRequestRemoveAccount(List<AccountInfoDetail> accountInfo, String userId);

    ResponseObject getAccountRequestSummary(FilterSim_VerifyTicket accountInfo);

    ResponseObject getRequestDetailByRequestId(FilterSim_VerifyTicket request);

    ResponseObject updVerifyAccountRegister(DecisionVerifyAccount request);

    ResponseObject getListResetPassRequest(String requestId);

    ResponseObject updverifyAccountResetPassword(DecisionVerifyAccount request);

    ResponseObject getListActiveInactiveRequest(String requestId);

    ResponseObject updVerifyAccountActiveInactive(DecisionVerifyAccount request);

    ResponseObject updVerifyAccountUpdateInfo(DecisionVerifyAccount request);

    ResponseObject updVerifyUpdatePermission(DecisionVerifyPermission request);

    ResponseObject getListRemoveAccountRequest(String requestId);

    ResponseObject updVerifyRemoveAccount(DecisionVerifyAccount request);

    ResponseObject getAccountInfoSearchReview(FilterSim_Application filter);

}
