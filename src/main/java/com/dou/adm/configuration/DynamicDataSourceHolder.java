package com.dou.adm.configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tu.Tran on 9/28/2018.
 */
public class DynamicDataSourceHolder {
    private static final ThreadLocal<String> dataSourceThreadLocal = new ThreadLocal<String>();

    private static Set<String> dataSourceKeys = new HashSet<String>();

    static {
        dataSourceKeys.add("default");
        dataSourceKeys.add("oracle");
    }

    public static void setDataSourceKey(String dataSourceKey) {
        dataSourceThreadLocal.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return dataSourceThreadLocal.get();
    }

    public static void clearDataSourceKey() {
        dataSourceThreadLocal.remove();
    }

    public static boolean containsDataSourceKey(String dataSourceKey) {
        return dataSourceKeys.contains(dataSourceKey);
    }
}

