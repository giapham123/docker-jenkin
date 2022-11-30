package com.dou.adm.controllers;

import com.dou.adm.models.LoginResponse;
import com.dou.adm.models.User;
import com.dou.adm.models.UserLogin;
import com.dou.adm.models.UserProfiles;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.security.JwtUser;
import com.dou.adm.services.JobRemoveUserTimeOut;
import com.dou.adm.services.UserService;
import com.dou.adm.shared.LoginAccountSaved;
import com.dou.home.models.ChangePasswordRequest;
import com.dou.home.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Value("${web.client.inactive.minutes}")
    private int clientInActiveMinutes;

    @Value("${web.client.default.password}")
    private String defaultPassword;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private JobRemoveUserTimeOut jobRemoveUserTimeOut;

    LoginAccountSaved userSaving = LoginAccountSaved.getInstance();

    @RequestMapping({"/login","/dashboard","/termination-simulation-report"
            ,"/termination-simulation-report-his"
            ,"/writeoff-ext","/waveoffamount",
    "/upload-file","/reject-upload-file","/reject-upload-file-gl-sap",
            "/termination-out-net-report","/close-soldout",
    "/schedule-bf-reduce-interest","/bank-statement"})
    public String index(){
        return "index.html";
    }

    @PostMapping("/login/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtUser user, HttpServletResponse response) {
        ResponseObject bizResponse = new ResponseObject();
        User userDB = validUser(user.getUsername(), user.getPassword());
        if(userDB != null){
            JwtUser jwtUser = JwtUser.create(userDB);
            UserProfiles profiles =  userService.retrieveUserProfile(userDB.getTargetProfileTable(), userDB.getAccountId());
            if (profiles == null) {
                LOGGER.warn(String.format("Can not found profile of user [%s]", user.getUsername()));
                profiles = new UserProfiles(userDB);
            }



            jwtUser.setProfiles(profiles);
            String jwt = jwtProvider.generateToken(jwtUser);

            LoginResponse respData = new LoginResponse();
            respData.setAccount(profiles);
            respData.setPermissions(userService.retrieveAccountPermission(userDB.getAccountId()));
            respData.setAccessToken(jwt);
            respData.setExpireHours(this.clientInActiveMinutes);

            if (StringUtils.hasText(this.defaultPassword)) {
                respData.setDefaultPassword(this.defaultPassword.equals(jwtUser.getPassword()));
            }

            bizResponse.setData(respData);
        }
        else {
            bizResponse.setSuccess(false);
            bizResponse.setMessage("User Name or Password is incorrect!");
        }
        return ResponseEntity.ok(bizResponse);
    }

    @GetMapping("/authenticate")
    @ResponseBody
    public ResponseObject authenticateToken(@RequestParam("token") String token) {
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            return ResponseObject.SUCCESS_WITHOUT_DATA;
        }
        else {
            return ResponseObject.FAILURE;
        }
    }

    @PutMapping("/user-password")
    @ResponseBody
    public ResponseObject changePassword(@RequestBody ChangePasswordRequest reqObject) {
        try {
            User user = validUser(reqObject.getUsername(), reqObject.getPassword());
            if(user != null) {
                String hashPassword = jwtProvider.generatePassword(reqObject.getNewPassword());
                int result = userService.changePassword(user.getAccountId(), hashPassword);
                if (result > 0) {
                    return ResponseObject.SUCCESS_WITHOUT_DATA;
                } else {
                    return ResponseObject.failResponse("Can not update new password to DB");
                }
            }
        }catch (Exception e){
            LOGGER.error(String.format("Error occurred while trying to change password of user [%s].", reqObject.getUsername()), e);
            return ResponseObject.FAILURE;
        }

        return ResponseObject.failResponse("Please input correct current username and password.");
    }

    @PostMapping("/login/save-user-login")
    @ResponseBody
    public ResponseObject saveUserLogin(@RequestBody UserLogin user, HttpServletRequest request) {
        ResponseObject rs = new ResponseObject();
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        user.setIpAddress(remoteAddr);
        Date date = new Date();
        user.setStartTimeLogin(date);
        userSaving.setUserLogin(user);
        List<UserLogin> a = userSaving.getAllUserLogin();
        jobRemoveUserTimeOut.jobStart();
        return rs;
    }

    @PostMapping("/login/check-user-islogin")
    @ResponseBody
    public ResponseObject checkUserIsLogin(@RequestBody UserLogin user, HttpServletRequest request) {
        ResponseObject rs = new ResponseObject();
        jobRemoveUserTimeOut.jobStart();
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        boolean isExist = userSaving.getUser(user.getAccountId(),remoteAddr);
        boolean isExistUrlPermissions = userSaving.getAccountWithUrl(user);
        if(isExistUrlPermissions){
            rs.setData(isExistUrlPermissions);
            rs.setMessage("This Account Is Logged On Another Computer!");
            return rs;
        }
        if(isExist){
            rs.setData(isExist);
            rs.setMessage("This Account Is Logged On Another Computer!");
        }else{
            rs.setData(isExist);
        }
        return rs;
    }


    @GetMapping("/login/remove-user-login")
    @ResponseBody
    public ResponseObject removeUserLogin(@RequestParam("username") String username) {
        ResponseObject rs = new ResponseObject();
        userSaving.removeUserLogin(username);
        return rs;
    }

    @PostMapping("/login/update-timeout")
    @ResponseBody
    public ResponseObject updateTimeOut(@RequestBody UserLogin model) {
        ResponseObject rs = new ResponseObject();

        userSaving.updateTimeLogIn(model.getTimeKeepAccount(),model.getAccountId());
        List<UserLogin> a = userSaving.getAllUserLogin();
        return rs;
    }

    private User validUser(String username, String password) {
        User user = userService.loginUser(username);
        if (user != null && jwtProvider.validatePassword(password,user.getPassword())) {
            return user;
        }
        return null;
    }
}
