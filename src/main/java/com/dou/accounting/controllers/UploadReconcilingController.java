package com.dou.accounting.controllers;

import com.dou.accounting.models.UploadReconcilingModel;
import com.dou.accounting.services.UploadReconcilingInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/sap-manual/")
public class UploadReconcilingController {

    @Autowired
    UploadReconcilingInterface uploadReconcilingInterface;

    @PostMapping("upload-reconciling")
    public ResponseObject uploadReconciling(@RequestParam(value = "file") MultipartFile excelFile,@RequestAttribute(REQ_USR) JwtUser authUser){
       try{
           ResponseObject rs = uploadReconcilingInterface.uploadFile(excelFile,authUser.getUsername());
           return rs;
       }catch (Exception e){
           ResponseObject rs = new ResponseObject();
           rs.setSuccess(false);
           rs.setMessage("Call Proc Failed, Please Check.");
           return rs;
       }
    }

    @GetMapping("get-init-data")
    public ResponseObject getInitData(){
        return uploadReconcilingInterface.getInitData();
    }

    @PostMapping("get-data-reconciling-import")
    public ResponseObject getDataUploadReconciling(@RequestBody UploadReconcilingModel model){
        return uploadReconcilingInterface.getDataUploadReconciling(model);
    }

    @PostMapping("delete-data-reconciling-import")
    public ResponseObject deteleDataImportSap(@RequestBody List<UploadReconcilingModel> model){
        return uploadReconcilingInterface.deteleDataImportSap(model);
    }
    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
    @GetMapping("export-err")
    public ResponseObject exportErr(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
}
