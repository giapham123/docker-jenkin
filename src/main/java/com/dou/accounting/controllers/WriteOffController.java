package com.dou.accounting.controllers;

import com.dou.accounting.models.WriteOffModel;
import com.dou.accounting.services.WriteOffInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/writeoff/")
@CrossOrigin
public class WriteOffController {
    @Autowired
    private WriteOffInterface _writeOffInterface;


    @PostMapping("getDataWriteOff")
    public ResponseObject getDataWriteOff(@RequestBody WriteOffModel writeOffModel){
        ResponseObject rs = new ResponseObject();
        try{
            Map result = _writeOffInterface.getListDataWriteOff(writeOffModel);
            rs.setData(result);
            rs.setMessage("Get Data Success");
            rs.setSuccess(true);
        }catch (Exception e){
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("exportExcelWriteOff")
    public ResponseObject exportExcelWriteOff(@RequestBody WriteOffModel writeOffModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = _writeOffInterface.exportWriteOffData(writeOffModel);
            if(dataBaseExcel != null){
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Success");
                rs.setSuccess(true);
            }else{
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Success Fails");
                rs.setSuccess(false);
            }
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }
}
