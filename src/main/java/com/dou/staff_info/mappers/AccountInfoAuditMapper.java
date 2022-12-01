package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoAudit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoAuditMapper {
    List<AccountInfoAudit> getAll(String supervisorId);
}
