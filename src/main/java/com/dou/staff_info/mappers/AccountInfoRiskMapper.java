package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoRisk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoRiskMapper {
    List<AccountInfoRisk> getAll(String supervisorId);
}
