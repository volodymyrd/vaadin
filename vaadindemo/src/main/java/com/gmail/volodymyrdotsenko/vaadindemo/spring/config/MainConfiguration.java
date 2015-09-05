package com.gmail.volodymyrdotsenko.vaadindemo.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

}