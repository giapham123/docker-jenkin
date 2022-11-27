package com.dou.adm.services;

import com.dou.adm.mappers.UserMapper;
import com.dou.adm.models.Feature;
import com.dou.adm.models.Permission;
import com.dou.adm.models.User;
import com.dou.adm.models.UserProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper mapper;

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

}
