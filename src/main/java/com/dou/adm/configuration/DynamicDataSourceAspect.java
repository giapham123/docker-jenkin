package com.dou.adm.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * Created by Tu.Tran on 9/28/2018.
 */
@Aspect
@Order(-10) // Ensure that the AOP is executed before @Transactional
@Component
public class DynamicDataSourceAspect {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
        // Get the current specified data source;
        String dataSourceKey = targetDataSource.value();
        // If it is not in the range of all data sources that we have injected, then output warning information, and the system automatically uses the default data source.
        if (!DynamicDataSourceHolder.containsDataSourceKey(dataSourceKey)) {
            log.info("The data source [{}] does not exist, using the default data source > {} ", targetDataSource.value(), point.getSignature());
        } else {
            log.info("Use DataSource : {} > {}", targetDataSource.value(), point.getSignature());
            // If found, set it to the dynamic data source context.
            DynamicDataSourceHolder.setDataSourceKey(targetDataSource.value());
        }
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        log.info("Revert DataSource : {} > {}", targetDataSource.value(), point.getSignature());
        // After the execution of the method, destroy the current data source information and carry out the garbage collection.
        DynamicDataSourceHolder.clearDataSourceKey();
    }
}
