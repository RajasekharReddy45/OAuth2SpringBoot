package com.oauth2.config;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = { "com.oauth2.repository" })
@EntityScan(basePackages = { "com.oauth2.model" })
@EnableTransactionManagement
public class PersistenceManagerConfig {

	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_HIBERNATE_SCHEMA = "hibernate.default_schema";

	private static final String PROPERTY_PACKAGES_TO_SCAN = "com.oauth2.model";

	@Resource
	private Environment environment;

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		// entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan(PROPERTY_PACKAGES_TO_SCAN);

		Properties jpaProperties = new Properties();

		jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
		jpaProperties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, "org.hibernate.cfg.ImprovedNamingStrategy");
		jpaProperties.put(PROPERTY_NAME_HIBERNATE_SCHEMA, "public");
		jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, true);

		jpaProperties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
		jpaProperties.put("hibernate.connection.url", System.getenv("HIBERNATE_CONNECTION_URL"));
		jpaProperties.put("hibernate.connection.username", System.getenv("HIBERNATE_CONNECTION_USERNAME"));
		jpaProperties.put("hibernate.connection.password", System.getenv("HIBERNATE_CONNECTION_PASSWORD"));
		jpaProperties.put("hibernate.cache.use_second_level_cache", false);

		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}
}
