package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.FeatureInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeatureInfoMapper {
    List<FeatureInfo> getAllFeatureInfo();
}
