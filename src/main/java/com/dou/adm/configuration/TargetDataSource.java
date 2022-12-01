package com.dou.adm.configuration;

import java.lang.annotation.*;

/**
 * Created by Tu.Tran on 9/28/2018.
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value() default "default";
}
