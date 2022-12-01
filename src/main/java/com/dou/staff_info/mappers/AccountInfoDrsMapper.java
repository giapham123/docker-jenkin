package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoDrs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoDrsMapper {
    List<AccountInfoDrs> getAll(String supervisorId);
    List<AccountInfoDrs> getInfo(AccountInfoDrs AccountInfoDrs);
    List<AccountInfoDrs> getInfoAccount(AccountInfoDrs AccountInfoDrs);
    List<AccountInfoDrs> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoDrs AccountInfoDrs);
    void updateInfo(AccountInfoDrs AccountInfoDrs);
    void delete(AccountInfoDrs AccountInfoDrs);

    void deleteAccountInfoDrs();
    void insertAccountInfoDrs_LastBackUp();
    void deleteAccountInfoDrs_LastBackUp();
    void importListAccountInfoDrs(List<AccountInfoDrs> accountInfoDrs);

}
