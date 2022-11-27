package com.dou.accounting.controllers;

import com.dou.accounting.models.RejectUploadFileModel;
import com.dou.accounting.models.UploadFileModel;
import com.dou.accounting.services.RejectUploadFileInterface;
import com.dou.accounting.services.UploadFileInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reject-upload-file/")
@CrossOrigin
public class RejectUploadFileController {
    @Autowired
    RejectUploadFileInterface getUploadFileData;

    @PostMapping("get-data-reject-upload-file")
    public ResponseObject getRejectUploadFileData(@RequestBody RejectUploadFileModel rejectUploadFileModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(getUploadFileData.getRejectUploadFileData(rejectUploadFileModel));
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
    @PostMapping("exportExcel-reject")
    public ResponseObject exportExcel(@RequestBody RejectUploadFileModel rejectUploadFileModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = getUploadFileData.exportRejectUploadFile(rejectUploadFileModel);
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
