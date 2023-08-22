package com.dou.adm.shared;

import com.dou.adm.mappers.LogActivitiesUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaveActivitiesLogs {

    @Autowired
    LogActivitiesUserMapper logActivitiesUserMapper;

    public void writeLogs(String user, String actions, String screen) {
        try {
            logActivitiesUserMapper.activitesUser(user,screen,actions);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void funcSaveLog(String apiPath, String userLogin){
        String screenPath = apiPath.split("/")[2];
        String getScreenCode = getScreenCode(screenPath);
        for(List lsApi: CommonActions.lsApi){
            for(Object api: lsApi){
                String pathAPI = api.toString().split(":")[0];
                if(pathAPI.equals(apiPath)){
                    writeLogs(userLogin,api.toString().split(":")[1],getScreenCode);
                }
            }
        }
    }
    private String getScreenCode(String screenPath){
        String screenCode = "";
        for(String screen: CommonUICode.SCREEN_PATH){
            String path = screen.split("/")[0];
            if(screenPath.equals(path)){
                screenCode =  screen.split("/")[1];
            }
        }
        return screenCode;
    }
}
