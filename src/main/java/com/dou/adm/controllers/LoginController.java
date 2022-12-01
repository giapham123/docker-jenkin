package com.dou.adm.controllers;

import com.dou.adm.models.ChangePasswordRequest;
import com.dou.adm.models.LoginResponse;
import com.dou.adm.models.User;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.security.JwtUser;
import com.dou.adm.services.UserService;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping({"/login","/dashboard","/sale","/und/assignment"})
    public String index(){
        return "index.html";
    }

    @PostMapping("/login/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtUser jwtUser, HttpServletResponse response) {
        ResponseObject bizResponse = new ResponseObject();
        User user = validUser(jwtUser.getUsername(), jwtUser.getPassword());
        if(user != null){
            jwtUser.setId(user.getId());
            jwtUser.setIsAdmin(user.getIsAdmin());
            String jwt = jwtProvider.generateToken(jwtUser);

            LoginResponse respData = new LoginResponse();
            respData.setAccount(user);
            respData.setPermissions(userService.retrieveAccountPermission(user.getAccountId()));
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

    private User validUser(String username, String password) {
        User user = userService.loginUser(username);
        if (user != null && jwtProvider.validatePassword(password,user.getPassword())) {
            return user;
        }

        return null;
    }
}
