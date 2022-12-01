package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoAnti;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoAntiMapper {
    List<AccountInfoAnti> getAll(String supervisorId);
}
