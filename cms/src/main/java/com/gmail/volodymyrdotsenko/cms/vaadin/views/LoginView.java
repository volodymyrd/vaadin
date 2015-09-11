package com.gmail.volodymyrdotsenko.cms.vaadin.views;

import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

	public LoginView() {
		setSizeFull();

		Component loginForm = buildLoginForm();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

		Notification notification = new Notification("Welcome to Dashboard Demo");
		notification.setDescription(
				"<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray dark small closable login-help");
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.setDelayMsec(20000);
		notification.show(Page.getCurrent());
	}

	private Component buildLoginForm() {
		final VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");

		// loginPanel.addComponent(buildLabels());
		// loginPanel.addComponent(buildFields());
		loginPanel.addComponent(new CheckBox("Remember me", true));
		return loginPanel;
	}
}