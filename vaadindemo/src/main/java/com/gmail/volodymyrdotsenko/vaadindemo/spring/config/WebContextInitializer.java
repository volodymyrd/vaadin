package com.gmail.volodymyrdotsenko.vaadindemo.spring.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.vaadin.spring.server.SpringVaadinServlet;

public class WebContextInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

		context.register(MainConfiguration.class);
		context.setConfigLocation("com.gmail.volodymyrdotsenko.vaadindemo");

		servletContext.addListener(new ContextLoaderListener(context));
		registerServlet(servletContext);
	}

	private void registerServlet(ServletContext servletContext) {
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("vaadin", SpringVaadinServlet.class);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");
	}
}