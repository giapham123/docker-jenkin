package com.dou.home.shared;

import org.springframework.util.StringUtils;

/**
 * Note: Just follow old version
 */
public enum Insurance {

    VIETIN  ("62", 1, "vietin"),

    PJICO   ("61", 2, "pjico"),

    PREVOIR   ("63", 3, "prevoir");

    private int val;

    private String f1Val;

    private String name;

    Insurance(String f1Val, int val, String name) {
        this.val = val;
        this.f1Val = f1Val;
        this.name = name;
    }

    public static Insurance fromString(String f1Val) {
        if (StringUtils.hasText(f1Val)) {
            for (Insurance ins : Insurance.values()) {
                if (ins.f1Val.equalsIgnoreCase(f1Val)) {
                    return ins;
                }
            }
        }
        return null;
    }

    public static Insurance fromStringName(String name) {
        if (StringUtils.hasText(name)) {
            for (Insurance ins : Insurance.values()) {
                if (ins.name.equalsIgnoreCase(name)) {
                    return ins;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
