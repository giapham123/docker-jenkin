package com.dou.accounting.controllers;

import com.dou.accounting.models.CloseSoldoutModel;
import com.dou.accounting.models.WriteOffModel;
import com.dou.accounting.services.CloseSoldoutInterface;
import com.dou.accounting.services.WriteOffInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/close-sold-out/")
@CrossOrigin
public class CloseSoldoutController {

    @Autowired
    CloseSoldoutInterface closeSoldoutInterface;

    @PostMapping("loading-details")
    public ResponseObject loadingDetailsData(@RequestParam ("file") MultipartFile excelFile){
        ResponseObject rs = new ResponseObject();
        Map result = closeSoldoutInterface.loadingDetailsData(excelFile);
        try{
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

    @PostMapping("close-app")
    public ResponseObject closeApp(@RequestBody List<CloseSoldoutModel> lsCloseSoldoutModel,@RequestAttribute(REQ_USR) JwtUser authUser){
        for(CloseSoldoutModel model: lsCloseSoldoutModel){
            model.setUserLogin(authUser.getUsername());
        }
        ResponseObject result = closeSoldoutInterface.closeLsApp(lsCloseSoldoutModel);
        return result;
    }

    @PostMapping("get-close-sold-out")
    public ResponseObject getCloseSoldout(@RequestBody List<CloseSoldoutModel> lsCloseSoldoutModel){
        ResponseObject rs = new ResponseObject();
        List<CloseSoldoutModel> result = closeSoldoutInterface.getCloseSoldout(lsCloseSoldoutModel);
        try{
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
}
