package io.yzecho.netdisk.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

/**
 * @className: MyBatisConfig
 * @description:
 * @author: liuzhe
 * @date: 2021/4/11
 **/
@Configuration
@MapperScan(basePackages = {"io.yzecho.netdisk.mapper"})
public class MyBatisConfig {
    @Bean
    @Primary
    public TransactionManager defaultTransactionManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }
}
