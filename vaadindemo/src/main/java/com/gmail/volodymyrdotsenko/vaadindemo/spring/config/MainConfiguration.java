package com.gmail.volodymyrdotsenko.vaadindemo.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.gmail.volodymyrdotsenko.vaadindemo.spring.beans.UtilsBean;
import com.vaadin.spring.annotation.EnableVaadin;

@Configuration
@EnableVaadin
@ComponentScan(basePackages = "com.gmail.volodymyrdotsenko.vaadindemo")
public class MainConfiguration {
	@Bean
	public UtilsBean myBean() {
		return new UtilsBean();
	}

	@Bean
	public in.virit.WidgetSet viritinCdnInitializer() {
		return new in.virit.WidgetSet();
	}

	@Bean
	public org.peimari.dawn.CdnFonts cdnFonts() {
		return new org.peimari.dawn.CdnFonts();
	}
	
	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setFallbackToSystemLocale(false);
		resource.setBasename("/WEB-INF/validationMessages");
		resource.setDefaultEncoding("UTF-8");
		return resource;
	}

}