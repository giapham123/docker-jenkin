package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.GrantPermissionHis;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GrantPermissionHisMapper {
    void insGrantPermissionHis(GrantPermissionHis grantPermissionHis);
    //   void insGrantGroupPermissionHis(List<GrantPermissionHis> listGrantPermissionHis);
}
