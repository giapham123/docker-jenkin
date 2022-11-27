package com.dou.adm.services;

// Created by BaoTD - 27/05/2019
import com.dou.adm.models.User;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class UserThirdPartyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserThirdPartyService.class);

    @Value("${web.client.inactive.minutes}")
    private int clientInActiveMinutes;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    private String hashSHA256(String text) {
        String resultHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            // bytes to hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            resultHash = sb.toString();
        }
        catch (Exception ex) {
            LOGGER.error("Have error when hashing SHA256.");
        }
        return resultHash;
    }

    private User validUser(String username, String password) {
        User user = userService.loginUser(username);
        String hashPassword = hashSHA256(password);
        if (user != null && jwtProvider.validatePassword(hashPassword,user.getPassword())) {
            return user;
        }
        return null;
    }

    private boolean authenticateToken(String token) {
        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex) {
            LOGGER.error("Have error when authenticate token.");
            return false;
        }
    }

    private String genTokenForThirdParty(String username, String password) {
        String jwt = "";
        try {
            User userDB = validUser(username, password);
            if(userDB != null){
                JwtUser jwtUser = JwtUser.create(userDB);
                jwt = jwtProvider.generateToken(jwtUser);
            }
        }
        catch (Exception ex) {
            LOGGER.error("Have error when valid username and password.");
        }
        return jwt;
    }

    public boolean loginThirdParty(String username, String password) {
        try {
            String jwt = genTokenForThirdParty(username, password);
            boolean checkToken = false;
            if (jwt != "") {
                checkToken = authenticateToken(jwt);
            }
            return checkToken;
        }
        catch (Exception ex) {
            LOGGER.error("Have error when thirdparty login.");
            return false;
        }
    }
}
