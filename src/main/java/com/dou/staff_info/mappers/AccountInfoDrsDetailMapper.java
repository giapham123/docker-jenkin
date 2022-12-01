package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoDrsDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoDrsDetailMapper {

    List<AccountInfoDrsDetail> getInfo(AccountInfoDrsDetail accountInfoDrsDetail);
    List<AccountInfoDrsDetail> getAccount(AccountInfoDrsDetail accountInfoDrsDetail);
    List<AccountInfoDrsDetail> checkExistAccountId(String accountID);

    void updateInfo(AccountInfoDrsDetail accountInfoDrsDetail);
    void insertInfo(AccountInfoDrsDetail accountInfoDrsDetail);

}
