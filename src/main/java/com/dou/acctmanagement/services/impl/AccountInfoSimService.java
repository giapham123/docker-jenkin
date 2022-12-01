package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.AccountInfoSimMapper;
import com.dou.acctmanagement.mappers.AccountPermissionFeatureButtonMapper;
import com.dou.acctmanagement.mappers.AccountPermissionGroupFeatureMapper;
import com.dou.acctmanagement.mappers.GrantPermissionHisMapper;
import com.dou.acctmanagement.models.*;
import com.dou.acctmanagement.services.AccountInfoServiceSimInterface;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AccountInfoSimService implements AccountInfoServiceSimInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountInfoSimService.class);

    private static String statusSummaryRequested = "REQUESTED";
    private static String statusSummaryDone = "DONE";
    private static String statusSummaryProcess = "INPROGRESS";

    private static String statusPermissionInsert = "INSERT";
    private static String statusPermissionRemove = "REMOVE";

    private static String decisionDetailStatusApprove = "APPROVED";
    private static String decisionDetailStatusReject = "REJECTED";

    private static String statusReset = "RESET";
    private static String statusActive = "ACT";
    private static String statusInActive = "IACT";
    private static String statusNewAcct = "NEWAC";
    private static String statusRemove = "REM";
    private static String statusUpdateInfo = "UPDIF";
    private static String statusUpdatePermission = "UPDPE";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AccountInfoSimMapper accountInfoMapper;

    @Autowired
    private AccountPermissionFeatureButtonMapper accountPermissionFeatureButtonMapper;

    @Autowired
    private AccountPermissionGroupFeatureMapper accountPermissionGroupFeatureMapper;

    @Autowired
    private GrantPermissionHisMapper grantPermissionHisMapper;

    public ResponseObject<List<AccountInfo>> getAccountInfoSearch(AccountInfo accountInfo) {
        List<AccountInfo> listAcctInfo = new ArrayList<>();
        if (accountInfo.getAccountId() != null && !accountInfo.getAccountId().equals("")) {
            accountInfo.setDepartmentId(null);
        }
        listAcctInfo = accountInfoMapper.getAccountInfoSearch(accountInfo.getAccountId(), accountInfo.getDepartmentId());

        return new ResponseObject(listAcctInfo);
    }

    @Override
    public ResponseObject getAccountRequestExist(String accountId) {
        //Get info account 2 table
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfo> accountInfoList = accountInfoMapper.getAccountInfo(accountId);
        if(accountInfoList.size() > 0) {
            responseObject.setData(accountInfoList);
            responseObject.setFailMessage("Account "+ accountId+ " exist in Account Info");
        }else {
            List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountId, statusNewAcct);
            if(listAcctRequest.size() > 0) {
                responseObject.setData(listAcctRequest);
                responseObject.setFailMessage("Request is exists in Request Summary");
            }
        }
        return responseObject;
    }

    public ResponseObject<List<AccountInfoDetail>> getAccountInfoDetail(AccountInfoSearch accountInfoSearch) {
        AccountInfoDetail accountInfoList = accountInfoMapper.getAccountInfoByAcctIdDepartment(accountInfoSearch);
        return new ResponseObject(accountInfoList);
    }

    public ResponseObject getListApplicationByDepartment(String departmentId) {
        List<String> listAppDepartment = accountInfoMapper.getListApplicationByDepartment(departmentId);
        return new ResponseObject(listAppDepartment);
    }

    public ResponseObject getGroupFeaturePermision(FilterSim_Application listAppCode) {
        List<HashMap> listAppDepartment = new ArrayList<>();
        if (listAppCode.getListAppCode().size() > 0) {
            listAppDepartment = accountInfoMapper.getGroupFeaturePermision(listAppCode.getListAppCode());
        } else {
            listAppDepartment = null;
        }
        return new ResponseObject(listAppDepartment);
    }

    @Override
    @Transactional
    public ResponseObject createAccountInfoDetail(AccountInfoDetail accountInfoDetail, String userId) {
        ResponseObject responseObject = new ResponseObject();
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .usePunctuation(false)
                .useLower(true)
                .useUpper(true)
                .build();
        String randomPassword = passwordGenerator.generate();

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountInfoDetail.getAccountId());
        accountInfo.setDepartmentId(accountInfoDetail.getDepartmentId());

        RequestSummary requestSummary = new RequestSummary();
        requestSummary.setAccountId(accountInfoDetail.getAccountId());
        requestSummary.setRequestTypeId(statusNewAcct);
        requestSummary.setRequestNote(accountInfoDetail.getNote());
        requestSummary.setRequestBy(userId);
        requestSummary.setVerifyStatus(statusSummaryRequested);

        List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountInfo.getAccountId(), statusNewAcct);
        List<AccountInfo> accountInfoList = accountInfoMapper.getAccountInfoSearch(accountInfo.getAccountId(), null);

        if (listAcctRequest.size() != 0) {
            responseObject.setData(listAcctRequest);
            responseObject.setFailMessage("Request is exists in Request Summary");
            return responseObject;
        }
        if (accountInfoList.size() != 0) {
            responseObject.setData(accountInfoList);
            responseObject.setFailMessage("Account "+ accountInfoDetail.getAccountId()+ " exist in Account Info");
            return responseObject;
        } else {
            //SAVE REQUEST SUMMARY to have requestId
            accountInfoMapper.insertRequestSummary(requestSummary);

            //SAVE REQUEST DETAIL
            accountInfoDetail.setStatus(statusActive);
            accountInfoDetail.setPassword(randomPassword);
            accountInfoDetail.setRequestId(requestSummary.getRequestId());
            accountInfoMapper.insertRequestDetailByReqId(accountInfoDetail);

            //SAVE REQUEST PERMISSION
            List<String> listPermision = accountInfoDetail.getListPermission();
            for (String permision :
                    listPermision) {
                accountInfoMapper.insertRequestPermissionByReqId(accountInfoDetail.getAccountId(), accountInfoDetail.getRequestId(), permision, statusPermissionInsert);
            }

            responseObject.setMessage("Register account success, RequestID: " + requestSummary.getRequestId());
        }
        return responseObject;
    }

    public String getSha256Hex(String text){
        String shaHex = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();
            shaHex = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage());
        }
        return shaHex.toLowerCase();
    }

    @Override
    @Transactional
    public ResponseObject createRequestUpdateInfoAccount(AccountInfoDetail accountInfoDetail, String userId) {
        ResponseObject responseObject = new ResponseObject();
        String typeVerifyStatus = statusUpdateInfo;
        List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountInfoDetail.getAccountId(), typeVerifyStatus);
        if (listAcctRequest.size() != 0) {
            responseObject.setData(listAcctRequest);
            responseObject.setFailMessage("Request is exists in Request Summary");
            return responseObject;
        }else {
            //SAVE REQUEST SUMMARY to have requestId
            RequestSummary requestSummary = new RequestSummary();
            requestSummary.setAccountId(accountInfoDetail.getAccountId());
            requestSummary.setRequestTypeId(statusUpdateInfo);
            requestSummary.setRequestNote(accountInfoDetail.getNote());
            requestSummary.setRequestBy(userId);
            requestSummary.setVerifyStatus(statusSummaryRequested);
            accountInfoMapper.insertRequestSummary(requestSummary);

            //SAVE REQUEST DETAIL
            accountInfoDetail.setStatus(statusActive);
            accountInfoDetail.setRequestId(requestSummary.getRequestId());
            accountInfoMapper.insertRequestDetailByReqId(accountInfoDetail);

            responseObject.setData(requestSummary.getRequestId());
            responseObject.setMessage("Update info account success, RequestID: " + requestSummary.getRequestId());
        }

        return responseObject;
    }


    public ResponseObject getGroupIdPermissionCurrentByAcct(AccountInfo accountInfo) {
        List<AccountPermissionGroupSimFeature> accountPermissionCurrent = accountInfoMapper.getGroupFeaturePermissionCurrent(accountInfo);
        return new ResponseObject(accountPermissionCurrent);
    }

    @Override
    @Transactional
    public ResponseObject createRequestUpdatePermissionAccount(GroupIdPermissionRequest groupPermission, String userId) {
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestPermission(groupPermission.getAccountId(), statusUpdatePermission);
        if (listAcctRequest.size() != 0) {
            responseObject.setData(listAcctRequest);
            responseObject.setFailMessage("Request is exists in Request Summary");
            return responseObject;
        }

        List<String> listGroupCurrent = accountInfoMapper.getGroupPermissionCurrent(groupPermission.getAccountId());
        List<String> listGroupRequest = groupPermission.getListGroupPermission();

        List<String> listRemoveTemp = new ArrayList<>();
        for (String itemRequest :
                listGroupRequest) {
            for (String itemCurrent :
                    listGroupCurrent) {
                boolean flagExist = false;
                if (itemRequest.equals(itemCurrent)) {
                    flagExist = true;
                }

                if (flagExist == true) {
                    listRemoveTemp.add(itemCurrent);
                }
            }
        }

        for (String itemRemove :
                listRemoveTemp) {
            listGroupRequest.remove(itemRemove);
            listGroupCurrent.remove(itemRemove);
        }

        //Neu co-> tao request update permission
        if (listGroupRequest.size() > 0 || listGroupCurrent.size() > 0) {
            RequestSummary requestSummary = new RequestSummary();
            requestSummary.setAccountId(groupPermission.getAccountId());
            requestSummary.setRequestTypeId(statusUpdatePermission);
            requestSummary.setRequestNote(groupPermission.getNote());
            requestSummary.setRequestBy(userId);
            requestSummary.setVerifyStatus(statusSummaryRequested);
            accountInfoMapper.insertRequestSummary(requestSummary);

            AccountInfoDetail accountInfoDetail = new AccountInfoDetail();
            accountInfoDetail.setRequestId(requestSummary.getRequestId());
            accountInfoDetail.setAccountId(requestSummary.getAccountId());
            accountInfoDetail.setStatus(statusUpdatePermission);
            accountInfoDetail.setDepartmentId(groupPermission.getDepartmentId());
            accountInfoDetail.setFullName(groupPermission.getFullName());
            accountInfoMapper.insertRequestDetailByReqId(accountInfoDetail);

            //List request after remove -> Create INSERT (STATUS)
            if (listGroupRequest.size() > 0) {
                for (String groupId :
                        listGroupRequest) {
                    accountInfoMapper.insertRequestPermissionByReqId(groupPermission.getAccountId(), requestSummary.getRequestId(), groupId, statusPermissionInsert);
                }
            }

            //List current after remove -> Update REMOVE (STATUS)
            if (listGroupCurrent.size() > 0) {
                for (String groupId :
                        listGroupCurrent) {
                    accountInfoMapper.insertRequestPermissionByReqId(groupPermission.getAccountId(), requestSummary.getRequestId(), groupId, statusPermissionRemove);
                }
            }
            responseObject.setData(requestSummary.getRequestId());
            responseObject.setMessage("Update Group Permission success, RequestID: " + requestSummary.getRequestId());
        } else {
            responseObject.setFailMessage("Group Permission is not change");
        }
        return responseObject;
    }

    @Override
    @Transactional
    public ResponseObject createRequestResetPassword(List<AccountInfoDetail> accountInfoDetail, String userId) {
        ResponseObject responseObject = new ResponseObject();
        for (AccountInfoDetail accountDetail:
             accountInfoDetail) {
            List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountDetail.getAccountId(), statusReset);
            if (listAcctRequest.size() != 0) {
                responseObject.setData(listAcctRequest);
                responseObject.setFailMessage("Request Account "+ accountDetail.getAccountId()+  " is exists in Request Summary");
                return responseObject;
            }
        }


        if (accountInfoDetail.size() > 0) {
            RequestSummary requestSummary = new RequestSummary();

            String strListAccountId = "";
            for (AccountInfoDetail accountInfo :
                    accountInfoDetail) {
                strListAccountId += accountInfo.getAccountId() + ",";
            }
            String strNewAccount = strListAccountId.substring(0, strListAccountId.length()-1);
            requestSummary.setAccountId(strNewAccount);
            requestSummary.setRequestTypeId(statusReset);
            requestSummary.setRequestNote("");
            requestSummary.setRequestBy(userId);
            requestSummary.setVerifyStatus(statusSummaryRequested);
            accountInfoMapper.insertRequestSummary(requestSummary);
            for (AccountInfoDetail accountInfo :
                    accountInfoDetail) {
                PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                        .useDigits(true)
                        .usePunctuation(false)
                        .useLower(true)
                        .useUpper(true)
                        .build();
                String randomPassword = passwordGenerator.generate();
                accountInfo.setStatus(statusReset);
                accountInfo.setRequestId(requestSummary.getRequestId());
                accountInfo.setPassword(randomPassword);
                accountInfo.setAccountId(accountInfo.getAccountId());
                accountInfo.setDepartmentId(accountInfo.getDepartmentId());
                accountInfoMapper.insertRequestDetailByReqId(accountInfo);
                responseObject.setData(requestSummary.getRequestId());
                responseObject.setMessage("Insert data success");
            }
        }

        return responseObject;
    }

    @Override
    @Transactional
    public ResponseObject createRequestActiveInactive(List<AccountInfoDetail> accountInfoList, String userId) {
        ResponseObject responseObject = new ResponseObject();
        String typeAccount = accountInfoList.get(0).getIsStatus();
        RequestSummary requestSummary = new RequestSummary();
//        requestSummary.setAccountId(null);

        //Neu dang 0(Inactive)-> Active
        if (typeAccount.equals("0")) {
            requestSummary.setRequestTypeId(statusActive);
        } else if (typeAccount.equals("1")) {
            requestSummary.setRequestTypeId(statusInActive);
        } else
            responseObject.setFailMessage("No read status to Active/Inactive");

        for (AccountInfoDetail accountDetail:
                accountInfoList) {
            List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountDetail.getAccountId(), requestSummary.getRequestTypeId());
            if (listAcctRequest.size() != 0) {
                responseObject.setData(listAcctRequest);
                responseObject.setFailMessage("Request Account "+ accountDetail.getAccountId()+  " is exists in Request Summary");
                return responseObject;
            }
        }

        requestSummary.setVerifyStatus(statusSummaryRequested);
        requestSummary.setRequestBy(userId);

        String strListAccountId = "";
        for (AccountInfoDetail accountInfo :
                accountInfoList) {
            strListAccountId += accountInfo.getAccountId() + ",";
        }
        String strNewAccount = strListAccountId.substring(0, strListAccountId.length()-1);
        requestSummary.setAccountId(strNewAccount);

        accountInfoMapper.insertRequestSummary(requestSummary);

        for (AccountInfoDetail accountInfo :
                accountInfoList) {
            accountInfo.setStatus(requestSummary.getRequestTypeId());
            accountInfo.setRequestId(requestSummary.getRequestId());
            accountInfo.setAccountId(accountInfo.getAccountId());
            accountInfo.setDepartmentId(accountInfo.getDepartmentId());
            accountInfo.setPassword(null);
            accountInfoMapper.insertRequestDetailByReqId(accountInfo);
        }
        responseObject.setData(requestSummary.getRequestId());
        responseObject.setMessage("Insert data success");
        return responseObject;
    }

    @Override
    @Transactional
    public ResponseObject createRequestRemoveAccount(List<AccountInfoDetail> accountInfoDetail, String userId) {
        ResponseObject responseObject = new ResponseObject();
        for (AccountInfoDetail accountDetail:
                accountInfoDetail) {
            List<AccountInfo> listAcctRequest = accountInfoMapper.getAccountExistRequestDetail(accountDetail.getAccountId(), statusRemove);
            if (listAcctRequest.size() != 0) {
                responseObject.setData(listAcctRequest);
                responseObject.setFailMessage("Request Account "+ accountDetail.getAccountId()+  " is exists in Request Summary");
                return responseObject;
            }
        }


        if (accountInfoDetail.size() > 0) {
            RequestSummary requestSummary = new RequestSummary();

            String strListAccountId = "";
            for (AccountInfoDetail accountInfo :
                    accountInfoDetail) {
                strListAccountId += accountInfo.getAccountId() + ",";
            }
            String strNewAccount = strListAccountId.substring(0, strListAccountId.length()-1);
            requestSummary.setAccountId(strNewAccount);
            requestSummary.setRequestTypeId("REM");
            requestSummary.setRequestNote("");
            requestSummary.setRequestBy(userId);
            requestSummary.setVerifyStatus(statusSummaryRequested);
            accountInfoMapper.insertRequestSummary(requestSummary);

            for (AccountInfoDetail accountInfo :
                    accountInfoDetail) {
                //Create request detail
                accountInfo.setStatus(statusRemove);
                accountInfo.setPassword(null);
                accountInfo.setRequestId(requestSummary.getRequestId());
                accountInfo.setAccountId(accountInfo.getAccountId());
                accountInfo.setDepartmentId(accountInfo.getDepartmentId());
                accountInfoMapper.insertRequestDetailByReqId(accountInfo);

                //Get permission current
                AccountInfo account = new AccountInfo();
                account.setAccountId(accountInfo.getAccountId());
                List<AccountPermissionGroupSimFeature> listPermissionGroup = accountInfoMapper.getGroupFeaturePermissionCurrent(account);

                //update permission remove by AccountId
                for (AccountPermissionGroupSimFeature accountPermissionGroup:
                        listPermissionGroup) {
                    accountInfoMapper.insertRequestPermissionByReqId(accountInfo.getAccountId(), accountInfo.getRequestId(), accountPermissionGroup.getGroupId(), statusPermissionRemove);
                }
            }
            responseObject.setData(requestSummary.getRequestId());
            responseObject.setMessage("Insert data success");
        }

        return responseObject;
    }

    //    VERIFY
    public ResponseObject<List<RequestSummary>> getAccountRequestSummary(FilterSim_VerifyTicket accountInfo) {
        List<RequestSummary> listAcctInfo = new ArrayList<>();
        if ((accountInfo.getAccountId() != null && !accountInfo.getAccountId().equals("")) || (accountInfo.getRequestId() != null && !accountInfo.getRequestId().equals(""))) {
            accountInfo.setRequestType(null);
        }
        listAcctInfo = accountInfoMapper.getRequestSummaryInVerifyTicket(accountInfo);
        return new ResponseObject(listAcctInfo);
    }

    public ResponseObject<List<HashMap>> getRequestDetailByRequestId(FilterSim_VerifyTicket request) {
        HashMap hashMap = new HashMap();
        List<AccountInfoDetail> infoDetailList = accountInfoMapper.getRequestDetailByRequestIdVerifyTicket(request);
        List<AccountPermissionGroupSimFeature> listGroupSimFeatures = accountInfoMapper.getGroupFeatureRequestPermission(request);
        hashMap.put("persionalInfo", infoDetailList);
        hashMap.put("groupPermission", listGroupSimFeatures);

        return new ResponseObject(hashMap);
    }

    @Override
    @Transactional
    public ResponseObject updVerifyAccountRegister(DecisionVerifyAccount request) {
        ResponseObject responseObject = new ResponseObject();
        if (request.getDecision().equals(decisionDetailStatusApprove)) {
            //Get info Detail by RequestId
            FilterSim_VerifyTicket requestParam = new FilterSim_VerifyTicket();
            requestParam.setRequestId(request.getRequestId());
            List<AccountInfoDetail> infoDetailList = accountInfoMapper.getRequestDetailByRequestIdVerifyTicket(requestParam);
            FilterSim_VerifyTicket filter_request = new FilterSim_VerifyTicket();
            filter_request.setRequestId(request.getRequestId());
            List<AccountPermissionGroupSimFeature> listPermission = accountInfoMapper.getGroupFeatureRequestPermission(filter_request);

            if (infoDetailList.size() > 0) {
                AccountInfoDetail accountInfoDetail = infoDetailList.get(0);
                //1. Thêm table accountinfo
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setAccountId(accountInfoDetail.getAccountId());
                responseObject.setData(accountInfoDetail.getPassword());
                String hashPassword = jwtProvider.generatePassword(getSha256Hex(accountInfoDetail.getPassword()));
                accountInfo.setPassword(hashPassword);
                accountInfo.setAccountId(accountInfoDetail.getAccountId());
                accountInfo.setIsStatus("1");
                accountInfo.setIsAdmin(accountInfoDetail.getIsAdmin());
                accountInfo.setAccessToken("None");
                accountInfo.setGenerateKey("None");
                accountInfo.setPersonalId(accountInfoDetail.getPersonalId());
                accountInfo.setDepartmentId(accountInfoDetail.getDepartmentId());
                accountInfo.setGroupUserId(accountInfoDetail.getGroupUserId());
                accountInfo.setAccountName(accountInfoDetail.getFullName());
                accountInfo.setCreateBy(request.getUserVerify());
                accountInfo.setIsChangePass("1");
                accountInfo.setIsBlocked("0");
                accountInfo.setIsDeleted("0");
                accountInfoMapper.insertAccountInfo(accountInfo);

                //Get table department
//                List<AccountInfo> infoList = accountInfoMapper.getAccountInfoSearch(accountInfoDetail.getAccountId(), accountInfoDetail.getDepartmentId());
                List<AccountInfo> infoList = accountInfoMapper.getAccountInfoRequestDetailDepartment(request.getRequestId());

                if (infoList.size() > 0 && !infoList.get(0).getTableDepartment().equals("") && infoList.get(0).getTableDepartment() != null) {
                    String tableDepartment = infoList.get(0).getTableDepartment();
                    //insert detail in tableDepartment ()
                    accountInfoDetail.setTableDepartment(tableDepartment);
                    accountInfoDetail.setUserVerify(request.getUserVerify());
                    accountInfoDetail.setIsDeleted("0");

                    accountInfoMapper.insertAccountInfoDepartment(accountInfoDetail);
                }

                //Insert accountpermissiongroupfeature
                if (listPermission.size() > 0) {
                    for (AccountPermissionGroupSimFeature groupPermission :
                            listPermission) {
                        accountInfoMapper.insertAccountPermissionGroupFeature(groupPermission);
                    }
                }

                //5. Update Request details: Status verify and status
                request.setPasswordHash(hashPassword);
                accountInfoMapper.updateVerifyRequestDetail(request);
                //Update Request Permission
                AccountPermissionGroupSimFeature accountPermissionGroup = new AccountPermissionGroupSimFeature();
                accountPermissionGroup.setRequestId(request.getRequestId());
                accountPermissionGroup.setVerifyNote(request.getNoteDecision());
                accountPermissionGroup.setVerifyBy(request.getUserVerify());
                accountPermissionGroup.setVerifyStatus(request.getDecision());
                accountPermissionGroup.setAccountId(request.getAccountId());
                accountInfoMapper.updateVerifyRequestPermissionAllGroup(accountPermissionGroup);

                //6. Update Request summary: Get list requestDetail with status != REJECT/APPROVED=> if > 0=> PROCESS
                //IF = 0 -> STATUS DONE SUMMARY
                List<AccountInfoDetail> listProcess = accountInfoMapper.getListRequestDetailProcessing(request.getRequestId());
                if (listProcess.size() > 0) {
                    accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
                } else {
                    accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
                }
                responseObject.setSuccess(true);
            } else {
                responseObject.setFailMessage("No find AccountId by RequestId to Verify");
            }
        } else if (request.getDecision().equals(decisionDetailStatusReject)) {
            accountInfoMapper.updateVerifyRequestDetail(request);
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
            responseObject.setSuccess(true);
        }

        return responseObject;
    }

    public ResponseObject getListResetPassRequest(String requestId) {
        List<AccountInfoDetail> listResetPass = accountInfoMapper.getListResetPassRequest(requestId);
        return new ResponseObject(listResetPass);
    }

    @Override
    @Transactional
    public ResponseObject updverifyAccountResetPassword(DecisionVerifyAccount request) {
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfoDetail> listResetPass = new ArrayList<>();
        if (request.getListAccountInfoDetails().size() > 0) {
            List<AccountInfoDetail> accountInfoDetails = request.getListAccountInfoDetails();

            //Approve
            if (request.getDecision().equals(decisionDetailStatusApprove)) {
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getNote());
                    String hashPassword = jwtProvider.generatePassword(getSha256Hex(accountInfoDetail.getPassword()));
                    decisionVerifyAccount.setPasswordHash(hashPassword);
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);

                    AccountInfoResetPass accountInfoResetPass = new AccountInfoResetPass();
                    accountInfoResetPass.setAccountId(accountInfoDetail.getAccountId());
                    accountInfoResetPass.setUpdateBy(request.getUserVerify());
                    accountInfoResetPass.setPasswordHash(hashPassword);
                    accountInfoResetPass.setIsChangePass(1);
                    accountInfoMapper.updateResetPasswordAccountInfo(accountInfoResetPass);
                }
            } else if (request.getDecision().equals(decisionDetailStatusReject)) {
                //Reject-> Save request detail status reject
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getNote());
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);
                }
            }
            //6. Update Request summary: Get list requestDetail with status != REJECT/APPROVED=> if > 0=> PROCESS
            //IF = 0 -> STATUS DONE SUMMARY
            List<AccountInfoDetail> listProcess = accountInfoMapper.getListRequestDetailProcessing(request.getRequestId());
            if (listProcess.size() > 0) {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
            } else {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
            }
            listResetPass = accountInfoMapper.getListResetPassRequest(request.getRequestId());
        } else {
            responseObject.setFailMessage("No Data to reset password");
        }
        return new ResponseObject(listResetPass);
    }

    @Override
    public ResponseObject getListActiveInactiveRequest(String requestId) {
        List<AccountInfoDetail> listResetPass = accountInfoMapper.getListActiveInactiveRequest(requestId);
        return new ResponseObject(listResetPass);
    }

    @Override
    public ResponseObject getListRemoveAccountRequest(String requestId) {
        //Get request detail by RequestId
        List<AccountInfoDetail> listRemove = accountInfoMapper.getListRemoveAccountRequest(requestId);
        return new ResponseObject(listRemove);
    }

    @Override
    @Transactional
    public ResponseObject updVerifyAccountActiveInactive(DecisionVerifyAccount request) {
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfoDetail> listActiveInactive = new ArrayList<>();
        if (request.getListAccountInfoDetails().size() > 0) {
            //Get list active/inactive in request
            List<AccountInfoDetail> accountInfoDetails = request.getListAccountInfoDetails();

            //Approve
            if (request.getDecision().equals(decisionDetailStatusApprove)) {
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getNote());
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);

                    String status = "";
                    if(accountInfoDetail.getStatus().equals(statusActive)) {
                        status = "1";
                    }else if(accountInfoDetail.getStatus().equals(statusInActive)) {
                        status = "0";
                    }
                    accountInfoMapper.updateActiveInactiveAccountInfo(accountInfoDetail.getAccountId(), status, request.getUserVerify(), null);

                    //GET DEPARTMENT-> Update status
                    List<AccountInfo> accountInfos = accountInfoMapper.getAccountInfoSearch(accountInfoDetail.getAccountId(), accountInfoDetail.getDepartmentId());
                    if(accountInfos.size() > 0 && (accountInfos.get(0).getTableDepartment() != null || !accountInfos.get(0).getTableDepartment().equals(""))) {
                        String tableDepartment = accountInfos.get(0).getTableDepartment();
                        accountInfoMapper.updateStatusAccountInfoDepartment(tableDepartment, accountInfoDetail.getAccountId(),accountInfoDetail.getStatus(), accountInfoDetail.getUserVerify(), null );
                    }

                }
            }else if (request.getDecision().equals(decisionDetailStatusReject)) {
                //Reject-> Save request detail status reject
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getNote());
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);
                }
            }

            //6. Update Request summary: Get list requestDetail with status != REJECT/APPROVED=> if > 0=> PROCESS
            //IF = 0 -> STATUS DONE SUMMARY
            List<AccountInfoDetail> listProcess = accountInfoMapper.getListRequestDetailProcessing(request.getRequestId());
            if (listProcess.size() > 0) {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
            } else {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
            }
            listActiveInactive = accountInfoMapper.getListResetPassRequest(request.getRequestId());
        }  else {
        responseObject.setFailMessage("No Data to reset password");
        }
        return new ResponseObject(listActiveInactive);
    }

    @Override
    @Transactional
    public ResponseObject updVerifyAccountUpdateInfo(DecisionVerifyAccount request) {
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfo> tableDepartmentList = new ArrayList<>();

        if (request.getDecision().equals(decisionDetailStatusApprove)) {
            //Get info Detail by RequestId
            FilterSim_VerifyTicket requestParam = new FilterSim_VerifyTicket();
            requestParam.setRequestId(request.getRequestId());
            List<AccountInfoDetail> infoDetailList = accountInfoMapper.getRequestDetailByRequestIdVerifyTicket(requestParam);

            if (infoDetailList.size() > 0) {
                AccountInfoDetail accountInfoDetail = infoDetailList.get(0);
                String departmentAccount = "";
                if(accountInfoDetail.getIsChangeDept() == 1 || accountInfoDetail.getIsChangeDept().equals(1)) {
                    //1. Neu change department -> Update table accountinfo
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.setAccountId(accountInfoDetail.getAccountId());
                    accountInfo.setDepartmentId(accountInfoDetail.getNewDept());
                    accountInfo.setUpdateBy(request.getUserVerify());
                    accountInfoMapper.updateAccountInfo(accountInfo);
                    tableDepartmentList = accountInfoMapper.getAccountInfoRequestDetailChangeDepartment(request.getRequestId());

                    //Update status REM in accountinfoUnd... -> Insert table accountinfoDRS 1 row
                    if (tableDepartmentList.size() > 0 && !tableDepartmentList.get(0).getTableDepartment().equals("") && tableDepartmentList.get(0).getTableDepartment() != null) {
                        String tableOldDepartment= "";

                        //Get table old department
                        List<AccountInfo> tableOldDepartmentList = accountInfoMapper.getAccountInfoRequestDetailDepartment(request.getRequestId());
                        if (tableOldDepartmentList.size() > 0 && !tableOldDepartmentList.get(0).getTableDepartment().equals("") && tableOldDepartmentList.get(0).getTableDepartment() != null) {
                            tableOldDepartment = tableOldDepartmentList.get(0).getTableDepartment();

                            //Update status in OLD accountinfoDepartment and insert other accountinfoDepartment
                            accountInfoMapper.updateStatusAccountInfoDepartment(tableOldDepartment, accountInfoDetail.getAccountId(), "REM", request.getUserVerify(), null);

                            //Update table change department
                            String tableChangeDepartment = tableDepartmentList.get(0).getTableDepartment();
                            accountInfoDetail.setTableDepartment(tableChangeDepartment);
                            accountInfoDetail.setDepartmentId(accountInfoDetail.getNewDept());
                            accountInfoMapper.insertAccountInfoDepartment(accountInfoDetail);
                        }
                    }
                }else{
                    departmentAccount = accountInfoDetail.getDepartmentId();
                    tableDepartmentList = accountInfoMapper.getAccountInfoRequestDetailDepartment(request.getRequestId());

                    //Check table department
                    if (tableDepartmentList.size() > 0 && !tableDepartmentList.get(0).getTableDepartment().equals("") && tableDepartmentList.get(0).getTableDepartment() != null) {
                        String tableDepartment = tableDepartmentList.get(0).getTableDepartment();
                        //update accountInfo
                        AccountInfo accountInfo = new AccountInfo();
                        accountInfo.setIsAdmin(accountInfoDetail.getIsAdmin());
                        accountInfo.setDepartmentId(accountInfoDetail.getDepartmentId());
                        accountInfo.setUpdateBy(request.getUserVerify());
                        accountInfo.setAccountId(accountInfoDetail.getAccountId());
                        accountInfoMapper.updateAccountInfo(accountInfo);

                        //update detail in tableDepartment ()
                        accountInfoDetail.setTableDepartment(tableDepartment);
                        accountInfoDetail.setDepartmentId(departmentAccount);
                        accountInfoDetail.setUserVerify(request.getUserVerify());
                        accountInfoMapper.updateAccountInfoDepartment(accountInfoDetail);
                    }
                }
                //5. Update Request details: Status verify and status
                accountInfoMapper.updateVerifyRequestDetail(request);
            } else {
                responseObject.setFailMessage("No find AccountId by RequestId to Verify");
            }
        } else if (request.getDecision().equals(decisionDetailStatusReject)) {
            accountInfoMapper.updateVerifyRequestDetail(request);
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
            responseObject.setSuccess(true);
        }
        //6. Update Request summary: Get list requestDetail with status != REJECT/APPROVED=> if > 0=> PROCESS
        //IF = 0 -> STATUS DONE SUMMARY
        List<AccountInfoDetail> listProcess = accountInfoMapper.getListRequestDetailProcessing(request.getRequestId());
        if (listProcess.size() > 0) {
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
        } else {
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
        }
        responseObject.setSuccess(true);

        return responseObject;
    }

    @Override
    @Transactional
    public ResponseObject updVerifyUpdatePermission(DecisionVerifyPermission request) {
        ResponseObject responseObject = new ResponseObject();

        //Approve
        if (request.getDecision().equals(decisionDetailStatusApprove)) {
            if(request.getListAccountInfoDetails().size() > 0) {
                List<AccountPermissionGroupSimFeature> listPermissionGroup = request.getListAccountInfoDetails();
                for (AccountPermissionGroupSimFeature accountGroup:
                     listPermissionGroup) {
                    //Update REQUEST_PERMISSION
                    accountGroup.setVerifyBy(request.getUserVerify());
                    accountGroup.setVerifyStatus(request.getDecision());
                    accountInfoMapper.updateVerifyRequestPermission(accountGroup);

                    //ACCOUNTPERMISSIONGROUPFEATURE
                    if(accountGroup.getAction().equals(statusPermissionInsert)) {
                        accountInfoMapper.insertAccountInfoPermissionGroupFeature(accountGroup);
                    }else if(accountGroup.getAction().equals(statusPermissionRemove)) {
                        accountInfoMapper.deleteAccountInfoPermissionGroupFeature(accountGroup);
                    }
                }
            }
        }else if (request.getDecision().equals(decisionDetailStatusReject)) {
            List<AccountPermissionGroupSimFeature> listPermissionGroup = request.getListAccountInfoDetails();
            for (AccountPermissionGroupSimFeature accountGroup:
                    listPermissionGroup) {
                //Update REQUEST_PERMISSION
                accountGroup.setVerifyBy(request.getUserVerify());
                accountGroup.setVerifyStatus(request.getDecision());
                accountInfoMapper.updateVerifyRequestPermission(accountGroup);
            }
        }

        FilterSim_VerifyTicket filterSimVerifyTicket = new FilterSim_VerifyTicket();
        filterSimVerifyTicket.setRequestId(request.getRequestId());
        filterSimVerifyTicket.setValidateStatus(1);
        List<AccountPermissionGroupSimFeature> listProcess = accountInfoMapper.getGroupFeatureRequestPermission(filterSimVerifyTicket);
        responseObject.setData(listProcess);
        if (listProcess.size() > 0) {
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
        } else {
            accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
        }
        return responseObject;
    }

    @Override
    @Transactional
    public ResponseObject updVerifyRemoveAccount(DecisionVerifyAccount request) {
        ResponseObject responseObject = new ResponseObject();
        List<AccountInfoDetail> listActiveInactive = new ArrayList<>();
        if (request.getListAccountInfoDetails().size() > 0) {
            //Get list active/inactive in request
            List<AccountInfoDetail> accountInfoDetails = request.getListAccountInfoDetails();

            //Approve
            if (request.getDecision().equals(decisionDetailStatusApprove)) {
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {

                    //Update status in MAACC.REQUEST_DETAILS
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getVerifyNote());
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setPasswordHash(null);
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);

                    //Update status in MAACC.REQUEST_PERMISSION
                    AccountPermissionGroupSimFeature accountInfoPermission= new AccountPermissionGroupSimFeature();
                    accountInfoPermission.setAccountId(accountInfoDetail.getAccountId());
                    accountInfoPermission.setVerifyStatus(request.getDecision());
                    accountInfoPermission.setVerifyBy(request.getUserVerify());
                    accountInfoPermission.setVerifyNote(accountInfoDetail.getVerifyNote());
                    accountInfoPermission.setRequestId(request.getRequestId());
                    accountInfoMapper.updateVerifyRequestPermissionAllGroup(accountInfoPermission);

                    //ACCOUNTINFO: IS DELETE=1, IS STATUS=0
                    accountInfoMapper.updateActiveInactiveAccountInfo(accountInfoDetail.getAccountId(), "0", request.getUserVerify(), 1);

                    //Get department by accountId
                    List<AccountInfo> listTableDepartment = accountInfoMapper.getAccountInfoSearch(accountInfoDetail.getAccountId(), null);
                    if(listTableDepartment.size() > 0 && !listTableDepartment.get(0).getTableDepartment().equals("") && listTableDepartment.get(0).getTableDepartment() != null) {
                        String tableDepartment = listTableDepartment.get(0).getTableDepartment();
                        //DEPARTMENT: STATUS: REM, IS DELETE=1
                        accountInfoMapper.updateStatusAccountInfoDepartment(tableDepartment, accountInfoDetail.getAccountId(), "REM", request.getUserVerify(), 1);

                        //ACCOUNTPERMISSIONGROUPFEATURE by AccountId
                        accountInfoMapper.deleteAccountInfoPermissionAllGroupFeature(accountInfoDetail.getAccountId());
                    }else{
                        responseObject.setFailMessage("No find table department to Remove account");
                        return responseObject;
                    }

                    accountInfoMapper.removeAccountInfo(accountInfoDetail.getAccountId());
                }
            }else if (request.getDecision().equals(decisionDetailStatusReject)) {
                //Reject-> Save request detail status reject
                for (AccountInfoDetail accountInfoDetail :
                        accountInfoDetails) {
                    DecisionVerifyAccount decisionVerifyAccount = new DecisionVerifyAccount();
                    decisionVerifyAccount.setAccountId(accountInfoDetail.getAccountId());
                    decisionVerifyAccount.setDecision(request.getDecision());
                    decisionVerifyAccount.setNoteDecision(accountInfoDetail.getNote());
                    decisionVerifyAccount.setRequestId(request.getRequestId());
                    decisionVerifyAccount.setUserVerify(request.getUserVerify());
                    accountInfoMapper.updateVerifyRequestDetail(decisionVerifyAccount);
                }
            }

            //6. Update Request summary: Get list requestDetail with status != REJECT/APPROVED=> if > 0=> PROCESS
            //IF = 0 -> STATUS DONE SUMMARY
            List<AccountInfoDetail> listProcess = accountInfoMapper.getListRequestDetailProcessing(request.getRequestId());
            if (listProcess.size() > 0) {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryProcess, request.getRequestId());
            } else {
                accountInfoMapper.updateStatusVerifyRequestSummary(statusSummaryDone, request.getRequestId());
            }
            listActiveInactive = accountInfoMapper.getListResetPassRequest(request.getRequestId());
        }  else {
            responseObject.setFailMessage("No Data to reset password");
        }
        return new ResponseObject(listActiveInactive);
    }

    @Override
    public ResponseObject getAccountInfoSearchReview(FilterSim_Application filter) {
        ResponseObject responseObject = new ResponseObject();
        if(filter.getListDepartment().size() > 0) {
           List<AccountInfo> accountInfoList = accountInfoMapper.getListAccountInfoReview(filter);
           responseObject.setData(accountInfoList);
        }else {
            responseObject.setFailMessage("Department is null");
        }
        return responseObject;
    }
}
