package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoTeleSale;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoTeleSaleMapper {
    List<AccountInfoTeleSale> getAll(String supervisorId);
    List<AccountInfoTeleSale> getInfo(AccountInfoTeleSale AccountInfoTeleSale);
    List<AccountInfoTeleSale> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoTeleSale AccountInfoTeleSale);
    void updateInfo(AccountInfoTeleSale AccountInfoTeleSale);
    void delete(AccountInfoTeleSale AccountInfoTeleSale);

    void deleteAccountInfoTeleSale();
    void insertAccountInfoTeleSale_LastBackUp();
    void deleteAccountInfoTeleSale_LastBackUp();
    void importListAccountInfoTeleSale(List<AccountInfoTeleSale> accountInfoDrs);


}
