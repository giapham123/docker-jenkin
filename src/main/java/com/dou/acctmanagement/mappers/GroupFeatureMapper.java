package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.GroupFeature;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupFeatureMapper {
    List<GroupFeature> getGroupFeatureByGroupId(String groupId);
    List<GroupFeature> getGroupFeatureByGroupId_01(String groupId);
    List<GroupFeature> getGroupFeatureById(GroupFeature groupFeature);
    List<GroupFeature> loadGroupFeature(GroupFeature groupFeature);
    void insGroupFeature(GroupFeature groupFeature);
    void delGroupFeature(GroupFeature groupFeature);
}
