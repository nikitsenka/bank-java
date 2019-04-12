package com.nikitsenka.bankjava;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class BankJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankJavaApplication.class, args);
	}

	@Bean
	public DataSource datasource(DataSourceProperties dataSourceProperties) {
		HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
		dataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
		return dataSource;
	}
}
