package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.GroupFeature;
import com.dou.adm.shared.ResponseObject;

public interface GroupFeatureServiceInterface {
    ResponseObject getGroupFeatureByGroupId(String groupId);
    ResponseObject getGroupFeatureById(GroupFeature groupFeature);
    ResponseObject loadGroupFeature(GroupFeature groupFeature);
    ResponseObject insGroupFeature(GroupFeature groupFeature);
    ResponseObject delGroupFeature(GroupFeature groupFeature);
}
