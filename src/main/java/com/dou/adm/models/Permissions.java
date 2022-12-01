package com.dou.adm.models;

import java.util.List;

public class Permissions {
    private String url;
    private List<String> btns;
    private String nameView;

    public Permissions() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameView() {
        return nameView;
    }

    public void setNameView(String nameView) {
        this.nameView = nameView;
    }

    public List<String> getBtns() {
        return btns;
    }

    public void setBtns(List<String> btns) {
        this.btns = btns;
    }
}
