package com.nikitsenka.bankjava;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableCaching
@SpringBootApplication
public class BankJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankJavaApplication.class, args);
	}

	@Bean
	public JdbcTemplate datasource(DataSourceProperties dataSourceProperties) {
		HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
		dataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
		dataSource.setMinimumIdle(5);
		dataSource.setIdleTimeout(300000);
		dataSource.setConnectionTimeout(20000);
		dataSource.setMaxLifetime(1200000);
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("balances");
	}
}
