package com.dou.adm.controllers;

import com.dou.adm.models.User;
import com.dou.adm.security.BizResponse;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userServices;

/*
    @RequestMapping("/api/getAllUserSlave")
    public List<User> getAllUserSlave() {
        return userServices.findAllUserSlave();
    }

    @RequestMapping("/api/getAllUser")
    public List<User> getAllUser() {
        return userServices.findAllUser();
    }


    @RequestMapping(path = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        BizResponse bizResponse = new BizResponse();
        String password = user.getUsrPwd();
        String hashPassword = jwtProvider.generatePassword(password);
        user.setUsrPwd(hashPassword);
        Boolean type = userServices.insertUser(user);
        if(type){
            bizResponse.setSuccess(true);
            bizResponse.setMessage("Email was registered successful!");
        }else{
            bizResponse.setSuccess(false);
            bizResponse.setMessage("Email was existed. Please input another email!");
        }
        return ResponseEntity.ok(bizResponse);
    }*/
}
