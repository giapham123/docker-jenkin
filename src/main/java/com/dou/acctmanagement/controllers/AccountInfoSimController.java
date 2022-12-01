package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.*;
import com.dou.acctmanagement.services.AccountInfoServiceSimInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("api/acctinfo/sim/")
public class AccountInfoSimController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoSimController.class);

    @Autowired
    private AccountInfoServiceSimInterface accountInfoServiceInterface;

    @PostMapping("/getAccountInfoSearchRequestTicket")
    public ResponseObject<List<AccountInfoSearch>> getAccountInfoSearchRequestTicket(@RequestBody AccountInfo accountInfo) {
        return accountInfoServiceInterface.getAccountInfoSearch(accountInfo);
    }

    @GetMapping("/getAccountRequestExist/{accountId}")
    public ResponseObject getAccountRequestExist(@PathVariable("accountId") String accountId) {
        return accountInfoServiceInterface.getAccountRequestExist(accountId);
    }

    @PostMapping("/getAccountInfoDetail")
    public ResponseObject<List<AccountInfoDetail>> getAccountInfoDetail(@RequestBody AccountInfoSearch accountInfo) {
        return accountInfoServiceInterface.getAccountInfoDetail(accountInfo);
    }

    @RequestMapping("/getApplicationByDepartment/{departmentId}")
    public ResponseObject<List<String>> getListApplicationByDepartment(@PathVariable("departmentId") String departmentId ) {
        return accountInfoServiceInterface.getListApplicationByDepartment(departmentId);
    }

    @PostMapping("/getGroupFeaturePermision")
    public ResponseObject getGroupFeaturePermision(@RequestBody FilterSim_Application listAppCode)  {
        return accountInfoServiceInterface.getGroupFeaturePermision(listAppCode);
    }


    @PostMapping("/createRequestRegisterAccount")
    public ResponseObject createAccountInfoDetail(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountInfoDetail accountInfoDetail)  {
        try {
            return accountInfoServiceInterface.createAccountInfoDetail(accountInfoDetail, user.getUsername());
        }catch (Exception e){
            LOGGER.error("Error when call createAccountInfoDetail class: .", e);
            return  ResponseObject.failResponse("Create request Account info detail fail");
        }
    }

    @PostMapping("/createRequestUpdateInfoAccount")
    public ResponseObject createRequestUpdateInfoAccount(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountInfoDetail accountInfoDetail)  {
        try {
            return accountInfoServiceInterface.createRequestUpdateInfoAccount(accountInfoDetail, user.getUsername());
        }catch (Exception e){
            LOGGER.error("Error when call createRequestUpdateInfoAccount class: .", e);
            return  ResponseObject.failResponse("Create request Update info fail");
        }
    }

    @PostMapping("/getGroupIdPermissionCurrentByAcct")
    public ResponseObject getGroupIdPermissionCurrentByAcct(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.getGroupIdPermissionCurrentByAcct(accountInfo);
    }

    @PostMapping("/createRequestUpdatePermissionAccount")
    public ResponseObject createRequestUpdatePermissionAccount(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody GroupIdPermissionRequest groupIdPermissionRequest)  {
        return accountInfoServiceInterface.createRequestUpdatePermissionAccount(groupIdPermissionRequest, user.getUsername());
    }

    @PostMapping("/createRequestResetPassword")
    public ResponseObject createRequestResetPassword(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody List<AccountInfoDetail> accountInfos)  {
        try {
            return accountInfoServiceInterface.createRequestResetPassword(accountInfos, user.getUsername());
        }catch (Exception e){
            LOGGER.error("Error when call createRequestResetPassword class: .", e);
            return  ResponseObject.failResponse("Create request reset password fail");
        }
    }

    @PostMapping("/createRequestActiveInactive")
    public ResponseObject createRequestActiveInactive(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody List<AccountInfoDetail> accountInfos)  {
        return accountInfoServiceInterface.createRequestActiveInactive(accountInfos, user.getUsername());
    }

    @PostMapping("/createRequestRemoveAccount")
    public ResponseObject createRequestRemoveAccount(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody List<AccountInfoDetail> accountInfos)  {
        try {
            return accountInfoServiceInterface.createRequestRemoveAccount(accountInfos, user.getUsername());
        }catch (Exception e){
            LOGGER.error("Error when call createRequestRemoveAccount class: .", e);
            return  ResponseObject.failResponse("Create request remove account fail");
        }
    }

    // VERIFY
    @PostMapping("/getAccountRequestSummary")
    public ResponseObject<List<AccountInfoSearch>> getAccountRequestSummary(@RequestBody FilterSim_VerifyTicket accountInfo) {
        return accountInfoServiceInterface.getAccountRequestSummary(accountInfo);
    }

    @PostMapping("/getRequestDetailByRequestId")
    public ResponseObject<List<HashMap>> getRequestDetailByRequestId(@RequestBody FilterSim_VerifyTicket request) {
        return accountInfoServiceInterface.getRequestDetailByRequestId(request);
    }

    @PostMapping("/verifyAccountRegister")
    public ResponseObject<List<HashMap>> updverifyAccountRegister(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyAccount request) {
        request.setUserVerify(user.getUsername());
        try {
            return accountInfoServiceInterface.updVerifyAccountRegister(request);

        }catch (Exception e){
            LOGGER.error("Error when call updverifyAccountRegister class: .", e);
            return  ResponseObject.failResponse("Verify Account register fail");
        }
    }

    @GetMapping("/getListResetPassRequest/{requestId}")
    public ResponseObject getListResetPassRequest(@PathVariable("requestId") String requestId) {
        return accountInfoServiceInterface.getListResetPassRequest(requestId);
    }

    @PostMapping("/verifyAccountResetPassword")
    public ResponseObject<List<HashMap>> updverifyAccountResetPassword(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyAccount request) {
        request.setUserVerify(user.getUsername());
        return accountInfoServiceInterface.updverifyAccountResetPassword(request);
    }

    @GetMapping("/getListActiveInactiveRequest/{requestId}")
    public ResponseObject getListActiveInactiveRequest(@PathVariable("requestId") String requestId) {
        return accountInfoServiceInterface.getListActiveInactiveRequest(requestId);
    }

    @GetMapping("/getListRemoveAccountRequest/{requestId}")
    public ResponseObject getListRemoveAccountRequest(@PathVariable("requestId") String requestId) {
        return accountInfoServiceInterface.getListRemoveAccountRequest(requestId);
    }

    @PostMapping("/verifyAccountActiveInactive")
    public ResponseObject<List<HashMap>> updVerifyAccountActiveInactive(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyAccount request) {
        request.setUserVerify(user.getUsername());
        return accountInfoServiceInterface.updVerifyAccountActiveInactive(request);
    }

    @PostMapping("/verifyAccountUpdateInfo")
    public ResponseObject<List<HashMap>> updVerifyAccountUpdateInfo(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyAccount request) {
        request.setUserVerify(user.getUsername());
        try {
            return accountInfoServiceInterface.updVerifyAccountUpdateInfo(request);

        }catch (Exception e){
            LOGGER.error("Error when call verifyAccountUpdateInfo class: .", e);
            return  ResponseObject.failResponse("Verify Account update info fail");
        }
    }

    @PostMapping("/verifyUpdatePermission")
    public ResponseObject<List<HashMap>> updVerifyUpdatePermission(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyPermission request) {
        request.setUserVerify(user.getUsername());

        try {
            return accountInfoServiceInterface.updVerifyUpdatePermission(request);
        }catch (Exception e){
            LOGGER.error("Error when call verifyUpdatePermission class: .", e);
            return  ResponseObject.failResponse("Verify Update permission fail");
        }
    }

    @PostMapping("/verifyRemoveAccount")
    public ResponseObject<List<HashMap>> updVerifyRemoveAccount(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody DecisionVerifyAccount request) {
        request.setUserVerify(user.getUsername());
        return accountInfoServiceInterface.updVerifyRemoveAccount(request);
    }

    //REVIEW
    @PostMapping("/getAccountInfoSearchReview")
    public ResponseObject<List<AccountInfoSearch>> getAccountInfoSearchReview(@RequestBody FilterSim_Application filterSim_application) {
        return accountInfoServiceInterface.getAccountInfoSearchReview(filterSim_application);
    }
}
