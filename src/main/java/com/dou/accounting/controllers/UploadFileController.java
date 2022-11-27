package com.dou.accounting.controllers;

import com.dou.accounting.models.AccountingHisModel;
import com.dou.accounting.models.UploadFileModel;
import com.dou.accounting.services.AccountingHisInterface;
import com.dou.accounting.services.UploadFileInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/receipt-upload-file/")
@CrossOrigin
public class UploadFileController {
    @Autowired
    UploadFileInterface uploadFileInterface;

    @PostMapping("get-data-upload-file")
    public ResponseObject getUploadFileData(@RequestBody UploadFileModel uploadFileModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(uploadFileInterface.getUploadFileData(uploadFileModel));
            rs.setMessage("Get Data Success");
            rs.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("get-total-rows")
    public ResponseObject getTotalRows(@RequestBody UploadFileModel uploadFileModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(uploadFileInterface.totalPage(uploadFileModel));
            rs.setMessage("Get Data Success");
            rs.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("exportExcel-upload")
    public ResponseObject exportExcel(@RequestBody UploadFileModel uploadFileModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = uploadFileInterface.exportUploadFile(uploadFileModel);
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
