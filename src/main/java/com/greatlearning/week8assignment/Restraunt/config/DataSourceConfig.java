package com.greatlearning.week8assignment.Restraunt.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "com.greatlearning.week8assignment.Restraunt.repository", entityManagerFactoryRef = "dataEntityManager", transactionManagerRef = "dataTransactionManager")
@EnableTransactionManagement
public class DataSourceConfig extends HikariConfig {

	@Autowired
	Environment env;

	public DataSourceConfig() {
		super();
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(this);
	}

	private Properties properties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.setProperty("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
		properties.setProperty("hibernate.open-in-view", env.getProperty("spring.jpa.open-in-view"));
//		System.out.println(properties);
		return properties;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean dataEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan("com.greatlearning.week8assignment.Restraunt.model");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(properties());
		return em;
	}

	@Bean
	public PlatformTransactionManager dataTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(dataEntityManager().getObject());
		return transactionManager;
	}

}
