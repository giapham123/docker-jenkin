package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.AccountInfoSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoMapper {
    List<AccountInfo> getAccountInfo(AccountInfo accountInfo);
    List<AccountInfoSearch> getAccountInfoSearch(AccountInfo accountInfo);
    List<AccountInfo> getAccountInfoByAcctId (AccountInfo accountInfo);
    void insAccountInfo(AccountInfo accountInfo);
    void updAccountInfo(AccountInfo accountInfo);
    void delAccountInfo(AccountInfo accountInfo);
    void resetPasswordAcctInfo(AccountInfo accountInfo);
}
