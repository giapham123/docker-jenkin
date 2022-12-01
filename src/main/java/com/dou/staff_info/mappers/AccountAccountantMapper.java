package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountAccountant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountAccountantMapper {
    List<AccountAccountant> getAll(String supervisorId);
}
