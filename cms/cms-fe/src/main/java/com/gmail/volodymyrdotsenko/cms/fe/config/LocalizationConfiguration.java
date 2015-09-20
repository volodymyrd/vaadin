package com.gmail.volodymyrdotsenko.cms.fe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

@Configuration
public class LocalizationConfiguration {

	@Bean
	MessageProvider communicationMessages() {
		return new ResourceBundleMessageProvider("messages");
	}
}