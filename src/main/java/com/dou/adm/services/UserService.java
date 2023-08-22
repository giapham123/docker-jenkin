package com.dou.adm.services;

import com.dou.adm.mappers.UserMapper;
import com.dou.adm.models.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper mapper;

    @Value("${uri-auth-ldap}")
    private  String uri;

    public User loginUser(String accountId){
        User user = new User();
        try {
            user = mapper.loginUser(accountId);
        }catch (Exception e){
            LOGGER.error("Fail when call loginUser.", e);
        }
        return user;
    }

    public Map retrieveAccountPermission(String accountId) {
        Map<String, Feature> permissions = new HashMap<>();
        try {
            List<Permission> rawData = mapper.getInfoPerMissionByAccount(accountId);
            if (rawData == null) return permissions;

            for (Permission permission : rawData) {
                if (permissions.containsKey(permission.getFeatureName())) {
                    Feature feature = permissions.get(permission.getFeatureName());

                    if (StringUtils.hasText(permission.getBtnId())) {
                        feature.getBtns().add(permission.getBtnId());
                    }
                    if (StringUtils.hasText(permission.getHiddenFieldId())) {
                        feature.getHiddenFields().add(permission.getHiddenFieldId());
                    }
                } else {
                    permissions.put(permission.getFeatureName(),
                            new Feature(permission.getFeatureUrl(),
                                    permission.getBtnId(), permission.getHiddenFieldId()));
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return permissions;
    }

    public UserProfiles retrieveUserProfile(String targetTable, String accountId) {
        try {
            if (StringUtils.hasText(targetTable)) {
                return this.mapper.retrieveUserProfile(targetTable, accountId);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while trying to retrieve UserProfile of %s from Database", accountId), e);
        }
        return null;
    }

    public int changePassword(String accountId, String password ){
        int result = 0;
        try {
            result  =  mapper.changePassword(accountId,password);
        }catch (Exception e){
            LOGGER.error("fail when call changePassword.", e);
        }
        return result;
    }

    public LdapRes loginWithLDAP(String accountId, String password){
        RestTemplate restTemplate = new RestTemplate();
        LdapRes resApi = new LdapRes();
        Map param = new HashMap();
        param.put("userName",accountId);
        param.put("password",password);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(param, header);
        ResponseEntity<String> response = null;
        response = restTemplate.postForEntity(uri, entity, String.class);
        JSONObject array = new JSONObject(response.getBody());
        String isAuth = array.get("success").toString();
        resApi.setSuccess(Boolean.parseBoolean(isAuth));
        resApi.setMessage( array.get("message").toString());
        return resApi;
    }

}
