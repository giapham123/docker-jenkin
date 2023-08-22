package com.dou.accounting.controllers;

import com.dou.accounting.models.RejectUploadFileGLSAPModel;
import com.dou.accounting.services.RejectUploadFileGLSAPInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reject-upload-file-gl-sap/")
@CrossOrigin
public class RejectUploadFileGLSAPController {
    @Autowired
    RejectUploadFileGLSAPInterface getRejectUploadFileData;

    @PostMapping("get-data-reject-upload-file-gl-sap")
    public ResponseObject getRejectUploadFileGLSAPData(@RequestBody RejectUploadFileGLSAPModel rejectUploadFileGLSAPModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(getRejectUploadFileData.getRejectUploadFileGLSAPData(rejectUploadFileGLSAPModel));
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

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

}
