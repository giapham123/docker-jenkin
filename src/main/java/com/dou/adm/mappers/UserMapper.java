package com.dou.adm.mappers;

import com.dou.adm.models.Permission;
import com.dou.adm.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserMapper {

    User loginUser(@Param("accountId") String accountId);

    List<Permission> getInfoPerMissionByAccount(@Param("accountId") String accountId);

    int changePassword(@Param("accountId") String accountId, @Param("password") String password);

}