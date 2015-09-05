package com.gmail.volodymyrdotsenko.vaadindemo.spring.config;

import java.io.IOException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@ComponentScan(basePackages = "com.gmail.volodymyrdotsenko.vaadindemo")
public class DataSourceConfiguration {
	@Bean(name = "database")
	public PropertiesFactoryBean database() throws IOException {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource("META-INF/database.properties"));
		return bean;
	}

	@Bean
	public BasicDataSource dataSource(@Qualifier("database") PropertiesFactoryBean database) throws IOException {
		BasicDataSource bean = new BasicDataSource();
		bean.setDriverClassName(database.getObject().getProperty("database.driverClassName"));
		bean.setUrl(database.getObject().getProperty("database.url"));
		bean.setUsername(database.getObject().getProperty("database.username"));
		bean.setPassword(database.getObject().getProperty("database.password"));
		bean.setTestOnBorrow(true);
		bean.setTestOnReturn(true);
		bean.setTestWhileIdle(true);
		bean.setTimeBetweenEvictionRunsMillis(1800000);
		bean.setNumTestsPerEvictionRun(3);
		bean.setMinEvictableIdleTimeMillis(1800000);
		bean.setValidationQuery("SELECT 1 ");
		return bean;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(BasicDataSource dataSource) {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setPersistenceUnitName("persistenceUnit");
		bean.setDataSource(dataSource);
		return bean;
	}
}