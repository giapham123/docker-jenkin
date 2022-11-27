package com.dou.adm.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tu.Tran on 9/28/2018.
 */
@Configuration
public class DynamicDataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceConfig.class);

//    @Value("${spring.datasource.home.jndi-name}")
//    private String homeJndiName;
//
//    @Value("${spring.datasource.oracle.jndi-name}")
//    private String oracleJndiName;
//
//    private JndiDataSourceLookup lookup = new JndiDataSourceLookup();
//
//    @Bean
//    public DataSource dataSourceDefault() {
//        return lookup.getDataSource(homeJndiName);
//    }
//
//    @Bean
//    public DataSource dataSourceOracle() {
//        return lookup.getDataSource(oracleJndiName);
//    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.home")
    public DataSource dataSourceDefault() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource dataSourceOracle() {
        return DataSourceBuilder.create().build();
    }


    //////////////////// Creating dynamic data sources
    @Bean
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // Create a default data source
        dynamicDataSource.setDefaultTargetDataSource(dataSourceDefault());
        // Configuring multiple data sources
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("default", dataSourceDefault());
        dataSourceMap.put("oracle", dataSourceOracle());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    //////////////////// Configuring SqlSessionFactory
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //Setting up a data source as a dynamic data source
        sqlSessionFactoryBean.setDataSource(dataSource());
        //mapperThe location of the.Xml file
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappers/*/*.xml"));
        final Resource configLocation = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
