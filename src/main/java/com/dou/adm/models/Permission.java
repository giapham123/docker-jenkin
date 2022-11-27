package com.dou.adm.models;

public class Permission {
    private String featureName;

    private String featureUrl;

    private String btnId;

    private String hiddenFieldId;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureUrl() {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl) {
        this.featureUrl = featureUrl;
    }

    public String getBtnId() {
        return btnId;
    }

    public void setBtnId(String btnId) {
        this.btnId = btnId;
    }

    public String getHiddenFieldId() {
        return hiddenFieldId;
    }

    public void setHiddenFieldId(String hiddenFieldId) {
        this.hiddenFieldId = hiddenFieldId;
    }
}
