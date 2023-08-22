package com.dou.adm.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogActivitiesUserMapper {

    int activitesUser (@Param("accountId") String accountId, @Param("screen") String screen, @Param("actions") String actions);

}