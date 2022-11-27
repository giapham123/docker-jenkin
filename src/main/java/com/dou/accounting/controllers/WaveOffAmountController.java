package com.dou.accounting.controllers;

import com.dou.accounting.models.AccountingHisModel;
import com.dou.accounting.models.WaveOffAmountModel;
import com.dou.accounting.services.AccountingHisInterface;
import com.dou.accounting.services.WaveOffAmountInterface;
import com.dou.adm.shared.ResponseObject;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/wave-off-amount/")
@CrossOrigin
public class WaveOffAmountController {
    @Autowired
    private WaveOffAmountInterface _waveOffAmountInterface;

    @GetMapping("uploadFileStatus")
    public ResponseObject uploadFileStatus(){
        ResponseObject rs = new ResponseObject();
        try{
            boolean result = _waveOffAmountInterface.uploadStatus();
            rs.setData(result);
            rs.setMessage("Upload Success");
            rs.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Upload Failed");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("uploadFileWaveOffAmount")
    public ResponseObject uploadFileWaveOffAmount(@RequestParam ("file") MultipartFile excelFile){
        return _waveOffAmountInterface.uploadFileWaveOffAmount(excelFile);
    }

    @PostMapping("getWaveOffAmountData")
    public ResponseObject getWaveOffAmountData(@RequestBody WaveOffAmountModel waveOffAmountModel){
        ResponseObject rs = new ResponseObject();
        try{
            List<WaveOffAmountModel> result = _waveOffAmountInterface.getWaveOffAmountData(waveOffAmountModel);
            rs.setData(result);
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

    @PostMapping("updateWaveOffAmount")
    public ResponseObject updateWaveOffAmount(@RequestBody WaveOffAmountModel waveOffAmountModel){
        ResponseObject rs = new ResponseObject();
        try{
            int result = _waveOffAmountInterface.updateWaveOffAmount(waveOffAmountModel);
            rs.setData(result);
            rs.setMessage("Update Success");
            rs.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Update Failed");
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
