package com.dou.bankstatement.controllers;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.services.FTPServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api/ftp", "/despatchAPI/"})
@CrossOrigin
public class FTPController {

    @Value("${ftp.mainfolder}")
    private String _folder;

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
}
