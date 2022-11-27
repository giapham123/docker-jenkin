package com.dou.adm.models;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Feature {
    private String url;
    private List<String> btns;
    private List<String> hiddenFields;

    public Feature() {}

    public Feature(String url, String btnId, String hiddenFieldId) {
        this.url = url;
        this.btns = new ArrayList<>();
        this.hiddenFields = new ArrayList<>();

        if (StringUtils.hasText(btnId)) {
            this.btns.add(btnId);
        }
        if (StringUtils.hasText(hiddenFieldId)) {
            this.hiddenFields.add(hiddenFieldId);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getBtns() {
        return btns;
    }

    public void setBtns(List<String> btns) {
        this.btns = btns;
    }

    public List<String> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<String> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }
}
