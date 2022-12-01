package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoBranchNetwork;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoBranchNetworkMapper {
    List<AccountInfoBranchNetwork> getAll(String supervisorId);
    List<AccountInfoBranchNetwork> getInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    List<AccountInfoBranchNetwork> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    void updateInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    void delete(AccountInfoBranchNetwork AccountInfoBranchNetwork);

    void deleteAccountInfoBranchNetwork();
    void insertAccountInfoBranchNetwork_LastBackUp();
    void deleteAccountInfoBranchNetwork_LastBackUp();
    void importListAccountInfoBranchNetwork(List<AccountInfoBranchNetwork> accountInfoDrs);


}
