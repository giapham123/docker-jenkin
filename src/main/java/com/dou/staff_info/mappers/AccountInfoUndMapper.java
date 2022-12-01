package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoUnd;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoUndMapper {
    List<AccountInfoUnd> getAll(String supervisorId);
    List<AccountInfoUnd> getInfo(AccountInfoUnd AccountInfoUnd);
    List<AccountInfoUnd> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoUnd AccountInfoUnd);
    void updateInfo(AccountInfoUnd AccountInfoUnd);
    void delete(AccountInfoUnd AccountInfoUnd);

    void deleteAccountInfoUnd();
    void insertAccountInfoUnd_LastBackUp();
    void deleteAccountInfoUnd_LastBackUp();
    void importListAccountInfoUnd(List<AccountInfoUnd> accountInfoDrs);


}
