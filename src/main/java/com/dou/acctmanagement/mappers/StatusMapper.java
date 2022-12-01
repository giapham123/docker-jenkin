package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.Status;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatusMapper {
    List<Status> getAllStatus();
}
