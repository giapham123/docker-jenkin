package com.dou.bankstatement.controllers;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.services.FTPServiceInterface;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api/ftp", "/despatchAPI/"})
@CrossOrigin
public class FTPController {

    @Value("${ftp.mainfolder}")
    private String _folder;

    @Value("${ftp.subfolder}")
    private String _collectorTransactionFolder;

    @Autowired
    private FTPServiceInterface _ftpServiceInterface;

    DateTimeFormatter _dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/SyncFTP")
    private ResponseObject SyncFTP(){
        ResponseObject obj = new ResponseObject();
        String result = "";
        List<String> list = new ArrayList<>();

        list = _ftpServiceInterface.listingFTP(_folder);
        for(String arr : list)
        {
            result += _ftpServiceInterface.excelFTP(arr);
            _ftpServiceInterface.CopyFileFTP(arr);
            _ftpServiceInterface.DeleteFTP(arr);
            obj.setData(result);
        }
        return obj;
    }

    @GetMapping("/collectorTransaction")
    private ResponseObject collectorTransaction() throws IOException, BiffException, URISyntaxException {
        ResponseObject obj = new ResponseObject();
        List<String> list = new ArrayList<>();

        list = _ftpServiceInterface.listingFTP(_collectorTransactionFolder);
        if(list.size() > 0)
        {
            for(String arr : list)
            {
                _ftpServiceInterface.procDelColl(arr.split("/")[2].split("_")[0], arr.split("/")[2].trim().toUpperCase());
                obj.setMessage(_ftpServiceInterface.collectorTransaction(arr));
                if(!obj.getMessage().equals("SUCCESS"))
                {
                    obj.setSuccess(false);
                    return obj;
                }
                _ftpServiceInterface.copyFile_collectorTransaction(arr);
                _ftpServiceInterface.DeleteFTP(arr);
                _ftpServiceInterface.procSyncColl(arr.split("/")[2].split("_")[0]);
            }
            obj.setMessage("successful.");
        }
        else
        {
            obj.setFailMessage("Oops!, no files were found in FTP.");
        }
        return obj;
    }
}
