package com.dou.accounting.controllers;

import com.dou.accounting.models.CollectionTransModel;
import com.dou.accounting.models.SigProcess;
import com.dou.accounting.services.CollectionTransInterface;
import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.services.FTPServiceInterface;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/coll-trans/")
public class CollectionTransController {

    @Autowired
    CollectionTransInterface collectionTransInterface;

    @Value("${ftp.mainfolder}")
    String _folder;

    @Value("${ftp.subfolder}")
    String _collectorTransactionFolder;

    @Autowired
    FTPServiceInterface _ftpServiceInterface;

    @PostMapping("get-coll-trans-data")
    public ResponseObject getCollTransData(@RequestBody CollectionTransModel model){
        return collectionTransInterface.getCollTransData(model);
    }

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
    SigProcess getTrans = SigProcess.getInstance();
    @GetMapping("get-transactions")
    public ResponseObject getTransactions() throws IOException, BiffException {
        ResponseObject obj = new ResponseObject();
        if(getTrans.getIsGetTrans()){
            obj.setSuccess(false);
            obj.setFailMessage("Get Transactions is in progressing!");
            return obj;
        }
        List<String> list = new ArrayList<>();
        list = _ftpServiceInterface.listingFTP(_collectorTransactionFolder);
        if(list.size() > 0)
        {
            getTrans.setIsGetTrans(true);
            for(String arr : list)
            {
                _ftpServiceInterface.procDelColl(arr.split("/")[2].split("_")[0], arr.split("/")[2].trim().toUpperCase());
                obj.setMessage(_ftpServiceInterface.collectorTransaction(arr));
                if(!obj.getMessage().equals("SUCCESS"))
                {
                    getTrans.setIsGetTrans(false);
                    obj.setSuccess(false);
                    return obj;
                }
                _ftpServiceInterface.copyFile_collectorTransaction(arr);
                _ftpServiceInterface.DeleteFTP(arr);
                _ftpServiceInterface.procSyncColl(arr.split("/")[2].split("_")[0]);
            }
            getTrans.setIsGetTrans(false);
            obj.setSuccess(true);
            obj.setMessage("successful.");
        }
        else
        {
            obj.setSuccess(false);
            obj.setFailMessage("Oops!, no files were found in FTP.");
        }
        return obj;
    }
    @GetMapping("is-get-trans")
    public ResponseObject getIsTrans(){
        ResponseObject rs = new ResponseObject();
        rs.setData(getTrans.getIsGetTrans());
        return rs;
    }
}
