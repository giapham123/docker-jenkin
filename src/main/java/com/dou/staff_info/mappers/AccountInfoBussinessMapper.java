package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoBussiness;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoBussinessMapper {
    List<AccountInfoBussiness> getAll(String supervisorId);
}
