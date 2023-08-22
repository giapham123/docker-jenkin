package com.dou.accounting.controllers;

import com.dou.accounting.models.PendingDisbursementModel;
import com.dou.accounting.services.PendingDisburementInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/pending-disbur/")
public class PendingDisbursementController {

    @Autowired
    PendingDisburementInterface disburementInterface;

    @PostMapping("upload-file-agre")
    public ResponseObject uploadReconciling(@RequestParam(value = "file") MultipartFile excelFile,@RequestAttribute(REQ_USR) JwtUser authUser){
       try{
           ResponseObject rs = disburementInterface.uploadFile(excelFile,authUser.getUsername());
           return rs;
       }catch (Exception e){
           ResponseObject rs = new ResponseObject();
           rs.setSuccess(false);
           rs.setMessage("Call Proc Failed, Please Check.");
           return rs;
       }
    }

    @GetMapping("insert-sig-agree")
    public ResponseObject insertSigAgre(@RequestParam ("agreementId")String agreementId,@RequestAttribute(REQ_USR) JwtUser authUser){
        return disburementInterface.insertSigAgre(agreementId, authUser.getUsername());
    }

    @GetMapping("get-data-pending-disbur")
    public ResponseObject getDataPendingDisbursement(@RequestParam ("agreementId")String agreementId){
        return disburementInterface.getDataPendingDisbursement(agreementId);
    }

    @PostMapping("delete-agree")
    public ResponseObject deleteAgreementId(@RequestBody List<PendingDisbursementModel> model){
        return disburementInterface.deleteAgreementId(model);
    }

    @GetMapping("process-pending-disbur")
    public ResponseObject processPending(@RequestAttribute(REQ_USR) JwtUser authUser) throws ParseException {
        return disburementInterface.processPending(authUser.getUsername());
    }
    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

    @GetMapping("is-process")
    public ResponseObject getIsProcess(){
        return disburementInterface.getProcess();
    }
}
