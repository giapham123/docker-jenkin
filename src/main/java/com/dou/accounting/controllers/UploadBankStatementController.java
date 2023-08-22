package com.dou.accounting.controllers;

import com.dou.accounting.models.UploadBankStatementModel;
import com.dou.accounting.services.UploadBankStatementInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/upload-bank-statement/")
@CrossOrigin
public class UploadBankStatementController {

    @Autowired
    UploadBankStatementInterface bankStatementInterface;

    @PostMapping("upload-bank-statement")
    public ResponseObject importFile(@RequestParam(value = "file",required = false) MultipartFile excelFile, @RequestAttribute(REQ_USR) JwtUser authUser){
        String userLogin = authUser.getUsername();
        ResponseObject rs = bankStatementInterface.insertUploadBankStatement(excelFile,userLogin);
        return rs;
    }

    @PostMapping("get-data-bank-statement")
    public ResponseObject getDataBankStatement(@RequestBody UploadBankStatementModel uploadBankStatementModel){
        ResponseObject rs = bankStatementInterface.getDataBankStatement(uploadBankStatementModel);
        return rs;
    }

    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody UploadBankStatementModel uploadBankStatementModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = bankStatementInterface.exportBankStatementData(uploadBankStatementModel);
            if(dataBaseExcel != null){
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Success");
                rs.setSuccess(true);
            }else{
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Fails");
                rs.setSuccess(false);
            }
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Export Fails");
            rs.setSuccess(false);
        }
        return rs;
    }
}
