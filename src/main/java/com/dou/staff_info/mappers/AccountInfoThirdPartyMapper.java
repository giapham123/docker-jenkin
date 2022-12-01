package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoThirdParty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoThirdPartyMapper {
    List<AccountInfoThirdParty> getAll(String supervisorId);
    List<AccountInfoThirdParty> getInfo(AccountInfoThirdParty AccountInfoThirdParty);
    List<AccountInfoThirdParty> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoThirdParty AccountInfoThirdParty);
    void updateInfo(AccountInfoThirdParty AccountInfoThirdParty);
    void delete(AccountInfoThirdParty AccountInfoThirdParty);

    void deleteAccountInfoThirdParty();
    void insertAccountInfoThirdParty_LastBackUp();
    void deleteAccountInfoThirdParty_LastBackUp();
    void importListAccountInfoThirdParty(List<AccountInfoThirdParty> accountInfoDrs);


}
