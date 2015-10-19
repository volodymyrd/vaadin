package com.gmail.volodymyrdotsenko.cms.fe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point into the demo application.
 *
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan(basePackages = "com.gmail.volodymyrdotsenko.cms.be.domain")
@EnableJpaRepositories(basePackages = "com.gmail.volodymyrdotsenko.cms.be.domain")
@ComponentScan(basePackages = "com.gmail.volodymyrdotsenko.cms")
public class MainApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}
}	