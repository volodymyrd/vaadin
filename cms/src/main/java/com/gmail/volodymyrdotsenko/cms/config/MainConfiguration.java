package com.gmail.volodymyrdotsenko.cms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.vaadin.spring.annotation.EnableVaadin;

@Configuration
@EnableVaadin
@ComponentScan(basePackages = "com.gmail.volodymyrdotsenko.cms")
public class MainConfiguration {

}