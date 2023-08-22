package com.dou.home.shared;

import org.springframework.util.StringUtils;

public enum ScanFileType {

    SCAN ("scan", ".pdf"),

    CONTRACT ("contract", "_HDTD.pdf"),

    SIGNED_CONTRACT ("signed_contract", "_SignedHDTD.pdf");

    private String name;

    private String postFixed;

    ScanFileType(String name, String postFixed) {
        this.name = name;
        this.postFixed = postFixed;
    }

    public static ScanFileType fromString(String val) {
        if (StringUtils.hasText(val)) {
            for (ScanFileType type : ScanFileType.values()) {
                if (type.name.equalsIgnoreCase(val)) {
                    return type;
                }
            }
        }
        return null;
    }

    public String getPostFixed() {
        return postFixed;
    }
}
