package com.dou.adm.shared;

import org.springframework.util.StringUtils;

public enum Insurance {

    VIETIN(62, "VIETIN"),

    PJICO(61, "PJICO"),

    PREVOIR(63, "PREVOIR");

    private int f1Val;

    private String name;

    Insurance(int f1Val, String name) {
        this.f1Val = f1Val;
        this.name = name;
    }

    public static Insurance findInsuranceCompany(int f1Val) {

        for (Insurance ins : Insurance.values()) {
            if (ins.f1Val == f1Val) {
                return ins;
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

    public int getF1Val() {
        return f1Val;
    }
}
