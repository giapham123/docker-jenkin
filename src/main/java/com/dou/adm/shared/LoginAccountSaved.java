package com.dou.adm.shared;
import com.dou.adm.models.UserLogin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginAccountSaved {
    private static LoginAccountSaved instance = null;

    public List<UserLogin> userLogin = new ArrayList<>();

    public LoginAccountSaved(){}
    public static LoginAccountSaved getInstance()
    {
        if (instance == null)
            instance = new LoginAccountSaved();
        return instance;
    }

    public void setUserLogin(UserLogin user){
        this.userLogin.add(user);
    }

    public List<UserLogin> getAllUserLogin(){
        return this.userLogin;
    }

    public boolean getUser(String user, String ipAddress){
        for(int i=0; i< this.userLogin.size(); i++){
            if(this.userLogin.get(i).getAccountId().equals(user.toUpperCase()) && !this.userLogin.get(i).getIpAddress().equals(ipAddress)){
                return true;
            }
        }return false;
    }

    public void removeUserLogin(String user){
        for(int i=0; i< this.userLogin.size(); i++){
            if(this.userLogin.get(i).getAccountId().equals(user.toUpperCase())){
                this.userLogin.remove(i);
            }
        }
    }

    public void updateTimeLogIn(Integer time, String user){
        for(int i=0; i< this.userLogin.size(); i++){
            if(this.userLogin.get(i).getAccountId().equals(user.toUpperCase())){
                this.userLogin.get(i).setTimeKeepAccount(time);
                Date date = new Date();
                this.userLogin.get(i).setStartTimeLogin(date);
            }
        }
    }

    public boolean getAccountWithUrl(UserLogin user){
        for(int i=0; i< this.userLogin.size(); i++){
            if(this.userLogin.get(i).getPermissions().containsKey("wave")){
                if(user.getPermissions().containsKey("wave")){
                    return true;
                }
            }
        }
        return false;
    }

}
