package com.gmail.volodymyrdotsenko.cms.vaadin;

import com.gmail.volodymyrdotsenko.cms.vaadin.views.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI
@Theme("mytheme")
@Widgetset("com.gmail.volodymyrdotsenko.cms.vaadin.MainWidgetset")
@Title("CMS Dashboard")
@SuppressWarnings("serial")
public class MainUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		setContent(new LoginView());

	}

}