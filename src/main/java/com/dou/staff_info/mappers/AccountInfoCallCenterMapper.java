package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoCallCenter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoCallCenterMapper {
    List<AccountInfoCallCenter> getAll(String supervisorId);
    List<AccountInfoCallCenter> getInfo(AccountInfoCallCenter AccountInfoCallCenter);
    List<AccountInfoCallCenter> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoCallCenter AccountInfoCallCenter);
    void updateInfo(AccountInfoCallCenter AccountInfoCallCenter);
    void delete(AccountInfoCallCenter AccountInfoCallCenter);

    void deleteAccountInfoCallCenter();
    void insertAccountInfoCallCenter_LastBackUp();
    void deleteAccountInfoCallCenter_LastBackUp();
    void importListAccountInfoCallCenter(List<AccountInfoCallCenter> accountInfoDrs);


}
