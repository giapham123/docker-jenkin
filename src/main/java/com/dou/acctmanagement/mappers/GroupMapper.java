package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<Group> getAllGroup();
}
