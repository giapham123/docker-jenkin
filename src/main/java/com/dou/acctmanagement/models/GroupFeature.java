package com.dou.acctmanagement.models;

public class GroupFeature {
    private String id;
    private String groupId;
    private String groupName;
    private String featureId;
    private String featureName;
    private String hiddenFieldId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getHiddenFieldId() {
        return hiddenFieldId;
    }

    public void setHiddenFieldId(String hiddenFieldId) {
        this.hiddenFieldId = hiddenFieldId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
