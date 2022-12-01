package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.Branch;
import com.dou.acctmanagement.models.RequestType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BranchMapper {
    List<Branch> getAllBranch();

    List<RequestType> getAllRequestType();
}
